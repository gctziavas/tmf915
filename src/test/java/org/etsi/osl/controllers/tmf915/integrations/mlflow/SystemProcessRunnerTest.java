package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Handoff 5.4. Uses /bin/sh, so it runs on Linux and macOS without Docker. */
class SystemProcessRunnerTest {

    private final SystemProcessRunner runner = new SystemProcessRunner();

    @Test
    void largeOutputDoesNotStallTheProcess() throws Exception {
        // 2 MB on stdout and stderr: far more than a pipe buffer.
        long start = System.nanoTime();
        ProcessRunner.ProcessResult result = runner.run(
                List.of("sh", "-c", "head -c 2000000 /dev/zero; head -c 2000000 /dev/zero >&2; exit 0"),
                Map.of(), Duration.ofSeconds(20));
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        assertTrue(result.succeeded(), "exit=" + result.exitCode() + " timedOut=" + result.timedOut());
        assertTrue(elapsedMs < 10_000, "took " + elapsedMs + " ms");
        assertEquals(SystemProcessRunner.MAX_CAPTURED_BYTES, result.stdout().length(), "stdout keeps the tail only");
    }

    @Test
    void timeoutKillsTheProcess() throws Exception {
        long start = System.nanoTime();
        ProcessRunner.ProcessResult result = runner.run(List.of("sh", "-c", "sleep 30"), Map.of(), Duration.ofMillis(300));
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        assertTrue(result.timedOut());
        assertFalse(result.succeeded());
        assertEquals(-1, result.exitCode());
        assertTrue(elapsedMs < 10_000, "took " + elapsedMs + " ms");
    }

    @Test
    void stdoutAndStderrAreKeptApart_andEnvIsPassed() throws Exception {
        ProcessRunner.ProcessResult result = runner.run(
                List.of("sh", "-c", "echo \"id-$PROBE\"; echo warning >&2; exit 3"),
                Map.of("PROBE", "42"), Duration.ofSeconds(10));

        assertEquals(3, result.exitCode());
        assertEquals("id-42", result.stdout().strip());
        assertEquals("warning", result.stderr().strip());
        assertTrue(result.combinedOutput().contains("warning"));
    }
}
