package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.etsi.osl.controllers.tmf915.model.Monitor;
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

@Schema(name = "MonitorDeleteEventPayload", description = "The event data structure")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class MonitorDeleteEventPayload {

  private @Nullable Monitor monitor;

  public MonitorDeleteEventPayload monitor(@Nullable Monitor monitor) {
    this.monitor = monitor;
    return this;
  }

  /**
   * Get monitor
   * @return monitor
   */
  @Valid 
  @Schema(name = "monitor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("monitor")
  public @Nullable Monitor getMonitor() {
    return monitor;
  }

  @JsonProperty("monitor")
  public void setMonitor(@Nullable Monitor monitor) {
    this.monitor = monitor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MonitorDeleteEventPayload monitorDeleteEventPayload = (MonitorDeleteEventPayload) o;
    return Objects.equals(this.monitor, monitorDeleteEventPayload.monitor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(monitor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MonitorDeleteEventPayload {\n");
    sb.append("    monitor: ").append(toIndentedString(monitor)).append("\n");
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

