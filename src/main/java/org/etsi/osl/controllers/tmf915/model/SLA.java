package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.etsi.osl.controllers.tmf915.model.RelatedParty;
import org.etsi.osl.controllers.tmf915.model.Rule;
import org.etsi.osl.controllers.tmf915.model.TemplateRef;
import org.etsi.osl.controllers.tmf915.model.TimePeriod;
import org.springframework.format.annotation.DateTimeFormat;
import org.jspecify.annotations.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Part of a business agreement between a Service Provider and a Customer, quantitatively specifying the service performance level the Service Provider commits to deliver.
 */

@Schema(name = "SLA", description = "Part of a business agreement between a Service Provider and a Customer, quantitatively specifying the service performance level the Service Provider commits to deliver.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class SLA {

  private @Nullable String id;

  private @Nullable String href;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime approvalDate;

  private @Nullable Boolean approved;

  private @Nullable String description;

  private @Nullable String name;

  private @Nullable String state;

  private @Nullable String version;

  @Valid
  private List<@Valid RelatedParty> relatedParty = new ArrayList<>();

  @Valid
  private List<@Valid Rule> rule = new ArrayList<>();

  private @Nullable TemplateRef template;

  private @Nullable TimePeriod validFor;

  private @Nullable String atBaseType;

  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public SLA id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * Unique identifier of the Service Level Agreement (SLA)
   * @return id
   */
  
  @Schema(name = "id", description = "Unique identifier of the Service Level Agreement (SLA)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(@Nullable String id) {
    this.id = id;
  }

  public SLA href(@Nullable String href) {
    this.href = href;
    return this;
  }

  /**
   * Reference of the Service Level Agreement (SLA)
   * @return href
   */
  
  @Schema(name = "href", description = "Reference of the Service Level Agreement (SLA)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("href")
  public @Nullable String getHref() {
    return href;
  }

  @JsonProperty("href")
  public void setHref(@Nullable String href) {
    this.href = href;
  }

  public SLA approvalDate(@Nullable OffsetDateTime approvalDate) {
    this.approvalDate = approvalDate;
    return this;
  }

  /**
   * Get approvalDate
   * @return approvalDate
   */
  @Valid 
  @Schema(name = "approvalDate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("approvalDate")
  public @Nullable OffsetDateTime getApprovalDate() {
    return approvalDate;
  }

  @JsonProperty("approvalDate")
  public void setApprovalDate(@Nullable OffsetDateTime approvalDate) {
    this.approvalDate = approvalDate;
  }

  public SLA approved(@Nullable Boolean approved) {
    this.approved = approved;
    return this;
  }

  /**
   * Get approved
   * @return approved
   */
  
  @Schema(name = "approved", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("approved")
  public @Nullable Boolean getApproved() {
    return approved;
  }

  @JsonProperty("approved")
  public void setApproved(@Nullable Boolean approved) {
    this.approved = approved;
  }

  public SLA description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Description of the Service Level Agreement (SLA)
   * @return description
   */
  
  @Schema(name = "description", description = "Description of the Service Level Agreement (SLA)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public SLA name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Name of the Service Level Agreement (SLA)
   * @return name
   */
  
  @Schema(name = "name", description = "Name of the Service Level Agreement (SLA)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(@Nullable String name) {
    this.name = name;
  }

  public SLA state(@Nullable String state) {
    this.state = state;
    return this;
  }

  /**
   * State of the Service Level Agreement (SLA)
   * @return state
   */
  
  @Schema(name = "state", description = "State of the Service Level Agreement (SLA)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  public @Nullable String getState() {
    return state;
  }

  @JsonProperty("state")
  public void setState(@Nullable String state) {
    this.state = state;
  }

  public SLA version(@Nullable String version) {
    this.version = version;
    return this;
  }

  /**
   * Version of the Service Level Agreement (SLA)
   * @return version
   */
  
  @Schema(name = "version", description = "Version of the Service Level Agreement (SLA)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("version")
  public @Nullable String getVersion() {
    return version;
  }

  @JsonProperty("version")
  public void setVersion(@Nullable String version) {
    this.version = version;
  }

  public SLA relatedParty(List<@Valid RelatedParty> relatedParty) {
    this.relatedParty = relatedParty;
    return this;
  }

  public SLA addRelatedPartyItem(RelatedParty relatedPartyItem) {
    if (this.relatedParty == null) {
      this.relatedParty = new ArrayList<>();
    }
    this.relatedParty.add(relatedPartyItem);
    return this;
  }

  /**
   * Get relatedParty
   * @return relatedParty
   */
  @Valid 
  @Schema(name = "relatedParty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relatedParty")
  public List<@Valid RelatedParty> getRelatedParty() {
    return relatedParty;
  }

  @JsonProperty("relatedParty")
  public void setRelatedParty(List<@Valid RelatedParty> relatedParty) {
    this.relatedParty = relatedParty;
  }

  public SLA rule(List<@Valid Rule> rule) {
    this.rule = rule;
    return this;
  }

  public SLA addRuleItem(Rule ruleItem) {
    if (this.rule == null) {
      this.rule = new ArrayList<>();
    }
    this.rule.add(ruleItem);
    return this;
  }

  /**
   * Get rule
   * @return rule
   */
  @Valid 
  @Schema(name = "rule", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rule")
  public List<@Valid Rule> getRule() {
    return rule;
  }

  @JsonProperty("rule")
  public void setRule(List<@Valid Rule> rule) {
    this.rule = rule;
  }

  public SLA template(@Nullable TemplateRef template) {
    this.template = template;
    return this;
  }

  /**
   * Get template
   * @return template
   */
  @Valid 
  @Schema(name = "template", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("template")
  public @Nullable TemplateRef getTemplate() {
    return template;
  }

  @JsonProperty("template")
  public void setTemplate(@Nullable TemplateRef template) {
    this.template = template;
  }

  public SLA validFor(@Nullable TimePeriod validFor) {
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

  public SLA atBaseType(@Nullable String atBaseType) {
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

  public SLA atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public SLA atType(@Nullable String atType) {
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
    SLA SLA = (SLA) o;
    return Objects.equals(this.id, SLA.id) &&
        Objects.equals(this.href, SLA.href) &&
        Objects.equals(this.approvalDate, SLA.approvalDate) &&
        Objects.equals(this.approved, SLA.approved) &&
        Objects.equals(this.description, SLA.description) &&
        Objects.equals(this.name, SLA.name) &&
        Objects.equals(this.state, SLA.state) &&
        Objects.equals(this.version, SLA.version) &&
        Objects.equals(this.relatedParty, SLA.relatedParty) &&
        Objects.equals(this.rule, SLA.rule) &&
        Objects.equals(this.template, SLA.template) &&
        Objects.equals(this.validFor, SLA.validFor) &&
        Objects.equals(this.atBaseType, SLA.atBaseType) &&
        Objects.equals(this.atSchemaLocation, SLA.atSchemaLocation) &&
        Objects.equals(this.atType, SLA.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, approvalDate, approved, description, name, state, version, relatedParty, rule, template, validFor, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SLA {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    approvalDate: ").append(toIndentedString(approvalDate)).append("\n");
    sb.append("    approved: ").append(toIndentedString(approved)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    relatedParty: ").append(toIndentedString(relatedParty)).append("\n");
    sb.append("    rule: ").append(toIndentedString(rule)).append("\n");
    sb.append("    template: ").append(toIndentedString(template)).append("\n");
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

