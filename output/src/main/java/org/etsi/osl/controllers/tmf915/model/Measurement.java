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
 * A counter/KPI to be used by the threshold rule
 */

@Schema(name = "Measurement", description = "A counter/KPI to be used by the threshold rule")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class Measurement {

  private @Nullable String id;

  private @Nullable URI href;

  private @Nullable String collectionType;

  private @Nullable String description;

  private @Nullable String measurementFormula;

  private @Nullable String measurementType;

  private @Nullable String measurementUnit;

  private @Nullable String name;

  private @Nullable String atBaseType;

  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public Measurement id(@Nullable String id) {
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

  public Measurement href(@Nullable URI href) {
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

  public Measurement collectionType(@Nullable String collectionType) {
    this.collectionType = collectionType;
    return this;
  }

  /**
   * This attribute indicates different forms in which the measurement data can be captured (please see details below)
   * @return collectionType
   */
  
  @Schema(name = "collectionType", description = "This attribute indicates different forms in which the measurement data can be captured (please see details below)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("collectionType")
  public @Nullable String getCollectionType() {
    return collectionType;
  }

  @JsonProperty("collectionType")
  public void setCollectionType(@Nullable String collectionType) {
    this.collectionType = collectionType;
  }

  public Measurement description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * A description of the measurement
   * @return description
   */
  
  @Schema(name = "description", description = "A description of the measurement", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public Measurement measurementFormula(@Nullable String measurementFormula) {
    this.measurementFormula = measurementFormula;
    return this;
  }

  /**
   * A formula that is used to calculate a measurement (a string)
   * @return measurementFormula
   */
  
  @Schema(name = "measurementFormula", description = "A formula that is used to calculate a measurement (a string)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("measurementFormula")
  public @Nullable String getMeasurementFormula() {
    return measurementFormula;
  }

  @JsonProperty("measurementFormula")
  public void setMeasurementFormula(@Nullable String measurementFormula) {
    this.measurementFormula = measurementFormula;
  }

  public Measurement measurementType(@Nullable String measurementType) {
    this.measurementType = measurementType;
    return this;
  }

  /**
   * A category of the measurement (different SDOs may be using different categories)
   * @return measurementType
   */
  
  @Schema(name = "measurementType", description = "A category of the measurement (different SDOs may be using different categories)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("measurementType")
  public @Nullable String getMeasurementType() {
    return measurementType;
  }

  @JsonProperty("measurementType")
  public void setMeasurementType(@Nullable String measurementType) {
    this.measurementType = measurementType;
  }

  public Measurement measurementUnit(@Nullable String measurementUnit) {
    this.measurementUnit = measurementUnit;
    return this;
  }

  /**
   * The unit of the measurement (e.g. second, bytes, Celsius, etc.)
   * @return measurementUnit
   */
  
  @Schema(name = "measurementUnit", description = "The unit of the measurement (e.g. second, bytes, Celsius, etc.)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("measurementUnit")
  public @Nullable String getMeasurementUnit() {
    return measurementUnit;
  }

  @JsonProperty("measurementUnit")
  public void setMeasurementUnit(@Nullable String measurementUnit) {
    this.measurementUnit = measurementUnit;
  }

  public Measurement name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * A word, term, or phrase by which a measurement is known and distinguished from other measurements
   * @return name
   */
  
  @Schema(name = "name", description = "A word, term, or phrase by which a measurement is known and distinguished from other measurements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(@Nullable String name) {
    this.name = name;
  }

  public Measurement atBaseType(@Nullable String atBaseType) {
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

  public Measurement atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public Measurement atType(@Nullable String atType) {
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
    Measurement measurement = (Measurement) o;
    return Objects.equals(this.id, measurement.id) &&
        Objects.equals(this.href, measurement.href) &&
        Objects.equals(this.collectionType, measurement.collectionType) &&
        Objects.equals(this.description, measurement.description) &&
        Objects.equals(this.measurementFormula, measurement.measurementFormula) &&
        Objects.equals(this.measurementType, measurement.measurementType) &&
        Objects.equals(this.measurementUnit, measurement.measurementUnit) &&
        Objects.equals(this.name, measurement.name) &&
        Objects.equals(this.atBaseType, measurement.atBaseType) &&
        Objects.equals(this.atSchemaLocation, measurement.atSchemaLocation) &&
        Objects.equals(this.atType, measurement.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, collectionType, description, measurementFormula, measurementType, measurementUnit, name, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Measurement {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    collectionType: ").append(toIndentedString(collectionType)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    measurementFormula: ").append(toIndentedString(measurementFormula)).append("\n");
    sb.append("    measurementType: ").append(toIndentedString(measurementType)).append("\n");
    sb.append("    measurementUnit: ").append(toIndentedString(measurementUnit)).append("\n");
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

