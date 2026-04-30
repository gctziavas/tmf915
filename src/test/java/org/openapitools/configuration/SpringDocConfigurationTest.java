package org.openapitools.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class SpringDocConfigurationTest {

    private final SpringDocConfiguration config = new SpringDocConfiguration();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(config, "authorizationUrl", "http://localhost:8080/auth/realms/openslice/protocol/openid-connect/auth");
        ReflectionTestUtils.setField(config, "tokenUrl", "http://localhost:8080/auth/realms/openslice/protocol/openid-connect/token");
    }

    @Test
    void apiInfo_returnsOpenAPIWithCorrectTitle() {
        OpenAPI openAPI = config.apiInfo();
        assertNotNull(openAPI);
        assertEquals("Ai Management", openAPI.getInfo().getTitle());
    }

    @Test
    void apiInfo_returnsOpenAPIWithCorrectVersion() {
        OpenAPI openAPI = config.apiInfo();
        assertEquals("4.0.0", openAPI.getInfo().getVersion());
    }

    @Test
    void apiInfo_returnsOpenAPIWithDescription() {
        OpenAPI openAPI = config.apiInfo();
        assertEquals("AI Management API", openAPI.getInfo().getDescription());
    }

    @Test
    void apiInfo_containsKeycloakSecurityScheme() {
        OpenAPI openAPI = config.apiInfo();
        assertNotNull(openAPI.getComponents());
        assertNotNull(openAPI.getComponents().getSecuritySchemes().get("keycloak"));
    }
}
