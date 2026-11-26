package org.etsi.osl.controllers.tmf915.artifacts;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Clock;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Writes an artifact as a zip while it is read from storage. Memory use is one copy buffer,
 * whatever the artifact's size: nothing is held in memory or written to disk.
 */
public final class ArtifactZipWriter {

    static final int BUFFER_SIZE = 64 * 1024;

    private final Clock clock;

    public ArtifactZipWriter(Clock clock) {
        this.clock = clock;
    }

    /**
     * Writes the artifact's files to {@code out} as a zip. Does not close {@code out}.
     *
     * @param deadline the writer stops with {@link DownloadTimeoutException} once this has
     *                 passed, so a slow download cannot hold a connection and a storage stream
     *                 open indefinitely
     * @return bytes of file content written (before compression)
     * @throws IllegalArgumentException if a file's path is unsafe; checked again here so no
     *                                  store can produce a zip that writes outside the target
     */
    public long write(ModelArtifact artifact, ArtifactStore store, OutputStream out, Instant deadline)
            throws IOException {
        ZipOutputStream zip = new ZipOutputStream(new NonClosingOutputStream(out));
        zip.setLevel(Deflater.BEST_SPEED);
        byte[] buffer = new byte[BUFFER_SIZE];
        Set<String> names = new HashSet<>();
        long written = 0;
        for (ArtifactFile file : artifact.files()) {
            String name = ArtifactPaths.normalize(file.path());
            if (!names.add(name)) {
                throw new IllegalArgumentException("duplicate artifact path: " + name);
            }
            checkDeadline(deadline);
            zip.putNextEntry(new ZipEntry(name));
            try (InputStream in = store.open(artifact.location(), file)) {
                int n;
                while ((n = in.read(buffer)) != -1) {
                    zip.write(buffer, 0, n);
                    written += n;
                    checkDeadline(deadline);
                }
            }
            zip.closeEntry();
        }
        zip.finish();
        zip.flush();
        return written;
    }

    private void checkDeadline(Instant deadline) throws DownloadTimeoutException {
        if (clock.instant().isAfter(deadline)) {
            throw new DownloadTimeoutException("download exceeded aimodel.artifacts.download-timeout");
        }
    }

    /** The zip is finished, not closed: closing the response stream is the container's job. */
    private static final class NonClosingOutputStream extends FilterOutputStream {
        NonClosingOutputStream(OutputStream out) {
            super(out);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
        }

        @Override
        public void close() throws IOException {
            flush();
        }
    }
}
