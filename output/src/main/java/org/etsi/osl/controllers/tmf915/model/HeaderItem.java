package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * An item typically included in a request or response
 */

@Schema(name = "HeaderItem", description = "An item typically included in a request or response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class HeaderItem {

  private String name;

  private String value;

  public HeaderItem() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HeaderItem(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public HeaderItem name(String name) {
    this.name = name;
    return this;
  }

  /**
   * The name of the header item, e.g. locale
   * @return name
   */
  @NotNull 
  @Schema(name = "name", description = "The name of the header item, e.g. locale", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  public HeaderItem value(String value) {
    this.value = value;
    return this;
  }

  /**
   * The value of the header item, e.g. en-us
   * @return value
   */
  @NotNull 
  @Schema(name = "value", description = "The value of the header item, e.g. en-us", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("value")
  public String getValue() {
    return value;
  }

  @JsonProperty("value")
  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HeaderItem headerItem = (HeaderItem) o;
    return Objects.equals(this.name, headerItem.name) &&
        Objects.equals(this.value, headerItem.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HeaderItem {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

