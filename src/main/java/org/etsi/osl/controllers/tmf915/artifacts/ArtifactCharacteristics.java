package org.etsi.osl.controllers.tmf915.artifacts;

import org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification;
import org.etsi.osl.controllers.tmf915.model.CharacteristicValueSpecification;

import java.util.List;
import java.util.Optional;

/**
 * The platform-neutral specification characteristics that model downloads use. Every catalog
 * source writes them; the generic download code reads them.
 */
public final class ArtifactCharacteristics {

    /** Model platform ({@code mlflow}, {@code kubeflow}, …); selects the {@link ArtifactLocator}. */
    public static final String PLATFORM = "platform";
    /** What the artifact's files are ({@code mlflow-model}, {@code onnx}, …). */
    public static final String ARTIFACT_FORMAT = "artifactFormat";
    /** {@code false} makes a specification non-downloadable (decisions A1, A3). */
    public static final String DOWNLOADABLE = "downloadable";

    private ArtifactCharacteristics() {
    }

    /**
     * The value of the named characteristic: its default value, else its first value.
     */
    public static Optional<String> value(List<CharacteristicSpecification> characteristics, String name) {
        if (characteristics == null) {
            return Optional.empty();
        }
        for (CharacteristicSpecification c : characteristics) {
            if (c == null || !name.equals(c.getName())) {
                continue;
            }
            List<CharacteristicValueSpecification> values = c.getCharacteristicValueSpecification();
            if (values == null || values.isEmpty()) {
                return Optional.empty();
            }
            CharacteristicValueSpecification chosen = values.stream()
                    .filter(v -> v != null && Boolean.TRUE.equals(v.getIsDefault()))
                    .findFirst()
                    .orElse(values.get(0));
            return chosen == null || chosen.getValue() == null
                    ? Optional.empty()
                    : Optional.of(String.valueOf(chosen.getValue()).trim());
        }
        return Optional.empty();
    }

    /** True if the characteristics opt the model out of downloads ({@code downloadable=false}). */
    public static boolean optedOut(List<CharacteristicSpecification> characteristics) {
        return value(characteristics, DOWNLOADABLE).map("false"::equalsIgnoreCase).orElse(false);
    }
}
