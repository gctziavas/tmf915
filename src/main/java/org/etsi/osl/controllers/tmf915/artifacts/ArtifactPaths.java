package org.etsi.osl.controllers.tmf915.artifacts;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry names for downloaded zips. A name must stay inside the directory the client unzips
 * into, whatever tool or operating system unzips it, so both {@code /} and {@code \} count as
 * separators here.
 */
public final class ArtifactPaths {

    private ArtifactPaths() {
    }

    /**
     * Returns the path with {@code /} separators and without empty or {@code .} segments.
     *
     * @throws IllegalArgumentException if the path is empty, absolute (leading separator or a
     *                                  drive letter) or has a {@code ..} segment
     */
    public static String normalize(String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("empty artifact path");
        }
        if (path.startsWith("/") || path.startsWith("\\") || path.matches("^[A-Za-z]:.*")) {
            throw new IllegalArgumentException("absolute artifact path: " + path);
        }
        List<String> segments = new ArrayList<>();
        for (String segment : path.split("[/\\\\]")) {
            if (segment.isEmpty() || segment.equals(".")) {
                continue;
            }
            if (segment.equals("..")) {
                throw new IllegalArgumentException("artifact path leaves its directory: " + path);
            }
            segments.add(segment);
        }
        if (segments.isEmpty()) {
            throw new IllegalArgumentException("empty artifact path: " + path);
        }
        return String.join("/", segments);
    }
}
