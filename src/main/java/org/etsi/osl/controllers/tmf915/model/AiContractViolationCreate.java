package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.etsi.osl.controllers.tmf915.model.EntityRef;
import org.etsi.osl.controllers.tmf915.model.RelatedParty;
import org.etsi.osl.controllers.tmf915.model.Violation;
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
 *  Skipped properties: id,href
 */

@Schema(name = "AiContractViolation_Create", description = " Skipped properties: id,href")
@JsonTypeName("AiContractViolation_Create")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class AiContractViolationCreate {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime date;

  private EntityRef aiContract;

  @Valid
  private List<@Valid RelatedParty> relatedParty = new ArrayList<>();

  private Violation violation;

  private @Nullable String atBaseType;

  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public AiContractViolationCreate() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AiContractViolationCreate(EntityRef aiContract, Violation violation) {
    this.aiContract = aiContract;
    this.violation = violation;
  }

  public AiContractViolationCreate date(@Nullable OffsetDateTime date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   * @return date
   */
  @Valid 
  @Schema(name = "date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("date")
  public @Nullable OffsetDateTime getDate() {
    return date;
  }

  @JsonProperty("date")
  public void setDate(@Nullable OffsetDateTime date) {
    this.date = date;
  }

  public AiContractViolationCreate aiContract(EntityRef aiContract) {
    this.aiContract = aiContract;
    return this;
  }

  /**
   * Get aiContract
   * @return aiContract
   */
  @NotNull @Valid 
  @Schema(name = "aiContract", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("aiContract")
  public EntityRef getAiContract() {
    return aiContract;
  }

  @JsonProperty("aiContract")
  public void setAiContract(EntityRef aiContract) {
    this.aiContract = aiContract;
  }

  public AiContractViolationCreate relatedParty(List<@Valid RelatedParty> relatedParty) {
    this.relatedParty = relatedParty;
    return this;
  }

  public AiContractViolationCreate addRelatedPartyItem(RelatedParty relatedPartyItem) {
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

  public AiContractViolationCreate violation(Violation violation) {
    this.violation = violation;
    return this;
  }

  /**
   * Get violation
   * @return violation
   */
  @NotNull @Valid 
  @Schema(name = "violation", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("violation")
  public Violation getViolation() {
    return violation;
  }

  @JsonProperty("violation")
  public void setViolation(Violation violation) {
    this.violation = violation;
  }

  public AiContractViolationCreate atBaseType(@Nullable String atBaseType) {
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

  public AiContractViolationCreate atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public AiContractViolationCreate atType(@Nullable String atType) {
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
    AiContractViolationCreate aiContractViolationCreate = (AiContractViolationCreate) o;
    return Objects.equals(this.date, aiContractViolationCreate.date) &&
        Objects.equals(this.aiContract, aiContractViolationCreate.aiContract) &&
        Objects.equals(this.relatedParty, aiContractViolationCreate.relatedParty) &&
        Objects.equals(this.violation, aiContractViolationCreate.violation) &&
        Objects.equals(this.atBaseType, aiContractViolationCreate.atBaseType) &&
        Objects.equals(this.atSchemaLocation, aiContractViolationCreate.atSchemaLocation) &&
        Objects.equals(this.atType, aiContractViolationCreate.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, aiContract, relatedParty, violation, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AiContractViolationCreate {\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    aiContract: ").append(toIndentedString(aiContract)).append("\n");
    sb.append("    relatedParty: ").append(toIndentedString(relatedParty)).append("\n");
    sb.append("    violation: ").append(toIndentedString(violation)).append("\n");
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

