package org.etsi.osl.controllers.tmf915.artifacts;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Generic part of model downloads: picks the {@link ArtifactLocator} for a specification and
 * the {@link ArtifactStore} for its location, and enforces the rules that hold for every
 * platform: the publisher's opt-out ({@code downloadable=false}), the storage allow-list
 * ({@code aimodel.artifacts.allowed-locations}), the size limit
 * ({@code aimodel.artifacts.max-download-bytes}) and the time limit
 * ({@code aimodel.artifacts.download-timeout}).
 *
 * <p>All storage access goes through this class, so a location outside the allow-list is
 * never listed or read.
 */
@Service
public class ArtifactService {

    private static final Logger log = LoggerFactory.getLogger(ArtifactService.class);

    private final List<ArtifactLocator> locators;
    private final List<ArtifactStore> stores;
    private final List<URI> allowedLocations;
    private final long maxDownloadBytes;
    private final Duration downloadTimeout;
    private final Clock clock;

    @Autowired
    public ArtifactService(ObjectProvider<ArtifactLocator> locators,
                           ObjectProvider<ArtifactStore> stores,
                           @Value("${aimodel.artifacts.allowed-locations:s3://mlflow/}") List<String> allowedLocations,
                           @Value("${aimodel.artifacts.max-download-bytes:2147483648}") long maxDownloadBytes,
                           @Value("${aimodel.artifacts.download-timeout:15m}") Duration downloadTimeout) {
        this(locators.orderedStream().toList(), stores.orderedStream().toList(),
                allowedLocations, maxDownloadBytes, downloadTimeout, Clock.systemUTC());
    }

    ArtifactService(List<ArtifactLocator> locators, List<ArtifactStore> stores, List<String> allowedLocations,
                    long maxDownloadBytes, Duration downloadTimeout, Clock clock) {
        this.locators = List.copyOf(locators);
        this.stores = List.copyOf(stores);
        this.allowedLocations = allowedLocations.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(URI::create)
                .toList();
        this.maxDownloadBytes = maxDownloadBytes;
        this.downloadTimeout = downloadTimeout;
        this.clock = clock;
        log.info("Model downloads: allowed locations {}, max {} bytes, timeout {}, {} store(s)",
                this.allowedLocations, maxDownloadBytes, downloadTimeout, this.stores.size());
    }

    /** How long one download may take; the endpoint sets its request timeout from this. */
    public Duration downloadTimeout() {
        return downloadTimeout;
    }

    /**
     * Resolves what {@code GET …/artifacts} would send for this specification.
     *
     * @throws ArtifactNotAvailableException opted out, no locator, location not allowed, no
     *                                       store for it, or nothing stored there ({@code 404})
     * @throws ArtifactTooLargeException     above the size limit ({@code 413})
     * @throws ArtifactStorageException      storage could not be read ({@code 503})
     */
    public ModelArtifact resolve(AiModelSpecification specification) {
        String id = specification.getId();
        if (ArtifactCharacteristics.optedOut(specification.getSpecCharacteristic())) {
            throw new ArtifactNotAvailableException(
                    "AiModelSpecification " + id + " is not downloadable: its publisher set downloadable=false");
        }
        ArtifactLocator locator = locators.stream()
                .filter(l -> l.supports(specification))
                .findFirst()
                .orElseThrow(() -> noArtifact(id));
        ArtifactLocation location = locator.locate(specification);
        ArtifactStore store = storeFor(location.uri(), id).orElseThrow(() -> noArtifact(id));

        List<ArtifactFile> files;
        try {
            files = store.list(location.uri());
        } catch (IllegalArgumentException e) {
            log.warn("AiModelSpecification {}: artifact at {} has an unsafe file path ({}); not served",
                    id, location.uri(), e.getMessage());
            throw noArtifact(id);
        } catch (IOException e) {
            throw new ArtifactStorageException("Artifact storage is unavailable", e);
        }
        if (files.isEmpty()) {
            log.debug("AiModelSpecification {}: nothing stored at {}", id, location.uri());
            throw noArtifact(id);
        }
        ModelArtifact artifact = ModelArtifact.of(location, files);
        if (artifact.totalBytes() > maxDownloadBytes) {
            throw new ArtifactTooLargeException("The artifact of AiModelSpecification " + id + " is "
                    + artifact.totalBytes() + " bytes, above this server's limit of " + maxDownloadBytes
                    + " bytes (aimodel.artifacts.max-download-bytes)");
        }
        return artifact;
    }

    /**
     * For catalog sources describing a new specification: the artifact at {@code location}, if
     * it would be downloadable (allowed, readable, not empty, within the size limit). Never
     * throws; problems are logged and give an empty result.
     */
    public Optional<ModelArtifact> inspect(ArtifactLocation location) {
        Optional<ArtifactStore> store = storeFor(location.uri(), "(new)");
        if (store.isEmpty()) {
            return Optional.empty();
        }
        try {
            List<ArtifactFile> files = store.get().list(location.uri());
            if (files.isEmpty()) {
                return Optional.empty();
            }
            ModelArtifact artifact = ModelArtifact.of(location, files);
            if (artifact.totalBytes() > maxDownloadBytes) {
                log.info("Artifact at {} is {} bytes, above aimodel.artifacts.max-download-bytes; not advertised",
                        location.uri(), artifact.totalBytes());
                return Optional.empty();
            }
            return Optional.of(artifact);
        } catch (IOException | RuntimeException e) {
            log.warn("Could not inspect artifact at {}: {}", location.uri(), e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * What {@link #resolve} would serve, for advertising it, or empty if the specification is
     * not downloadable. Never throws.
     */
    public Optional<ModelArtifact> describe(AiModelSpecification specification) {
        if (ArtifactCharacteristics.optedOut(specification.getSpecCharacteristic())) {
            return Optional.empty();
        }
        Optional<ArtifactLocator> locator = locators.stream().filter(l -> l.supports(specification)).findFirst();
        if (locator.isEmpty()) {
            return Optional.empty();
        }
        try {
            return inspect(locator.get().locate(specification));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    /** False if no artifact store is configured (no S3 settings): nothing can be downloaded. */
    public boolean hasStores() {
        return !stores.isEmpty();
    }

    /**
     * Streams the artifact as a zip and logs the download (who, which specification, format,
     * bytes). Stops with {@link DownloadTimeoutException} after the download timeout.
     *
     * @return bytes of file content sent
     */
    public long writeZip(String specificationId, String user, ModelArtifact artifact, OutputStream out)
            throws IOException {
        ArtifactStore store = storeFor(artifact.location(), specificationId)
                .orElseThrow(() -> noArtifact(specificationId));
        Instant started = clock.instant();
        try {
            long bytes = new ArtifactZipWriter(clock).write(artifact, store, out, started.plus(downloadTimeout));
            log.info("Model download: user={} specification={} format={} files={} bytes={} durationMs={}",
                    user, specificationId, artifact.format(), artifact.files().size(), bytes,
                    Duration.between(started, clock.instant()).toMillis());
            return bytes;
        } catch (IOException | RuntimeException e) {
            log.warn("Model download failed: user={} specification={} format={} after {} ms: {}",
                    user, specificationId, artifact.format(),
                    Duration.between(started, clock.instant()).toMillis(), e.toString());
            throw e;
        }
    }

    /** The store for an allowed location; empty (and logged) if the location is not allowed. */
    private Optional<ArtifactStore> storeFor(URI location, String specificationId) {
        if (!isAllowed(location)) {
            log.warn("AiModelSpecification {}: artifact location {} is outside aimodel.artifacts.allowed-locations; "
                    + "not served", specificationId, location);
            return Optional.empty();
        }
        Optional<ArtifactStore> store = stores.stream().filter(s -> s.supports(location)).findFirst();
        if (store.isEmpty()) {
            log.debug("AiModelSpecification {}: no artifact store configured for {}", specificationId, location);
        }
        return store;
    }

    /**
     * True if the location lies under an allowed location: same scheme and authority (for S3,
     * the bucket), and a path below the allowed path, compared by whole segments. Locations
     * with {@code .} or {@code ..} segments, a query or a fragment are never allowed.
     */
    boolean isAllowed(URI location) {
        if (location == null || location.getScheme() == null || location.getRawAuthority() == null
                || location.getRawQuery() != null || location.getRawFragment() != null) {
            return false;
        }
        String path = location.getPath() == null ? "" : location.getPath();
        for (String segment : path.split("/")) {
            if (segment.equals(".") || segment.equals("..")) {
                return false;
            }
        }
        String dirPath = path.endsWith("/") ? path : path + "/";
        for (URI allowed : allowedLocations) {
            String allowedPath = allowed.getPath() == null || allowed.getPath().isEmpty() ? "/" : allowed.getPath();
            if (!allowedPath.endsWith("/")) {
                allowedPath = allowedPath + "/";
            }
            if (location.getScheme().equalsIgnoreCase(allowed.getScheme())
                    && location.getRawAuthority().equals(allowed.getRawAuthority())
                    && dirPath.startsWith(allowedPath)) {
                return true;
            }
        }
        return false;
    }

    /** One message for every "nothing to download" case, so it does not reveal which one. */
    private static ArtifactNotAvailableException noArtifact(String id) {
        return new ArtifactNotAvailableException("AiModelSpecification " + id + " has no downloadable artifact");
    }
}
