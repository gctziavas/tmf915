package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import org.etsi.osl.controllers.tmf915.model.AiContractViolationAttributeValueChangeEventPayload;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * The notification data structure
 */

@Schema(name = "AiContractViolationAttributeValueChangeEvent", description = "The notification data structure")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class AiContractViolationAttributeValueChangeEvent {

  private @Nullable String eventId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime eventTime;

  private @Nullable String eventType;

  private @Nullable String correlationId;

  private @Nullable String domain;

  private @Nullable String title;

  private @Nullable String description;

  private @Nullable String priority;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timeOcurred;

  private @Nullable String fieldPath;

  private @Nullable AiContractViolationAttributeValueChangeEventPayload event;

  public AiContractViolationAttributeValueChangeEvent eventId(@Nullable String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * The identifier of the notification.
   * @return eventId
   */
  
  @Schema(name = "eventId", description = "The identifier of the notification.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventId")
  public @Nullable String getEventId() {
    return eventId;
  }

  @JsonProperty("eventId")
  public void setEventId(@Nullable String eventId) {
    this.eventId = eventId;
  }

  public AiContractViolationAttributeValueChangeEvent eventTime(@Nullable OffsetDateTime eventTime) {
    this.eventTime = eventTime;
    return this;
  }

  /**
   * Time of the event occurrence.
   * @return eventTime
   */
  @Valid 
  @Schema(name = "eventTime", description = "Time of the event occurrence.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventTime")
  public @Nullable OffsetDateTime getEventTime() {
    return eventTime;
  }

  @JsonProperty("eventTime")
  public void setEventTime(@Nullable OffsetDateTime eventTime) {
    this.eventTime = eventTime;
  }

  public AiContractViolationAttributeValueChangeEvent eventType(@Nullable String eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * The type of the notification.
   * @return eventType
   */
  
  @Schema(name = "eventType", description = "The type of the notification.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventType")
  public @Nullable String getEventType() {
    return eventType;
  }

  @JsonProperty("eventType")
  public void setEventType(@Nullable String eventType) {
    this.eventType = eventType;
  }

  public AiContractViolationAttributeValueChangeEvent correlationId(@Nullable String correlationId) {
    this.correlationId = correlationId;
    return this;
  }

  /**
   * The correlation id for this event.
   * @return correlationId
   */
  
  @Schema(name = "correlationId", description = "The correlation id for this event.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("correlationId")
  public @Nullable String getCorrelationId() {
    return correlationId;
  }

  @JsonProperty("correlationId")
  public void setCorrelationId(@Nullable String correlationId) {
    this.correlationId = correlationId;
  }

  public AiContractViolationAttributeValueChangeEvent domain(@Nullable String domain) {
    this.domain = domain;
    return this;
  }

  /**
   * The domain of the event.
   * @return domain
   */
  
  @Schema(name = "domain", description = "The domain of the event.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("domain")
  public @Nullable String getDomain() {
    return domain;
  }

  @JsonProperty("domain")
  public void setDomain(@Nullable String domain) {
    this.domain = domain;
  }

  public AiContractViolationAttributeValueChangeEvent title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * The title of the event.
   * @return title
   */
  
  @Schema(name = "title", description = "The title of the event.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  @JsonProperty("title")
  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public AiContractViolationAttributeValueChangeEvent description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * An explnatory of the event.
   * @return description
   */
  
  @Schema(name = "description", description = "An explnatory of the event.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public AiContractViolationAttributeValueChangeEvent priority(@Nullable String priority) {
    this.priority = priority;
    return this;
  }

  /**
   * A priority.
   * @return priority
   */
  
  @Schema(name = "priority", description = "A priority.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public @Nullable String getPriority() {
    return priority;
  }

  @JsonProperty("priority")
  public void setPriority(@Nullable String priority) {
    this.priority = priority;
  }

  public AiContractViolationAttributeValueChangeEvent timeOcurred(@Nullable OffsetDateTime timeOcurred) {
    this.timeOcurred = timeOcurred;
    return this;
  }

  /**
   * The time the event occured.
   * @return timeOcurred
   */
  @Valid 
  @Schema(name = "timeOcurred", description = "The time the event occured.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeOcurred")
  public @Nullable OffsetDateTime getTimeOcurred() {
    return timeOcurred;
  }

  @JsonProperty("timeOcurred")
  public void setTimeOcurred(@Nullable OffsetDateTime timeOcurred) {
    this.timeOcurred = timeOcurred;
  }

  public AiContractViolationAttributeValueChangeEvent fieldPath(@Nullable String fieldPath) {
    this.fieldPath = fieldPath;
    return this;
  }

  /**
   * The path identifying the object field concerned by this notification.
   * @return fieldPath
   */
  
  @Schema(name = "fieldPath", description = "The path identifying the object field concerned by this notification.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fieldPath")
  public @Nullable String getFieldPath() {
    return fieldPath;
  }

  @JsonProperty("fieldPath")
  public void setFieldPath(@Nullable String fieldPath) {
    this.fieldPath = fieldPath;
  }

  public AiContractViolationAttributeValueChangeEvent event(@Nullable AiContractViolationAttributeValueChangeEventPayload event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
   */
  @Valid 
  @Schema(name = "event", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event")
  public @Nullable AiContractViolationAttributeValueChangeEventPayload getEvent() {
    return event;
  }

  @JsonProperty("event")
  public void setEvent(@Nullable AiContractViolationAttributeValueChangeEventPayload event) {
    this.event = event;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AiContractViolationAttributeValueChangeEvent aiContractViolationAttributeValueChangeEvent = (AiContractViolationAttributeValueChangeEvent) o;
    return Objects.equals(this.eventId, aiContractViolationAttributeValueChangeEvent.eventId) &&
        Objects.equals(this.eventTime, aiContractViolationAttributeValueChangeEvent.eventTime) &&
        Objects.equals(this.eventType, aiContractViolationAttributeValueChangeEvent.eventType) &&
        Objects.equals(this.correlationId, aiContractViolationAttributeValueChangeEvent.correlationId) &&
        Objects.equals(this.domain, aiContractViolationAttributeValueChangeEvent.domain) &&
        Objects.equals(this.title, aiContractViolationAttributeValueChangeEvent.title) &&
        Objects.equals(this.description, aiContractViolationAttributeValueChangeEvent.description) &&
        Objects.equals(this.priority, aiContractViolationAttributeValueChangeEvent.priority) &&
        Objects.equals(this.timeOcurred, aiContractViolationAttributeValueChangeEvent.timeOcurred) &&
        Objects.equals(this.fieldPath, aiContractViolationAttributeValueChangeEvent.fieldPath) &&
        Objects.equals(this.event, aiContractViolationAttributeValueChangeEvent.event);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, eventTime, eventType, correlationId, domain, title, description, priority, timeOcurred, fieldPath, event);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AiContractViolationAttributeValueChangeEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    eventTime: ").append(toIndentedString(eventTime)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    correlationId: ").append(toIndentedString(correlationId)).append("\n");
    sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    timeOcurred: ").append(toIndentedString(timeOcurred)).append("\n");
    sb.append("    fieldPath: ").append(toIndentedString(fieldPath)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
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

