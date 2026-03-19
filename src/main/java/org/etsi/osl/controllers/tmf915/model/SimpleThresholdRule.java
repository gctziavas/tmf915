package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.etsi.osl.controllers.tmf915.model.Consequence;
import org.etsi.osl.controllers.tmf915.model.Measurement;
import org.etsi.osl.controllers.tmf915.model.PerformanceAlarmSpecification;
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
 * A threshold rule that is based on explicit definition of attributes that represent a Boolean expression
 */

@Schema(name = "SimpleThresholdRule", description = "A threshold rule that is based on explicit definition of attributes that represent a Boolean expression")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class SimpleThresholdRule {

  private @Nullable String id;

  private @Nullable URI href;

  private @Nullable Integer conformanceComparatorLower;

  private @Nullable String conformanceComparatorUpper;

  private @Nullable Integer conformanceTargetLower;

  private @Nullable Integer conformanceTargetUpper;

  private @Nullable Integer gracePeriods;

  private @Nullable String name;

  private @Nullable String perfAlarmSpecThresholdCrossingDescription;

  private @Nullable String thresholdRuleCondition;

  private @Nullable String thresholdRuleName;

  private @Nullable String thresholdRuleSeverity;

  private @Nullable Integer thresholdTarget;

  private @Nullable TimePeriod conformancePeriod;

  @Valid
  private List<@Valid Consequence> consequence = new ArrayList<>();

  private @Nullable Measurement measurement;

  private @Nullable PerformanceAlarmSpecification performanceAlarmSpecification;

  private @Nullable TimePeriod tolerancePeriod;

  private @Nullable String atBaseType;

  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public SimpleThresholdRule id(@Nullable String id) {
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

  public SimpleThresholdRule href(@Nullable URI href) {
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

  public SimpleThresholdRule conformanceComparatorLower(@Nullable Integer conformanceComparatorLower) {
    this.conformanceComparatorLower = conformanceComparatorLower;
    return this;
  }

  /**
   * An operator that when applied on a value (of conformanceTargetLower) specifies whether a Performance threshold rule is crossed or ceased to be crossed.
   * @return conformanceComparatorLower
   */
  
  @Schema(name = "conformanceComparatorLower", description = "An operator that when applied on a value (of conformanceTargetLower) specifies whether a Performance threshold rule is crossed or ceased to be crossed.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conformanceComparatorLower")
  public @Nullable Integer getConformanceComparatorLower() {
    return conformanceComparatorLower;
  }

  @JsonProperty("conformanceComparatorLower")
  public void setConformanceComparatorLower(@Nullable Integer conformanceComparatorLower) {
    this.conformanceComparatorLower = conformanceComparatorLower;
  }

  public SimpleThresholdRule conformanceComparatorUpper(@Nullable String conformanceComparatorUpper) {
    this.conformanceComparatorUpper = conformanceComparatorUpper;
    return this;
  }

  /**
   * An operator that when applied on a value (of conformanceTargetUpper) specifies whether a Performance threshold rule is crossed or ceased to be crossed.
   * @return conformanceComparatorUpper
   */
  
  @Schema(name = "conformanceComparatorUpper", description = "An operator that when applied on a value (of conformanceTargetUpper) specifies whether a Performance threshold rule is crossed or ceased to be crossed.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conformanceComparatorUpper")
  public @Nullable String getConformanceComparatorUpper() {
    return conformanceComparatorUpper;
  }

  @JsonProperty("conformanceComparatorUpper")
  public void setConformanceComparatorUpper(@Nullable String conformanceComparatorUpper) {
    this.conformanceComparatorUpper = conformanceComparatorUpper;
  }

  public SimpleThresholdRule conformanceTargetLower(@Nullable Integer conformanceTargetLower) {
    this.conformanceTargetLower = conformanceTargetLower;
    return this;
  }

  /**
   * A value used to determine if the Performance threshold is crossed or ceased to be crossed, serving as an lower value
   * @return conformanceTargetLower
   */
  
  @Schema(name = "conformanceTargetLower", description = "A value used to determine if the Performance threshold is crossed or ceased to be crossed, serving as an lower value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conformanceTargetLower")
  public @Nullable Integer getConformanceTargetLower() {
    return conformanceTargetLower;
  }

  @JsonProperty("conformanceTargetLower")
  public void setConformanceTargetLower(@Nullable Integer conformanceTargetLower) {
    this.conformanceTargetLower = conformanceTargetLower;
  }

  public SimpleThresholdRule conformanceTargetUpper(@Nullable Integer conformanceTargetUpper) {
    this.conformanceTargetUpper = conformanceTargetUpper;
    return this;
  }

  /**
   * A value used to determine if the Performance threshold is crossed or ceased to be crossed, serving as an upper value
   * @return conformanceTargetUpper
   */
  
  @Schema(name = "conformanceTargetUpper", description = "A value used to determine if the Performance threshold is crossed or ceased to be crossed, serving as an upper value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conformanceTargetUpper")
  public @Nullable Integer getConformanceTargetUpper() {
    return conformanceTargetUpper;
  }

  @JsonProperty("conformanceTargetUpper")
  public void setConformanceTargetUpper(@Nullable Integer conformanceTargetUpper) {
    this.conformanceTargetUpper = conformanceTargetUpper;
  }

  public SimpleThresholdRule gracePeriods(@Nullable Integer gracePeriods) {
    this.gracePeriods = gracePeriods;
    return this;
  }

  /**
   * The number of times an objective can remain un-updated in reference to the conformancePeriod without a Performance threshold Consequence being initiated
   * @return gracePeriods
   */
  
  @Schema(name = "gracePeriods", description = "The number of times an objective can remain un-updated in reference to the conformancePeriod without a Performance threshold Consequence being initiated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gracePeriods")
  public @Nullable Integer getGracePeriods() {
    return gracePeriods;
  }

  @JsonProperty("gracePeriods")
  public void setGracePeriods(@Nullable Integer gracePeriods) {
    this.gracePeriods = gracePeriods;
  }

  public SimpleThresholdRule name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * The name of the rule.
   * @return name
   */
  
  @Schema(name = "name", description = "The name of the rule.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(@Nullable String name) {
    this.name = name;
  }

  public SimpleThresholdRule perfAlarmSpecThresholdCrossingDescription(@Nullable String perfAlarmSpecThresholdCrossingDescription) {
    this.perfAlarmSpecThresholdCrossingDescription = perfAlarmSpecThresholdCrossingDescription;
    return this;
  }

  /**
   * A specific description of the threshold crossing (intended to be populated e under the alarm under CrossedThreshold structure).
   * @return perfAlarmSpecThresholdCrossingDescription
   */
  
  @Schema(name = "perfAlarmSpecThresholdCrossingDescription", description = "A specific description of the threshold crossing (intended to be populated e under the alarm under CrossedThreshold structure).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("perfAlarmSpecThresholdCrossingDescription")
  public @Nullable String getPerfAlarmSpecThresholdCrossingDescription() {
    return perfAlarmSpecThresholdCrossingDescription;
  }

  @JsonProperty("perfAlarmSpecThresholdCrossingDescription")
  public void setPerfAlarmSpecThresholdCrossingDescription(@Nullable String perfAlarmSpecThresholdCrossingDescription) {
    this.perfAlarmSpecThresholdCrossingDescription = perfAlarmSpecThresholdCrossingDescription;
  }

  public SimpleThresholdRule thresholdRuleCondition(@Nullable String thresholdRuleCondition) {
    this.thresholdRuleCondition = thresholdRuleCondition;
    return this;
  }

  /**
   * A concrete threshold may have two possible values: \\\"Raise\\\" - a threshold was crossed or \\\"Clear\\\" - a threshold ceased crossing
   * @return thresholdRuleCondition
   */
  
  @Schema(name = "thresholdRuleCondition", description = "A concrete threshold may have two possible values: \\\"Raise\\\" - a threshold was crossed or \\\"Clear\\\" - a threshold ceased crossing", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("thresholdRuleCondition")
  public @Nullable String getThresholdRuleCondition() {
    return thresholdRuleCondition;
  }

  @JsonProperty("thresholdRuleCondition")
  public void setThresholdRuleCondition(@Nullable String thresholdRuleCondition) {
    this.thresholdRuleCondition = thresholdRuleCondition;
  }

  public SimpleThresholdRule thresholdRuleName(@Nullable String thresholdRuleName) {
    this.thresholdRuleName = thresholdRuleName;
    return this;
  }

  /**
   * A word, term, or phrase by which a Performance threshold rule is known and distinguished from other threshold rules
   * @return thresholdRuleName
   */
  
  @Schema(name = "thresholdRuleName", description = "A word, term, or phrase by which a Performance threshold rule is known and distinguished from other threshold rules", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("thresholdRuleName")
  public @Nullable String getThresholdRuleName() {
    return thresholdRuleName;
  }

  @JsonProperty("thresholdRuleName")
  public void setThresholdRuleName(@Nullable String thresholdRuleName) {
    this.thresholdRuleName = thresholdRuleName;
  }

  public SimpleThresholdRule thresholdRuleSeverity(@Nullable String thresholdRuleSeverity) {
    this.thresholdRuleSeverity = thresholdRuleSeverity;
    return this;
  }

  /**
   * A threshold can be generated in different severity levels. A crossing for each level may require a different condition and possibly trigger a different consequence. The supported severity levels are equivalent to the possible severity level of alarms.
   * @return thresholdRuleSeverity
   */
  
  @Schema(name = "thresholdRuleSeverity", description = "A threshold can be generated in different severity levels. A crossing for each level may require a different condition and possibly trigger a different consequence. The supported severity levels are equivalent to the possible severity level of alarms.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("thresholdRuleSeverity")
  public @Nullable String getThresholdRuleSeverity() {
    return thresholdRuleSeverity;
  }

  @JsonProperty("thresholdRuleSeverity")
  public void setThresholdRuleSeverity(@Nullable String thresholdRuleSeverity) {
    this.thresholdRuleSeverity = thresholdRuleSeverity;
  }

  public SimpleThresholdRule thresholdTarget(@Nullable Integer thresholdTarget) {
    this.thresholdTarget = thresholdTarget;
    return this;
  }

  /**
   * A percent that is used to specify when a warning should be used that indicates a Performance threshold is in danger of not being met.
   * @return thresholdTarget
   */
  
  @Schema(name = "thresholdTarget", description = "A percent that is used to specify when a warning should be used that indicates a Performance threshold is in danger of not being met.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("thresholdTarget")
  public @Nullable Integer getThresholdTarget() {
    return thresholdTarget;
  }

  @JsonProperty("thresholdTarget")
  public void setThresholdTarget(@Nullable Integer thresholdTarget) {
    this.thresholdTarget = thresholdTarget;
  }

  public SimpleThresholdRule conformancePeriod(@Nullable TimePeriod conformancePeriod) {
    this.conformancePeriod = conformancePeriod;
    return this;
  }

  /**
   * Get conformancePeriod
   * @return conformancePeriod
   */
  @Valid 
  @Schema(name = "conformancePeriod", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conformancePeriod")
  public @Nullable TimePeriod getConformancePeriod() {
    return conformancePeriod;
  }

  @JsonProperty("conformancePeriod")
  public void setConformancePeriod(@Nullable TimePeriod conformancePeriod) {
    this.conformancePeriod = conformancePeriod;
  }

  public SimpleThresholdRule consequence(List<@Valid Consequence> consequence) {
    this.consequence = consequence;
    return this;
  }

  public SimpleThresholdRule addConsequenceItem(Consequence consequenceItem) {
    if (this.consequence == null) {
      this.consequence = new ArrayList<>();
    }
    this.consequence.add(consequenceItem);
    return this;
  }

  /**
   * A threshold crossing or a threshold ceased to be crossing results in a Performance consequence.
   * @return consequence
   */
  @Valid 
  @Schema(name = "consequence", description = "A threshold crossing or a threshold ceased to be crossing results in a Performance consequence.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequence")
  public List<@Valid Consequence> getConsequence() {
    return consequence;
  }

  @JsonProperty("consequence")
  public void setConsequence(List<@Valid Consequence> consequence) {
    this.consequence = consequence;
  }

  public SimpleThresholdRule measurement(@Nullable Measurement measurement) {
    this.measurement = measurement;
    return this;
  }

  /**
   * Get measurement
   * @return measurement
   */
  @Valid 
  @Schema(name = "measurement", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("measurement")
  public @Nullable Measurement getMeasurement() {
    return measurement;
  }

  @JsonProperty("measurement")
  public void setMeasurement(@Nullable Measurement measurement) {
    this.measurement = measurement;
  }

  public SimpleThresholdRule performanceAlarmSpecification(@Nullable PerformanceAlarmSpecification performanceAlarmSpecification) {
    this.performanceAlarmSpecification = performanceAlarmSpecification;
    return this;
  }

  /**
   * Get performanceAlarmSpecification
   * @return performanceAlarmSpecification
   */
  @Valid 
  @Schema(name = "performanceAlarmSpecification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("performanceAlarmSpecification")
  public @Nullable PerformanceAlarmSpecification getPerformanceAlarmSpecification() {
    return performanceAlarmSpecification;
  }

  @JsonProperty("performanceAlarmSpecification")
  public void setPerformanceAlarmSpecification(@Nullable PerformanceAlarmSpecification performanceAlarmSpecification) {
    this.performanceAlarmSpecification = performanceAlarmSpecification;
  }

  public SimpleThresholdRule tolerancePeriod(@Nullable TimePeriod tolerancePeriod) {
    this.tolerancePeriod = tolerancePeriod;
    return this;
  }

  /**
   * Get tolerancePeriod
   * @return tolerancePeriod
   */
  @Valid 
  @Schema(name = "tolerancePeriod", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tolerancePeriod")
  public @Nullable TimePeriod getTolerancePeriod() {
    return tolerancePeriod;
  }

  @JsonProperty("tolerancePeriod")
  public void setTolerancePeriod(@Nullable TimePeriod tolerancePeriod) {
    this.tolerancePeriod = tolerancePeriod;
  }

  public SimpleThresholdRule atBaseType(@Nullable String atBaseType) {
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

  public SimpleThresholdRule atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public SimpleThresholdRule atType(@Nullable String atType) {
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
    SimpleThresholdRule simpleThresholdRule = (SimpleThresholdRule) o;
    return Objects.equals(this.id, simpleThresholdRule.id) &&
        Objects.equals(this.href, simpleThresholdRule.href) &&
        Objects.equals(this.conformanceComparatorLower, simpleThresholdRule.conformanceComparatorLower) &&
        Objects.equals(this.conformanceComparatorUpper, simpleThresholdRule.conformanceComparatorUpper) &&
        Objects.equals(this.conformanceTargetLower, simpleThresholdRule.conformanceTargetLower) &&
        Objects.equals(this.conformanceTargetUpper, simpleThresholdRule.conformanceTargetUpper) &&
        Objects.equals(this.gracePeriods, simpleThresholdRule.gracePeriods) &&
        Objects.equals(this.name, simpleThresholdRule.name) &&
        Objects.equals(this.perfAlarmSpecThresholdCrossingDescription, simpleThresholdRule.perfAlarmSpecThresholdCrossingDescription) &&
        Objects.equals(this.thresholdRuleCondition, simpleThresholdRule.thresholdRuleCondition) &&
        Objects.equals(this.thresholdRuleName, simpleThresholdRule.thresholdRuleName) &&
        Objects.equals(this.thresholdRuleSeverity, simpleThresholdRule.thresholdRuleSeverity) &&
        Objects.equals(this.thresholdTarget, simpleThresholdRule.thresholdTarget) &&
        Objects.equals(this.conformancePeriod, simpleThresholdRule.conformancePeriod) &&
        Objects.equals(this.consequence, simpleThresholdRule.consequence) &&
        Objects.equals(this.measurement, simpleThresholdRule.measurement) &&
        Objects.equals(this.performanceAlarmSpecification, simpleThresholdRule.performanceAlarmSpecification) &&
        Objects.equals(this.tolerancePeriod, simpleThresholdRule.tolerancePeriod) &&
        Objects.equals(this.atBaseType, simpleThresholdRule.atBaseType) &&
        Objects.equals(this.atSchemaLocation, simpleThresholdRule.atSchemaLocation) &&
        Objects.equals(this.atType, simpleThresholdRule.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, conformanceComparatorLower, conformanceComparatorUpper, conformanceTargetLower, conformanceTargetUpper, gracePeriods, name, perfAlarmSpecThresholdCrossingDescription, thresholdRuleCondition, thresholdRuleName, thresholdRuleSeverity, thresholdTarget, conformancePeriod, consequence, measurement, performanceAlarmSpecification, tolerancePeriod, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SimpleThresholdRule {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    conformanceComparatorLower: ").append(toIndentedString(conformanceComparatorLower)).append("\n");
    sb.append("    conformanceComparatorUpper: ").append(toIndentedString(conformanceComparatorUpper)).append("\n");
    sb.append("    conformanceTargetLower: ").append(toIndentedString(conformanceTargetLower)).append("\n");
    sb.append("    conformanceTargetUpper: ").append(toIndentedString(conformanceTargetUpper)).append("\n");
    sb.append("    gracePeriods: ").append(toIndentedString(gracePeriods)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    perfAlarmSpecThresholdCrossingDescription: ").append(toIndentedString(perfAlarmSpecThresholdCrossingDescription)).append("\n");
    sb.append("    thresholdRuleCondition: ").append(toIndentedString(thresholdRuleCondition)).append("\n");
    sb.append("    thresholdRuleName: ").append(toIndentedString(thresholdRuleName)).append("\n");
    sb.append("    thresholdRuleSeverity: ").append(toIndentedString(thresholdRuleSeverity)).append("\n");
    sb.append("    thresholdTarget: ").append(toIndentedString(thresholdTarget)).append("\n");
    sb.append("    conformancePeriod: ").append(toIndentedString(conformancePeriod)).append("\n");
    sb.append("    consequence: ").append(toIndentedString(consequence)).append("\n");
    sb.append("    measurement: ").append(toIndentedString(measurement)).append("\n");
    sb.append("    performanceAlarmSpecification: ").append(toIndentedString(performanceAlarmSpecification)).append("\n");
    sb.append("    tolerancePeriod: ").append(toIndentedString(tolerancePeriod)).append("\n");
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

