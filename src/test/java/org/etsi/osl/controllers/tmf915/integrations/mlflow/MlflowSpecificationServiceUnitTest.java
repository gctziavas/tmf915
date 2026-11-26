package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.artifacts.ArtifactCharacteristics;
import org.etsi.osl.controllers.tmf915.artifacts.ArtifactFile;
import org.etsi.osl.controllers.tmf915.artifacts.ArtifactLocation;
import org.etsi.osl.controllers.tmf915.artifacts.ArtifactService;
import org.etsi.osl.controllers.tmf915.artifacts.DownloadAttachments;
import org.etsi.osl.controllers.tmf915.artifacts.ModelArtifact;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.AttachmentRefOrValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mlflow.api.proto.ModelRegistry.ModelVersion;
import org.mlflow.api.proto.ModelRegistry.ModelVersionTag;
import org.mlflow.api.proto.ModelRegistry.RegisteredModel;
import org.mlflow.api.proto.ModelRegistry.RegisteredModelTag;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Handoff 4.5 (lifecycleStatus) and model downloads (platform, artifactFormat, attachment). */
class MlflowSpecificationServiceUnitTest {

    private static final String ARTIFACT_URI = "s3://mlflow/1/models/m-8a1e/artifacts";

    private final MlflowClientService mlflowClient = mock(MlflowClientService.class);
    private final ArtifactService artifactService = mock(ArtifactService.class);
    private MlflowSpecificationService service;

    @BeforeEach
    void setUp() {
        service = new MlflowSpecificationService(mlflowClient, artifactService);
        ReflectionTestUtils.setField(service, "defaultLifecycleStatus", "Active");
        when(artifactService.inspect(any())).thenAnswer(inv -> {
            ArtifactLocation location = inv.getArgument(0);
            return Optional.of(ModelArtifact.of(location,
                    List.of(new ArtifactFile("MLmodel", 100), new ArtifactFile("model.pkl", 1900))));
        });
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
    void loggedModel_getsPlatformFormatAndDownloadAttachment() {
        AiModelSpecificationCreate spec = service.createSpecificationFromLoggedModel(loggedModel(), "iris-classifier", "");

        assertEquals("mlflow", characteristic(spec, "platform"));
        assertEquals("mlflow-model", characteristic(spec, "artifactFormat"));
        assertNull(characteristic(spec, "downloadable"));

        verify(artifactService).inspect(new ArtifactLocation(URI.create(ARTIFACT_URI), "mlflow-model"));
        assertEquals(1, spec.getAttachment().size());
        AttachmentRefOrValue attachment = spec.getAttachment().get(0);
        assertEquals(DownloadAttachments.TYPE, attachment.getAtType());
        assertEquals("mlflow-model", attachment.getAttachmentType());
        assertEquals("application/zip", attachment.getMimeType());
        assertEquals("iris-classifier-m-8a1e.zip", attachment.getName());
        assertEquals(2000f, attachment.getSize().getAmount());
        assertEquals("bytes", attachment.getSize().getUnits());
        assertTrue(attachment.getDescription().contains("2000 bytes"));
        assertNull(attachment.getHref(), "set once the specification has an id");
    }

    @Test
    void loggedModel_downloadableFalseTag_optsOut_withoutReadingStorage() {
        AiModelSpecificationCreate spec = service.createSpecificationFromLoggedModel(
                loggedModel(tag("downloadable", "false")), "iris-classifier", "");

        assertEquals("false", characteristic(spec, "downloadable"));
        assertEquals("mlflow", characteristic(spec, "platform"));
        assertTrue(spec.getAttachment().isEmpty());
        verify(artifactService, never()).inspect(any());
    }

    @Test
    void loggedModel_notDownloadable_getsNoAttachment() {
        org.mockito.Mockito.doReturn(Optional.empty()).when(artifactService).inspect(any());

        AiModelSpecificationCreate spec = service.createSpecificationFromLoggedModel(loggedModel(), "iris", "");

        assertTrue(spec.getAttachment().isEmpty());
        assertEquals("mlflow", characteristic(spec, "platform"));
    }

    @Test
    void classicRegistryModel_getsTheSameFields_andRegisteredModelTagOptsOut() throws Exception {
        String source = "s3://mlflow/2/abc/artifacts/model";
        ModelVersion version = ModelVersion.newBuilder().setName("churn").setVersion("2").setSource(source).build();
        when(mlflowClient.getRegisteredModel("churn")).thenReturn(Optional.of(RegisteredModel.newBuilder().setName("churn").build()));
        when(mlflowClient.getModelVersion("churn", "2")).thenReturn(Optional.of(version));

        AiModelSpecificationCreate spec = service.createSpecificationFromMlflow("churn", "2", "");

        assertEquals("mlflow", characteristic(spec, "platform"));
        assertEquals("mlflow-model", characteristic(spec, "artifactFormat"));
        assertEquals(source, characteristic(spec, "artifactUri"));
        assertEquals("churn-2.zip", spec.getAttachment().get(0).getName());

        // Opt-out on the registered model applies to all its versions; a version tag wins.
        when(mlflowClient.getRegisteredModel("churn")).thenReturn(Optional.of(RegisteredModel.newBuilder()
                .setName("churn").addTags(RegisteredModelTag.newBuilder().setKey("downloadable").setValue("false")).build()));
        AiModelSpecificationCreate optedOut = service.createSpecificationFromMlflow("churn", "2", "");
        assertEquals("false", characteristic(optedOut, "downloadable"));
        assertTrue(optedOut.getAttachment().isEmpty());

        when(mlflowClient.getModelVersion("churn", "2")).thenReturn(Optional.of(version.toBuilder()
                .addTags(ModelVersionTag.newBuilder().setKey("downloadable").setValue("true")).build()));
        AiModelSpecificationCreate versionAllows = service.createSpecificationFromMlflow("churn", "2", "");
        assertEquals("true", characteristic(versionAllows, "downloadable"));
        assertEquals(1, versionAllows.getAttachment().size());
    }
}
