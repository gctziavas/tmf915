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

@Schema(name = "Request", description = "A response to a request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class Request {

  private String body;

  private @Nullable String method;

  private @Nullable String to;

  @Valid
  private List<@Valid HeaderItem> header = new ArrayList<>();

  public Request() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Request(String body, List<@Valid HeaderItem> header) {
    this.body = body;
    this.header = header;
  }

  public Request body(String body) {
    this.body = body;
    return this;
  }

  /**
   * The body of the request. For example for an HTTP request might contain content of a form .
   * @return body
   */
  @NotNull 
  @Schema(name = "body", description = "The body of the request. For example for an HTTP request might contain content of a form .", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("body")
  public String getBody() {
    return body;
  }

  @JsonProperty("body")
  public void setBody(String body) {
    this.body = body;
  }

  public Request method(@Nullable String method) {
    this.method = method;
    return this;
  }

  /**
   * The protocol of the request, e.g. http
   * @return method
   */
  
  @Schema(name = "method", description = "The protocol of the request, e.g. http", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("method")
  public @Nullable String getMethod() {
    return method;
  }

  @JsonProperty("method")
  public void setMethod(@Nullable String method) {
    this.method = method;
  }

  public Request to(@Nullable String to) {
    this.to = to;
    return this;
  }

  /**
   * The target of the request, e.g. a URL for an HTTP request
   * @return to
   */
  
  @Schema(name = "to", description = "The target of the request, e.g. a URL for an HTTP request", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("to")
  public @Nullable String getTo() {
    return to;
  }

  @JsonProperty("to")
  public void setTo(@Nullable String to) {
    this.to = to;
  }

  public Request header(List<@Valid HeaderItem> header) {
    this.header = header;
    return this;
  }

  public Request addHeaderItem(HeaderItem headerItem) {
    if (this.header == null) {
      this.header = new ArrayList<>();
    }
    this.header.add(headerItem);
    return this;
  }

  /**
   * Items included in the header of the request. For example for an HTTP request might contain requested locale, basic authentication.
   * @return header
   */
  @NotNull @Valid @Size(min = 1) 
  @Schema(name = "header", description = "Items included in the header of the request. For example for an HTTP request might contain requested locale, basic authentication.", requiredMode = Schema.RequiredMode.REQUIRED)
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
    Request request = (Request) o;
    return Objects.equals(this.body, request.body) &&
        Objects.equals(this.method, request.method) &&
        Objects.equals(this.to, request.to) &&
        Objects.equals(this.header, request.header);
  }

  @Override
  public int hashCode() {
    return Objects.hash(body, method, to, header);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Request {\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    method: ").append(toIndentedString(method)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
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

