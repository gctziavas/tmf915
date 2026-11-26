package org.etsi.osl.controllers.tmf915.artifacts;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** Test store for {@code s3://} locations, holding files in memory. */
public class InMemoryArtifactStore implements ArtifactStore {

    private final Map<URI, Map<String, byte[]>> artifacts = new ConcurrentHashMap<>();
    /** Locations that were listed or read: shows that disallowed ones were never touched. */
    public final Set<URI> touched = ConcurrentHashMap.newKeySet();

    public InMemoryArtifactStore put(String location, String path, byte[] content) {
        artifacts.computeIfAbsent(URI.create(location), k -> new LinkedHashMap<>()).put(path, content);
        return this;
    }

    public void clear() {
        artifacts.clear();
        touched.clear();
    }

    @Override
    public boolean supports(URI location) {
        return "s3".equals(location.getScheme());
    }

    @Override
    public List<ArtifactFile> list(URI location) throws IOException {
        touched.add(location);
        List<ArtifactFile> files = new ArrayList<>();
        artifacts.getOrDefault(location, Map.of())
                .forEach((path, content) -> files.add(new ArtifactFile(ArtifactPaths.normalize(path), content.length)));
        return files;
    }

    @Override
    public InputStream open(URI location, ArtifactFile file) throws IOException {
        touched.add(location);
        byte[] content = artifacts.getOrDefault(location, Map.of()).get(file.path());
        if (content == null) {
            throw new IOException("no such file: " + file.path());
        }
        return new ByteArrayInputStream(content);
    }
}
