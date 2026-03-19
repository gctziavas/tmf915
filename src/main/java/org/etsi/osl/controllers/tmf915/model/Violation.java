package org.etsi.osl.controllers.tmf915.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.Objects;

/**
 * Violation
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class Violation {

  private @Nullable String id;

  private @Nullable URI href;

  private String actualValue;

  private @Nullable String comment;

  private String consequence;

  private String operator;

  private String referenceValue;

  private @Nullable String tolerance;

  private @Nullable String unit;

  private @Nullable String violationAverage;

  private @Nullable AttachmentRef attachment;

  private @Nullable RuleRef rule;

  private @Nullable String atBaseType;

  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public Violation() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Violation(String actualValue, String consequence, String operator, String referenceValue) {
    this.actualValue = actualValue;
    this.consequence = consequence;
    this.operator = operator;
    this.referenceValue = referenceValue;
  }

  public Violation id(@Nullable String id) {
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

  public Violation href(@Nullable URI href) {
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

  public Violation actualValue(String actualValue) {
    this.actualValue = actualValue;
    return this;
  }

  /**
   * Get actualValue
   * @return actualValue
   */
  @NotNull 
  @Schema(name = "actualValue", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("actualValue")
  public String getActualValue() {
    return actualValue;
  }

  @JsonProperty("actualValue")
  public void setActualValue(String actualValue) {
    this.actualValue = actualValue;
  }

  public Violation comment(@Nullable String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * Get comment
   * @return comment
   */
  
  @Schema(name = "comment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("comment")
  public @Nullable String getComment() {
    return comment;
  }

  @JsonProperty("comment")
  public void setComment(@Nullable String comment) {
    this.comment = comment;
  }

  public Violation consequence(String consequence) {
    this.consequence = consequence;
    return this;
  }

  /**
   * Get consequence
   * @return consequence
   */
  @NotNull 
  @Schema(name = "consequence", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("consequence")
  public String getConsequence() {
    return consequence;
  }

  @JsonProperty("consequence")
  public void setConsequence(String consequence) {
    this.consequence = consequence;
  }

  public Violation operator(String operator) {
    this.operator = operator;
    return this;
  }

  /**
   * Get operator
   * @return operator
   */
  @NotNull 
  @Schema(name = "operator", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("operator")
  public String getOperator() {
    return operator;
  }

  @JsonProperty("operator")
  public void setOperator(String operator) {
    this.operator = operator;
  }

  public Violation referenceValue(String referenceValue) {
    this.referenceValue = referenceValue;
    return this;
  }

  /**
   * Get referenceValue
   * @return referenceValue
   */
  @NotNull 
  @Schema(name = "referenceValue", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("referenceValue")
  public String getReferenceValue() {
    return referenceValue;
  }

  @JsonProperty("referenceValue")
  public void setReferenceValue(String referenceValue) {
    this.referenceValue = referenceValue;
  }

  public Violation tolerance(@Nullable String tolerance) {
    this.tolerance = tolerance;
    return this;
  }

  /**
   * Get tolerance
   * @return tolerance
   */
  
  @Schema(name = "tolerance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tolerance")
  public @Nullable String getTolerance() {
    return tolerance;
  }

  @JsonProperty("tolerance")
  public void setTolerance(@Nullable String tolerance) {
    this.tolerance = tolerance;
  }

  public Violation unit(@Nullable String unit) {
    this.unit = unit;
    return this;
  }

  /**
   * Get unit
   * @return unit
   */
  
  @Schema(name = "unit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unit")
  public @Nullable String getUnit() {
    return unit;
  }

  @JsonProperty("unit")
  public void setUnit(@Nullable String unit) {
    this.unit = unit;
  }

  public Violation violationAverage(@Nullable String violationAverage) {
    this.violationAverage = violationAverage;
    return this;
  }

  /**
   * Get violationAverage
   * @return violationAverage
   */
  
  @Schema(name = "violationAverage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("violationAverage")
  public @Nullable String getViolationAverage() {
    return violationAverage;
  }

  @JsonProperty("violationAverage")
  public void setViolationAverage(@Nullable String violationAverage) {
    this.violationAverage = violationAverage;
  }

  public Violation attachment(@Nullable AttachmentRef attachment) {
    this.attachment = attachment;
    return this;
  }

  /**
   * Get attachment
   * @return attachment
   */
  @Valid 
  @Schema(name = "attachment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachment")
  public @Nullable AttachmentRef getAttachment() {
    return attachment;
  }

  @JsonProperty("attachment")
  public void setAttachment(@Nullable AttachmentRef attachment) {
    this.attachment = attachment;
  }

  public Violation rule(@Nullable RuleRef rule) {
    this.rule = rule;
    return this;
  }

  /**
   * Get rule
   * @return rule
   */
  @Valid 
  @Schema(name = "rule", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rule")
  public @Nullable RuleRef getRule() {
    return rule;
  }

  @JsonProperty("rule")
  public void setRule(@Nullable RuleRef rule) {
    this.rule = rule;
  }

  public Violation atBaseType(@Nullable String atBaseType) {
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

  public Violation atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public Violation atType(@Nullable String atType) {
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
    Violation violation = (Violation) o;
    return Objects.equals(this.id, violation.id) &&
        Objects.equals(this.href, violation.href) &&
        Objects.equals(this.actualValue, violation.actualValue) &&
        Objects.equals(this.comment, violation.comment) &&
        Objects.equals(this.consequence, violation.consequence) &&
        Objects.equals(this.operator, violation.operator) &&
        Objects.equals(this.referenceValue, violation.referenceValue) &&
        Objects.equals(this.tolerance, violation.tolerance) &&
        Objects.equals(this.unit, violation.unit) &&
        Objects.equals(this.violationAverage, violation.violationAverage) &&
        Objects.equals(this.attachment, violation.attachment) &&
        Objects.equals(this.rule, violation.rule) &&
        Objects.equals(this.atBaseType, violation.atBaseType) &&
        Objects.equals(this.atSchemaLocation, violation.atSchemaLocation) &&
        Objects.equals(this.atType, violation.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, actualValue, comment, consequence, operator, referenceValue, tolerance, unit, violationAverage, attachment, rule, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Violation {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    actualValue: ").append(toIndentedString(actualValue)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    consequence: ").append(toIndentedString(consequence)).append("\n");
    sb.append("    operator: ").append(toIndentedString(operator)).append("\n");
    sb.append("    referenceValue: ").append(toIndentedString(referenceValue)).append("\n");
    sb.append("    tolerance: ").append(toIndentedString(tolerance)).append("\n");
    sb.append("    unit: ").append(toIndentedString(unit)).append("\n");
    sb.append("    violationAverage: ").append(toIndentedString(violationAverage)).append("\n");
    sb.append("    attachment: ").append(toIndentedString(attachment)).append("\n");
    sb.append("    rule: ").append(toIndentedString(rule)).append("\n");
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

