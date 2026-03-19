package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;

/**
 * An aggregation, migration, substitution, dependency or exclusivity relationship between/among FeatureSpecificationCharacteristics.
 */

@Schema(name = "FeatureSpecificationCharacteristicRelationship", description = "An aggregation, migration, substitution, dependency or exclusivity relationship between/among FeatureSpecificationCharacteristics.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class FeatureSpecificationCharacteristicRelationship {

  private @Nullable String id;

  private @Nullable URI href;

  private @Nullable String characteristicId;

  private @Nullable String featureId;

  private @Nullable String name;

  private @Nullable String relationshipType;

  private @Nullable URI resourceSpecificationHref;

  private @Nullable String resourceSpecificationId;

  private @Nullable TimePeriod validFor;

  private @Nullable String atBaseType;

  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public FeatureSpecificationCharacteristicRelationship id(@Nullable String id) {
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

  public FeatureSpecificationCharacteristicRelationship href(@Nullable URI href) {
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

  public FeatureSpecificationCharacteristicRelationship characteristicId(@Nullable String characteristicId) {
    this.characteristicId = characteristicId;
    return this;
  }

  /**
   * Unique identifier of the characteristic within the the target feature specification
   * @return characteristicId
   */
  
  @Schema(name = "characteristicId", description = "Unique identifier of the characteristic within the the target feature specification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("characteristicId")
  public @Nullable String getCharacteristicId() {
    return characteristicId;
  }

  @JsonProperty("characteristicId")
  public void setCharacteristicId(@Nullable String characteristicId) {
    this.characteristicId = characteristicId;
  }

  public FeatureSpecificationCharacteristicRelationship featureId(@Nullable String featureId) {
    this.featureId = featureId;
    return this;
  }

  /**
   * Unique identifier of the target feature specification within the resource specification.
   * @return featureId
   */
  
  @Schema(name = "featureId", description = "Unique identifier of the target feature specification within the resource specification.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("featureId")
  public @Nullable String getFeatureId() {
    return featureId;
  }

  @JsonProperty("featureId")
  public void setFeatureId(@Nullable String featureId) {
    this.featureId = featureId;
  }

  public FeatureSpecificationCharacteristicRelationship name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Name of the target characteristic
   * @return name
   */
  
  @Schema(name = "name", description = "Name of the target characteristic", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(@Nullable String name) {
    this.name = name;
  }

  public FeatureSpecificationCharacteristicRelationship relationshipType(@Nullable String relationshipType) {
    this.relationshipType = relationshipType;
    return this;
  }

  /**
   * Type of relationship such as aggregation, migration, substitution, dependency, exclusivity
   * @return relationshipType
   */
  
  @Schema(name = "relationshipType", description = "Type of relationship such as aggregation, migration, substitution, dependency, exclusivity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationshipType")
  public @Nullable String getRelationshipType() {
    return relationshipType;
  }

  @JsonProperty("relationshipType")
  public void setRelationshipType(@Nullable String relationshipType) {
    this.relationshipType = relationshipType;
  }

  public FeatureSpecificationCharacteristicRelationship resourceSpecificationHref(@Nullable URI resourceSpecificationHref) {
    this.resourceSpecificationHref = resourceSpecificationHref;
    return this;
  }

  /**
   * Hyperlink reference to the resource specification containing the target feature and feature characteristic
   * @return resourceSpecificationHref
   */
  @Valid 
  @Schema(name = "resourceSpecificationHref", description = "Hyperlink reference to the resource specification containing the target feature and feature characteristic", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resourceSpecificationHref")
  public @Nullable URI getResourceSpecificationHref() {
    return resourceSpecificationHref;
  }

  @JsonProperty("resourceSpecificationHref")
  public void setResourceSpecificationHref(@Nullable URI resourceSpecificationHref) {
    this.resourceSpecificationHref = resourceSpecificationHref;
  }

  public FeatureSpecificationCharacteristicRelationship resourceSpecificationId(@Nullable String resourceSpecificationId) {
    this.resourceSpecificationId = resourceSpecificationId;
    return this;
  }

  /**
   * Unique identifier of the resource specification containing the target feature and feature characteristic
   * @return resourceSpecificationId
   */
  
  @Schema(name = "resourceSpecificationId", description = "Unique identifier of the resource specification containing the target feature and feature characteristic", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resourceSpecificationId")
  public @Nullable String getResourceSpecificationId() {
    return resourceSpecificationId;
  }

  @JsonProperty("resourceSpecificationId")
  public void setResourceSpecificationId(@Nullable String resourceSpecificationId) {
    this.resourceSpecificationId = resourceSpecificationId;
  }

  public FeatureSpecificationCharacteristicRelationship validFor(@Nullable TimePeriod validFor) {
    this.validFor = validFor;
    return this;
  }

  /**
   * Get validFor
   * @return validFor
   */
  @Valid 
  @Schema(name = "validFor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("validFor")
  public @Nullable TimePeriod getValidFor() {
    return validFor;
  }

  @JsonProperty("validFor")
  public void setValidFor(@Nullable TimePeriod validFor) {
    this.validFor = validFor;
  }

  public FeatureSpecificationCharacteristicRelationship atBaseType(@Nullable String atBaseType) {
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

  public FeatureSpecificationCharacteristicRelationship atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public FeatureSpecificationCharacteristicRelationship atType(@Nullable String atType) {
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
    FeatureSpecificationCharacteristicRelationship featureSpecificationCharacteristicRelationship = (FeatureSpecificationCharacteristicRelationship) o;
    return Objects.equals(this.id, featureSpecificationCharacteristicRelationship.id) &&
        Objects.equals(this.href, featureSpecificationCharacteristicRelationship.href) &&
        Objects.equals(this.characteristicId, featureSpecificationCharacteristicRelationship.characteristicId) &&
        Objects.equals(this.featureId, featureSpecificationCharacteristicRelationship.featureId) &&
        Objects.equals(this.name, featureSpecificationCharacteristicRelationship.name) &&
        Objects.equals(this.relationshipType, featureSpecificationCharacteristicRelationship.relationshipType) &&
        Objects.equals(this.resourceSpecificationHref, featureSpecificationCharacteristicRelationship.resourceSpecificationHref) &&
        Objects.equals(this.resourceSpecificationId, featureSpecificationCharacteristicRelationship.resourceSpecificationId) &&
        Objects.equals(this.validFor, featureSpecificationCharacteristicRelationship.validFor) &&
        Objects.equals(this.atBaseType, featureSpecificationCharacteristicRelationship.atBaseType) &&
        Objects.equals(this.atSchemaLocation, featureSpecificationCharacteristicRelationship.atSchemaLocation) &&
        Objects.equals(this.atType, featureSpecificationCharacteristicRelationship.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, characteristicId, featureId, name, relationshipType, resourceSpecificationHref, resourceSpecificationId, validFor, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FeatureSpecificationCharacteristicRelationship {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    characteristicId: ").append(toIndentedString(characteristicId)).append("\n");
    sb.append("    featureId: ").append(toIndentedString(featureId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    relationshipType: ").append(toIndentedString(relationshipType)).append("\n");
    sb.append("    resourceSpecificationHref: ").append(toIndentedString(resourceSpecificationHref)).append("\n");
    sb.append("    resourceSpecificationId: ").append(toIndentedString(resourceSpecificationId)).append("\n");
    sb.append("    validFor: ").append(toIndentedString(validFor)).append("\n");
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

