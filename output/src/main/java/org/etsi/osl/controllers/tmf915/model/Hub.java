package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * A Hub is used to subscribe to an event notification
 */

@Schema(name = "Hub", description = "A Hub is used to subscribe to an event notification")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class Hub {

  private String id;

  private @Nullable URI href;

  private URI callback;

  private @Nullable String query;

  private @Nullable String atBaseType;

  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public Hub() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Hub(String id, URI callback) {
    this.id = id;
    this.callback = callback;
  }

  public Hub id(String id) {
    this.id = id;
    return this;
  }

  /**
   * The unique-id for your subscription - referenced when updating or deleting a subscription
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "4aafacbd-11ff-4dc8-b445-305f2215715f", description = "The unique-id for your subscription - referenced when updating or deleting a subscription", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  public Hub href(@Nullable URI href) {
    this.href = href;
    return this;
  }

  /**
   * Hyperlink reference
   * @return href
   */
  @Valid 
  @Schema(name = "href", description = "Hyperlink reference", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("href")
  public @Nullable URI getHref() {
    return href;
  }

  @JsonProperty("href")
  public void setHref(@Nullable URI href) {
    this.href = href;
  }

  public Hub callback(URI callback) {
    this.callback = callback;
    return this;
  }

  /**
   * The URI that will be POSTed to when a notification is triggered
   * @return callback
   */
  @NotNull @Valid 
  @Schema(name = "callback", example = "http://host/resource/id/listener", description = "The URI that will be POSTed to when a notification is triggered", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("callback")
  public URI getCallback() {
    return callback;
  }

  @JsonProperty("callback")
  public void setCallback(URI callback) {
    this.callback = callback;
  }

  public Hub query(@Nullable String query) {
    this.query = query;
    return this;
  }

  /**
   * This is a query string used to filter notifications in the context of the notifier
   * @return query
   */
  
  @Schema(name = "query", example = "status=active", description = "This is a query string used to filter notifications in the context of the notifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("query")
  public @Nullable String getQuery() {
    return query;
  }

  @JsonProperty("query")
  public void setQuery(@Nullable String query) {
    this.query = query;
  }

  public Hub atBaseType(@Nullable String atBaseType) {
    this.atBaseType = atBaseType;
    return this;
  }

  /**
   * When sub-classing, this defines the super-class
   * @return atBaseType
   */
  
  @Schema(name = "@baseType", description = "When sub-classing, this defines the super-class", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("@baseType")
  public @Nullable String getAtBaseType() {
    return atBaseType;
  }

  @JsonProperty("@baseType")
  public void setAtBaseType(@Nullable String atBaseType) {
    this.atBaseType = atBaseType;
  }

  public Hub atSchemaLocation(@Nullable URI atSchemaLocation) {
    this.atSchemaLocation = atSchemaLocation;
    return this;
  }

  /**
   * A URI to a JSON-Schema file that defines additional attributes and relationships
   * @return atSchemaLocation
   */
  @Valid 
  @Schema(name = "@schemaLocation", description = "A URI to a JSON-Schema file that defines additional attributes and relationships", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("@schemaLocation")
  public @Nullable URI getAtSchemaLocation() {
    return atSchemaLocation;
  }

  @JsonProperty("@schemaLocation")
  public void setAtSchemaLocation(@Nullable URI atSchemaLocation) {
    this.atSchemaLocation = atSchemaLocation;
  }

  public Hub atType(@Nullable String atType) {
    this.atType = atType;
    return this;
  }

  /**
   * When sub-classing, this defines the sub-class Extensible name
   * @return atType
   */
  
  @Schema(name = "@type", description = "When sub-classing, this defines the sub-class Extensible name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("@type")
  public @Nullable String getAtType() {
    return atType;
  }

  @JsonProperty("@type")
  public void setAtType(@Nullable String atType) {
    this.atType = atType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Hub hub = (Hub) o;
    return Objects.equals(this.id, hub.id) &&
        Objects.equals(this.href, hub.href) &&
        Objects.equals(this.callback, hub.callback) &&
        Objects.equals(this.query, hub.query) &&
        Objects.equals(this.atBaseType, hub.atBaseType) &&
        Objects.equals(this.atSchemaLocation, hub.atSchemaLocation) &&
        Objects.equals(this.atType, hub.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, callback, query, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Hub {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    callback: ").append(toIndentedString(callback)).append("\n");
    sb.append("    query: ").append(toIndentedString(query)).append("\n");
    sb.append("    atBaseType: ").append(toIndentedString(atBaseType)).append("\n");
    sb.append("    atSchemaLocation: ").append(toIndentedString(atSchemaLocation)).append("\n");
    sb.append("    atType: ").append(toIndentedString(atType)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }
}

