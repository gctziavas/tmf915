package org.openapitools.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String PREFERRED_USERNAME = "preferred_username";

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${keycloak.issuer-uri:${spring.security.oauth2.resourceserver.jwt.issuer-uri:}}")
    private String issuerUri;

    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        OAuth2TokenValidator<Jwt> validators = new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(),
                new JwtClaimValidator<>("iss", iss -> issuerUri.equals(String.valueOf(iss)))
        );
        decoder.setJwtValidator(validators);
        return decoder;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",
                    "/health",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/actuator/**",
                    "/mcp/**",
                    "/sse/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );
        return http.build();
    }

    /**
     * Roles from {@code realm_access.roles}; the name ({@code Authentication.getName()}, which the
     * download audit log records) from {@code sub}, or from {@code preferred_username} when the
     * token has no {@code sub}. Keycloak 25 and later add {@code sub} through the {@code basic}
     * client scope, which realms imported from older exports lack.
     */
    static Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return jwt -> {
            AbstractAuthenticationToken authentication = converter.convert(jwt);
            String username = jwt.getClaimAsString(PREFERRED_USERNAME);
            if (jwt.getSubject() != null || username == null) {
                return authentication;
            }
            return new JwtAuthenticationToken(jwt, authentication.getAuthorities(), username);
        };
    }

    /**
     * Extracts realm-level roles from a Keycloak JWT's {@code realm_access.roles} claim
     * and converts them to Spring Security {@link GrantedAuthority} instances.
     */
    static class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        @Override
        @SuppressWarnings("unchecked")
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess == null) {
                return Collections.emptyList();
            }
            Object rolesObj = realmAccess.get("roles");
            if (!(rolesObj instanceof List<?> roles)) {
                return Collections.emptyList();
            }
            return roles.stream()
                    .filter(String.class::isInstance)
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority((String) role))
                    .collect(Collectors.toList());
        }
    }
}
