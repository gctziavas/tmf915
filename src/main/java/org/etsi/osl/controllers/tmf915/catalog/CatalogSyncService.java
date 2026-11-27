package org.etsi.osl.controllers.tmf915.catalog;

import org.etsi.osl.controllers.tmf915.artifacts.ArtifactCharacteristics;
import org.etsi.osl.controllers.tmf915.artifacts.ArtifactService;
import org.etsi.osl.controllers.tmf915.artifacts.DownloadAttachments;
import org.etsi.osl.controllers.tmf915.artifacts.ModelArtifact;
import org.etsi.osl.controllers.tmf915.mappers.AiModelSpecificationMapper;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;

/**
 * Publishes every enabled platform's models as {@code AiModelSpecification}s. On each tick, for
 * each {@link CatalogSource}:
 * <ol>
 *   <li>Searches the platform: in full every {@code aimodel.catalog.sync.full-interval-ms}
 *       (default 10 minutes), and in between only for models created since the newest one seen,
 *       as long as the source supports that.</li>
 *   <li>Skips models the platform has not finished writing ({@link DiscoveredModel#status()}), and
 *       models ready for less than {@code aimodel.catalog.sync.settle-ms} (default 1 minute), so
 *       that what a publisher adds right after the model, such as MLflow metrics, is in the
 *       specification. Both are looked at again on the next search that returns them.</li>
 *   <li>Skips models already in the catalog (same {@code name} and {@code version}), remembering
 *       them so the database is asked once per model.</li>
 *   <li>Skips models whose files are not stored yet, and checks them again after 10 minutes.</li>
 *   <li>Creates a specification for each remaining model, newest first, with the
 *       platform-neutral fields every source gets: the characteristics {@code platform} and
 *       {@code artifactFormat}, and, if the model can be downloaded, the download
 *       {@code attachment} with its size.</li>
 * </ol>
 * A source that fails is logged and does not stop the others.
 */
@Service
public class CatalogSyncService {

    private static final Logger log = LoggerFactory.getLogger(CatalogSyncService.class);

    /**
     * Incremental searches ask for models created after (newest creation time seen - overlap),
     * so a model created while a search is running, or stamped by a slightly skewed clock, is
     * still found. Models found again are skipped by the cache of synced models.
     */
    static final long INCREMENTAL_OVERLAP_MS = 5 * 60 * 1000;

    /** How long to wait before checking again a model whose files were not stored yet. */
    static final long MISSING_ARTIFACTS_BACKOFF_MS = 10 * 60 * 1000;

    private final List<CatalogSource<?>> sources;
    private final AiModelSpecificationRepositoryService specifications;
    private final ArtifactService artifactService;
    /**
     * How often a full search runs instead of an incremental one. Full searches are what retry
     * models skipped for missing files (their creation time is behind the watermark), so the
     * default matches {@link #MISSING_ARTIFACTS_BACKOFF_MS}.
     */
    private final long fullSyncIntervalMs;
    /**
     * How long after a model is ready ({@link DiscoveredModel#readyAtMs()}) it is published, so
     * that metadata the publisher adds right after the model is included.
     */
    private final long settleMs;
    private final Map<CatalogSource<?>, SourceState> states = new IdentityHashMap<>();

    /** Overridable in tests. */
    LongSupplier clock = System::currentTimeMillis;

    @Autowired
    public CatalogSyncService(ObjectProvider<CatalogSource<?>> sources,
                              AiModelSpecificationRepositoryService specifications,
                              ArtifactService artifactService,
                              @Value("${aimodel.catalog.sync.full-interval-ms:${mlflow.sync.full-interval-ms:600000}}")
                              long fullSyncIntervalMs,
                              @Value("${aimodel.catalog.sync.settle-ms:60000}") long settleMs) {
        this(sources.orderedStream().toList(), specifications, artifactService, fullSyncIntervalMs, settleMs);
    }

    CatalogSyncService(List<CatalogSource<?>> sources, AiModelSpecificationRepositoryService specifications,
                       ArtifactService artifactService, long fullSyncIntervalMs, long settleMs) {
        this.sources = List.copyOf(sources);
        this.specifications = specifications;
        this.artifactService = artifactService;
        this.fullSyncIntervalMs = fullSyncIntervalMs;
        this.settleMs = Math.max(0, settleMs);
        if (this.settleMs > INCREMENTAL_OVERLAP_MS) {
            log.warn("Catalog sync: aimodel.catalog.sync.settle-ms ({} ms) is longer than the {} ms incremental searches "
                    + "look back, so new models are often found only by full searches and published up to "
                    + "aimodel.catalog.sync.full-interval-ms later", this.settleMs, INCREMENTAL_OVERLAP_MS);
        }
        for (CatalogSource<?> source : this.sources) {
            states.put(source, new SourceState());
        }
        if (!this.sources.isEmpty()) {
            log.info("Catalog sync: sources {}", this.sources.stream().map(CatalogSource::platform).toList());
        }
    }

    @Scheduled(fixedRateString = "${aimodel.catalog.sync.interval-ms:${mlflow.sync.interval-ms:60000}}",
               initialDelayString = "${aimodel.catalog.sync.interval-ms:${mlflow.sync.interval-ms:60000}}")
    public void syncAll() {
        for (CatalogSource<?> source : sources) {
            try {
                sync(source);
            } catch (RuntimeException e) {
                log.warn("Catalog sync ({}): search failed: {}", source.platform(), e.toString());
            }
        }
    }

    /** One tick for one source. */
    <T> void sync(CatalogSource<T> source) {
        SourceState state = states.get(source);
        String platform = source.platform();
        long now = clock.getAsLong();
        Long watermark = state.watermarkMs;
        boolean incremental = state.incrementalSupported
                && watermark != null
                && now - state.lastFullSearchMs < fullSyncIntervalMs;

        List<DiscoveredModel<T>> models = null;
        if (incremental) {
            try {
                models = source.discover(watermark - INCREMENTAL_OVERLAP_MS);
            } catch (RuntimeException e) {
                state.incrementalSupported = false;
                incremental = false;
                log.warn("Catalog sync ({}): incremental search failed ({}); using full searches from now on",
                        platform, e.getMessage());
            }
        }
        if (!incremental) {
            models = source.discover(null);
            state.lastFullSearchMs = now;
        }

        long newest = models.stream().mapToLong(DiscoveredModel::createdAtMs).max().orElse(0L);
        if (newest > 0 && (watermark == null || newest > watermark)) {
            state.watermarkMs = newest;
        }

        // Newest first; models without a creation time keep the source's order, last.
        List<DiscoveredModel<T>> ordered = new ArrayList<>(models);
        ordered.sort(Comparator.comparingLong((DiscoveredModel<T> m) -> m.createdAtMs()).reversed());

        int created = 0;
        int skipped = 0;
        for (DiscoveredModel<T> model : ordered) {
            if (syncOne(source, state, model, now)) {
                created++;
            } else {
                skipped++;
            }
        }
        if (created > 0 || !incremental) {
            log.info("Catalog sync ({}, {} search): finished – {} created, {} skipped",
                    platform, incremental ? "incremental" : "full", created, skipped);
        } else {
            log.debug("Catalog sync ({}, incremental search): nothing new", platform);
        }
    }

    /** @return true if a specification was created */
    private <T> boolean syncOne(CatalogSource<T> source, SourceState state, DiscoveredModel<T> model, long now) {
        String key = model.key();
        // 1. Already synced: no database lookup.
        if (state.synced.contains(key)) {
            return false;
        }
        // 2. Not finished on the platform. The status comes with every search, so there is no
        //    back-off: the model is looked at again on the next search that returns it.
        if (model.status() != DiscoveredModel.Status.READY) {
            if (state.reportedStatus.put(key, model.status()) != model.status()) {
                if (model.status() == DiscoveredModel.Status.PENDING) {
                    log.info("Catalog sync ({}): skipped model {} ({} {}) because the platform is still writing it. "
                            + "It is published once it is complete.", source.platform(), key, model.name(), model.version());
                } else {
                    log.warn("Catalog sync ({}): skipped model {} ({} {}) because the platform reports that writing it "
                            + "failed. It is not published unless that changes.", source.platform(), key, model.name(),
                            model.version());
                }
            }
            return false;
        }
        // 3. Ready only just now: wait, so that what the publisher adds right after the model
        //    (MLflow: the metrics linked to it) is in the specification, which is never updated.
        if (model.readyAtMs() > 0 && now - model.readyAtMs() < settleMs) {
            log.debug("Catalog sync ({}): model {} is ready since {} ms; publishing it after {} ms",
                    source.platform(), key, now - model.readyAtMs(), settleMs);
            return false;
        }
        // 4. Already in the catalog.
        if (specifications.findAiModelSpecificationByNameAndVersion(model.name(), model.version()) != null) {
            state.synced.add(key);
            return false;
        }
        // 5. Backing off after missing files.
        Long nextCheck = state.nextArtifactCheck.get(key);
        if (nextCheck != null && now < nextCheck) {
            return false;
        }
        // 6. Files stored yet?
        if (!source.hasArtifacts(model)) {
            if (nextCheck == null) {
                log.info("Catalog sync ({}): skipped model {} ({} {}) because no artifacts were found in storage. "
                        + "Backing off for 10 minutes.", source.platform(), key, model.name(), model.version());
            }
            state.nextArtifactCheck.put(key, now + MISSING_ARTIFACTS_BACKOFF_MS);
            return false;
        }
        try {
            AiModelSpecificationCreate specification = source.toSpecification(model);
            addPlatformNeutralFields(source, specification);
            specifications.createAiModelSpecification(specification);
            log.info("Catalog sync ({}): created AiModelSpecification for {} {}{}", source.platform(),
                    model.name(), model.version(), nextCheck != null ? " after previous back-off" : "");
            state.synced.add(key);
            state.nextArtifactCheck.remove(key);
            state.reportedStatus.remove(key);
            return true;
        } catch (Exception e) {
            log.warn("Catalog sync ({}): failed to create specification for {} {}: {}",
                    source.platform(), model.name(), model.version(), e.getMessage());
            return false;
        }
    }

    /**
     * {@code platform} and {@code artifactFormat} (unless the source set them), and one download
     * attachment per format the model can be downloaded in. That is decided exactly as the
     * download endpoint decides it ({@link ArtifactService#describeAll}: opt-out, locator,
     * allow-list, storage, size limit), so the catalog never advertises a download that would
     * fail.
     */
    void addPlatformNeutralFields(CatalogSource<?> source, AiModelSpecificationCreate specification) {
        if (specification.getSpecCharacteristic() == null) {
            specification.setSpecCharacteristic(new ArrayList<>());
        }
        ArtifactCharacteristics.addIfAbsent(specification.getSpecCharacteristic(), ArtifactCharacteristics.PLATFORM,
                "Model platform", source.platform());
        ArtifactCharacteristics.addIfAbsent(specification.getSpecCharacteristic(), ArtifactCharacteristics.ARTIFACT_FORMAT,
                "Format of the model files (see GET .../artifacts)", source.artifactFormat());
        if (DownloadAttachments.find(specification.getAttachment()).isPresent()) {
            return;
        }
        for (ModelArtifact artifact : artifactService.describeAll(AiModelSpecificationMapper.fromCreate(specification))) {
            specification.addAttachmentItem(
                    DownloadAttachments.create(specification.getName(), specification.getVersion(), artifact));
        }
    }

    /** What the sync remembers about one source between ticks. */
    private static final class SourceState {
        final Set<String> synced = ConcurrentHashMap.newKeySet();
        final Map<String, Long> nextArtifactCheck = new ConcurrentHashMap<>();
        /** Last status logged for a model that was not ready, so each change is logged once. */
        final Map<String, DiscoveredModel.Status> reportedStatus = new ConcurrentHashMap<>();
        /** Newest creation time seen; null until a search found a model with one. */
        volatile Long watermarkMs;
        volatile long lastFullSearchMs;
        /** Cleared for good when an incremental search fails. */
        volatile boolean incrementalSupported = true;
    }
}
