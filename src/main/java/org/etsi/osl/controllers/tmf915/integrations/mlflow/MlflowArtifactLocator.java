package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.artifacts.ArtifactCharacteristics;
import org.etsi.osl.controllers.tmf915.artifacts.ArtifactLocation;
import org.etsi.osl.controllers.tmf915.artifacts.ArtifactLocator;
import org.etsi.osl.controllers.tmf915.artifacts.ArtifactNotAvailableException;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

/**
 * Locates MLflow models: the {@code artifactUri} characteristic is the MLflow model directory,
 * format {@code mlflow-model} (the primary format). If the publisher logged an ONNX copy with
 * {@code log_model(..., extra_files=["model.onnx"])}, MLflow stores it as
 * {@code extra_files/model.onnx}, and the model is offered as {@code onnx} too: that one file.
 *
 * <p>Handles specifications with {@code platform=mlflow}, and, for specifications synced before
 * the {@code platform} characteristic existed, those without {@code platform} that carry
 * {@code artifactUri} plus {@code loggedModelId} (logged models) or {@code modelVersion}
 * (classic registry). That fallback can go once existing rows are backfilled.
 *
 * <p>Not conditional on {@code mlflow.enabled}: it reads the specification only, so models
 * synced earlier stay downloadable while the MLflow integration is off.
 */
@Component
public class MlflowArtifactLocator implements ArtifactLocator {

    public static final String PLATFORM = "mlflow";
    public static final String FORMAT = "mlflow-model";
    static final String ARTIFACT_URI = "artifactUri";
    public static final String ONNX_FORMAT = "onnx";
    /** Where MLflow puts {@code extra_files}, relative to the model directory. */
    static final String EXTRA_FILES_DIRECTORY = "extra_files";
    static final String ONNX_FILE = "model.onnx";

    @Override
    public boolean supports(AiModelSpecification specification) {
        List<CharacteristicSpecification> c = specification.getSpecCharacteristic();
        return ArtifactCharacteristics.value(c, ArtifactCharacteristics.PLATFORM)
                .map(PLATFORM::equalsIgnoreCase)
                .orElseGet(() -> isLegacyMlflowSpecification(c));
    }

    private static boolean isLegacyMlflowSpecification(List<CharacteristicSpecification> c) {
        return ArtifactCharacteristics.value(c, ARTIFACT_URI).isPresent()
                && (ArtifactCharacteristics.value(c, "loggedModelId").isPresent()
                    || ArtifactCharacteristics.value(c, "modelVersion").isPresent());
    }

    @Override
    public List<ArtifactLocation> locate(AiModelSpecification specification) {
        List<CharacteristicSpecification> c = specification.getSpecCharacteristic();
        String uri = ArtifactCharacteristics.value(c, ARTIFACT_URI).filter(s -> !s.isEmpty())
                .orElseThrow(() -> noArtifact(specification));
        URI location;
        try {
            location = new URI(uri);
        } catch (Exception e) {
            throw noArtifact(specification);
        }
        String format = ArtifactCharacteristics.value(c, ArtifactCharacteristics.ARTIFACT_FORMAT)
                .filter(s -> !s.isEmpty())
                .orElse(FORMAT);
        ArtifactLocation primary = new ArtifactLocation(location, format);
        if (ONNX_FORMAT.equals(format)) {
            return List.of(primary);
        }
        String root = uri.endsWith("/") ? uri.substring(0, uri.length() - 1) : uri;
        ArtifactLocation onnx = new ArtifactLocation(URI.create(root + "/" + EXTRA_FILES_DIRECTORY),
                ONNX_FORMAT, List.of(ONNX_FILE));
        return List.of(primary, onnx);
    }

    private static ArtifactNotAvailableException noArtifact(AiModelSpecification specification) {
        return new ArtifactNotAvailableException(
                "AiModelSpecification " + specification.getId() + " has no downloadable artifact");
    }
}
