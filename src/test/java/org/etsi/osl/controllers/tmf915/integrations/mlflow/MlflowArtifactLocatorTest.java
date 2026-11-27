package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.artifacts.ArtifactLocation;
import org.etsi.osl.controllers.tmf915.artifacts.ArtifactNotAvailableException;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification;
import org.etsi.osl.controllers.tmf915.model.CharacteristicValueSpecification;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MlflowArtifactLocatorTest {

    private static final String URI_ = "s3://mlflow/1/models/m-1/artifacts";
    private final MlflowArtifactLocator locator = new MlflowArtifactLocator();

    static AiModelSpecification spec(String... nameValuePairs) {
        AiModelSpecification spec = new AiModelSpecification();
        spec.setId("spec-1");
        for (int i = 0; i < nameValuePairs.length; i += 2) {
            CharacteristicSpecification c = new CharacteristicSpecification();
            c.setName(nameValuePairs[i]);
            CharacteristicValueSpecification v = new CharacteristicValueSpecification();
            v.setValue(nameValuePairs[i + 1]);
            v.setIsDefault(true);
            c.addCharacteristicValueSpecificationItem(v);
            spec.addSpecCharacteristicItem(c);
        }
        return spec;
    }

    @Test
    void platformMlflow_isSupported_andLocatedByArtifactUri() {
        AiModelSpecification spec = spec("platform", "mlflow", "artifactUri", URI_);

        assertTrue(locator.supports(spec));
        assertEquals(new ArtifactLocation(URI.create(URI_), "mlflow-model"), locator.locate(spec).get(0));
    }

    @Test
    void artifactFormatCharacteristic_overridesTheDefaultFormat() {
        AiModelSpecification spec = spec("platform", "MLflow", "artifactUri", URI_, "artifactFormat", "onnx");
        assertEquals("onnx", locator.locate(spec).get(0).format());
    }

    @Test
    void otherPlatforms_areNotSupported_evenWithMlflowCharacteristics() {
        assertFalse(locator.supports(spec("platform", "kubeflow", "artifactUri", URI_, "loggedModelId", "m-1")));
    }

    @Test
    void legacySpecificationsWithoutPlatform_areSupported() {
        assertTrue(locator.supports(spec("loggedModelId", "m-1", "artifactUri", URI_)));
        assertTrue(locator.supports(spec("modelName", "churn", "modelVersion", "2", "artifactUri", URI_)));
    }

    @Test
    void specificationsWithoutMlflowIdentity_areNotSupported() {
        assertFalse(locator.supports(spec()));
        assertFalse(locator.supports(spec("artifactUri", URI_)));
        assertFalse(locator.supports(spec("loggedModelId", "m-1")));
    }

    @Test
    void missingOrInvalidArtifactUri_isNotAvailable() {
        assertThrows(ArtifactNotAvailableException.class, () -> locator.locate(spec("platform", "mlflow")));
        assertThrows(ArtifactNotAvailableException.class,
                () -> locator.locate(spec("platform", "mlflow", "artifactUri", "s3://mlflow/a b|c")));
    }

    @Test
    void offersOnnx_fromExtraFiles_asASecondFormat() {
        java.util.List<ArtifactLocation> locations = locator.locate(spec("platform", "mlflow", "artifactUri", URI_ + "/"));

        assertEquals(2, locations.size());
        assertEquals(new ArtifactLocation(URI.create(URI_ + "/"), "mlflow-model"), locations.get(0));
        assertEquals(new ArtifactLocation(URI.create(URI_ + "/extra_files"), "onnx", java.util.List.of("model.onnx")),
                locations.get(1));
    }

    @Test
    void anOnnxPrimary_isNotOfferedTwice() {
        assertEquals(1, locator.locate(spec("platform", "mlflow", "artifactUri", URI_, "artifactFormat", "onnx")).size());
    }
}
