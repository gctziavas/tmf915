package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.artifacts.ArtifactFile;
import org.etsi.osl.controllers.tmf915.artifacts.ArtifactStore;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification;
import org.etsi.osl.controllers.tmf915.model.CharacteristicValueSpecification;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Decision A2: a download that runs past {@code aimodel.artifacts.download-timeout} is cut off,
 * so it does not keep the connection (and the storage stream) open. The store here delivers
 * 64 KB every 200 ms, 40 times (8 s); the limit is 1 s.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "aimodel.artifacts.download-timeout=1s")
@Import(ArtifactDownloadTimeoutHttpTest.Config.class)
class ArtifactDownloadTimeoutHttpTest {

    static class Config {
        @Bean
        SlowStore slowArtifactStore() {
            return new SlowStore();
        }
    }

    static class SlowStore implements ArtifactStore {
        static final int CHUNK = 64 * 1024;
        static final int CHUNKS = 40;

        @Override
        public boolean supports(URI location) {
            return "s3".equals(location.getScheme());
        }

        @Override
        public List<ArtifactFile> list(URI location) {
            return List.of(new ArtifactFile("weights.bin", (long) CHUNK * CHUNKS));
        }

        @Override
        public InputStream open(URI location, ArtifactFile file) {
            return new InputStream() {
                int left = CHUNKS;

                @Override
                public int read() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public int read(byte[] b, int off, int len) throws IOException {
                    if (left-- == 0) {
                        return -1;
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new IOException(e);
                    }
                    return Math.min(len, CHUNK);
                }
            };
        }
    }

    @LocalServerPort
    private int port;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private AiModelSpecificationRepositoryService specifications;

    @Test
    void downloadIsCutOffAtTheTimeout() throws Exception {
        when(jwtDecoder.decode(anyString())).thenReturn(Jwt.withTokenValue("user")
                .header("alg", "none").subject("alice")
                .issuedAt(Instant.now()).expiresAt(Instant.now().plusSeconds(300))
                .claim("realm_access", Map.of("roles", List.of("ROLE_USER")))
                .build());
        AiModelSpecificationCreate create = new AiModelSpecificationCreate();
        create.setName("slow");
        CharacteristicSpecification c = new CharacteristicSpecification();
        c.setName("artifactUri");
        CharacteristicValueSpecification v = new CharacteristicValueSpecification();
        v.setValue("s3://mlflow/slow");
        c.addCharacteristicValueSpecificationItem(v);
        create.addSpecCharacteristicItem(c);
        CharacteristicSpecification p = new CharacteristicSpecification();
        p.setName("platform");
        CharacteristicValueSpecification pv = new CharacteristicValueSpecification();
        pv.setValue("mlflow");
        p.addCharacteristicValueSpecificationItem(pv);
        create.addSpecCharacteristicItem(p);
        String id = specifications.createAiModelSpecification(create).getId();

        HttpRequest request = HttpRequest.newBuilder(URI.create(
                        "http://localhost:" + port + "/tmf-api/AiM/v4/aiModelSpecification/" + id + "/artifacts"))
                .header("Authorization", "Bearer user")
                .timeout(Duration.ofSeconds(20))
                .GET().build();

        long started = System.nanoTime();
        try {
            HttpResponse<byte[]> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() == 200) {
                // Some of the zip had been sent: the client gets it truncated.
                assertTrue(isTruncated(response.body()), "a complete zip arrived despite the timeout");
            } else {
                // Nothing had been flushed yet (it compresses well): the server could still answer 500.
                assertEquals(500, response.statusCode());
            }
        } catch (IOException expected) {
            // The server aborted the chunked response: also a cut-off download.
        }
        long seconds = Duration.ofNanos(System.nanoTime() - started).toSeconds();
        assertTrue(seconds < 6, "took " + seconds + " s; the full download would take 8 s");
    }

    private static boolean isTruncated(byte[] zip) {
        try (ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(zip))) {
            if (in.getNextEntry() == null) {
                return true;
            }
            in.readAllBytes();
            in.getNextEntry();
            return false;
        } catch (IOException e) {
            return true;
        } catch (RuntimeException e) {
            fail(e);
            return false;
        }
    }
}
