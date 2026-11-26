package org.etsi.osl.controllers.tmf915.artifacts;

import java.net.URI;

/**
 * Where a specification's artifact is stored, and what it is.
 *
 * @param uri    the artifact's root, e.g. {@code s3://mlflow/1/models/m-123/artifacts}; read
 *               through the {@link ArtifactStore} for its scheme
 * @param format what the files are, e.g. {@code mlflow-model} (see the formats table in
 *               {@code docs/INTEGRATION_GUIDE.md})
 */
public record ArtifactLocation(URI uri, String format) {
}
