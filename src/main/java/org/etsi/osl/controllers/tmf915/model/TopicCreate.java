package org.etsi.osl.controllers.tmf915.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.Objects;

/**
 * Is a event channel provided by the Event Streaming API Skipped properties: id,href
 */

@Schema(name = "Topic_Create", description = "Is a event channel provided by the Event Streaming API Skipped properties: id,href")
@JsonTypeName("Topic_Create")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class TopicCreate {

  private @Nullable String contentQuery;

  private @Nullable String headerQuery;

  private @Nullable String name;

  private @Nullable String atBaseType;

  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public TopicCreate contentQuery(@Nullable String contentQuery) {
    this.contentQuery = contentQuery;
    return this;
  }

  /**
   * is the filter that will be applied on the content of the Event.
   * @return contentQuery
   */
  
  @Schema(name = "contentQuery", description = "is the filter that will be applied on the content of the Event.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contentQuery")
  public @Nullable String getContentQuery() {
    return contentQuery;
  }

  @JsonProperty("contentQuery")
  public void setContentQuery(@Nullable String contentQuery) {
    this.contentQuery = contentQuery;
  }

  public TopicCreate headerQuery(@Nullable String headerQuery) {
    this.headerQuery = headerQuery;
    return this;
  }

  /**
   * is the filter that will be applied on the Event header properties.
   * @return headerQuery
   */
  
  @Schema(name = "headerQuery", description = "is the filter that will be applied on the Event header properties.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("headerQuery")
  public @Nullable String getHeaderQuery() {
    return headerQuery;
  }

  @JsonProperty("headerQuery")
  public void setHeaderQuery(@Nullable String headerQuery) {
    this.headerQuery = headerQuery;
  }

  public TopicCreate name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * use to identify grouping of events, per domain, per event types, per access control-right and so on.
   * @return name
   */
  
  @Schema(name = "name", description = "use to identify grouping of events, per domain, per event types, per access control-right and so on.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(@Nullable String name) {
    this.name = name;
  }

  public TopicCreate atBaseType(@Nullable String atBaseType) {
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

  public TopicCreate atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public TopicCreate atType(@Nullable String atType) {
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
    TopicCreate topicCreate = (TopicCreate) o;
    return Objects.equals(this.contentQuery, topicCreate.contentQuery) &&
        Objects.equals(this.headerQuery, topicCreate.headerQuery) &&
        Objects.equals(this.name, topicCreate.name) &&
        Objects.equals(this.atBaseType, topicCreate.atBaseType) &&
        Objects.equals(this.atSchemaLocation, topicCreate.atSchemaLocation) &&
        Objects.equals(this.atType, topicCreate.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contentQuery, headerQuery, name, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TopicCreate {\n");
    sb.append("    contentQuery: ").append(toIndentedString(contentQuery)).append("\n");
    sb.append("    headerQuery: ").append(toIndentedString(headerQuery)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

