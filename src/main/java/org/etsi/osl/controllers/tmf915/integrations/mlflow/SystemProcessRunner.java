package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * {@link ProcessRunner} backed by {@link ProcessBuilder}.
 *
 * <p>stdout and stderr are read continuously on their own threads while the process runs.
 * Without that, a process that writes more than the OS pipe buffer (about 64 KB on Linux;
 * {@code mlflow models build-docker} easily does) blocks on write and appears hung until the
 * timeout. Only the last {@link #MAX_CAPTURED_BYTES} bytes of each stream are kept.
 */
@Component
public class SystemProcessRunner implements ProcessRunner {

    static final int MAX_CAPTURED_BYTES = 1024 * 1024;

    private static final Duration DRAIN_GRACE = Duration.ofSeconds(5);

    @Override
    public ProcessResult run(List<String> command, Map<String, String> env, Duration timeout) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.environment().putAll(env);
        Process process = builder.start();

        StreamTail stdout = new StreamTail(process.getInputStream(), "stdout-" + process.pid());
        StreamTail stderr = new StreamTail(process.getErrorStream(), "stderr-" + process.pid());
        stdout.start();
        stderr.start();

        try {
            boolean finished = process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);
            if (!finished) {
                killTree(process);
                process.waitFor(DRAIN_GRACE.toMillis(), TimeUnit.MILLISECONDS);
            }
            stdout.join(DRAIN_GRACE.toMillis());
            stderr.join(DRAIN_GRACE.toMillis());
            return new ProcessResult(finished ? process.exitValue() : -1, stdout.text(), stderr.text(), !finished);
        } catch (InterruptedException e) {
            killTree(process);
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted while running " + command.get(0), e);
        }
    }

    /**
     * Kills the process and everything it started. Children (for example the {@code docker build}
     * that {@code mlflow models build-docker} runs) would otherwise keep running and keep the
     * output pipes open.
     */
    private static void killTree(Process process) {
        process.descendants().forEach(ProcessHandle::destroyForcibly);
        process.destroyForcibly();
    }

    /** Reads a stream to the end on its own daemon thread, keeping only the tail. */
    private static final class StreamTail extends Thread {

        private final InputStream in;
        private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        StreamTail(InputStream in, String name) {
            super(name);
            this.in = in;
            setDaemon(true);
        }

        @Override
        public void run() {
            byte[] chunk = new byte[8192];
            try (in) {
                int n;
                while ((n = in.read(chunk)) != -1) {
                    synchronized (buffer) {
                        buffer.write(chunk, 0, n);
                        if (buffer.size() > 2 * MAX_CAPTURED_BYTES) {
                            byte[] all = buffer.toByteArray();
                            buffer.reset();
                            buffer.write(all, all.length - MAX_CAPTURED_BYTES, MAX_CAPTURED_BYTES);
                        }
                    }
                }
            } catch (IOException ignored) {
                // The process was killed or closed the stream; keep what was read.
            }
        }

        String text() {
            synchronized (buffer) {
                byte[] all = buffer.toByteArray();
                int from = Math.max(0, all.length - MAX_CAPTURED_BYTES);
                return new String(all, from, all.length - from, StandardCharsets.UTF_8);
            }
        }
    }
}
