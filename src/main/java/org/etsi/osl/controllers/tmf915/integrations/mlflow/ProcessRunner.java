package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Runs an external command (Docker, the MLflow CLI) to completion or timeout.
 *
 * <p>The default implementation is {@link SystemProcessRunner}. Tests use a fake, so that
 * {@code mvn test} needs no Docker.
 */
public interface ProcessRunner {

    /**
     * Runs {@code command} without a shell.
     *
     * @param command the program and its arguments
     * @param env     variables added to the inherited environment
     * @param timeout the process is killed when it runs longer
     * @return the outcome, also when the process failed or timed out
     * @throws IOException if the process cannot be started, or the calling thread is interrupted
     */
    ProcessResult run(List<String> command, Map<String, String> env, Duration timeout) throws IOException;

    /**
     * Outcome of a command. Output is the tail of each stream, at most
     * {@link SystemProcessRunner#MAX_CAPTURED_BYTES} bytes.
     *
     * @param exitCode the exit code, or -1 if the process timed out
     */
    record ProcessResult(int exitCode, String stdout, String stderr, boolean timedOut) {

        public boolean succeeded() {
            return !timedOut && exitCode == 0;
        }

        /** stderr and stdout together, for error messages and pattern checks. */
        public String combinedOutput() {
            if (stderr.isEmpty()) return stdout;
            if (stdout.isEmpty()) return stderr;
            return stderr + "\n" + stdout;
        }

        /** The last {@code maxChars} characters of {@link #combinedOutput()}. */
        public String outputTail(int maxChars) {
            String all = combinedOutput().strip();
            return all.length() <= maxChars ? all : "…" + all.substring(all.length() - maxChars);
        }
    }
}
