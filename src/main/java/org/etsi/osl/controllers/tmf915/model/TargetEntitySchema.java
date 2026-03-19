package org.etsi.osl.controllers.tmf915.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * The reference object to the schema and type of target entity which is described by a specification
 */

@Embeddable
@Schema(name = "TargetEntitySchema", description = "The reference object to the schema and type of target entity which is described by a specification")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class TargetEntitySchema {

  private String atSchemaLocation;

  private String atType;

  public TargetEntitySchema() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TargetEntitySchema(String atSchemaLocation, String atType) {
    this.atSchemaLocation = atSchemaLocation;
    this.atType = atType;
  }

  public TargetEntitySchema atSchemaLocation(String atSchemaLocation) {
    this.atSchemaLocation = atSchemaLocation;
    return this;
  }

  /**
   * This field provides a link to the schema describing the target entity
   * @return atSchemaLocation
   */
  @NotNull 
  @Schema(name = "@schemaLocation", description = "This field provides a link to the schema describing the target entity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("@schemaLocation")
  public String getAtSchemaLocation() {
    return atSchemaLocation;
  }

  @JsonProperty("@schemaLocation")
  public void setAtSchemaLocation(String atSchemaLocation) {
    this.atSchemaLocation = atSchemaLocation;
  }

  public TargetEntitySchema atType(String atType) {
    this.atType = atType;
    return this;
  }

  /**
   * Class type of the target entity
   * @return atType
   */
  @NotNull 
  @Schema(name = "@type", description = "Class type of the target entity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("@type")
  public String getAtType() {
    return atType;
  }

  @JsonProperty("@type")
  public void setAtType(String atType) {
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
    TargetEntitySchema targetEntitySchema = (TargetEntitySchema) o;
    return Objects.equals(this.atSchemaLocation, targetEntitySchema.atSchemaLocation) &&
        Objects.equals(this.atType, targetEntitySchema.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TargetEntitySchema {\n");
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

