package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mlflow.api.proto.ModelRegistry.ModelVersion;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MlflowSyncServiceUnitTest {

    private static final long FULL_INTERVAL = 600_000;
    private static final long TICK = 60_000;
    private static final long OVERLAP = MlflowSyncService.INCREMENTAL_OVERLAP_MS;

    @Mock
    private MlflowClientService mlflowClient;

    @Mock
    private MlflowSpecificationService specificationService;

    @Mock
    private AiModelSpecificationRepositoryService specificationRepository;

    private MlflowSyncService sync;
    private long now;

    @BeforeEach
    void setUp() throws Exception {
        sync = new MlflowSyncService(mlflowClient, specificationService, specificationRepository);
        org.springframework.test.util.ReflectionTestUtils.setField(sync, "fullSyncIntervalMs", FULL_INTERVAL);
        now = 10_000_000;
        sync.clock = () -> now;

        when(mlflowClient.getExperiment(anyString())).thenReturn(Optional.empty());
        when(mlflowClient.hasArtifactsByUri(any())).thenReturn(true);
        when(specificationService.createSpecificationFromLoggedModel(any(), any(), any()))
                .thenReturn(new AiModelSpecificationCreate());
        when(specificationService.createSpecificationFromMlflow(any(), any(), any()))
                .thenReturn(new AiModelSpecificationCreate());
    }

    // ── Item 3: incremental logged-model search ──────────────────────────

    @Test
    void firstTickIsFull_thenIncrementalSinceNewestMinusOverlap_thenFullAgainAfterInterval() {
        when(mlflowClient.searchAllLoggedModels()).thenReturn(List.of(loggedModel("m-1", now - 100_000)));
        when(mlflowClient.searchAllLoggedModelsCreatedAfter(anyLong())).thenReturn(List.of());

        sync.syncModels();                                   // t0: full
        verify(mlflowClient, times(1)).searchAllLoggedModels();

        now += TICK;
        sync.syncModels();                                   // t0+1m: incremental
        verify(mlflowClient).searchAllLoggedModelsCreatedAfter(10_000_000 - 100_000 - OVERLAP);
        verify(mlflowClient, times(1)).searchAllLoggedModels();

        now = 10_000_000 + FULL_INTERVAL;
        sync.syncModels();                                   // t0+10m: full again
        verify(mlflowClient, times(2)).searchAllLoggedModels();
    }

    @Test
    void emptyIncrementalResult_doesNotFallBackToClassicRegistry() {
        when(mlflowClient.searchAllLoggedModels()).thenReturn(List.of(loggedModel("m-1", now - 100_000)));
        when(mlflowClient.searchAllLoggedModelsCreatedAfter(anyLong())).thenReturn(List.of());

        sync.syncModels();
        now += TICK;
        sync.syncModels();

        verify(mlflowClient, never()).searchAllModelVersions();
    }

    @Test
    void watermarkAdvancesToNewestModelSeen() {
        when(mlflowClient.searchAllLoggedModels()).thenReturn(List.of(loggedModel("m-1", 9_000_000)));
        when(mlflowClient.searchAllLoggedModelsCreatedAfter(anyLong()))
                .thenReturn(List.of(loggedModel("m-2", 9_500_000)))
                .thenReturn(List.of());

        sync.syncModels();                                   // full: watermark 9_000_000
        now += TICK;
        sync.syncModels();                                   // incremental: finds m-2
        now += TICK;
        sync.syncModels();                                   // incremental: since m-2

        verify(mlflowClient).searchAllLoggedModelsCreatedAfter(9_000_000 - OVERLAP);
        verify(mlflowClient).searchAllLoggedModelsCreatedAfter(9_500_000 - OVERLAP);
        verify(specificationRepository, times(2)).createAiModelSpecification(any());
    }

    @Test
    void rejectedFilter_fallsBackToFullSearch_andStopsTryingIncremental() {
        when(mlflowClient.searchAllLoggedModels()).thenReturn(List.of(loggedModel("m-1", now - 100_000)));
        when(mlflowClient.searchAllLoggedModelsCreatedAfter(anyLong()))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        sync.syncModels();                                   // full
        now += TICK;
        sync.syncModels();                                   // incremental fails -> full in the same tick
        now += TICK;
        sync.syncModels();                                   // full, no further incremental attempt

        verify(mlflowClient, times(1)).searchAllLoggedModelsCreatedAfter(anyLong());
        verify(mlflowClient, times(3)).searchAllLoggedModels();
        verify(mlflowClient, never()).searchAllModelVersions();
    }

    @Test
    void reFetchedModelsInOverlapWindow_areNotCreatedTwice() {
        LoggedModel m1 = loggedModel("m-1", now - 100_000);
        when(mlflowClient.searchAllLoggedModels()).thenReturn(List.of(m1));
        when(mlflowClient.searchAllLoggedModelsCreatedAfter(anyLong())).thenReturn(List.of(m1));

        sync.syncModels();
        now += TICK;
        sync.syncModels();

        verify(specificationRepository, times(1)).createAiModelSpecification(any());
        verify(specificationRepository, times(1)).findAiModelSpecificationByNameAndVersion(any(), eq("m-1"));
    }

    // ── Item 2: classic registry caches ──────────────────────────────────

    @Test
    void classicRegistry_cachesSyncedVersions_andBacksOffOnMissingArtifacts() {
        when(mlflowClient.searchAllLoggedModels()).thenReturn(List.of());
        ModelVersion existing = modelVersion("fraud", "1");
        ModelVersion noArtifacts = modelVersion("churn", "2");
        when(mlflowClient.searchAllModelVersions()).thenReturn(List.of(existing, noArtifacts));
        when(specificationRepository.findAiModelSpecificationByNameAndVersion("fraud", "1"))
                .thenReturn(new AiModelSpecification());
        when(mlflowClient.hasArtifactsByUri("s3://mlflow/runs/churn/2")).thenReturn(false);

        sync.syncModels();                                   // t0
        verify(specificationRepository).findAiModelSpecificationByNameAndVersion("fraud", "1");
        verify(mlflowClient, times(1)).hasArtifactsByUri("s3://mlflow/runs/churn/2");

        now += TICK;
        sync.syncModels();                                   // t0+1m: fraud cached, churn backing off
        verify(specificationRepository, times(1)).findAiModelSpecificationByNameAndVersion("fraud", "1");
        verify(mlflowClient, times(1)).hasArtifactsByUri("s3://mlflow/runs/churn/2");

        now += 10 * TICK;
        when(mlflowClient.hasArtifactsByUri("s3://mlflow/runs/churn/2")).thenReturn(true);
        sync.syncModels();                                   // t0+11m: back-off over, churn synced
        verify(mlflowClient, times(2)).hasArtifactsByUri("s3://mlflow/runs/churn/2");
        verify(specificationRepository, times(1)).createAiModelSpecification(any());

        now += TICK;
        sync.syncModels();                                   // churn now cached as well
        // One DB lookup per tick while it was not yet synced (ticks 1-3; the DB is checked
        // before the back-off, as on the logged-model path), none once cached (tick 4).
        verify(specificationRepository, times(3)).findAiModelSpecificationByNameAndVersion("churn", "2");
        verify(specificationRepository, times(1)).createAiModelSpecification(any());
    }

    // ── helpers ──────────────────────────────────────────────────────────

    private static LoggedModel loggedModel(String id, long createdMs) {
        LoggedModel.LoggedModelInfo info = new LoggedModel.LoggedModelInfo();
        info.setModelId(id);
        info.setExperimentId("1");
        info.setCreationTimestampMs(createdMs);
        info.setArtifactUri("s3://mlflow/1/models/" + id + "/artifacts");
        LoggedModel model = new LoggedModel();
        model.setInfo(info);
        return model;
    }

    private static ModelVersion modelVersion(String name, String version) {
        return ModelVersion.newBuilder()
                .setName(name)
                .setVersion(version)
                .setSource("runs/" + name + "/" + version)
                .build();
    }
}
