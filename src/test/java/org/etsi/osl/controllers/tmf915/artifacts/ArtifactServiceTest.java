package org.etsi.osl.controllers.tmf915.artifacts;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification;
import org.etsi.osl.controllers.tmf915.model.CharacteristicValueSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArtifactServiceTest {

    private static final String LOCATION = "s3://mlflow/1/models/m-1/artifacts";

    private final InMemoryArtifactStore store = new InMemoryArtifactStore()
            .put(LOCATION, "MLmodel", new byte[]{1, 2})
            .put(LOCATION, "model.pkl", new byte[]{3, 4, 5});

    /** Locates the {@code artifactUri} characteristic, format {@code test-format}. */
    private final ArtifactLocator locator = new ArtifactLocator() {
        @Override
        public boolean supports(AiModelSpecification spec) {
            return ArtifactCharacteristics.value(spec.getSpecCharacteristic(), "artifactUri").isPresent();
        }

        @Override
        public ArtifactLocation locate(AiModelSpecification spec) {
            return new ArtifactLocation(
                    URI.create(ArtifactCharacteristics.value(spec.getSpecCharacteristic(), "artifactUri").orElseThrow()),
                    "test-format");
        }
    };

    private ArtifactService service(long maxBytes) {
        return new ArtifactService(List.of(locator), List.of(store), List.of("s3://mlflow/"),
                maxBytes, Duration.ofMinutes(15), Clock.systemUTC());
    }

    static AiModelSpecification spec(String... nameValuePairs) {
        AiModelSpecification spec = new AiModelSpecification();
        spec.setId("spec-1");
        for (int i = 0; i < nameValuePairs.length; i += 2) {
            CharacteristicSpecification c = new CharacteristicSpecification();
            c.setName(nameValuePairs[i]);
            CharacteristicValueSpecification v = new CharacteristicValueSpecification();
            v.setValue(nameValuePairs[i + 1]);
            v.setIsDefault(true);
            c.addCharacteristicValueSpecificationItem(v);
            spec.addSpecCharacteristicItem(c);
        }
        return spec;
    }

    @Test
    void resolvesFilesFormatAndSize() {
        ModelArtifact artifact = service(100).resolve(spec("artifactUri", LOCATION));

        assertEquals("test-format", artifact.format());
        assertEquals(5, artifact.totalBytes());
        assertEquals(List.of("MLmodel", "model.pkl"), artifact.files().stream().map(ArtifactFile::path).toList());
    }

    @Test
    void optOut_isNotDownloadable_andSaysWhy() {
        ArtifactNotAvailableException e = assertThrows(ArtifactNotAvailableException.class,
                () -> service(100).resolve(spec("artifactUri", LOCATION, "downloadable", "FALSE")));
        assertTrue(e.getMessage().contains("downloadable=false"), e.getMessage());
        assertTrue(store.touched.isEmpty());
    }

    @Test
    void downloadableTrue_isDownloadable() {
        assertEquals(5, service(100).resolve(spec("artifactUri", LOCATION, "downloadable", "true")).totalBytes());
    }

    @Test
    void noLocator_isNotAvailable() {
        assertThrows(ArtifactNotAvailableException.class, () -> service(100).resolve(spec("platform", "other")));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "s3://other-bucket/1/models/m-1/artifacts",
            "s3://mlflow-private/x",
            "s3://mlflow/1/../../other/x",
            "s3://mlflow/./x",
            "s3://mlflow/x?versionId=1",
            "https://mlflow/1/models/m-1/artifacts"})
    void locationsOutsideTheAllowList_areNeverRead(String location) {
        store.put(location, "MLmodel", new byte[]{1});
        store.touched.clear();

        ArtifactNotAvailableException e = assertThrows(ArtifactNotAvailableException.class,
                () -> service(100).resolve(spec("artifactUri", location)));

        assertTrue(store.touched.isEmpty(), "store was asked for " + store.touched);
        // Same message as "nothing stored": a 404 does not confirm that the location exists.
        assertEquals("AiModelSpecification spec-1 has no downloadable artifact", e.getMessage());
    }

    @Test
    void allowListMatchesWholePathSegments() {
        ArtifactService narrow = new ArtifactService(List.of(locator), List.of(store), List.of(" s3://mlflow/1 ", ""),
                100, Duration.ofMinutes(1), Clock.systemUTC());
        assertTrue(narrow.isAllowed(URI.create("s3://mlflow/1/models/x")));
        assertTrue(narrow.isAllowed(URI.create("s3://mlflow/1")));
        assertFalse(narrow.isAllowed(URI.create("s3://mlflow/10/models/x")));
        assertFalse(narrow.isAllowed(URI.create("s3://mlflow/2/models/x")));
    }

    @Test
    void nothingStored_isNotAvailable() {
        assertThrows(ArtifactNotAvailableException.class,
                () -> service(100).resolve(spec("artifactUri", "s3://mlflow/empty")));
    }

    @Test
    void unsafePathInStorage_isNotAvailable() {
        ArtifactStore unsafe = new InMemoryArtifactStore() {
            @Override
            public List<ArtifactFile> list(URI location) {
                return List.of(new ArtifactFile(ArtifactPaths.normalize("../../etc/cron.d/x"), 1));
            }
        };
        ArtifactService service = new ArtifactService(List.of(locator), List.of(unsafe), List.of("s3://mlflow/"),
                100, Duration.ofMinutes(1), Clock.systemUTC());
        assertThrows(ArtifactNotAvailableException.class, () -> service.resolve(spec("artifactUri", LOCATION)));
    }

    @Test
    void storageError_isAStorageException() {
        ArtifactStore broken = new InMemoryArtifactStore() {
            @Override
            public List<ArtifactFile> list(URI location) throws IOException {
                throw new IOException("connection refused to minio:9000");
            }
        };
        ArtifactService service = new ArtifactService(List.of(locator), List.of(broken), List.of("s3://mlflow/"),
                100, Duration.ofMinutes(1), Clock.systemUTC());
        ArtifactStorageException e = assertThrows(ArtifactStorageException.class,
                () -> service.resolve(spec("artifactUri", LOCATION)));
        assertFalse(e.getMessage().contains("minio"));
    }

    @Test
    void aboveTheSizeLimit_isTooLarge() {
        assertEquals(5, service(5).resolve(spec("artifactUri", LOCATION)).totalBytes());
        ArtifactTooLargeException e = assertThrows(ArtifactTooLargeException.class,
                () -> service(4).resolve(spec("artifactUri", LOCATION)));
        assertTrue(e.getMessage().contains("5 bytes"), e.getMessage());
    }

    @Test
    void inspect_describesAllowedArtifacts_andNeverThrows() {
        ArtifactService service = service(100);
        assertEquals(5, service.inspect(new ArtifactLocation(URI.create(LOCATION), "f")).orElseThrow().totalBytes());
        assertTrue(service.inspect(new ArtifactLocation(URI.create("s3://other/x"), "f")).isEmpty());
        assertTrue(service.inspect(new ArtifactLocation(URI.create("s3://mlflow/empty"), "f")).isEmpty());
        assertTrue(service(4).inspect(new ArtifactLocation(URI.create(LOCATION), "f")).isEmpty());
    }

    @Test
    void writeZip_sendsTheFiles() throws IOException {
        ArtifactService service = service(100);
        ModelArtifact artifact = service.resolve(spec("artifactUri", LOCATION));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        assertEquals(5, service.writeZip("spec-1", "tester", artifact, out));

        Map<String, byte[]> entries = ArtifactZipWriterTest.unzip(out.toByteArray());
        assertArrayEquals(new byte[]{3, 4, 5}, entries.get("model.pkl"));
    }

    @Test
    void writeZip_rechecksTheAllowList() {
        ModelArtifact forged = new ModelArtifact(URI.create("s3://other/x"), "f",
                List.of(new ArtifactFile("a", 1)), 1);
        store.put("s3://other/x", "a", new byte[]{1});
        assertThrows(ArtifactNotAvailableException.class,
                () -> service(100).writeZip("spec-1", "tester", forged, new ByteArrayOutputStream()));
    }

    @Test
    void streamFromAStoreThatFailsMidway_propagates() {
        ArtifactStore failing = new InMemoryArtifactStore().put(LOCATION, "a", new byte[]{1});
        ArtifactStore wrapper = new ArtifactStore() {
            @Override
            public boolean supports(URI location) {
                return true;
            }

            @Override
            public List<ArtifactFile> list(URI location) throws IOException {
                return failing.list(location);
            }

            @Override
            public InputStream open(URI location, ArtifactFile file) throws IOException {
                throw new IOException("reset by peer");
            }
        };
        ArtifactService service = new ArtifactService(List.of(locator), List.of(wrapper), List.of("s3://mlflow/"),
                100, Duration.ofMinutes(1), Clock.systemUTC());
        ModelArtifact artifact = service.resolve(spec("artifactUri", LOCATION));
        assertThrows(IOException.class,
                () -> service.writeZip("spec-1", "tester", artifact, new ByteArrayOutputStream()));
    }
}
