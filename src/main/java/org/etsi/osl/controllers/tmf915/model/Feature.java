package org.etsi.osl.controllers.tmf915.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Configuration feature.
 */

@Schema(name = "Feature", description = "Configuration feature.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class Feature {

  private @Nullable String id;

  private @Nullable Boolean isBundle;

  private @Nullable Boolean isEnabled;

  private String name;

  @Valid
  private List<@Valid ConstraintRef> constraint = new ArrayList<>();

  @Valid
  private List<@Valid Characteristic> featureCharacteristic = new ArrayList<>();

  @Valid
  private List<@Valid FeatureRelationship> featureRelationship = new ArrayList<>();

  public Feature() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Feature(String name, List<@Valid Characteristic> featureCharacteristic) {
    this.name = name;
    this.featureCharacteristic = featureCharacteristic;
  }

  public Feature id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * Unique identifier of the feature.
   * @return id
   */
  
  @Schema(name = "id", description = "Unique identifier of the feature.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(@Nullable String id) {
    this.id = id;
  }

  public Feature isBundle(@Nullable Boolean isBundle) {
    this.isBundle = isBundle;
    return this;
  }

  /**
   * True if this is a feature group. Default is false.
   * @return isBundle
   */
  
  @Schema(name = "isBundle", description = "True if this is a feature group. Default is false.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isBundle")
  public @Nullable Boolean getIsBundle() {
    return isBundle;
  }

  @JsonProperty("isBundle")
  public void setIsBundle(@Nullable Boolean isBundle) {
    this.isBundle = isBundle;
  }

  public Feature isEnabled(@Nullable Boolean isEnabled) {
    this.isEnabled = isEnabled;
    return this;
  }

  /**
   * True if this feature is enabled. Default is true.
   * @return isEnabled
   */
  
  @Schema(name = "isEnabled", description = "True if this feature is enabled. Default is true.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isEnabled")
  public @Nullable Boolean getIsEnabled() {
    return isEnabled;
  }

  @JsonProperty("isEnabled")
  public void setIsEnabled(@Nullable Boolean isEnabled) {
    this.isEnabled = isEnabled;
  }

  public Feature name(String name) {
    this.name = name;
    return this;
  }

  /**
   * This is the name for the feature.
   * @return name
   */
  @NotNull 
  @Schema(name = "name", description = "This is the name for the feature.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  public Feature constraint(List<@Valid ConstraintRef> constraint) {
    this.constraint = constraint;
    return this;
  }

  public Feature addConstraintItem(ConstraintRef constraintItem) {
    if (this.constraint == null) {
      this.constraint = new ArrayList<>();
    }
    this.constraint.add(constraintItem);
    return this;
  }

  /**
   * This is a list of feature constraints.
   * @return constraint
   */
  @Valid 
  @Schema(name = "constraint", description = "This is a list of feature constraints.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("constraint")
  public List<@Valid ConstraintRef> getConstraint() {
    return constraint;
  }

  @JsonProperty("constraint")
  public void setConstraint(List<@Valid ConstraintRef> constraint) {
    this.constraint = constraint;
  }

  public Feature featureCharacteristic(List<@Valid Characteristic> featureCharacteristic) {
    this.featureCharacteristic = featureCharacteristic;
    return this;
  }

  public Feature addFeatureCharacteristicItem(Characteristic featureCharacteristicItem) {
    if (this.featureCharacteristic == null) {
      this.featureCharacteristic = new ArrayList<>();
    }
    this.featureCharacteristic.add(featureCharacteristicItem);
    return this;
  }

  /**
   * This is a list of Characteristics for a particular feature.
   * @return featureCharacteristic
   */
  @NotNull @Valid @Size(min = 1) 
  @Schema(name = "featureCharacteristic", description = "This is a list of Characteristics for a particular feature.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("featureCharacteristic")
  public List<@Valid Characteristic> getFeatureCharacteristic() {
    return featureCharacteristic;
  }

  @JsonProperty("featureCharacteristic")
  public void setFeatureCharacteristic(List<@Valid Characteristic> featureCharacteristic) {
    this.featureCharacteristic = featureCharacteristic;
  }

  public Feature featureRelationship(List<@Valid FeatureRelationship> featureRelationship) {
    this.featureRelationship = featureRelationship;
    return this;
  }

  public Feature addFeatureRelationshipItem(FeatureRelationship featureRelationshipItem) {
    if (this.featureRelationship == null) {
      this.featureRelationship = new ArrayList<>();
    }
    this.featureRelationship.add(featureRelationshipItem);
    return this;
  }

  /**
   * Get featureRelationship
   * @return featureRelationship
   */
  @Valid 
  @Schema(name = "featureRelationship", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("featureRelationship")
  public List<@Valid FeatureRelationship> getFeatureRelationship() {
    return featureRelationship;
  }

  @JsonProperty("featureRelationship")
  public void setFeatureRelationship(List<@Valid FeatureRelationship> featureRelationship) {
    this.featureRelationship = featureRelationship;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Feature feature = (Feature) o;
    return Objects.equals(this.id, feature.id) &&
        Objects.equals(this.isBundle, feature.isBundle) &&
        Objects.equals(this.isEnabled, feature.isEnabled) &&
        Objects.equals(this.name, feature.name) &&
        Objects.equals(this.constraint, feature.constraint) &&
        Objects.equals(this.featureCharacteristic, feature.featureCharacteristic) &&
        Objects.equals(this.featureRelationship, feature.featureRelationship);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, isBundle, isEnabled, name, constraint, featureCharacteristic, featureRelationship);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Feature {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    isBundle: ").append(toIndentedString(isBundle)).append("\n");
    sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    constraint: ").append(toIndentedString(constraint)).append("\n");
    sb.append("    featureCharacteristic: ").append(toIndentedString(featureCharacteristic)).append("\n");
    sb.append("    featureRelationship: ").append(toIndentedString(featureRelationship)).append("\n");
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

