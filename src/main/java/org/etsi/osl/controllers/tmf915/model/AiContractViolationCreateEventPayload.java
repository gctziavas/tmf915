package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.etsi.osl.controllers.tmf915.model.AiContractViolation;
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

@Schema(name = "AiContractViolationCreateEventPayload", description = "The event data structure")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class AiContractViolationCreateEventPayload {

  private @Nullable AiContractViolation aiContractViolation;

  public AiContractViolationCreateEventPayload aiContractViolation(@Nullable AiContractViolation aiContractViolation) {
    this.aiContractViolation = aiContractViolation;
    return this;
  }

  /**
   * Get aiContractViolation
   * @return aiContractViolation
   */
  @Valid 
  @Schema(name = "aiContractViolation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aiContractViolation")
  public @Nullable AiContractViolation getAiContractViolation() {
    return aiContractViolation;
  }

  @JsonProperty("aiContractViolation")
  public void setAiContractViolation(@Nullable AiContractViolation aiContractViolation) {
    this.aiContractViolation = aiContractViolation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AiContractViolationCreateEventPayload aiContractViolationCreateEventPayload = (AiContractViolationCreateEventPayload) o;
    return Objects.equals(this.aiContractViolation, aiContractViolationCreateEventPayload.aiContractViolation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aiContractViolation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AiContractViolationCreateEventPayload {\n");
    sb.append("    aiContractViolation: ").append(toIndentedString(aiContractViolation)).append("\n");
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

