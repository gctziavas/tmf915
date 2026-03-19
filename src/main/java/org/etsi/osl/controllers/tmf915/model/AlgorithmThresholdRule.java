package org.etsi.osl.controllers.tmf915.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A threshold rule that is a reference to an algorithm
 */

@Schema(name = "AlgorithmThresholdRule", description = "A threshold rule that is a reference to an algorithm")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class AlgorithmThresholdRule {

  private @Nullable String id;

  private @Nullable URI href;

  private @Nullable String algorithmRef;

  private @Nullable String name;

  private @Nullable String perfAlarmSpecThresholdCrossingDescription;

  private @Nullable String thresholdRuleCondition;

  private @Nullable String thresholdRuleName;

  private @Nullable String thresholdRuleSeverity;

  @Valid
  private List<@Valid AlgorithmParams> algorithmParams = new ArrayList<>();

  @Valid
  private List<@Valid Consequence> consequence = new ArrayList<>();

  private @Nullable Measurement measurement;

  private @Nullable PerformanceAlarmSpecification performanceAlarmSpecification;

  private @Nullable String atBaseType;

  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public AlgorithmThresholdRule id(@Nullable String id) {
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

  public AlgorithmThresholdRule href(@Nullable URI href) {
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

  public AlgorithmThresholdRule algorithmRef(@Nullable String algorithmRef) {
    this.algorithmRef = algorithmRef;
    return this;
  }

  /**
   * Get algorithmRef
   * @return algorithmRef
   */
  
  @Schema(name = "algorithmRef", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("algorithmRef")
  public @Nullable String getAlgorithmRef() {
    return algorithmRef;
  }

  @JsonProperty("algorithmRef")
  public void setAlgorithmRef(@Nullable String algorithmRef) {
    this.algorithmRef = algorithmRef;
  }

  public AlgorithmThresholdRule name(@Nullable String name) {
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

  public AlgorithmThresholdRule perfAlarmSpecThresholdCrossingDescription(@Nullable String perfAlarmSpecThresholdCrossingDescription) {
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

  public AlgorithmThresholdRule thresholdRuleCondition(@Nullable String thresholdRuleCondition) {
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

  public AlgorithmThresholdRule thresholdRuleName(@Nullable String thresholdRuleName) {
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

  public AlgorithmThresholdRule thresholdRuleSeverity(@Nullable String thresholdRuleSeverity) {
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

  public AlgorithmThresholdRule algorithmParams(List<@Valid AlgorithmParams> algorithmParams) {
    this.algorithmParams = algorithmParams;
    return this;
  }

  public AlgorithmThresholdRule addAlgorithmParamsItem(AlgorithmParams algorithmParamsItem) {
    if (this.algorithmParams == null) {
      this.algorithmParams = new ArrayList<>();
    }
    this.algorithmParams.add(algorithmParamsItem);
    return this;
  }

  /**
   * Get algorithmParams
   * @return algorithmParams
   */
  @Valid 
  @Schema(name = "algorithmParams", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("algorithmParams")
  public List<@Valid AlgorithmParams> getAlgorithmParams() {
    return algorithmParams;
  }

  @JsonProperty("algorithmParams")
  public void setAlgorithmParams(List<@Valid AlgorithmParams> algorithmParams) {
    this.algorithmParams = algorithmParams;
  }

  public AlgorithmThresholdRule consequence(List<@Valid Consequence> consequence) {
    this.consequence = consequence;
    return this;
  }

  public AlgorithmThresholdRule addConsequenceItem(Consequence consequenceItem) {
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

  public AlgorithmThresholdRule measurement(@Nullable Measurement measurement) {
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

  public AlgorithmThresholdRule performanceAlarmSpecification(@Nullable PerformanceAlarmSpecification performanceAlarmSpecification) {
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

  public AlgorithmThresholdRule atBaseType(@Nullable String atBaseType) {
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

  public AlgorithmThresholdRule atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public AlgorithmThresholdRule atType(@Nullable String atType) {
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
    AlgorithmThresholdRule algorithmThresholdRule = (AlgorithmThresholdRule) o;
    return Objects.equals(this.id, algorithmThresholdRule.id) &&
        Objects.equals(this.href, algorithmThresholdRule.href) &&
        Objects.equals(this.algorithmRef, algorithmThresholdRule.algorithmRef) &&
        Objects.equals(this.name, algorithmThresholdRule.name) &&
        Objects.equals(this.perfAlarmSpecThresholdCrossingDescription, algorithmThresholdRule.perfAlarmSpecThresholdCrossingDescription) &&
        Objects.equals(this.thresholdRuleCondition, algorithmThresholdRule.thresholdRuleCondition) &&
        Objects.equals(this.thresholdRuleName, algorithmThresholdRule.thresholdRuleName) &&
        Objects.equals(this.thresholdRuleSeverity, algorithmThresholdRule.thresholdRuleSeverity) &&
        Objects.equals(this.algorithmParams, algorithmThresholdRule.algorithmParams) &&
        Objects.equals(this.consequence, algorithmThresholdRule.consequence) &&
        Objects.equals(this.measurement, algorithmThresholdRule.measurement) &&
        Objects.equals(this.performanceAlarmSpecification, algorithmThresholdRule.performanceAlarmSpecification) &&
        Objects.equals(this.atBaseType, algorithmThresholdRule.atBaseType) &&
        Objects.equals(this.atSchemaLocation, algorithmThresholdRule.atSchemaLocation) &&
        Objects.equals(this.atType, algorithmThresholdRule.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, algorithmRef, name, perfAlarmSpecThresholdCrossingDescription, thresholdRuleCondition, thresholdRuleName, thresholdRuleSeverity, algorithmParams, consequence, measurement, performanceAlarmSpecification, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AlgorithmThresholdRule {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    algorithmRef: ").append(toIndentedString(algorithmRef)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    perfAlarmSpecThresholdCrossingDescription: ").append(toIndentedString(perfAlarmSpecThresholdCrossingDescription)).append("\n");
    sb.append("    thresholdRuleCondition: ").append(toIndentedString(thresholdRuleCondition)).append("\n");
    sb.append("    thresholdRuleName: ").append(toIndentedString(thresholdRuleName)).append("\n");
    sb.append("    thresholdRuleSeverity: ").append(toIndentedString(thresholdRuleSeverity)).append("\n");
    sb.append("    algorithmParams: ").append(toIndentedString(algorithmParams)).append("\n");
    sb.append("    consequence: ").append(toIndentedString(consequence)).append("\n");
    sb.append("    measurement: ").append(toIndentedString(measurement)).append("\n");
    sb.append("    performanceAlarmSpecification: ").append(toIndentedString(performanceAlarmSpecification)).append("\n");
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

