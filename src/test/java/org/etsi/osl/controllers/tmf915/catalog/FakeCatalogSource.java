package org.etsi.osl.controllers.tmf915.catalog;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/** A {@link CatalogSource} whose answers tests set, and which records how it was called. */
class FakeCatalogSource implements CatalogSource<String> {

    private final String platform;
    /** Answer to a full search. */
    List<DiscoveredModel<String>> all = new ArrayList<>();
    /** Answers to incremental searches, in order; when empty, incremental searches return nothing. */
    final Deque<List<DiscoveredModel<String>>> incrementalAnswers = new ArrayDeque<>();
    RuntimeException incrementalFailure;
    RuntimeException fullFailure;
    final Set<String> withoutArtifacts = new HashSet<>();
    Function<DiscoveredModel<String>, AiModelSpecificationCreate> converter = m -> {
        AiModelSpecificationCreate c = new AiModelSpecificationCreate();
        c.setName(m.name());
        c.setVersion(m.version());
        return c;
    };

    /** null for a full search, else the "created after" argument. */
    final List<Long> searches = new ArrayList<>();
    final List<String> artifactChecks = new ArrayList<>();
    final List<String> converted = new ArrayList<>();

    FakeCatalogSource(String platform) {
        this.platform = platform;
    }

    static DiscoveredModel<String> model(String key, long createdAtMs) {
        return new DiscoveredModel<>(key, "name-" + key, "v-" + key, createdAtMs, key);
    }

    static DiscoveredModel<String> model(String key, long createdAtMs, DiscoveredModel.Status status, long readyAtMs) {
        return new DiscoveredModel<>(key, "name-" + key, "v-" + key, createdAtMs, key, status, readyAtMs);
    }

    long fullSearches() {
        return searches.stream().filter(s -> s == null).count();
    }

    List<Long> incrementalSearches() {
        return searches.stream().filter(s -> s != null).toList();
    }

    @Override
    public String platform() {
        return platform;
    }

    @Override
    public String artifactFormat() {
        return "fake-format";
    }

    @Override
    public List<DiscoveredModel<String>> discover(Long createdAfterMs) {
        searches.add(createdAfterMs);
        if (createdAfterMs == null) {
            if (fullFailure != null) {
                throw fullFailure;
            }
            return all;
        }
        if (incrementalFailure != null) {
            throw incrementalFailure;
        }
        return incrementalAnswers.isEmpty() ? List.of() : incrementalAnswers.poll();
    }

    @Override
    public boolean hasArtifacts(DiscoveredModel<String> model) {
        artifactChecks.add(model.key());
        return !withoutArtifacts.contains(model.key());
    }

    @Override
    public AiModelSpecificationCreate toSpecification(DiscoveredModel<String> model) throws IOException {
        converted.add(model.key());
        return converter.apply(model);
    }
}
