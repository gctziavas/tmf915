package org.etsi.osl.controllers.tmf915.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.jspecify.annotations.Nullable;

import java.net.URI;

/**
 * Slim TMF-style reference to an {@link AiModelSpecification}, used when an {@link AiModel} is
 * written as JSON so the full specification is never embedded.
 *
 * <p>Only {@code jackson-annotations} are used here, so the shape is the same under Jackson 3
 * (Spring MVC responses) and Jackson 2 (webhook payloads).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "AiModelSpecificationRef", description = "Reference to an AiModelSpecification")
public record AiModelSpecificationRef(
        @JsonProperty("id") String id,
        @JsonProperty("href") @Nullable URI href,
        @JsonProperty("name") @Nullable String name,
        @JsonProperty("version") @Nullable String version,
        @JsonProperty("@baseType") @Nullable String atBaseType,
        @JsonProperty("@schemaLocation") @Nullable URI atSchemaLocation,
        @JsonProperty("@type") @Nullable String atType) {

    public static final String BASE_PATH = "/tmf-api/AiM/v4/aiModelSpecification/";
    public static final String DEFAULT_TYPE = "AIModelSpecification";
    public static final String DEFAULT_BASE_TYPE = "ServiceSpecification";

    /**
     * Builds the reference without loading the specification when possible.
     *
     * <p>If {@code specification} is an uninitialised Hibernate proxy and {@code stored} (the
     * reference persisted on the AiModel row) points at the same id and carries a name, the
     * reference is built from {@code stored} and the proxy is left untouched. Otherwise the
     * values are read from the specification itself, which loads it if it is a proxy; that only
     * happens for rows created before the stored reference was populated.
     */
    public static @Nullable AiModelSpecificationRef of(@Nullable AiModelSpecification specification,
                                                       @Nullable ServiceSpecificationRef stored) {
        if (specification == null) {
            return null;
        }

        String id = identifier(specification);
        if (!Hibernate.isInitialized(specification)
                && stored != null
                && id != null
                && id.equals(stored.getId())
                && stored.getName() != null) {
            return new AiModelSpecificationRef(
                    id,
                    stored.getHref() != null ? stored.getHref() : hrefFor(id),
                    stored.getName(),
                    stored.getVersion(),
                    DEFAULT_BASE_TYPE,
                    null,
                    DEFAULT_TYPE);
        }

        return new AiModelSpecificationRef(
                specification.getId(),
                specification.getHref() != null ? specification.getHref() : hrefFor(specification.getId()),
                specification.getName(),
                specification.getVersion(),
                specification.getAtBaseType(),
                specification.getAtSchemaLocation(),
                specification.getAtType());
    }

    /** Reads the id without initialising a Hibernate proxy. */
    private static @Nullable String identifier(AiModelSpecification specification) {
        if (specification instanceof HibernateProxy proxy) {
            Object id = proxy.getHibernateLazyInitializer().getIdentifier();
            return id != null ? id.toString() : null;
        }
        return specification.getId();
    }

    private static @Nullable URI hrefFor(@Nullable String id) {
        return id != null ? URI.create(BASE_PATH + id) : null;
    }
}
