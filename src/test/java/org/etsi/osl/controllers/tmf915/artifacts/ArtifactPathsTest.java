package org.etsi.osl.controllers.tmf915.artifacts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArtifactPathsTest {

    @Test
    void normalisesSeparatorsAndDotSegments() {
        assertEquals("MLmodel", ArtifactPaths.normalize("MLmodel"));
        assertEquals("model/model.pkl", ArtifactPaths.normalize("model//./model.pkl"));
        assertEquals("a/b/c.txt", ArtifactPaths.normalize("a\\b\\c.txt"));
        assertEquals("a/b", ArtifactPaths.normalize("./a/b/"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "/etc/passwd", "\\windows\\system32", "C:\\x", "c:/x",
            "../x", "a/../../x", "a\\..\\..\\x", "..", "./", "."})
    void rejectsPathsThatCouldLeaveTheTargetDirectory(String path) {
        assertThrows(IllegalArgumentException.class, () -> ArtifactPaths.normalize(path));
    }
}
