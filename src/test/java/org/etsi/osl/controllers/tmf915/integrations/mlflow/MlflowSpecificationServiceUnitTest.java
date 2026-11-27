package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.artifacts.ArtifactCharacteristics;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mlflow.api.proto.ModelRegistry.ModelVersion;
import org.mlflow.api.proto.ModelRegistry.ModelVersionTag;
import org.mlflow.api.proto.ModelRegistry.RegisteredModel;
import org.mlflow.api.proto.ModelRegistry.RegisteredModelTag;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The MLflow → specification mapping: handoff 4.5 (lifecycleStatus) and the MLflow part of model
 * downloads (the {@code downloadable} tag, and the {@code artifactUri} the locator reads).
 * {@code platform}, {@code artifactFormat} and the attachment are the catalog sync's
 * ({@code CatalogSyncServiceTest}).
 */
class MlflowSpecificationServiceUnitTest {

    private static final String ARTIFACT_URI = "s3://mlflow/1/models/m-8a1e/artifacts";

    private final MlflowClientService mlflowClient = mock(MlflowClientService.class);
    private MlflowSpecificationService service;

    @BeforeEach
    void setUp() {
        service = new MlflowSpecificationService(mlflowClient);
        ReflectionTestUtils.setField(service, "defaultLifecycleStatus", "Active");
    }

    private static LoggedModel loggedModel(LoggedModel.Tag... tags) {
        LoggedModel.LoggedModelInfo info = new LoggedModel.LoggedModelInfo();
        info.setModelId("m-8a1e");
        info.setExperimentId("1");
        info.setArtifactUri(ARTIFACT_URI);
        info.setTags(List.of(tags));
        LoggedModel model = new LoggedModel();
        model.setInfo(info);
        return model;
    }

    private static LoggedModel.Tag tag(String key, String value) {
        LoggedModel.Tag tag = new LoggedModel.Tag();
        ReflectionTestUtils.setField(tag, "key", key);
        ReflectionTestUtils.setField(tag, "value", value);
        return tag;
    }

    private static String characteristic(AiModelSpecificationCreate spec, String name) {
        return ArtifactCharacteristics.value(spec.getSpecCharacteristic(), name).orElse(null);
    }

    @Test
    void loggedModelSpecification_getsTheConfiguredLifecycleStatus() {
        AiModelSpecificationCreate spec = service.createSpecificationFromLoggedModel(loggedModel(), "iris-classifier", "");

        assertEquals("Active", spec.getLifecycleStatus());
        assertEquals("iris-classifier", spec.getName());
        assertEquals("m-8a1e", spec.getVersion());
    }

    @Test
    void loggedModel_carriesItsArtifactUri_andNoPlatformNeutralFields() {
        AiModelSpecificationCreate spec = service.createSpecificationFromLoggedModel(loggedModel(), "iris-classifier", "");

        assertEquals(ARTIFACT_URI, characteristic(spec, "artifactUri"));
        assertNull(characteristic(spec, "downloadable"));
        assertNull(characteristic(spec, "platform"), "added by the catalog sync");
        assertTrue(spec.getAttachment().isEmpty(), "added by the catalog sync");
    }

    @Test
    void loggedModel_downloadableTag_becomesTheCharacteristic() {
        AiModelSpecificationCreate spec = service.createSpecificationFromLoggedModel(
                loggedModel(tag("downloadable", " false ")), "iris-classifier", "");

        assertEquals("false", characteristic(spec, "downloadable"));
    }

    @Test
    void classicRegistry_registeredModelTagOptsOut_andAVersionTagWins() throws Exception {
        String source = "s3://mlflow/2/abc/artifacts/model";
        ModelVersion version = ModelVersion.newBuilder().setName("churn").setVersion("2").setSource(source).build();
        when(mlflowClient.getModelVersion("churn", "2")).thenReturn(Optional.of(version));
        when(mlflowClient.getRegisteredModel("churn")).thenReturn(Optional.of(RegisteredModel.newBuilder().setName("churn").build()));

        AiModelSpecificationCreate plain = service.createSpecificationFromMlflow("churn", "2", "");
        assertEquals(source, characteristic(plain, "artifactUri"));
        assertNull(characteristic(plain, "downloadable"));

        when(mlflowClient.getRegisteredModel("churn")).thenReturn(Optional.of(RegisteredModel.newBuilder()
                .setName("churn").addTags(RegisteredModelTag.newBuilder().setKey("downloadable").setValue("false")).build()));
        assertEquals("false", characteristic(service.createSpecificationFromMlflow("churn", "2", ""), "downloadable"));

        when(mlflowClient.getModelVersion("churn", "2")).thenReturn(Optional.of(version.toBuilder()
                .addTags(ModelVersionTag.newBuilder().setKey("downloadable").setValue("true")).build()));
        assertEquals("true", characteristic(service.createSpecificationFromMlflow("churn", "2", ""), "downloadable"));
    }
}
