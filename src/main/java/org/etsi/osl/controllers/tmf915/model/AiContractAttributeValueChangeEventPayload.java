package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.etsi.osl.controllers.tmf915.model.AiContract;
import org.springframework.lang.Nullable;
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

@Schema(name = "AiContractAttributeValueChangeEventPayload", description = "The event data structure")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class AiContractAttributeValueChangeEventPayload {

  private @Nullable AiContract aiContract;

  public AiContractAttributeValueChangeEventPayload aiContract(@Nullable AiContract aiContract) {
    this.aiContract = aiContract;
    return this;
  }

  /**
   * Get aiContract
   * @return aiContract
   */
  @Valid 
  @Schema(name = "aiContract", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aiContract")
  public @Nullable AiContract getAiContract() {
    return aiContract;
  }

  @JsonProperty("aiContract")
  public void setAiContract(@Nullable AiContract aiContract) {
    this.aiContract = aiContract;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AiContractAttributeValueChangeEventPayload aiContractAttributeValueChangeEventPayload = (AiContractAttributeValueChangeEventPayload) o;
    return Objects.equals(this.aiContract, aiContractAttributeValueChangeEventPayload.aiContract);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aiContract);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AiContractAttributeValueChangeEventPayload {\n");
    sb.append("    aiContract: ").append(toIndentedString(aiContract)).append("\n");
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

