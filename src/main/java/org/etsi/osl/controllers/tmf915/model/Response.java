package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.etsi.osl.controllers.tmf915.model.HeaderItem;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * A response to a request
 */

@Schema(name = "Response", description = "A response to a request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class Response {

  private String body;

  private @Nullable String statusCode;

  @Valid
  private List<@Valid HeaderItem> header = new ArrayList<>();

  public Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Response(String body, List<@Valid HeaderItem> header) {
    this.body = body;
    this.header = header;
  }

  public Response body(String body) {
    this.body = body;
    return this;
  }

  /**
   * The body of the response. For example for an HTTP response might contain HTML for rendering.
   * @return body
   */
  @NotNull 
  @Schema(name = "body", description = "The body of the response. For example for an HTTP response might contain HTML for rendering.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("body")
  public String getBody() {
    return body;
  }

  @JsonProperty("body")
  public void setBody(String body) {
    this.body = body;
  }

  public Response statusCode(@Nullable String statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  /**
   * The status of the response. For example for an HTTP response would be codes such as 200, 400, etc.
   * @return statusCode
   */
  
  @Schema(name = "statusCode", description = "The status of the response. For example for an HTTP response would be codes such as 200, 400, etc.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("statusCode")
  public @Nullable String getStatusCode() {
    return statusCode;
  }

  @JsonProperty("statusCode")
  public void setStatusCode(@Nullable String statusCode) {
    this.statusCode = statusCode;
  }

  public Response header(List<@Valid HeaderItem> header) {
    this.header = header;
    return this;
  }

  public Response addHeaderItem(HeaderItem headerItem) {
    if (this.header == null) {
      this.header = new ArrayList<>();
    }
    this.header.add(headerItem);
    return this;
  }

  /**
   * Items included in the header of the response. For example for an HTTP response might contain negotiated locale.
   * @return header
   */
  @NotNull @Valid @Size(min = 1) 
  @Schema(name = "header", description = "Items included in the header of the response. For example for an HTTP response might contain negotiated locale.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("header")
  public List<@Valid HeaderItem> getHeader() {
    return header;
  }

  @JsonProperty("header")
  public void setHeader(List<@Valid HeaderItem> header) {
    this.header = header;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Response response = (Response) o;
    return Objects.equals(this.body, response.body) &&
        Objects.equals(this.statusCode, response.statusCode) &&
        Objects.equals(this.header, response.header);
  }

  @Override
  public int hashCode() {
    return Objects.hash(body, statusCode, header);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Response {\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    header: ").append(toIndentedString(header)).append("\n");
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

