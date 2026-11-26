package org.etsi.osl.controllers.tmf915.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks gzip response compression on a real embedded server (MockMvc bypasses the servlet
 * container, which is where compression happens). Uses /v3/api-docs because it is a large
 * JSON response that needs no authentication. The 200 checks also guard springdoc: version 2.6.0
 * failed on Spring 7 as soon as a @ControllerAdvice bean existed (fixed by upgrading to 3.0.3).
 */
// server.compression comes from src/main/resources/application.yml, not from test settings.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ResponseCompressionTest {

    @LocalServerPort
    private int port;

    private final HttpClient client = HttpClient.newHttpClient();

    @Test
    void jsonIsGzippedWhenClientAcceptsIt() throws Exception {
        HttpResponse<byte[]> response = client.send(
                HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/tmf-api/v3/api-docs"))
                        .header("Accept-Encoding", "gzip").build(),
                HttpResponse.BodyHandlers.ofByteArray());

        assertEquals(200, response.statusCode());
        assertEquals("gzip", response.headers().firstValue("Content-Encoding").orElse(null));
        String json;
        try (GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(response.body()))) {
            json = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
        assertTrue(json.startsWith("{"), json.substring(0, Math.min(200, json.length())));
        System.out.printf("COMPRESSION /v3/api-docs: %d bytes on the wire, %d bytes of JSON%n",
                response.body().length, json.getBytes(StandardCharsets.UTF_8).length);
    }

    @Test
    void responseIsPlainWhenClientDoesNotAcceptGzip() throws Exception {
        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/tmf-api/v3/api-docs")).build(),
                HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("Content-Encoding").isEmpty());
        assertTrue(response.body().startsWith("{"));
    }
}
