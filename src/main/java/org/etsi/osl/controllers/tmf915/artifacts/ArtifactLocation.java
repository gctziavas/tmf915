package org.etsi.osl.controllers.tmf915.artifacts;

import java.net.URI;
import java.util.List;

/**
 * Where one format of a specification's artifact is stored, and what it is.
 *
 * @param uri    the root the files are under, e.g. {@code s3://mlflow/1/models/m-123/artifacts};
 *               read through the {@link ArtifactStore} for its scheme. Zip entry names are
 *               relative to it.
 * @param format what the files are, e.g. {@code mlflow-model} (see the formats table in
 *               {@code docs/INTEGRATION_GUIDE.md})
 * @param paths  the files that make up this format, relative to {@code uri}; all must exist, or
 *               the format is not available. Empty: every file under {@code uri}.
 */
public record ArtifactLocation(URI uri, String format, List<String> paths) {

    public ArtifactLocation {
        paths = List.copyOf(paths);
    }

    /** Every file under {@code uri}. */
    public ArtifactLocation(URI uri, String format) {
        this(uri, format, List.of());
    }
}
