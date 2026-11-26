package org.etsi.osl.controllers.tmf915.artifacts;

import java.net.URI;
import java.util.List;

/**
 * A resolved, downloadable artifact: its location, format and files.
 *
 * @param totalBytes sum of the files' sizes
 */
public record ModelArtifact(URI location, String format, List<ArtifactFile> files, long totalBytes) {

    public ModelArtifact {
        files = List.copyOf(files);
    }

    public static ModelArtifact of(ArtifactLocation location, List<ArtifactFile> files) {
        long total = files.stream().mapToLong(ArtifactFile::size).sum();
        return new ModelArtifact(location.uri(), location.format(), files, total);
    }
}
