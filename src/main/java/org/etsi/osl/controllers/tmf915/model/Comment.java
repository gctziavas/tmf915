package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.jspecify.annotations.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Indicates the comments entered on the alarm.
 */

@Schema(name = "Comment", description = "Indicates the comments entered on the alarm.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class Comment {

  private @Nullable String comment;

  private @Nullable String systemId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime time;

  private @Nullable String userId;

  public Comment comment(@Nullable String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * Indicates the text of the comment.
   * @return comment
   */
  
  @Schema(name = "comment", description = "Indicates the text of the comment.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("comment")
  public @Nullable String getComment() {
    return comment;
  }

  @JsonProperty("comment")
  public void setComment(@Nullable String comment) {
    this.comment = comment;
  }

  public Comment systemId(@Nullable String systemId) {
    this.systemId = systemId;
    return this;
  }

  /**
   * Indicates the system identifier on which the client set the comment.
   * @return systemId
   */
  
  @Schema(name = "systemId", description = "Indicates the system identifier on which the client set the comment.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("systemId")
  public @Nullable String getSystemId() {
    return systemId;
  }

  @JsonProperty("systemId")
  public void setSystemId(@Nullable String systemId) {
    this.systemId = systemId;
  }

  public Comment time(@Nullable OffsetDateTime time) {
    this.time = time;
    return this;
  }

  /**
   * Indicates the time commenting the alarm
   * @return time
   */
  @Valid 
  @Schema(name = "time", description = "Indicates the time commenting the alarm", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time")
  public @Nullable OffsetDateTime getTime() {
    return time;
  }

  @JsonProperty("time")
  public void setTime(@Nullable OffsetDateTime time) {
    this.time = time;
  }

  public Comment userId(@Nullable String userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Indicates the user commenting the alarm.
   * @return userId
   */
  
  @Schema(name = "userId", description = "Indicates the user commenting the alarm.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("userId")
  public @Nullable String getUserId() {
    return userId;
  }

  @JsonProperty("userId")
  public void setUserId(@Nullable String userId) {
    this.userId = userId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Comment comment = (Comment) o;
    return Objects.equals(this.comment, comment.comment) &&
        Objects.equals(this.systemId, comment.systemId) &&
        Objects.equals(this.time, comment.time) &&
        Objects.equals(this.userId, comment.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(comment, systemId, time, userId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Comment {\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    systemId: ").append(toIndentedString(systemId)).append("\n");
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
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

