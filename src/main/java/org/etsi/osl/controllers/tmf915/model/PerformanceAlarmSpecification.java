package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.etsi.osl.controllers.tmf915.model.PerceivedSeverity;
import org.etsi.osl.controllers.tmf915.model.PerformanceConsequence;
import org.jspecify.annotations.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * The specification of how to populate the alarm fields when generating a threshold crossing alarm.
 */

@Schema(name = "PerformanceAlarmSpecification", description = "The specification of how to populate the alarm fields when generating a threshold crossing alarm.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class PerformanceAlarmSpecification {

  private @Nullable String perfAlarmSpecAdditionalText;

  private @Nullable String perfAlarmSpecAlarmType;

  private @Nullable String perfAlarmSpecProbableCause;

  private @Nullable String perfAlarmSpecSpecificProblem;

  private @Nullable String perfAlarmThresholdCrossingDescription;

  private @Nullable PerceivedSeverity perfAlarmSpecSeverity;

  private @Nullable PerformanceConsequence performanceConsequence;

  public PerformanceAlarmSpecification perfAlarmSpecAdditionalText(@Nullable String perfAlarmSpecAdditionalText) {
    this.perfAlarmSpecAdditionalText = perfAlarmSpecAdditionalText;
    return this;
  }

  /**
   * Get perfAlarmSpecAdditionalText
   * @return perfAlarmSpecAdditionalText
   */
  
  @Schema(name = "perfAlarmSpecAdditionalText", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("perfAlarmSpecAdditionalText")
  public @Nullable String getPerfAlarmSpecAdditionalText() {
    return perfAlarmSpecAdditionalText;
  }

  @JsonProperty("perfAlarmSpecAdditionalText")
  public void setPerfAlarmSpecAdditionalText(@Nullable String perfAlarmSpecAdditionalText) {
    this.perfAlarmSpecAdditionalText = perfAlarmSpecAdditionalText;
  }

  public PerformanceAlarmSpecification perfAlarmSpecAlarmType(@Nullable String perfAlarmSpecAlarmType) {
    this.perfAlarmSpecAlarmType = perfAlarmSpecAlarmType;
    return this;
  }

  /**
   * Get perfAlarmSpecAlarmType
   * @return perfAlarmSpecAlarmType
   */
  
  @Schema(name = "perfAlarmSpecAlarmType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("perfAlarmSpecAlarmType")
  public @Nullable String getPerfAlarmSpecAlarmType() {
    return perfAlarmSpecAlarmType;
  }

  @JsonProperty("perfAlarmSpecAlarmType")
  public void setPerfAlarmSpecAlarmType(@Nullable String perfAlarmSpecAlarmType) {
    this.perfAlarmSpecAlarmType = perfAlarmSpecAlarmType;
  }

  public PerformanceAlarmSpecification perfAlarmSpecProbableCause(@Nullable String perfAlarmSpecProbableCause) {
    this.perfAlarmSpecProbableCause = perfAlarmSpecProbableCause;
    return this;
  }

  /**
   * Get perfAlarmSpecProbableCause
   * @return perfAlarmSpecProbableCause
   */
  
  @Schema(name = "perfAlarmSpecProbableCause", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("perfAlarmSpecProbableCause")
  public @Nullable String getPerfAlarmSpecProbableCause() {
    return perfAlarmSpecProbableCause;
  }

  @JsonProperty("perfAlarmSpecProbableCause")
  public void setPerfAlarmSpecProbableCause(@Nullable String perfAlarmSpecProbableCause) {
    this.perfAlarmSpecProbableCause = perfAlarmSpecProbableCause;
  }

  public PerformanceAlarmSpecification perfAlarmSpecSpecificProblem(@Nullable String perfAlarmSpecSpecificProblem) {
    this.perfAlarmSpecSpecificProblem = perfAlarmSpecSpecificProblem;
    return this;
  }

  /**
   * Get perfAlarmSpecSpecificProblem
   * @return perfAlarmSpecSpecificProblem
   */
  
  @Schema(name = "perfAlarmSpecSpecificProblem", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("perfAlarmSpecSpecificProblem")
  public @Nullable String getPerfAlarmSpecSpecificProblem() {
    return perfAlarmSpecSpecificProblem;
  }

  @JsonProperty("perfAlarmSpecSpecificProblem")
  public void setPerfAlarmSpecSpecificProblem(@Nullable String perfAlarmSpecSpecificProblem) {
    this.perfAlarmSpecSpecificProblem = perfAlarmSpecSpecificProblem;
  }

  public PerformanceAlarmSpecification perfAlarmThresholdCrossingDescription(@Nullable String perfAlarmThresholdCrossingDescription) {
    this.perfAlarmThresholdCrossingDescription = perfAlarmThresholdCrossingDescription;
    return this;
  }

  /**
   * Get perfAlarmThresholdCrossingDescription
   * @return perfAlarmThresholdCrossingDescription
   */
  
  @Schema(name = "perfAlarmThresholdCrossingDescription", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("perfAlarmThresholdCrossingDescription")
  public @Nullable String getPerfAlarmThresholdCrossingDescription() {
    return perfAlarmThresholdCrossingDescription;
  }

  @JsonProperty("perfAlarmThresholdCrossingDescription")
  public void setPerfAlarmThresholdCrossingDescription(@Nullable String perfAlarmThresholdCrossingDescription) {
    this.perfAlarmThresholdCrossingDescription = perfAlarmThresholdCrossingDescription;
  }

  public PerformanceAlarmSpecification perfAlarmSpecSeverity(@Nullable PerceivedSeverity perfAlarmSpecSeverity) {
    this.perfAlarmSpecSeverity = perfAlarmSpecSeverity;
    return this;
  }

  /**
   * Get perfAlarmSpecSeverity
   * @return perfAlarmSpecSeverity
   */
  @Valid 
  @Schema(name = "perfAlarmSpecSeverity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("perfAlarmSpecSeverity")
  public @Nullable PerceivedSeverity getPerfAlarmSpecSeverity() {
    return perfAlarmSpecSeverity;
  }

  @JsonProperty("perfAlarmSpecSeverity")
  public void setPerfAlarmSpecSeverity(@Nullable PerceivedSeverity perfAlarmSpecSeverity) {
    this.perfAlarmSpecSeverity = perfAlarmSpecSeverity;
  }

  public PerformanceAlarmSpecification performanceConsequence(@Nullable PerformanceConsequence performanceConsequence) {
    this.performanceConsequence = performanceConsequence;
    return this;
  }

  /**
   * Get performanceConsequence
   * @return performanceConsequence
   */
  @Valid 
  @Schema(name = "performanceConsequence", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("performanceConsequence")
  public @Nullable PerformanceConsequence getPerformanceConsequence() {
    return performanceConsequence;
  }

  @JsonProperty("performanceConsequence")
  public void setPerformanceConsequence(@Nullable PerformanceConsequence performanceConsequence) {
    this.performanceConsequence = performanceConsequence;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformanceAlarmSpecification performanceAlarmSpecification = (PerformanceAlarmSpecification) o;
    return Objects.equals(this.perfAlarmSpecAdditionalText, performanceAlarmSpecification.perfAlarmSpecAdditionalText) &&
        Objects.equals(this.perfAlarmSpecAlarmType, performanceAlarmSpecification.perfAlarmSpecAlarmType) &&
        Objects.equals(this.perfAlarmSpecProbableCause, performanceAlarmSpecification.perfAlarmSpecProbableCause) &&
        Objects.equals(this.perfAlarmSpecSpecificProblem, performanceAlarmSpecification.perfAlarmSpecSpecificProblem) &&
        Objects.equals(this.perfAlarmThresholdCrossingDescription, performanceAlarmSpecification.perfAlarmThresholdCrossingDescription) &&
        Objects.equals(this.perfAlarmSpecSeverity, performanceAlarmSpecification.perfAlarmSpecSeverity) &&
        Objects.equals(this.performanceConsequence, performanceAlarmSpecification.performanceConsequence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(perfAlarmSpecAdditionalText, perfAlarmSpecAlarmType, perfAlarmSpecProbableCause, perfAlarmSpecSpecificProblem, perfAlarmThresholdCrossingDescription, perfAlarmSpecSeverity, performanceConsequence);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformanceAlarmSpecification {\n");
    sb.append("    perfAlarmSpecAdditionalText: ").append(toIndentedString(perfAlarmSpecAdditionalText)).append("\n");
    sb.append("    perfAlarmSpecAlarmType: ").append(toIndentedString(perfAlarmSpecAlarmType)).append("\n");
    sb.append("    perfAlarmSpecProbableCause: ").append(toIndentedString(perfAlarmSpecProbableCause)).append("\n");
    sb.append("    perfAlarmSpecSpecificProblem: ").append(toIndentedString(perfAlarmSpecSpecificProblem)).append("\n");
    sb.append("    perfAlarmThresholdCrossingDescription: ").append(toIndentedString(perfAlarmThresholdCrossingDescription)).append("\n");
    sb.append("    perfAlarmSpecSeverity: ").append(toIndentedString(perfAlarmSpecSeverity)).append("\n");
    sb.append("    performanceConsequence: ").append(toIndentedString(performanceConsequence)).append("\n");
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

