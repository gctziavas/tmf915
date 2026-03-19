package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.etsi.osl.controllers.tmf915.model.ConstraintRef;
import org.etsi.osl.controllers.tmf915.model.FeatureSpecificationCharacteristic;
import org.etsi.osl.controllers.tmf915.model.FeatureSpecificationRelationship;
import org.etsi.osl.controllers.tmf915.model.TimePeriod;
import org.jspecify.annotations.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Specification for resource, service or product features
 */

@Schema(name = "FeatureSpecification", description = "Specification for resource, service or product features")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class FeatureSpecification {

  private @Nullable String id;

  private @Nullable URI href;

  private @Nullable Boolean isBundle;

  private @Nullable Boolean isEnabled;

  private @Nullable String name;

  private @Nullable String version;

  @Valid
  private List<@Valid ConstraintRef> constraint = new ArrayList<>();

  @Valid
  private List<@Valid FeatureSpecificationCharacteristic> featureSpecCharacteristic = new ArrayList<>();

  @Valid
  private List<@Valid FeatureSpecificationRelationship> featureSpecRelationship = new ArrayList<>();

  private @Nullable TimePeriod validFor;

  private @Nullable String atBaseType;

  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public FeatureSpecification id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * Identifier of the feature specification. Must be locally unique within the containing specification, thus allowing direct access to the feature spec.
   * @return id
   */
  
  @Schema(name = "id", description = "Identifier of the feature specification. Must be locally unique within the containing specification, thus allowing direct access to the feature spec.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(@Nullable String id) {
    this.id = id;
  }

  public FeatureSpecification href(@Nullable URI href) {
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

  public FeatureSpecification isBundle(@Nullable Boolean isBundle) {
    this.isBundle = isBundle;
    return this;
  }

  /**
   * A flag indicating if this is a feature group (true) or not (false)
   * @return isBundle
   */
  
  @Schema(name = "isBundle", description = "A flag indicating if this is a feature group (true) or not (false)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isBundle")
  public @Nullable Boolean getIsBundle() {
    return isBundle;
  }

  @JsonProperty("isBundle")
  public void setIsBundle(@Nullable Boolean isBundle) {
    this.isBundle = isBundle;
  }

  public FeatureSpecification isEnabled(@Nullable Boolean isEnabled) {
    this.isEnabled = isEnabled;
    return this;
  }

  /**
   * A flag indicating if the feature is enabled (true) or not (false)
   * @return isEnabled
   */
  
  @Schema(name = "isEnabled", description = "A flag indicating if the feature is enabled (true) or not (false)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isEnabled")
  public @Nullable Boolean getIsEnabled() {
    return isEnabled;
  }

  @JsonProperty("isEnabled")
  public void setIsEnabled(@Nullable Boolean isEnabled) {
    this.isEnabled = isEnabled;
  }

  public FeatureSpecification name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Unique name given to the feature specification
   * @return name
   */
  
  @Schema(name = "name", description = "Unique name given to the feature specification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(@Nullable String name) {
    this.name = name;
  }

  public FeatureSpecification version(@Nullable String version) {
    this.version = version;
    return this;
  }

  /**
   * Version of the feature specification
   * @return version
   */
  
  @Schema(name = "version", description = "Version of the feature specification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("version")
  public @Nullable String getVersion() {
    return version;
  }

  @JsonProperty("version")
  public void setVersion(@Nullable String version) {
    this.version = version;
  }

  public FeatureSpecification constraint(List<@Valid ConstraintRef> constraint) {
    this.constraint = constraint;
    return this;
  }

  public FeatureSpecification addConstraintItem(ConstraintRef constraintItem) {
    if (this.constraint == null) {
      this.constraint = new ArrayList<>();
    }
    this.constraint.add(constraintItem);
    return this;
  }

  /**
   * This is a list of feature constraints
   * @return constraint
   */
  @Valid 
  @Schema(name = "constraint", description = "This is a list of feature constraints", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("constraint")
  public List<@Valid ConstraintRef> getConstraint() {
    return constraint;
  }

  @JsonProperty("constraint")
  public void setConstraint(List<@Valid ConstraintRef> constraint) {
    this.constraint = constraint;
  }

  public FeatureSpecification featureSpecCharacteristic(List<@Valid FeatureSpecificationCharacteristic> featureSpecCharacteristic) {
    this.featureSpecCharacteristic = featureSpecCharacteristic;
    return this;
  }

  public FeatureSpecification addFeatureSpecCharacteristicItem(FeatureSpecificationCharacteristic featureSpecCharacteristicItem) {
    if (this.featureSpecCharacteristic == null) {
      this.featureSpecCharacteristic = new ArrayList<>();
    }
    this.featureSpecCharacteristic.add(featureSpecCharacteristicItem);
    return this;
  }

  /**
   * This is a list of characteristics for a particular feature
   * @return featureSpecCharacteristic
   */
  @Valid 
  @Schema(name = "featureSpecCharacteristic", description = "This is a list of characteristics for a particular feature", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("featureSpecCharacteristic")
  public List<@Valid FeatureSpecificationCharacteristic> getFeatureSpecCharacteristic() {
    return featureSpecCharacteristic;
  }

  @JsonProperty("featureSpecCharacteristic")
  public void setFeatureSpecCharacteristic(List<@Valid FeatureSpecificationCharacteristic> featureSpecCharacteristic) {
    this.featureSpecCharacteristic = featureSpecCharacteristic;
  }

  public FeatureSpecification featureSpecRelationship(List<@Valid FeatureSpecificationRelationship> featureSpecRelationship) {
    this.featureSpecRelationship = featureSpecRelationship;
    return this;
  }

  public FeatureSpecification addFeatureSpecRelationshipItem(FeatureSpecificationRelationship featureSpecRelationshipItem) {
    if (this.featureSpecRelationship == null) {
      this.featureSpecRelationship = new ArrayList<>();
    }
    this.featureSpecRelationship.add(featureSpecRelationshipItem);
    return this;
  }

  /**
   * A dependency, exclusivity or aggratation relationship between/among feature specifications.
   * @return featureSpecRelationship
   */
  @Valid 
  @Schema(name = "featureSpecRelationship", description = "A dependency, exclusivity or aggratation relationship between/among feature specifications.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("featureSpecRelationship")
  public List<@Valid FeatureSpecificationRelationship> getFeatureSpecRelationship() {
    return featureSpecRelationship;
  }

  @JsonProperty("featureSpecRelationship")
  public void setFeatureSpecRelationship(List<@Valid FeatureSpecificationRelationship> featureSpecRelationship) {
    this.featureSpecRelationship = featureSpecRelationship;
  }

  public FeatureSpecification validFor(@Nullable TimePeriod validFor) {
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

  public FeatureSpecification atBaseType(@Nullable String atBaseType) {
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

  public FeatureSpecification atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public FeatureSpecification atType(@Nullable String atType) {
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
    FeatureSpecification featureSpecification = (FeatureSpecification) o;
    return Objects.equals(this.id, featureSpecification.id) &&
        Objects.equals(this.href, featureSpecification.href) &&
        Objects.equals(this.isBundle, featureSpecification.isBundle) &&
        Objects.equals(this.isEnabled, featureSpecification.isEnabled) &&
        Objects.equals(this.name, featureSpecification.name) &&
        Objects.equals(this.version, featureSpecification.version) &&
        Objects.equals(this.constraint, featureSpecification.constraint) &&
        Objects.equals(this.featureSpecCharacteristic, featureSpecification.featureSpecCharacteristic) &&
        Objects.equals(this.featureSpecRelationship, featureSpecification.featureSpecRelationship) &&
        Objects.equals(this.validFor, featureSpecification.validFor) &&
        Objects.equals(this.atBaseType, featureSpecification.atBaseType) &&
        Objects.equals(this.atSchemaLocation, featureSpecification.atSchemaLocation) &&
        Objects.equals(this.atType, featureSpecification.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, isBundle, isEnabled, name, version, constraint, featureSpecCharacteristic, featureSpecRelationship, validFor, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FeatureSpecification {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    isBundle: ").append(toIndentedString(isBundle)).append("\n");
    sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    constraint: ").append(toIndentedString(constraint)).append("\n");
    sb.append("    featureSpecCharacteristic: ").append(toIndentedString(featureSpecCharacteristic)).append("\n");
    sb.append("    featureSpecRelationship: ").append(toIndentedString(featureSpecRelationship)).append("\n");
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

