package org.etsi.osl.controllers.tmf915.artifacts;

/**
 * One file of a model artifact.
 *
 * @param path normalised path relative to the artifact's root (see {@link ArtifactPaths}); it is
 *             the entry name in the downloaded zip
 * @param size size in bytes
 */
public record ArtifactFile(String path, long size) {
}
