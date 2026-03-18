package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import org.etsi.osl.controllers.tmf915.model.Request;
import org.etsi.osl.controllers.tmf915.model.Response;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Monitoring of resources
 */

@Schema(name = "Monitor", description = "Monitoring of resources")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class Monitor {

  private @Nullable String id;

  private @Nullable String href;

  private @Nullable String sourceHref;

  private @Nullable String state;

  private @Nullable Request request;

  private @Nullable Response response;

  private @Nullable String atBaseType;

  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public Monitor id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * Identifier of an instance of the monitor. Required to be unique within the resource type.  Used in URIs as the identifier for specific instances of a type
   * @return id
   */
  
  @Schema(name = "id", description = "Identifier of an instance of the monitor. Required to be unique within the resource type.  Used in URIs as the identifier for specific instances of a type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(@Nullable String id) {
    this.id = id;
  }

  public Monitor href(@Nullable String href) {
    this.href = href;
    return this;
  }

  /**
   * reference to this monitor
   * @return href
   */
  
  @Schema(name = "href", description = "reference to this monitor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("href")
  public @Nullable String getHref() {
    return href;
  }

  @JsonProperty("href")
  public void setHref(@Nullable String href) {
    this.href = href;
  }

  public Monitor sourceHref(@Nullable String sourceHref) {
    this.sourceHref = sourceHref;
    return this;
  }

  /**
   * The monitored resource href
   * @return sourceHref
   */
  
  @Schema(name = "sourceHref", description = "The monitored resource href", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sourceHref")
  public @Nullable String getSourceHref() {
    return sourceHref;
  }

  @JsonProperty("sourceHref")
  public void setSourceHref(@Nullable String sourceHref) {
    this.sourceHref = sourceHref;
  }

  public Monitor state(@Nullable String state) {
    this.state = state;
    return this;
  }

  /**
   * The Monitor state of the resource.  InProgress, InError, Completed
   * @return state
   */
  
  @Schema(name = "state", description = "The Monitor state of the resource.  InProgress, InError, Completed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  public @Nullable String getState() {
    return state;
  }

  @JsonProperty("state")
  public void setState(@Nullable String state) {
    this.state = state;
  }

  public Monitor request(@Nullable Request request) {
    this.request = request;
    return this;
  }

  /**
   * Get request
   * @return request
   */
  @Valid 
  @Schema(name = "request", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("request")
  public @Nullable Request getRequest() {
    return request;
  }

  @JsonProperty("request")
  public void setRequest(@Nullable Request request) {
    this.request = request;
  }

  public Monitor response(@Nullable Response response) {
    this.response = response;
    return this;
  }

  /**
   * Get response
   * @return response
   */
  @Valid 
  @Schema(name = "response", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("response")
  public @Nullable Response getResponse() {
    return response;
  }

  @JsonProperty("response")
  public void setResponse(@Nullable Response response) {
    this.response = response;
  }

  public Monitor atBaseType(@Nullable String atBaseType) {
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

  public Monitor atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public Monitor atType(@Nullable String atType) {
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
    Monitor monitor = (Monitor) o;
    return Objects.equals(this.id, monitor.id) &&
        Objects.equals(this.href, monitor.href) &&
        Objects.equals(this.sourceHref, monitor.sourceHref) &&
        Objects.equals(this.state, monitor.state) &&
        Objects.equals(this.request, monitor.request) &&
        Objects.equals(this.response, monitor.response) &&
        Objects.equals(this.atBaseType, monitor.atBaseType) &&
        Objects.equals(this.atSchemaLocation, monitor.atSchemaLocation) &&
        Objects.equals(this.atType, monitor.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, sourceHref, state, request, response, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Monitor {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    sourceHref: ").append(toIndentedString(sourceHref)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    request: ").append(toIndentedString(request)).append("\n");
    sb.append("    response: ").append(toIndentedString(response)).append("\n");
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

