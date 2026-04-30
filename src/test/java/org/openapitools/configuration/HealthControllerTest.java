package org.openapitools.configuration;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthControllerTest {

    private final HealthController healthController = new HealthController();

    @Test
    void health_returnsUpStatus() {
        ResponseEntity<Map<String, String>> response = healthController.health();

        assertEquals(200, response.getStatusCode().value());
        assertEquals("UP", response.getBody().get("status"));
    }
}
