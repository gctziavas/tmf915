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
import org.etsi.osl.controllers.tmf915.model.Characteristic;
import org.etsi.osl.controllers.tmf915.model.EntityRef;
import org.etsi.osl.controllers.tmf915.model.RelatedParty;
import org.etsi.osl.controllers.tmf915.model.Rule;
import org.etsi.osl.controllers.tmf915.model.TemplateRef;
import org.etsi.osl.controllers.tmf915.model.TimePeriod;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * An Ai Contract represents a contract or arrangement, either written or verbal and sometimes enforceable by law, such as a service level agreement or a customer price agreement. An agreement involves a number of other business entities, such as products, services, and resources and/or their specifications.
 */

@Schema(name = "AiContract", description = "An Ai Contract represents a contract or arrangement, either written or verbal and sometimes enforceable by law, such as a service level agreement or a customer price agreement. An agreement involves a number of other business entities, such as products, services, and resources and/or their specifications.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class AiContract {

  private @Nullable String id;

  private @Nullable URI href;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime approvalDate;

  private @Nullable Boolean approved;

  private @Nullable String description;

  private @Nullable String name;

  private @Nullable String state;

  private @Nullable String version;

  private @Nullable EntityRef aiContractSpecification;

  private @Nullable EntityRef aiModel;

  @Valid
  private List<@Valid Characteristic> characteristic = new ArrayList<>();

  @Valid
  private List<@Valid RelatedParty> relatedParty = new ArrayList<>();

  @Valid
  private List<@Valid Rule> rule = new ArrayList<>();

  private @Nullable TemplateRef template;

  private @Nullable TimePeriod validFor;

  private @Nullable String atBaseType;

  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public AiContract id(@Nullable String id) {
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

  public AiContract href(@Nullable URI href) {
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

  public AiContract approvalDate(@Nullable OffsetDateTime approvalDate) {
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

  public AiContract approved(@Nullable Boolean approved) {
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

  public AiContract description(@Nullable String description) {
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

  public AiContract name(@Nullable String name) {
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

  public AiContract state(@Nullable String state) {
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

  public AiContract version(@Nullable String version) {
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

  public AiContract aiContractSpecification(@Nullable EntityRef aiContractSpecification) {
    this.aiContractSpecification = aiContractSpecification;
    return this;
  }

  /**
   * Get aiContractSpecification
   * @return aiContractSpecification
   */
  @Valid 
  @Schema(name = "aiContractSpecification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aiContractSpecification")
  public @Nullable EntityRef getAiContractSpecification() {
    return aiContractSpecification;
  }

  @JsonProperty("aiContractSpecification")
  public void setAiContractSpecification(@Nullable EntityRef aiContractSpecification) {
    this.aiContractSpecification = aiContractSpecification;
  }

  public AiContract aiModel(@Nullable EntityRef aiModel) {
    this.aiModel = aiModel;
    return this;
  }

  /**
   * Get aiModel
   * @return aiModel
   */
  @Valid 
  @Schema(name = "aiModel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aiModel")
  public @Nullable EntityRef getAiModel() {
    return aiModel;
  }

  @JsonProperty("aiModel")
  public void setAiModel(@Nullable EntityRef aiModel) {
    this.aiModel = aiModel;
  }

  public AiContract characteristic(List<@Valid Characteristic> characteristic) {
    this.characteristic = characteristic;
    return this;
  }

  public AiContract addCharacteristicItem(Characteristic characteristicItem) {
    if (this.characteristic == null) {
      this.characteristic = new ArrayList<>();
    }
    this.characteristic.add(characteristicItem);
    return this;
  }

  /**
   * Get characteristic
   * @return characteristic
   */
  @Valid 
  @Schema(name = "characteristic", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("characteristic")
  public List<@Valid Characteristic> getCharacteristic() {
    return characteristic;
  }

  @JsonProperty("characteristic")
  public void setCharacteristic(List<@Valid Characteristic> characteristic) {
    this.characteristic = characteristic;
  }

  public AiContract relatedParty(List<@Valid RelatedParty> relatedParty) {
    this.relatedParty = relatedParty;
    return this;
  }

  public AiContract addRelatedPartyItem(RelatedParty relatedPartyItem) {
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

  public AiContract rule(List<@Valid Rule> rule) {
    this.rule = rule;
    return this;
  }

  public AiContract addRuleItem(Rule ruleItem) {
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

  public AiContract template(@Nullable TemplateRef template) {
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

  public AiContract validFor(@Nullable TimePeriod validFor) {
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

  public AiContract atBaseType(@Nullable String atBaseType) {
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

  public AiContract atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public AiContract atType(@Nullable String atType) {
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
    AiContract aiContract = (AiContract) o;
    return Objects.equals(this.id, aiContract.id) &&
        Objects.equals(this.href, aiContract.href) &&
        Objects.equals(this.approvalDate, aiContract.approvalDate) &&
        Objects.equals(this.approved, aiContract.approved) &&
        Objects.equals(this.description, aiContract.description) &&
        Objects.equals(this.name, aiContract.name) &&
        Objects.equals(this.state, aiContract.state) &&
        Objects.equals(this.version, aiContract.version) &&
        Objects.equals(this.aiContractSpecification, aiContract.aiContractSpecification) &&
        Objects.equals(this.aiModel, aiContract.aiModel) &&
        Objects.equals(this.characteristic, aiContract.characteristic) &&
        Objects.equals(this.relatedParty, aiContract.relatedParty) &&
        Objects.equals(this.rule, aiContract.rule) &&
        Objects.equals(this.template, aiContract.template) &&
        Objects.equals(this.validFor, aiContract.validFor) &&
        Objects.equals(this.atBaseType, aiContract.atBaseType) &&
        Objects.equals(this.atSchemaLocation, aiContract.atSchemaLocation) &&
        Objects.equals(this.atType, aiContract.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, approvalDate, approved, description, name, state, version, aiContractSpecification, aiModel, characteristic, relatedParty, rule, template, validFor, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AiContract {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    approvalDate: ").append(toIndentedString(approvalDate)).append("\n");
    sb.append("    approved: ").append(toIndentedString(approved)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    aiContractSpecification: ").append(toIndentedString(aiContractSpecification)).append("\n");
    sb.append("    aiModel: ").append(toIndentedString(aiModel)).append("\n");
    sb.append("    characteristic: ").append(toIndentedString(characteristic)).append("\n");
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

