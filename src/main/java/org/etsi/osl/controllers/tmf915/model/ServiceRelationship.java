package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.etsi.osl.controllers.tmf915.model.Characteristic;
import org.etsi.osl.controllers.tmf915.model.ServiceRefOrValue;
import org.jspecify.annotations.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ServiceRelationship
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class ServiceRelationship {

  private @Nullable String id;

  private @Nullable URI href;

  private String relationshipType;

  private @Nullable ServiceRefOrValue service;

  @Valid
  private List<@Valid Characteristic> serviceRelationshipCharacteristic = new ArrayList<>();

  private @Nullable String atBaseType;

  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public ServiceRelationship() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ServiceRelationship(String relationshipType) {
    this.relationshipType = relationshipType;
  }

  public ServiceRelationship id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * unique identifier
   * @return id
   */
  
  @Schema(name = "id", description = "unique identifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(@Nullable String id) {
    this.id = id;
  }

  public ServiceRelationship href(@Nullable URI href) {
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

  public ServiceRelationship relationshipType(String relationshipType) {
    this.relationshipType = relationshipType;
    return this;
  }

  /**
   * Get relationshipType
   * @return relationshipType
   */
  @NotNull 
  @Schema(name = "relationshipType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("relationshipType")
  public String getRelationshipType() {
    return relationshipType;
  }

  @JsonProperty("relationshipType")
  public void setRelationshipType(String relationshipType) {
    this.relationshipType = relationshipType;
  }

  public ServiceRelationship service(@Nullable ServiceRefOrValue service) {
    this.service = service;
    return this;
  }

  /**
   * Get service
   * @return service
   */
  @Valid 
  @Schema(name = "service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("service")
  public @Nullable ServiceRefOrValue getService() {
    return service;
  }

  @JsonProperty("service")
  public void setService(@Nullable ServiceRefOrValue service) {
    this.service = service;
  }

  public ServiceRelationship serviceRelationshipCharacteristic(List<@Valid Characteristic> serviceRelationshipCharacteristic) {
    this.serviceRelationshipCharacteristic = serviceRelationshipCharacteristic;
    return this;
  }

  public ServiceRelationship addServiceRelationshipCharacteristicItem(Characteristic serviceRelationshipCharacteristicItem) {
    if (this.serviceRelationshipCharacteristic == null) {
      this.serviceRelationshipCharacteristic = new ArrayList<>();
    }
    this.serviceRelationshipCharacteristic.add(serviceRelationshipCharacteristicItem);
    return this;
  }

  /**
   * Get serviceRelationshipCharacteristic
   * @return serviceRelationshipCharacteristic
   */
  @Valid 
  @Schema(name = "serviceRelationshipCharacteristic", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("serviceRelationshipCharacteristic")
  public List<@Valid Characteristic> getServiceRelationshipCharacteristic() {
    return serviceRelationshipCharacteristic;
  }

  @JsonProperty("serviceRelationshipCharacteristic")
  public void setServiceRelationshipCharacteristic(List<@Valid Characteristic> serviceRelationshipCharacteristic) {
    this.serviceRelationshipCharacteristic = serviceRelationshipCharacteristic;
  }

  public ServiceRelationship atBaseType(@Nullable String atBaseType) {
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

  public ServiceRelationship atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public ServiceRelationship atType(@Nullable String atType) {
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
    ServiceRelationship serviceRelationship = (ServiceRelationship) o;
    return Objects.equals(this.id, serviceRelationship.id) &&
        Objects.equals(this.href, serviceRelationship.href) &&
        Objects.equals(this.relationshipType, serviceRelationship.relationshipType) &&
        Objects.equals(this.service, serviceRelationship.service) &&
        Objects.equals(this.serviceRelationshipCharacteristic, serviceRelationship.serviceRelationshipCharacteristic) &&
        Objects.equals(this.atBaseType, serviceRelationship.atBaseType) &&
        Objects.equals(this.atSchemaLocation, serviceRelationship.atSchemaLocation) &&
        Objects.equals(this.atType, serviceRelationship.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, relationshipType, service, serviceRelationshipCharacteristic, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceRelationship {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    relationshipType: ").append(toIndentedString(relationshipType)).append("\n");
    sb.append("    service: ").append(toIndentedString(service)).append("\n");
    sb.append("    serviceRelationshipCharacteristic: ").append(toIndentedString(serviceRelationshipCharacteristic)).append("\n");
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

