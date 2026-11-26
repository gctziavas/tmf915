package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Records every command and answers with the first matching scripted result
 * (default: success with empty output). Lets the Docker layer be tested without Docker.
 */
class FakeProcessRunner implements ProcessRunner {

    record Call(List<String> command, Map<String, String> env) {
        String line() {
            return String.join(" ", command);
        }
    }

    private record Rule(Predicate<List<String>> matches, ProcessResult result) {}

    final List<Call> calls = new ArrayList<>();
    private final List<Rule> rules = new ArrayList<>();

    /** Answers commands whose joined text contains {@code fragment}. Earlier rules win. */
    FakeProcessRunner when(String fragment, ProcessResult result) {
        rules.add(new Rule(cmd -> String.join(" ", cmd).contains(fragment), result));
        return this;
    }

    FakeProcessRunner when(Predicate<List<String>> matches, ProcessResult result) {
        rules.add(new Rule(matches, result));
        return this;
    }

    static ProcessResult ok(String stdout) {
        return new ProcessResult(0, stdout, "", false);
    }

    static ProcessResult fail(String stderr) {
        return new ProcessResult(125, "", stderr, false);
    }

    static ProcessResult timeout() {
        return new ProcessResult(-1, "", "", true);
    }

    @Override
    public ProcessResult run(List<String> command, Map<String, String> env, Duration timeout) {
        calls.add(new Call(List.copyOf(command), Map.copyOf(env)));
        for (Rule rule : rules) {
            if (rule.matches().test(command)) {
                return rule.result();
            }
        }
        return ok("");
    }

    List<Call> callsContaining(String fragment) {
        return calls.stream().filter(c -> c.line().contains(fragment)).toList();
    }
}
