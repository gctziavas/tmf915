package org.etsi.osl.controllers.tmf915.artifacts;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArtifactZipWriterTest {

    private static final URI LOCATION = URI.create("s3://mlflow/1/models/m-1/artifacts");
    private static final Instant NOW = Instant.parse("2026-11-26T12:00:00Z");
    private static final Instant FAR = NOW.plus(Duration.ofHours(1));

    private final ArtifactZipWriter writer = new ArtifactZipWriter(Clock.fixed(NOW, ZoneOffset.UTC));

    @Test
    void entriesAreTheFilesRelativePaths_withTheirContent() throws IOException {
        InMemoryArtifactStore store = new InMemoryArtifactStore()
                .put(LOCATION.toString(), "MLmodel", "flavors: {}".getBytes(StandardCharsets.UTF_8))
                .put(LOCATION.toString(), "model/model.pkl", new byte[]{1, 2, 3});
        ModelArtifact artifact = ModelArtifact.of(new ArtifactLocation(LOCATION, "mlflow-model"), store.list(LOCATION));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        long written = writer.write(artifact, store, out, FAR);

        assertEquals(14, written);
        Map<String, byte[]> entries = unzip(out.toByteArray());
        assertEquals(List.of("MLmodel", "model/model.pkl"), List.copyOf(entries.keySet()));
        assertArrayEquals(new byte[]{1, 2, 3}, entries.get("model/model.pkl"));
    }

    @Test
    void refusesUnsafePaths_evenIfAStoreReturnsThem() {
        for (String unsafe : List.of("../outside", "/abs/path", "a/../../b")) {
            ModelArtifact artifact = new ModelArtifact(LOCATION, "mlflow-model",
                    List.of(new ArtifactFile(unsafe, 1)), 1);
            ArtifactStore store = new InMemoryArtifactStore();
            assertThrows(IllegalArgumentException.class,
                    () -> writer.write(artifact, store, OutputStream.nullOutputStream(), FAR), unsafe);
        }
    }

    @Test
    void refusesDuplicateEntries() {
        ModelArtifact artifact = new ModelArtifact(LOCATION, "mlflow-model",
                List.of(new ArtifactFile("a/b", 1), new ArtifactFile("a//b", 1)), 2);
        InMemoryArtifactStore store = new InMemoryArtifactStore().put(LOCATION.toString(), "a/b", new byte[]{1});
        assertThrows(IllegalArgumentException.class,
                () -> writer.write(artifact, store, OutputStream.nullOutputStream(), FAR));
    }

    /**
     * 50 MB of incompressible data: at every read, what has been read from storage but not yet
     * written to the client stays below 1 MB. The zip is passed through, not assembled first.
     */
    @Test
    void streams_withoutHoldingTheArtifactInMemory() throws IOException {
        long size = 50L * 1024 * 1024;
        AtomicLong readFromStorage = new AtomicLong();
        AtomicLong sentToClient = new AtomicLong();
        AtomicLong maxBacklog = new AtomicLong();

        ArtifactStore store = new ArtifactStore() {
            @Override
            public boolean supports(URI location) {
                return true;
            }

            @Override
            public List<ArtifactFile> list(URI location) {
                return List.of(new ArtifactFile("model/weights.bin", size));
            }

            @Override
            public InputStream open(URI location, ArtifactFile file) {
                return new RandomBytes(size, readFromStorage, sentToClient, maxBacklog);
            }
        };
        OutputStream client = new OutputStream() {
            @Override
            public void write(int b) {
                sentToClient.incrementAndGet();
            }

            @Override
            public void write(byte[] b, int off, int len) {
                sentToClient.addAndGet(len);
            }
        };
        ModelArtifact artifact = ModelArtifact.of(new ArtifactLocation(LOCATION, "mlflow-model"), store.list(LOCATION));

        long written = writer.write(artifact, store, client, FAR);

        assertEquals(size, written);
        assertEquals(size, readFromStorage.get());
        assertTrue(sentToClient.get() >= size, "random data does not compress");
        assertTrue(maxBacklog.get() < 1024 * 1024, "backlog was " + maxBacklog.get() + " bytes");
    }

    @Test
    void stopsAtTheDeadline() throws IOException {
        // Each read moves the clock on by one minute; the deadline is 90 seconds away.
        MutableClock clock = new MutableClock(NOW);
        ArtifactZipWriter timed = new ArtifactZipWriter(clock);
        ArtifactStore store = new ArtifactStore() {
            @Override
            public boolean supports(URI location) {
                return true;
            }

            @Override
            public List<ArtifactFile> list(URI location) {
                return List.of(new ArtifactFile("big.bin", 10L * ArtifactZipWriter.BUFFER_SIZE));
            }

            @Override
            public InputStream open(URI location, ArtifactFile file) {
                return new InputStream() {
                    int left = 10;

                    @Override
                    public int read() {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public int read(byte[] b, int off, int len) {
                        clock.advance(Duration.ofMinutes(1));
                        return left-- > 0 ? len : -1;
                    }
                };
            }
        };
        ModelArtifact artifact = ModelArtifact.of(new ArtifactLocation(LOCATION, "mlflow-model"), store.list(LOCATION));

        assertThrows(DownloadTimeoutException.class,
                () -> timed.write(artifact, store, OutputStream.nullOutputStream(), NOW.plusSeconds(90)));
    }

    @Test
    void doesNotCloseTheClientStream() throws IOException {
        boolean[] closed = {false};
        OutputStream out = new ByteArrayOutputStream() {
            @Override
            public void close() {
                closed[0] = true;
            }
        };
        InMemoryArtifactStore store = new InMemoryArtifactStore().put(LOCATION.toString(), "MLmodel", new byte[]{1});
        writer.write(ModelArtifact.of(new ArtifactLocation(LOCATION, "mlflow-model"), store.list(LOCATION)),
                store, out, FAR);
        assertFalse(closed[0]);
    }

    static Map<String, byte[]> unzip(byte[] zip) throws IOException {
        Map<String, byte[]> entries = new LinkedHashMap<>();
        try (ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(zip))) {
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                entries.put(entry.getName(), in.readAllBytes());
            }
        }
        return entries;
    }

    /** Pseudo-random bytes generated on demand; records how far reading runs ahead of the client. */
    private static final class RandomBytes extends InputStream {
        private final Random random = new Random(42);
        private long left;
        private final AtomicLong read;
        private final AtomicLong sent;
        private final AtomicLong maxBacklog;

        RandomBytes(long size, AtomicLong read, AtomicLong sent, AtomicLong maxBacklog) {
            this.left = size;
            this.read = read;
            this.sent = sent;
            this.maxBacklog = maxBacklog;
        }

        @Override
        public int read() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int read(byte[] b, int off, int len) {
            maxBacklog.accumulateAndGet(read.get() - sent.get(), Math::max);
            if (left == 0) {
                return -1;
            }
            int n = (int) Math.min(len, left);
            byte[] chunk = new byte[n];
            random.nextBytes(chunk);
            System.arraycopy(chunk, 0, b, off, n);
            left -= n;
            read.addAndGet(n);
            return n;
        }
    }

    static final class MutableClock extends Clock {
        private Instant now;

        MutableClock(Instant now) {
            this.now = now;
        }

        void advance(Duration d) {
            now = now.plus(d);
        }

        @Override
        public ZoneOffset getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(java.time.ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return now;
        }
    }
}
