package org.etsi.osl.controllers.tmf915.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * Identifies the details of the threshold that has been crossed.
 */

@Schema(name = "CrossedThresholdInformation", description = "Identifies the details of the threshold that has been crossed.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class CrossedThresholdInformation {

  private @Nullable String direction;

  private @Nullable String granularity;

  private @Nullable String indicatorName;

  private @Nullable String indicatorUnit;

  private @Nullable String observedValue;

  private @Nullable String thresholdCrossingDescription;

  private @Nullable ThresholdRef threshold;

  public CrossedThresholdInformation direction(@Nullable String direction) {
    this.direction = direction;
    return this;
  }

  /**
   * Indicates the threshold crossing direction: up or down.
   * @return direction
   */
  
  @Schema(name = "direction", description = "Indicates the threshold crossing direction: up or down.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("direction")
  public @Nullable String getDirection() {
    return direction;
  }

  @JsonProperty("direction")
  public void setDirection(@Nullable String direction) {
    this.direction = direction;
  }

  public CrossedThresholdInformation granularity(@Nullable String granularity) {
    this.granularity = granularity;
    return this;
  }

  /**
   * Indicates the granularity at which the indicator is evaluated for threshold crossing
   * @return granularity
   */
  
  @Schema(name = "granularity", description = "Indicates the granularity at which the indicator is evaluated for threshold crossing", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("granularity")
  public @Nullable String getGranularity() {
    return granularity;
  }

  @JsonProperty("granularity")
  public void setGranularity(@Nullable String granularity) {
    this.granularity = granularity;
  }

  public CrossedThresholdInformation indicatorName(@Nullable String indicatorName) {
    this.indicatorName = indicatorName;
    return this;
  }

  /**
   * Indicates the name of indicator which crossed the threshold.
   * @return indicatorName
   */
  
  @Schema(name = "indicatorName", description = "Indicates the name of indicator which crossed the threshold.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("indicatorName")
  public @Nullable String getIndicatorName() {
    return indicatorName;
  }

  @JsonProperty("indicatorName")
  public void setIndicatorName(@Nullable String indicatorName) {
    this.indicatorName = indicatorName;
  }

  public CrossedThresholdInformation indicatorUnit(@Nullable String indicatorUnit) {
    this.indicatorUnit = indicatorUnit;
    return this;
  }

  /**
   * Indicates the unit of the measurement of the indicator corresponding to the threshold that has been crossed.
   * @return indicatorUnit
   */
  
  @Schema(name = "indicatorUnit", description = "Indicates the unit of the measurement of the indicator corresponding to the threshold that has been crossed.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("indicatorUnit")
  public @Nullable String getIndicatorUnit() {
    return indicatorUnit;
  }

  @JsonProperty("indicatorUnit")
  public void setIndicatorUnit(@Nullable String indicatorUnit) {
    this.indicatorUnit = indicatorUnit;
  }

  public CrossedThresholdInformation observedValue(@Nullable String observedValue) {
    this.observedValue = observedValue;
    return this;
  }

  /**
   * Indicates the value of the indicator which crossed the threshold.
   * @return observedValue
   */
  
  @Schema(name = "observedValue", description = "Indicates the value of the indicator which crossed the threshold.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("observedValue")
  public @Nullable String getObservedValue() {
    return observedValue;
  }

  @JsonProperty("observedValue")
  public void setObservedValue(@Nullable String observedValue) {
    this.observedValue = observedValue;
  }

  public CrossedThresholdInformation thresholdCrossingDescription(@Nullable String thresholdCrossingDescription) {
    this.thresholdCrossingDescription = thresholdCrossingDescription;
    return this;
  }

  /**
   * Indicates further information on the threshold crossing alarm.
   * @return thresholdCrossingDescription
   */
  
  @Schema(name = "thresholdCrossingDescription", description = "Indicates further information on the threshold crossing alarm.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("thresholdCrossingDescription")
  public @Nullable String getThresholdCrossingDescription() {
    return thresholdCrossingDescription;
  }

  @JsonProperty("thresholdCrossingDescription")
  public void setThresholdCrossingDescription(@Nullable String thresholdCrossingDescription) {
    this.thresholdCrossingDescription = thresholdCrossingDescription;
  }

  public CrossedThresholdInformation threshold(@Nullable ThresholdRef threshold) {
    this.threshold = threshold;
    return this;
  }

  /**
   * Get threshold
   * @return threshold
   */
  @Valid 
  @Schema(name = "threshold", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("threshold")
  public @Nullable ThresholdRef getThreshold() {
    return threshold;
  }

  @JsonProperty("threshold")
  public void setThreshold(@Nullable ThresholdRef threshold) {
    this.threshold = threshold;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CrossedThresholdInformation crossedThresholdInformation = (CrossedThresholdInformation) o;
    return Objects.equals(this.direction, crossedThresholdInformation.direction) &&
        Objects.equals(this.granularity, crossedThresholdInformation.granularity) &&
        Objects.equals(this.indicatorName, crossedThresholdInformation.indicatorName) &&
        Objects.equals(this.indicatorUnit, crossedThresholdInformation.indicatorUnit) &&
        Objects.equals(this.observedValue, crossedThresholdInformation.observedValue) &&
        Objects.equals(this.thresholdCrossingDescription, crossedThresholdInformation.thresholdCrossingDescription) &&
        Objects.equals(this.threshold, crossedThresholdInformation.threshold);
  }

  @Override
  public int hashCode() {
    return Objects.hash(direction, granularity, indicatorName, indicatorUnit, observedValue, thresholdCrossingDescription, threshold);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CrossedThresholdInformation {\n");
    sb.append("    direction: ").append(toIndentedString(direction)).append("\n");
    sb.append("    granularity: ").append(toIndentedString(granularity)).append("\n");
    sb.append("    indicatorName: ").append(toIndentedString(indicatorName)).append("\n");
    sb.append("    indicatorUnit: ").append(toIndentedString(indicatorUnit)).append("\n");
    sb.append("    observedValue: ").append(toIndentedString(observedValue)).append("\n");
    sb.append("    thresholdCrossingDescription: ").append(toIndentedString(thresholdCrossingDescription)).append("\n");
    sb.append("    threshold: ").append(toIndentedString(threshold)).append("\n");
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

