package org.etsi.osl.controllers.tmf915.api;

import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractJacksonHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implements the TMF {@code fields} query parameter for every GET in this API package:
 * {@code ?fields=name,version} returns only those top-level attributes of each resource,
 * plus {@code id}, {@code href} and {@code @type} so the caller can fetch the full resource.
 *
 * <p>The body is first converted to a JSON tree with the application's own mapper, so the
 * result is exactly what the normal response would contain (including computed properties
 * such as an AiModel's {@code aiModelSpecification} reference), minus the unrequested keys.
 * Entities are never modified. Without {@code fields} the body is passed through untouched.
 *
 * <p>This trims the response, not the database read.
 *
 * <p>Deliberately not a {@code @ControllerAdvice}: springdoc 2.6.0 calls a
 * {@code ControllerAdviceBean} constructor that Spring Framework 7 removed, so any
 * {@code @ControllerAdvice} bean makes {@code /v3/api-docs} (and Swagger UI) fail. It is
 * registered by {@link FieldsResponseBodyAdviceConfiguration} instead.
 */
public class FieldsResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    static final String PARAM = "fields";

    /** Always returned, so a caller can follow up with a GET by id. */
    private static final Set<String> ALWAYS = Set.of("id", "href", "@type");

    private final JsonMapper jsonMapper;

    public FieldsResponseBodyAdvice(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // Only this API's controllers, not springdoc or error responses.
        return returnType.getContainingClass().getPackageName().equals(getClass().getPackageName())
                && AbstractJacksonHttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public @Nullable Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType,
                                            MediaType selectedContentType,
                                            Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                            ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null || request.getMethod() != HttpMethod.GET
                || !(request instanceof ServletServerHttpRequest servletRequest)) {
            return body;
        }
        Set<String> fields = parse(servletRequest.getServletRequest());
        if (fields == null) {
            return body;
        }

        JsonNode tree = jsonMapper.valueToTree(body);
        if (tree instanceof ArrayNode array) {
            array.forEach(item -> retain(item, fields));
        } else {
            retain(tree, fields);
        }
        return tree;
    }

    /** @return the requested attribute names, or null when {@code fields} is absent or blank */
    static @Nullable Set<String> parse(HttpServletRequest request) {
        String fields = request.getParameter(PARAM);
        if (fields == null || fields.isBlank()) {
            return null;
        }
        Set<String> names = Arrays.stream(fields.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return names.isEmpty() ? null : names;
    }

    private static void retain(JsonNode node, Set<String> fields) {
        if (node instanceof ObjectNode object) {
            for (String property : new ArrayList<>(object.propertyNames())) {
                if (!fields.contains(property) && !ALWAYS.contains(property)) {
                    object.remove(property);
                }
            }
        }
    }
}
