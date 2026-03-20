package org.openapitools.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class SpringDocConfiguration {

    @Value("${springdoc.oAuthFlow.authorizationUrl}")
    private String authorizationUrl;

    @Value("${springdoc.oAuthFlow.tokenUrl}")
    private String tokenUrl;

    @Bean(name = "org.openapitools.configuration.SpringDocConfiguration.apiInfo")
    OpenAPI apiInfo() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Ai Management")
                                .description("AI Management API")
                                .version("4.0.0")
                )
                .tags(List.of(
                        new Tag().name("aiModelSpecification").description("the aiModelSpecification API"),
                        new Tag().name("aiModel").description("the aiModel API"),
                        new Tag().name("aiContract").description("the aiContract API"),
                        new Tag().name("aiContractSpecification").description("the aiContractSpecification API"),
                        new Tag().name("aiContractViolation").description("the aiContractViolation API"),
                        new Tag().name("alarm").description("the alarm API"),
                        new Tag().name("event").description("the event API"),
                        new Tag().name("events subscription").description("the events subscription API"),
                        new Tag().name("hub").description("the hub API"),
                        new Tag().name("monitor").description("the monitor API"),
                        new Tag().name("notification listeners (client side)").description("the notification listeners (client side) API"),
                        new Tag().name("rule").description("the rule API"),
                        new Tag().name("topic").description("the topic API")
                ))
                .addSecurityItem(new SecurityRequirement().addList("keycloak"))
                .components(new Components()
                        .addSecuritySchemes("keycloak", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows()
                                        .authorizationCode(new OAuthFlow()
                                                .authorizationUrl(authorizationUrl)
                                                .tokenUrl(tokenUrl)))));
    }
}