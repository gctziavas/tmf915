package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.artifacts.InMemoryArtifactStore;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification;
import org.etsi.osl.controllers.tmf915.model.CharacteristicValueSpecification;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * {@code GET /aiModelSpecification/{id}/artifacts} on a real server (the zip is streamed
 * asynchronously, which MockMvc does not run end to end), with an in-memory {@code s3://} store
 * and a 1000-byte size limit.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "aimodel.artifacts.max-download-bytes=1000")
// A plain class, not a nested @TestConfiguration: see DeploymentSchedulerSpringTest.
@Import(ArtifactDownloadHttpTest.Store.class)
class ArtifactDownloadHttpTest {

    static class Store {
        @org.springframework.context.annotation.Bean
        InMemoryArtifactStore inMemoryArtifactStore() {
            return new InMemoryArtifactStore();
        }
    }

    private static final String LOCATION = "s3://mlflow/1/models/m-1/artifacts";
    private static final byte[] MLMODEL = "artifact_path: model\nflavors: {}\n".getBytes(StandardCharsets.UTF_8);
    private static final byte[] PICKLE = {(byte) 0x80, 5, 'K', 1, '.'};
    private static final byte[] ONNX = {8, 7, 18, 4, 'o', 'n', 'n', 'x'};

    @LocalServerPort
    private int port;

    /** Token "user" has ROLE_USER; token "no-role" has no roles. */
    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private InMemoryArtifactStore store;

    @Autowired
    private AiModelSpecificationRepositoryService specifications;

    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    void setUp() {
        when(jwtDecoder.decode(anyString())).thenAnswer(inv -> {
            String token = inv.getArgument(0);
            List<String> roles = token.equals("user") ? List.of("ROLE_USER") : List.of();
            return Jwt.withTokenValue(token)
                    .header("alg", "none")
                    .subject("alice")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(300))
                    .claim("realm_access", Map.of("roles", roles))
                    .build();
        });
        store.clear();
        store.put(LOCATION, "MLmodel", MLMODEL)
                .put(LOCATION, "model/model.pkl", PICKLE)
                .put("s3://other-bucket/secrets", "dump.sql", new byte[]{1});
        store.put("s3://mlflow/big", "weights.bin", new byte[2000]);
        // An ONNX copy logged with extra_files=["model.onnx"], next to another extra file.
        store.put(LOCATION + "/extra_files", "model.onnx", ONNX).put(LOCATION + "/extra_files", "notes.txt", new byte[]{1});
    }

    private String create(String... characteristics) {
        AiModelSpecificationCreate create = new AiModelSpecificationCreate();
        create.setName("iris classifier");
        create.setVersion("m-1");
        for (int i = 0; i < characteristics.length; i += 2) {
            CharacteristicSpecification c = new CharacteristicSpecification();
            c.setName(characteristics[i]);
            CharacteristicValueSpecification v = new CharacteristicValueSpecification();
            v.setValue(characteristics[i + 1]);
            v.setIsDefault(true);
            c.addCharacteristicValueSpecificationItem(v);
            create.addSpecCharacteristicItem(c);
        }
        AiModelSpecification created = specifications.createAiModelSpecification(create);
        return created.getId();
    }

    private HttpResponse<byte[]> download(String id, String token, String accept) throws Exception {
        return download(id, "", token, accept);
    }

    private HttpResponse<byte[]> download(String id, String query, String token, String accept) throws Exception {
        HttpRequest.Builder request = HttpRequest.newBuilder(
                URI.create("http://localhost:" + port + "/tmf-api/AiM/v4/aiModelSpecification/" + id + "/artifacts" + query));
        if (token != null) {
            request.header("Authorization", "Bearer " + token);
        }
        if (accept != null) {
            request.header("Accept", accept);
        }
        return client.send(request.GET().build(), HttpResponse.BodyHandlers.ofByteArray());
    }

    private static Map<String, byte[]> unzip(byte[] zip) throws IOException {
        Map<String, byte[]> entries = new LinkedHashMap<>();
        try (ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(zip))) {
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                entries.put(entry.getName(), in.readAllBytes());
            }
        }
        return entries;
    }

    private static String text(HttpResponse<byte[]> response) {
        return new String(response.body(), StandardCharsets.UTF_8);
    }

    @Test
    void downloadsTheFilesAsAZip() throws Exception {
        String id = create("platform", "mlflow", "artifactUri", LOCATION);

        HttpResponse<byte[]> response = download(id, "user", "application/zip");

        assertEquals(200, response.statusCode(), text(response));
        assertEquals("application/zip", response.headers().firstValue("Content-Type").orElseThrow());
        String disposition = response.headers().firstValue("Content-Disposition").orElseThrow();
        assertTrue(disposition.startsWith("attachment;") && disposition.contains("iris_classifier-m-1.zip"), disposition);
        assertTrue(response.headers().firstValue("Content-Encoding").isEmpty(), "a zip is not gzipped again");
        Map<String, byte[]> entries = unzip(response.body());
        assertEquals(List.of("MLmodel", "model/model.pkl", "extra_files/model.onnx", "extra_files/notes.txt"),
                List.copyOf(entries.keySet()));
        assertArrayEquals(MLMODEL, entries.get("MLmodel"));
        assertArrayEquals(PICKLE, entries.get("model/model.pkl"));
    }

    @Test
    void legacyMlflowSpecificationWithoutPlatform_downloads() throws Exception {
        String id = create("loggedModelId", "m-1", "artifactUri", LOCATION);
        assertEquals(200, download(id, "user", null).statusCode());
    }

    @Test
    void unknownId_is404() throws Exception {
        HttpResponse<byte[]> response = download("no-such-spec", "user", "application/zip");

        assertEquals(404, response.statusCode());
        assertTrue(text(response).contains("No AiModelSpecification with ID: no-such-spec"), text(response));
        assertTrue(response.headers().firstValue("Content-Type").orElseThrow().startsWith("text/plain"));
    }

    @Test
    void specificationWithoutALocator_is404() throws Exception {
        String id = create("platform", "kubeflow", "artifactUri", LOCATION);

        HttpResponse<byte[]> response = download(id, "user", null);

        assertEquals(404, response.statusCode());
        assertTrue(text(response).contains("has no downloadable artifact"), text(response));
    }

    @Test
    void locationOutsideTheAllowList_is404_andStorageIsNotRead() throws Exception {
        // Anyone with ROLE_USER can POST a specification pointing anywhere (decision A5).
        String id = create("platform", "mlflow", "artifactUri", "s3://other-bucket/secrets");
        store.touched.clear();

        HttpResponse<byte[]> response = download(id, "user", "application/json");

        assertEquals(404, response.statusCode());
        assertEquals("AiModelSpecification " + id + " has no downloadable artifact", text(response));
        assertTrue(response.headers().firstValue("Content-Type").orElseThrow().startsWith("text/plain"));
        assertTrue(store.touched.isEmpty(), "store was asked for " + store.touched);
    }

    @Test
    void optedOut_is404_withTheReason() throws Exception {
        String id = create("platform", "mlflow", "artifactUri", LOCATION, "downloadable", "false");

        HttpResponse<byte[]> response = download(id, "user", null);

        assertEquals(404, response.statusCode());
        assertTrue(text(response).contains("downloadable=false"), text(response));
    }

    @Test
    void aboveTheSizeLimit_is413() throws Exception {
        String id = create("platform", "mlflow", "artifactUri", "s3://mlflow/big");

        HttpResponse<byte[]> response = download(id, "user", "application/zip");

        assertEquals(413, response.statusCode());
        assertTrue(text(response).contains("2000 bytes"), text(response));
    }

    @Test
    void withoutRoleUser_is403() throws Exception {
        String id = create("platform", "mlflow", "artifactUri", LOCATION);
        assertEquals(403, download(id, "no-role", null).statusCode());
    }

    @Test
    void withoutToken_is401() throws Exception {
        String id = create("platform", "mlflow", "artifactUri", LOCATION);
        assertEquals(401, download(id, null, null).statusCode());
    }

    @Autowired
    private org.etsi.osl.controllers.tmf915.artifacts.DownloadAttachmentBackfill backfill;

    @Test
    void backfill_advertisesTheDownloadInTheSpecification() throws Exception {
        String downloadable = create("platform", "mlflow", "artifactUri", LOCATION);
        String optedOut = create("platform", "mlflow", "artifactUri", LOCATION, "downloadable", "false");
        String outside = create("platform", "mlflow", "artifactUri", "s3://other-bucket/secrets");

        backfill.run();

        HttpResponse<String> response = client.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port
                        + "/tmf-api/AiM/v4/aiModelSpecification/" + downloadable + "?fields=attachment"))
                .header("Authorization", "Bearer user").GET().build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        String json = response.body();
        assertTrue(json.contains("\"href\":\"/tmf-api/AiM/v4/aiModelSpecification/" + downloadable + "/artifacts?format=mlflow-model\""), json);
        assertTrue(json.contains("\"@type\":\"ModelArtifactAttachment\""), json);
        assertTrue(json.contains("\"attachmentType\":\"mlflow-model\""), json);
        assertTrue(json.contains("\"mimeType\":\"application/zip\""), json);
        assertTrue(json.contains("\"units\":\"bytes\""), json);
        assertTrue(json.contains("\"name\":\"iris_classifier-m-1-mlflow-model.zip\""), json);
        // The ONNX copy is a second attachment, with its own link and size.
        assertTrue(json.contains("\"href\":\"/tmf-api/AiM/v4/aiModelSpecification/" + downloadable + "/artifacts?format=onnx\""), json);
        assertTrue(json.contains("\"attachmentType\":\"onnx\""), json);
        assertTrue(json.contains("\"name\":\"iris_classifier-m-1-onnx.zip\""), json);
        assertTrue(json.contains("\"amount\":8.0"), json);

        assertTrue(specifications.findAiModelSpecificationById(optedOut).getAttachment().isEmpty());
        assertTrue(specifications.findAiModelSpecificationById(outside).getAttachment().isEmpty());
    }

    // ── Formats ──────────────────────────────────────────────────────────

    @Test
    void formatOnnx_downloadsOnlyTheOnnxFile() throws Exception {
        String id = create("platform", "mlflow", "artifactUri", LOCATION);

        HttpResponse<byte[]> response = download(id, "?format=onnx", "user", null);

        assertEquals(200, response.statusCode(), text(response));
        assertTrue(response.headers().firstValue("Content-Disposition").orElseThrow().contains("iris_classifier-m-1-onnx.zip"));
        Map<String, byte[]> entries = unzip(response.body());
        assertEquals(List.of("model.onnx"), List.copyOf(entries.keySet()));
        assertArrayEquals(ONNX, entries.get("model.onnx"));
    }

    @Test
    void namedPrimaryFormat_isTheSameAsNoFormat() throws Exception {
        String id = create("platform", "mlflow", "artifactUri", LOCATION);
        HttpResponse<byte[]> response = download(id, "?format=mlflow-model", "user", null);
        assertEquals(200, response.statusCode());
        assertEquals(4, unzip(response.body()).size());
    }

    @Test
    void formatNotOffered_is404_withTheReason() throws Exception {
        String id = create("platform", "mlflow", "artifactUri", LOCATION);
        HttpResponse<byte[]> response = download(id, "?format=pytorch", "user", null);
        assertEquals(404, response.statusCode());
        assertEquals("AiModelSpecification " + id + " has no download in format pytorch", text(response));

        // Offered by the locator, but this model has no ONNX copy.
        String withoutOnnx = create("platform", "mlflow", "artifactUri", "s3://mlflow/big");
        HttpResponse<byte[]> missing = download(withoutOnnx, "?format=onnx", "user", null);
        assertEquals(404, missing.statusCode());
        assertTrue(text(missing).contains("has no download in format onnx"), text(missing));
    }

    @Test
    void malformedFormat_is400() throws Exception {
        String id = create("platform", "mlflow", "artifactUri", LOCATION);
        HttpResponse<byte[]> response = download(id, "?format=..%2Fsecret", "user", null);
        assertEquals(400, response.statusCode());
        assertTrue(text(response).contains("Invalid format"), text(response));
    }

    @Test
    void optedOut_isNotDownloadableInAnyFormat() throws Exception {
        String id = create("platform", "mlflow", "artifactUri", LOCATION, "downloadable", "false");
        assertEquals(404, download(id, "?format=onnx", "user", null).statusCode());
    }
}
