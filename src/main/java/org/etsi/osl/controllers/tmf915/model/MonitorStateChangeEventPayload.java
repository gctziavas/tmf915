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

@Schema(name = "MonitorStateChangeEventPayload", description = "The event data structure")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class MonitorStateChangeEventPayload {

  private @Nullable Monitor monitor;

  public MonitorStateChangeEventPayload monitor(@Nullable Monitor monitor) {
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
    MonitorStateChangeEventPayload monitorStateChangeEventPayload = (MonitorStateChangeEventPayload) o;
    return Objects.equals(this.monitor, monitorStateChangeEventPayload.monitor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(monitor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MonitorStateChangeEventPayload {\n");
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

