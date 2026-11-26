package org.etsi.osl.controllers.tmf915.artifacts;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Test store for {@code s3://} locations, holding files in memory. Like S3, a location lists
 * every file under it, including those put at a location below it.
 */
public class InMemoryArtifactStore implements ArtifactStore {

    /** Full key ({@code <location>/<path>}) → content, in insertion order per location. */
    private final Map<String, byte[]> objects = new java.util.LinkedHashMap<>();
    /** Locations that were listed or read: shows that disallowed ones were never touched. */
    public final Set<URI> touched = ConcurrentHashMap.newKeySet();

    public synchronized InMemoryArtifactStore put(String location, String path, byte[] content) {
        objects.put(trim(location) + "/" + path, content);
        return this;
    }

    public synchronized void clear() {
        objects.clear();
        touched.clear();
    }

    private static String trim(String location) {
        return location.endsWith("/") ? location.substring(0, location.length() - 1) : location;
    }

    @Override
    public boolean supports(URI location) {
        return "s3".equals(location.getScheme());
    }

    @Override
    public synchronized List<ArtifactFile> list(URI location) throws IOException {
        touched.add(location);
        String prefix = trim(location.toString()) + "/";
        List<ArtifactFile> files = new ArrayList<>();
        objects.forEach((key, content) -> {
            if (key.startsWith(prefix)) {
                files.add(new ArtifactFile(ArtifactPaths.normalize(key.substring(prefix.length())), content.length));
            }
        });
        return files;
    }

    @Override
    public synchronized InputStream open(URI location, ArtifactFile file) throws IOException {
        touched.add(location);
        byte[] content = objects.get(trim(location.toString()) + "/" + file.path());
        if (content == null) {
            throw new IOException("no such file: " + file.path());
        }
        return new ByteArrayInputStream(content);
    }
}
