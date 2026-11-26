package org.etsi.osl.controllers.tmf915.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Error responses on a real server (MockMvc skips Spring Boot's error handling). Settings come
 * from application.yml: {@code spring.web.error.include-message: never},
 * {@code include-binding-errors: always} (handoff 5.11).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ErrorResponseTest {

    @LocalServerPort
    private int port;

    /** Accepts any bearer token as a user with ROLE_USER. */
    @MockitoBean
    private JwtDecoder jwtDecoder;

    /** Lets a test force an unexpected exception inside a request. */
    @MockitoBean
    private org.etsi.osl.controllers.tmf915.reposervices.AiModelRepositoryService aiModelRepositoryService;

    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    void acceptAnyToken() {
        when(jwtDecoder.decode(anyString())).thenReturn(Jwt.withTokenValue("test-token")
                .header("alg", "none")
                .subject("tester")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .claim("realm_access", Map.of("roles", List.of("ROLE_USER")))
                .build());
    }

    private HttpResponse<String> send(String method, String path, String json) throws Exception {
        HttpRequest.Builder request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/tmf-api/AiM/v4" + path))
                .header("Authorization", "Bearer test-token")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
        request.method(method, HttpRequest.BodyPublishers.ofString(json));
        return client.send(request.build(), HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void validationError_namesTheMissingField() throws Exception {
        // `software` is required; the API used to answer this with a 400 that gave no reason.
        HttpResponse<String> response = send("POST", "/aiModel",
                "{\"state\":\"designed\",\"aiModelSpecification\":{\"id\":\"x\"}}");

        assertEquals(400, response.statusCode(), response.body());
        assertTrue(response.body().contains("\"field\":\"software\""), response.body());
    }

    @Test
    void unexpectedServerError_doesNotEchoTheExceptionMessage() throws Exception {
        when(aiModelRepositoryService.findAiModels(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any()))
                .thenThrow(new IllegalStateException("Connection to jdbc:mysql://mysql:3306/aim915db refused"));

        HttpResponse<String> response = send("GET", "/aiModel", "");

        assertEquals(500, response.statusCode(), response.body());
        assertFalse(response.body().contains("jdbc:mysql"), response.body());
        assertFalse(response.body().contains("\"message\""), response.body());
    }

    @Test
    void unknownId_isA404WithAReason() throws Exception {
        // Real server: the advice's 404 body reaches the client (handoff 4.4).
        HttpResponse<String> response = send("DELETE", "/aiModelSpecification/no-such-spec", "");

        assertEquals(404, response.statusCode(), response.body());
        assertTrue(response.body().contains("No AiModelSpecification with ID: no-such-spec"), response.body());
    }

    @Test
    void expectedBadRequest_stillExplainsItself() throws Exception {
        // 400s raised on purpose set their own body, independent of include-message.
        HttpResponse<String> response = send("POST", "/hub", "{\"callback\":\"http://169.254.169.254/\"}");

        assertEquals(400, response.statusCode(), response.body());
        assertTrue(response.body().contains("internal address"), response.body());
    }
}
