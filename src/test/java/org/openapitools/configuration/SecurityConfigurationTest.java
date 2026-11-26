package org.openapitools.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/** How a Keycloak token becomes the request's authentication: its name and its roles. */
class SecurityConfigurationTest {

    private static Jwt.Builder token() {
        return Jwt.withTokenValue("t")
                .header("alg", "RS256")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .claim("realm_access", Map.of("roles", List.of("ROLE_USER")));
    }

    private static AbstractAuthenticationToken convert(Jwt jwt) {
        return SecurityConfiguration.jwtAuthenticationConverter().convert(jwt);
    }

    @Test
    void namedAfterTheSubject() {
        AbstractAuthenticationToken authentication =
                convert(token().subject("3f2c-uuid").claim("preferred_username", "admin").build());

        assertEquals("3f2c-uuid", authentication.getName());
    }

    /**
     * Keycloak 25 and later add {@code sub} through the {@code basic} client scope, which realms
     * imported from older exports lack, so their tokens have no subject. The download audit log
     * must still say who downloaded.
     */
    @Test
    void withoutSubject_namedAfterThePreferredUsername() {
        AbstractAuthenticationToken authentication = convert(token().claim("preferred_username", "admin").build());

        assertEquals("admin", authentication.getName());
    }

    @Test
    void withNeitherClaim_hasNoName() {
        AbstractAuthenticationToken authentication = convert(token().claim("scope", "email").build());

        assertNull(authentication.getName());
    }

    @Test
    void rolesComeFromTheRealmAccessClaim() {
        Set<String> authorities = authorities(convert(token().subject("s").build()));

        assertTrue(authorities.contains("ROLE_USER"), authorities.toString());
        assertFalse(authorities.contains("ROLE_ADMIN"), authorities.toString());
    }

    /** The fallback changes only the name: same authorities (including Spring's own) as with a subject. */
    @Test
    void withoutSubject_sameAuthoritiesAsWithOne() {
        AbstractAuthenticationToken withSubject = convert(token().subject("s").build());
        AbstractAuthenticationToken withoutSubject = convert(token().claim("preferred_username", "admin").build());

        assertEquals(authorities(withSubject), authorities(withoutSubject));
        assertTrue(withoutSubject.isAuthenticated());
        assertInstanceOf(JwtAuthenticationToken.class, withoutSubject);
    }

    private static Set<String> authorities(AbstractAuthenticationToken authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
