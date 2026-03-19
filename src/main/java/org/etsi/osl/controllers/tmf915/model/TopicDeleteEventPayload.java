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

@Schema(name = "TopicDeleteEventPayload", description = "The event data structure")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class TopicDeleteEventPayload {

  private @Nullable Topic topic;

  public TopicDeleteEventPayload topic(@Nullable Topic topic) {
    this.topic = topic;
    return this;
  }

  /**
   * Get topic
   * @return topic
   */
  @Valid 
  @Schema(name = "topic", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("topic")
  public @Nullable Topic getTopic() {
    return topic;
  }

  @JsonProperty("topic")
  public void setTopic(@Nullable Topic topic) {
    this.topic = topic;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TopicDeleteEventPayload topicDeleteEventPayload = (TopicDeleteEventPayload) o;
    return Objects.equals(this.topic, topicDeleteEventPayload.topic);
  }

  @Override
  public int hashCode() {
    return Objects.hash(topic);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TopicDeleteEventPayload {\n");
    sb.append("    topic: ").append(toIndentedString(topic)).append("\n");
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

