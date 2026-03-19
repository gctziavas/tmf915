package org.etsi.osl.controllers.tmf915.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * The event data structure
 */

@Schema(name = "AiModelCreateEventPayload", description = "The event data structure")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class AiModelCreateEventPayload {

  private @Nullable AiModel aiModel;

  public AiModelCreateEventPayload aiModel(@Nullable AiModel aiModel) {
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
  public @Nullable AiModel getAiModel() {
    return aiModel;
  }

  @JsonProperty("aiModel")
  public void setAiModel(@Nullable AiModel aiModel) {
    this.aiModel = aiModel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AiModelCreateEventPayload aiModelCreateEventPayload = (AiModelCreateEventPayload) o;
    return Objects.equals(this.aiModel, aiModelCreateEventPayload.aiModel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aiModel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AiModelCreateEventPayload {\n");
    sb.append("    aiModel: ").append(toIndentedString(aiModel)).append("\n");
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

