package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.etsi.osl.controllers.tmf915.model.AiContractSpecification;
import org.jspecify.annotations.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * The event data structure
 */

@Schema(name = "AiContractSpecificationAttributeValueChangeEventPayload", description = "The event data structure")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class AiContractSpecificationAttributeValueChangeEventPayload {

  private @Nullable AiContractSpecification aiContractSpecification;

  public AiContractSpecificationAttributeValueChangeEventPayload aiContractSpecification(@Nullable AiContractSpecification aiContractSpecification) {
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
  public @Nullable AiContractSpecification getAiContractSpecification() {
    return aiContractSpecification;
  }

  @JsonProperty("aiContractSpecification")
  public void setAiContractSpecification(@Nullable AiContractSpecification aiContractSpecification) {
    this.aiContractSpecification = aiContractSpecification;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AiContractSpecificationAttributeValueChangeEventPayload aiContractSpecificationAttributeValueChangeEventPayload = (AiContractSpecificationAttributeValueChangeEventPayload) o;
    return Objects.equals(this.aiContractSpecification, aiContractSpecificationAttributeValueChangeEventPayload.aiContractSpecification);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aiContractSpecification);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AiContractSpecificationAttributeValueChangeEventPayload {\n");
    sb.append("    aiContractSpecification: ").append(toIndentedString(aiContractSpecification)).append("\n");
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

