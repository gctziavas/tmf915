package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.catalog.DiscoveredModel;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mlflow.api.proto.ModelRegistry.ModelVersion;
import org.mlflow.api.proto.Service.Experiment;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** The MLflow parts of the catalog sync: which MLflow search runs, and how models are described. */
class MlflowCatalogSourceTest {

    private final MlflowClientService mlflowClient = mock(MlflowClientService.class);
    private final MlflowSpecificationService specificationService = mock(MlflowSpecificationService.class);
    private final MlflowCatalogSource source = new MlflowCatalogSource(mlflowClient, specificationService);

    @BeforeEach
    void setUp() {
        when(mlflowClient.getExperiment(anyString())).thenReturn(Optional.empty());
        when(mlflowClient.getExperiment("1")).thenReturn(Optional.of(Experiment.newBuilder()
                .setExperimentId("1").setName("iris-classifier").build()));
    }

    private static LoggedModel loggedModel(String id, String experimentId, long createdMs) {
        LoggedModel.LoggedModelInfo info = new LoggedModel.LoggedModelInfo();
        info.setModelId(id);
        info.setExperimentId(experimentId);
        info.setCreationTimestampMs(createdMs);
        info.setArtifactUri("s3://mlflow/" + experimentId + "/models/" + id + "/artifacts");
        LoggedModel model = new LoggedModel();
        model.setInfo(info);
        return model;
    }

    private static ModelVersion modelVersion(String name, String version, String source) {
        return ModelVersion.newBuilder().setName(name).setVersion(version).setSource(source).build();
    }

    @Test
    void fullSearch_describesLoggedModels_byExperimentAndModelId() {
        when(mlflowClient.searchAllLoggedModels()).thenReturn(List.of(
                loggedModel("m-1", "1", 1_000), loggedModel("m-2", "1", 2_000), loggedModel("m-3", "7", 3_000)));

        List<DiscoveredModel<MlflowCatalogSource.MlflowModel>> found = source.discover(null);

        assertEquals(List.of("iris-classifier:m-1", "iris-classifier:m-2", "experiment-7:m-3"),
                found.stream().map(DiscoveredModel::key).toList());
        assertEquals("iris-classifier", found.get(0).name());
        assertEquals("m-1", found.get(0).version());
        assertEquals(1_000, found.get(0).createdAtMs());
        verify(mlflowClient, times(1)).getExperiment("1");   // once per experiment
        verify(mlflowClient, never()).searchAllModelVersions();
    }

    @Test
    void fullSearch_withoutLoggedModels_usesTheClassicRegistry_withoutCreationTime() {
        when(mlflowClient.searchAllLoggedModels()).thenReturn(List.of());
        when(mlflowClient.searchAllModelVersions()).thenReturn(List.of(
                modelVersion("churn", "2", "runs/churn/2"),
                modelVersion("churn", "2", "runs/churn/2"),          // duplicate
                modelVersion("fraud", "1", "s3://mlflow/2/abc/artifacts/model")));

        List<DiscoveredModel<MlflowCatalogSource.MlflowModel>> found = source.discover(null);

        assertEquals(List.of("registry:churn:2", "registry:fraud:1"), found.stream().map(DiscoveredModel::key).toList());
        assertEquals("churn", found.get(0).name());
        assertEquals("2", found.get(0).version());
        assertEquals(0, found.get(0).createdAtMs(), "registry versions never start incremental searches");
    }

    @Test
    void failedLoggedModelSearch_fallsBackToTheClassicRegistry() {
        when(mlflowClient.searchAllLoggedModels()).thenThrow(new IllegalStateException("404"));
        when(mlflowClient.searchAllModelVersions()).thenReturn(List.of(modelVersion("churn", "2", "runs/churn/2")));

        assertEquals(1, source.discover(null).size());
    }

    @Test
    void incrementalSearch_asksForNewerLoggedModels_andNeverTheRegistry() {
        when(mlflowClient.searchAllLoggedModelsCreatedAfter(5_000)).thenReturn(List.of());

        assertTrue(source.discover(5_000L).isEmpty());

        verify(mlflowClient).searchAllLoggedModelsCreatedAfter(5_000);
        verify(mlflowClient, never()).searchAllLoggedModels();
        verify(mlflowClient, never()).searchAllModelVersions();
    }

    @Test
    void incrementalSearch_rejectedByMlflow_throws_soTheSyncFallsBack() {
        when(mlflowClient.searchAllLoggedModelsCreatedAfter(5_000))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        assertThrows(HttpClientErrorException.class, () -> source.discover(5_000L));
    }

    @Test
    void artifactsAreCheckedInStorage() {
        when(mlflowClient.searchAllLoggedModels()).thenReturn(List.of(loggedModel("m-1", "1", 1_000)));
        when(mlflowClient.hasArtifactsByUri(any())).thenReturn(true);
        assertTrue(source.hasArtifacts(source.discover(null).get(0)));
        verify(mlflowClient).hasArtifactsByUri("s3://mlflow/1/models/m-1/artifacts");

        when(mlflowClient.searchAllLoggedModels()).thenReturn(List.of());
        when(mlflowClient.searchAllModelVersions()).thenReturn(List.of(
                modelVersion("churn", "2", "runs/churn/2"),
                modelVersion("fraud", "1", "s3://mlflow/2/abc/artifacts/model")));
        List<DiscoveredModel<MlflowCatalogSource.MlflowModel>> registry = source.discover(null);
        when(mlflowClient.hasArtifactsByUri("s3://mlflow/runs/churn/2")).thenReturn(false);
        assertFalse(source.hasArtifacts(registry.get(0)));
        source.hasArtifacts(registry.get(1));
        verify(mlflowClient).hasArtifactsByUri("s3://mlflow/runs/churn/2");
        // A full URI is used as it is (it used to get the bucket prefixed twice).
        verify(mlflowClient).hasArtifactsByUri("s3://mlflow/2/abc/artifacts/model");
    }

    @Test
    void registryArtifactUri() {
        assertEquals("s3://mlflow/runs/a/1", MlflowCatalogSource.registryArtifactUri("runs/a/1"));
        assertEquals("s3://bucket/x", MlflowCatalogSource.registryArtifactUri("s3://bucket/x"));
        assertEquals("models:/m-1", MlflowCatalogSource.registryArtifactUri("models:/m-1"));
        assertEquals("s3://mlflow/a b", MlflowCatalogSource.registryArtifactUri("a b"));
    }

    @Test
    void specificationsComeFromMlflowSpecificationService() throws Exception {
        LoggedModel logged = loggedModel("m-1", "1", 1_000);
        when(mlflowClient.searchAllLoggedModels()).thenReturn(List.of(logged));
        AiModelSpecificationCreate fromLogged = new AiModelSpecificationCreate();
        when(specificationService.createSpecificationFromLoggedModel(logged, "iris-classifier", "")).thenReturn(fromLogged);

        DiscoveredModel<MlflowCatalogSource.MlflowModel> model = source.discover(null).get(0);
        assertInstanceOf(MlflowCatalogSource.LoggedModelEntry.class, model.details());
        assertSame(fromLogged, source.toSpecification(model));

        when(mlflowClient.searchAllLoggedModels()).thenReturn(List.of());
        when(mlflowClient.searchAllModelVersions()).thenReturn(List.of(modelVersion("churn", "2", "runs/churn/2")));
        AiModelSpecificationCreate fromRegistry = new AiModelSpecificationCreate();
        when(specificationService.createSpecificationFromMlflow("churn", "2", "")).thenReturn(fromRegistry);
        assertSame(fromRegistry, source.toSpecification(source.discover(null).get(0)));
    }

    @Test
    void platformAndFormat() {
        assertEquals("mlflow", source.platform());
        assertEquals("mlflow-model", source.artifactFormat());
    }
}
