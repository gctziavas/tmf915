package org.etsi.osl.controllers.tmf915.catalog;

import org.etsi.osl.controllers.tmf915.artifacts.ArtifactCharacteristics;
import org.etsi.osl.controllers.tmf915.artifacts.ArtifactFile;
import org.etsi.osl.controllers.tmf915.artifacts.ArtifactService;
import org.etsi.osl.controllers.tmf915.artifacts.DownloadAttachments;
import org.etsi.osl.controllers.tmf915.artifacts.ModelArtifact;
import org.etsi.osl.controllers.tmf915.catalog.DiscoveredModel.Status;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.net.URI;
import java.util.List;

import static org.etsi.osl.controllers.tmf915.catalog.FakeCatalogSource.model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The generic sync, with a fake source. These are the scenarios of the former
 * {@code MlflowSyncServiceUnitTest} (fix-plan Part 3 items 2 and 3), now platform-neutral.
 */
class CatalogSyncServiceTest {

    private static final long FULL_INTERVAL = 600_000;
    private static final long TICK = 60_000;
    private static final long OVERLAP = CatalogSyncService.INCREMENTAL_OVERLAP_MS;
    /** The default of {@code aimodel.catalog.sync.settle-ms}. */
    private static final long SETTLE = 60_000;
    private static final long T0 = 10_000_000;

    private final AiModelSpecificationRepositoryService specifications = mock(AiModelSpecificationRepositoryService.class);
    private final ArtifactService artifactService = mock(ArtifactService.class);
    private final FakeCatalogSource source = new FakeCatalogSource("fake");
    private CatalogSyncService sync;
    private long now;

    @BeforeEach
    void setUp() {
        sync = newSync(source);
    }

    private CatalogSyncService newSync(CatalogSource<?>... sources) {
        return newSync(SETTLE, sources);
    }

    private CatalogSyncService newSync(long settleMs, CatalogSource<?>... sources) {
        CatalogSyncService s = new CatalogSyncService(List.of(sources), specifications, artifactService,
                FULL_INTERVAL, settleMs);
        now = T0;
        s.clock = () -> now;
        return s;
    }

    // ── Incremental search (fix-plan item 3) ─────────────────────────────

    @Test
    void firstTickIsFull_thenIncrementalSinceNewestMinusOverlap_thenFullAgainAfterInterval() {
        source.all = List.of(model("m-1", T0 - 100_000));

        sync.syncAll();                                      // t0: full
        now += TICK;
        sync.syncAll();                                      // t0+1m: incremental
        now = T0 + FULL_INTERVAL;
        sync.syncAll();                                      // t0+10m: full again

        assertEquals(java.util.Arrays.asList(null, T0 - 100_000 - OVERLAP, null), source.searches);
    }

    @Test
    void emptyIncrementalResult_isNotFollowedByAFullSearch() {
        source.all = List.of(model("m-1", T0 - 100_000));

        sync.syncAll();
        now += TICK;
        sync.syncAll();

        assertEquals(1, source.fullSearches());
        assertEquals(1, source.incrementalSearches().size());
    }

    @Test
    void watermarkAdvancesToNewestModelSeen() {
        source.all = List.of(model("m-1", 9_000_000));
        source.incrementalAnswers.add(List.of(model("m-2", 9_500_000)));

        sync.syncAll();                                      // full: watermark 9_000_000
        now += TICK;
        sync.syncAll();                                      // incremental: finds m-2
        now += TICK;
        sync.syncAll();                                      // incremental: since m-2

        assertEquals(List.of(9_000_000 - OVERLAP, 9_500_000 - OVERLAP), source.incrementalSearches());
        verify(specifications, times(2)).createAiModelSpecification(any());
    }

    @Test
    void failedIncrementalSearch_fallsBackToFull_andIsNotTriedAgain() {
        source.all = List.of(model("m-1", T0 - 100_000));
        source.incrementalFailure = new IllegalStateException("400 Bad Request: unknown filter");

        sync.syncAll();                                      // full
        now += TICK;
        sync.syncAll();                                      // incremental fails -> full in the same tick
        now += TICK;
        sync.syncAll();                                      // full, no further incremental attempt

        assertEquals(1, source.incrementalSearches().size());
        assertEquals(3, source.fullSearches());
    }

    @Test
    void modelsFoundAgainInTheOverlap_areNotCreatedTwice() {
        DiscoveredModel<String> m1 = model("m-1", T0 - 100_000);
        source.all = List.of(m1);
        source.incrementalAnswers.add(List.of(m1));

        sync.syncAll();
        now += TICK;
        sync.syncAll();

        verify(specifications, times(1)).createAiModelSpecification(any());
        verify(specifications, times(1)).findAiModelSpecificationByNameAndVersion("name-m-1", "v-m-1");
    }

    @Test
    void modelsWithoutCreationTime_neverStartIncrementalSearches() {
        // E.g. MLflow's classic registry: searched in full every tick.
        source.all = List.of(model("r-1", 0), model("r-2", 0));

        sync.syncAll();
        now += TICK;
        sync.syncAll();

        assertEquals(2, source.fullSearches());
        assertEquals(0, source.incrementalSearches().size());
    }

    @Test
    void newestModelsAreProcessedFirst() {
        source.all = List.of(model("old", 1_000), model("unknown", 0), model("new", 3_000), model("mid", 2_000));

        sync.syncAll();

        assertEquals(List.of("new", "mid", "old", "unknown"), source.converted);
    }

    // ── Caches and back-off (fix-plan item 2) ────────────────────────────

    @Test
    void cachesSyncedModels_andBacksOffOnMissingArtifacts() {
        source.all = List.of(model("existing", 0), model("churn", 0));
        when(specifications.findAiModelSpecificationByNameAndVersion("name-existing", "v-existing"))
                .thenReturn(new AiModelSpecification());
        source.withoutArtifacts.add("churn");

        sync.syncAll();                                      // t0
        verify(specifications).findAiModelSpecificationByNameAndVersion("name-existing", "v-existing");
        assertEquals(List.of("churn"), source.artifactChecks);

        now += TICK;
        sync.syncAll();                                      // t0+1m: existing cached, churn backing off
        verify(specifications, times(1)).findAiModelSpecificationByNameAndVersion("name-existing", "v-existing");
        assertEquals(1, source.artifactChecks.size());

        now += 10 * TICK;
        source.withoutArtifacts.clear();
        sync.syncAll();                                      // t0+11m: back-off over, churn synced
        assertEquals(2, source.artifactChecks.size());
        verify(specifications, times(1)).createAiModelSpecification(any());

        now += TICK;
        sync.syncAll();                                      // churn now cached as well
        // One DB lookup per tick while it was not yet synced (ticks 1-3; the DB is checked
        // before the back-off), none once cached (tick 4).
        verify(specifications, times(3)).findAiModelSpecificationByNameAndVersion("name-churn", "v-churn");
        verify(specifications, times(1)).createAiModelSpecification(any());
    }

    @Test
    void createsTheSourcesSpecification() {
        source.all = List.of(model("m-1", 1_000));

        sync.syncAll();

        ArgumentCaptor<AiModelSpecificationCreate> created = ArgumentCaptor.forClass(AiModelSpecificationCreate.class);
        verify(specifications).createAiModelSpecification(created.capture());
        assertEquals("name-m-1", created.getValue().getName());
        assertEquals("v-m-1", created.getValue().getVersion());
    }

    // ── Models the platform has not finished, and the settle time ────────

    @Test
    void pendingModel_isSkippedWithoutBackOff_andPublishedOnceReadyAndSettled() {
        long created = T0 - 100_000;
        long ready = T0 + TICK - 1_000;
        source.all = List.of(model("m-1", created, Status.PENDING, created));

        sync.syncAll();                                      // t0: still being written
        assertEquals(List.of(), source.artifactChecks, "a pending model is not checked in storage");
        verify(specifications, times(0)).findAiModelSpecificationByNameAndVersion(any(), any());

        now += TICK;
        source.incrementalAnswers.add(List.of(model("m-1", created, Status.READY, ready)));
        sync.syncAll();                                      // t0+1m: ready 1 s ago, settling
        verify(specifications, times(0)).createAiModelSpecification(any());

        now += TICK;
        source.incrementalAnswers.add(List.of(model("m-1", created, Status.READY, ready)));
        sync.syncAll();                                      // t0+2m: settled; no 10-minute back-off
        verify(specifications, times(1)).createAiModelSpecification(any());
        assertEquals(List.of("m-1"), source.converted);
    }

    @Test
    void failedModel_isNotPublished_whileItStaysFailed() {
        DiscoveredModel<String> failed = model("m-1", T0 - 100_000, Status.FAILED, T0 - 100_000);
        source.all = List.of(failed);

        sync.syncAll();
        now += TICK;
        source.incrementalAnswers.add(List.of(failed));
        sync.syncAll();

        verify(specifications, times(0)).createAiModelSpecification(any());
        verify(specifications, times(0)).findAiModelSpecificationByNameAndVersion(any(), any());
        assertEquals(List.of(), source.artifactChecks);
    }

    @Test
    void failedModel_isPublished_ifThePlatformLaterReportsItReady() {
        long created = T0 - 100_000;
        source.all = List.of(model("m-1", created, Status.FAILED, created));

        sync.syncAll();
        now += TICK;
        source.incrementalAnswers.add(List.of(model("m-1", created, Status.READY, created)));
        sync.syncAll();

        verify(specifications, times(1)).createAiModelSpecification(any());
    }

    @Test
    void settleTime_countsFromWhenTheModelWasReady_notFromItsCreation() {
        // Created an hour ago (a slow upload), ready 10 s ago.
        long created = T0 - 3_600_000;
        long ready = T0 - 10_000;
        DiscoveredModel<String> m1 = model("m-1", created, Status.READY, ready);
        source.all = List.of(m1);

        sync.syncAll();                                      // 10 s after ready
        verify(specifications, times(0)).createAiModelSpecification(any());
        assertEquals(List.of(), source.artifactChecks, "not checked in storage while settling");

        now += TICK;
        source.incrementalAnswers.add(List.of(m1));
        sync.syncAll();                                      // 70 s after ready
        verify(specifications, times(1)).createAiModelSpecification(any());
    }

    @Test
    void justCreatedModel_ofASourceWithoutStatus_settlesFromItsCreation() {
        DiscoveredModel<String> m1 = model("m-1", T0 - 1_000);
        source.all = List.of(m1);

        sync.syncAll();
        verify(specifications, times(0)).createAiModelSpecification(any());

        now += TICK;
        source.incrementalAnswers.add(List.of(m1));
        sync.syncAll();
        verify(specifications, times(1)).createAiModelSpecification(any());
    }

    @Test
    void modelWithoutReadyTime_isNotDelayed() {
        source.all = List.of(model("r-1", 0, Status.READY, 0));

        sync.syncAll();

        verify(specifications, times(1)).createAiModelSpecification(any());
    }

    @Test
    void settleZero_publishesAModelReadyNow() {
        CatalogSyncService noSettle = newSync(0, source);
        source.all = List.of(model("m-1", T0, Status.READY, T0));

        noSettle.syncAll();

        verify(specifications, times(1)).createAiModelSpecification(any());
    }

    // ── Failures ─────────────────────────────────────────────────────────

    @Test
    void aFailingModel_doesNotStopTheOthers_andIsRetriedNextTick() {
        source.all = List.of(model("bad", 2_000), model("good", 1_000));
        source.converter = m -> {
            if (m.key().equals("bad")) {
                throw new IllegalStateException("cannot map");
            }
            AiModelSpecificationCreate c = new AiModelSpecificationCreate();
            c.setName(m.name());
            return c;
        };

        sync.syncAll();
        verify(specifications, times(1)).createAiModelSpecification(any());

        now += TICK;
        source.incrementalAnswers.add(List.of(model("bad", 2_000)));
        sync.syncAll();
        assertEquals(List.of("bad", "good", "bad"), source.converted);
    }

    @Test
    void aFailingSource_doesNotStopTheOthers_andEachSourceHasItsOwnState() {
        FakeCatalogSource broken = new FakeCatalogSource("broken");
        broken.fullFailure = new IllegalStateException("connection refused");
        FakeCatalogSource other = new FakeCatalogSource("other");
        // Same key in both sources: the caches are per source.
        source.all = List.of(model("m-1", 1_000));
        other.all = List.of(model("m-1", 0));
        when(specifications.findAiModelSpecificationByNameAndVersion(any(), eq("v-m-1"))).thenReturn(null);
        CatalogSyncService multi = newSync(broken, source, other);

        multi.syncAll();

        assertEquals(1, broken.fullSearches());
        assertEquals(List.of("m-1"), source.converted);
        assertEquals(List.of("m-1"), other.converted);

        now += TICK;
        multi.syncAll();
        assertEquals(2, broken.fullSearches(), "a failed full search does not count as one");
        assertEquals(1, source.incrementalSearches().size());
        assertEquals(0, other.incrementalSearches().size());
    }

    @Test
    void withoutSources_doesNothing() {
        newSync().syncAll();
        verify(specifications, times(0)).createAiModelSpecification(any());
    }

    // ── Platform-neutral fields (every source) ───────────────────────────

    private static final ModelArtifact ARTIFACT = new ModelArtifact(URI.create("s3://mlflow/x"), "fake-format",
            List.of(new ArtifactFile("model.bin", 2000)), 2000);

    private AiModelSpecificationCreate createdSpecification() {
        ArgumentCaptor<AiModelSpecificationCreate> created = ArgumentCaptor.forClass(AiModelSpecificationCreate.class);
        verify(specifications).createAiModelSpecification(created.capture());
        return created.getValue();
    }

    @Test
    void everySpecificationGetsPlatformAndFormat_andTheAttachmentIfDownloadable() {
        source.all = List.of(model("m-1", 1_000));
        source.converter = m -> {
            AiModelSpecificationCreate c = new AiModelSpecificationCreate();
            c.setName("iris");
            c.setVersion("m-1");
            c.addSpecCharacteristicItem(ArtifactCharacteristics.of("artifactUri", "", "s3://mlflow/x"));
            return c;
        };
        when(artifactService.describeAll(any())).thenReturn(List.of(ARTIFACT));

        sync.syncAll();

        AiModelSpecificationCreate spec = createdSpecification();
        assertEquals("fake", ArtifactCharacteristics.value(spec.getSpecCharacteristic(), "platform").orElseThrow());
        assertEquals("fake-format", ArtifactCharacteristics.value(spec.getSpecCharacteristic(), "artifactFormat").orElseThrow());
        assertEquals(1, spec.getAttachment().size());
        assertEquals(DownloadAttachments.TYPE, spec.getAttachment().get(0).getAtType());
        assertEquals("iris-m-1-fake-format.zip", spec.getAttachment().get(0).getName());
        assertEquals(2000f, spec.getAttachment().get(0).getSize().getAmount());
        // Decided like the download endpoint: on the specification as it will be stored.
        ArgumentCaptor<AiModelSpecification> described = ArgumentCaptor.forClass(AiModelSpecification.class);
        verify(artifactService).describeAll(described.capture());
        assertEquals("fake", ArtifactCharacteristics.value(described.getValue().getSpecCharacteristic(), "platform").orElseThrow());
        assertEquals("s3://mlflow/x", ArtifactCharacteristics.value(described.getValue().getSpecCharacteristic(), "artifactUri").orElseThrow());
    }

    @Test
    void notDownloadable_getsNoAttachment_butStillPlatformAndFormat() {
        source.all = List.of(model("m-1", 1_000));
        when(artifactService.describeAll(any())).thenReturn(List.of());

        sync.syncAll();

        AiModelSpecificationCreate spec = createdSpecification();
        assertTrue(spec.getAttachment() == null || spec.getAttachment().isEmpty());
        assertEquals("fake", ArtifactCharacteristics.value(spec.getSpecCharacteristic(), "platform").orElseThrow());
    }

    @Test
    void valuesTheSourceSetItself_areKept() {
        source.all = List.of(model("m-1", 1_000));
        source.converter = m -> {
            AiModelSpecificationCreate c = new AiModelSpecificationCreate();
            c.addSpecCharacteristicItem(ArtifactCharacteristics.of("artifactFormat", "", "onnx"));
            c.addAttachmentItem(DownloadAttachments.create("n", "v", ARTIFACT));
            return c;
        };

        sync.syncAll();

        AiModelSpecificationCreate spec = createdSpecification();
        assertEquals(1, spec.getSpecCharacteristic().stream().filter(c -> "artifactFormat".equals(c.getName())).count());
        assertEquals("onnx", ArtifactCharacteristics.value(spec.getSpecCharacteristic(), "artifactFormat").orElseThrow());
        assertEquals(1, spec.getAttachment().size());
        verify(artifactService, times(0)).describeAll(any());
    }
}
