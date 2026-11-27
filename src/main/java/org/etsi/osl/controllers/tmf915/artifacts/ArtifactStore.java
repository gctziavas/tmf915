package org.etsi.osl.controllers.tmf915.artifacts;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

/**
 * Reads artifacts from one kind of storage, chosen by URI scheme ({@code s3://}, later
 * {@code oci://}, …). Has no platform knowledge. Callers go through {@link ArtifactService},
 * which checks the location against the allow-list before a store is asked.
 */
public interface ArtifactStore {

    /** Whether this store reads locations like this one (usually: the URI's scheme). */
    boolean supports(URI location);

    /**
     * Lists the files under the location, recursively, with paths relative to it (normalised
     * with {@link ArtifactPaths#normalize}). Empty if nothing is stored there.
     *
     * @throws IOException if storage cannot be read, or a file has an unsafe path
     */
    List<ArtifactFile> list(URI location) throws IOException;

    /** Opens one file returned by {@link #list} for reading. The caller closes the stream. */
    InputStream open(URI location, ArtifactFile file) throws IOException;
}
