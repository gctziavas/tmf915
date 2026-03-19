package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.etsi.osl.controllers.tmf915.model.Characteristic;
import org.etsi.osl.controllers.tmf915.model.EntityRef;
import org.etsi.osl.controllers.tmf915.model.RelatedParty;
import org.springframework.format.annotation.DateTimeFormat;
import org.jspecify.annotations.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.etsi.osl.controllers.tmf915.mappers.converters.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * event with common attributes.
 */

@Entity
@Table(name = "aim915_event")
@Schema(name = "Event", description = "event with common attributes.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class Event {

  @Id
  private @Nullable String id;

  @Convert(converter = UriToStringConverter.class)
  private @Nullable URI href;

  private @Nullable String correlationId;

  private @Nullable String description;

  private @Nullable String domain;

  private @Nullable String eventId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime eventTime;

  private @Nullable String eventType;

  private @Nullable String priority;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timeOccurred;

  private @Nullable String title;

  @Valid
  @Convert(converter = CharacteristicListConverter.class)
  @Column(columnDefinition = "TEXT")
  private List<@Valid Characteristic> analyticCharacteristic = new ArrayList<>();

  @Convert(converter = ObjectToJsonConverter.class)
  @Column(columnDefinition = "TEXT")
  private @Nullable Object event;

  @Valid
  @Convert(converter = RelatedPartyListConverter.class)
  @Column(columnDefinition = "TEXT")
  private List<@Valid RelatedParty> relatedParty = new ArrayList<>();

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name="id", column=@Column(name="rsys_id")),
      @AttributeOverride(name="href", column=@Column(name="rsys_href")),
      @AttributeOverride(name="name", column=@Column(name="rsys_name")),
      @AttributeOverride(name="atBaseType", column=@Column(name="rsys_base_type")),
      @AttributeOverride(name="atSchemaLocation", column=@Column(name="rsys_schema_loc")),
      @AttributeOverride(name="atType", column=@Column(name="rsys_type")),
      @AttributeOverride(name="atReferredType", column=@Column(name="rsys_referred_type"))
  })
  private @Nullable EntityRef reportingSystem;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name="id", column=@Column(name="src_id")),
      @AttributeOverride(name="href", column=@Column(name="src_href")),
      @AttributeOverride(name="name", column=@Column(name="src_name")),
      @AttributeOverride(name="atBaseType", column=@Column(name="src_base_type")),
      @AttributeOverride(name="atSchemaLocation", column=@Column(name="src_schema_loc")),
      @AttributeOverride(name="atType", column=@Column(name="src_type")),
      @AttributeOverride(name="atReferredType", column=@Column(name="src_referred_type"))
  })
  private @Nullable EntityRef source;

  private @Nullable String atBaseType;

  @Convert(converter = UriToStringConverter.class)
  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  /** Topic-scoped storage field (not part of API spec). */
  @Column(name = "topic_id")
  private String topicId;

  public Event id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * unique identifier
   * @return id
   */
  
  @Schema(name = "id", description = "unique identifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(@Nullable String id) {
    this.id = id;
  }

  public Event href(@Nullable URI href) {
    this.href = href;
    return this;
  }

  /**
   * Hyperlink reference
   * @return href
   */
  @Valid 
  @Schema(name = "href", description = "Hyperlink reference", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("href")
  public @Nullable URI getHref() {
    return href;
  }

  @JsonProperty("href")
  public void setHref(@Nullable URI href) {
    this.href = href;
  }

  public Event correlationId(@Nullable String correlationId) {
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

  public Event description(@Nullable String description) {
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

  public Event domain(@Nullable String domain) {
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

  public Event eventId(@Nullable String eventId) {
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

  public Event eventTime(@Nullable OffsetDateTime eventTime) {
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

  public Event eventType(@Nullable String eventType) {
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

  public Event priority(@Nullable String priority) {
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

  public Event timeOccurred(@Nullable OffsetDateTime timeOccurred) {
    this.timeOccurred = timeOccurred;
    return this;
  }

  /**
   * The time the event occurred.
   * @return timeOccurred
   */
  @Valid 
  @Schema(name = "timeOccurred", description = "The time the event occurred.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeOccurred")
  public @Nullable OffsetDateTime getTimeOccurred() {
    return timeOccurred;
  }

  @JsonProperty("timeOccurred")
  public void setTimeOccurred(@Nullable OffsetDateTime timeOccurred) {
    this.timeOccurred = timeOccurred;
  }

  public Event title(@Nullable String title) {
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

  public Event analyticCharacteristic(List<@Valid Characteristic> analyticCharacteristic) {
    this.analyticCharacteristic = analyticCharacteristic;
    return this;
  }

  public Event addAnalyticCharacteristicItem(Characteristic analyticCharacteristicItem) {
    if (this.analyticCharacteristic == null) {
      this.analyticCharacteristic = new ArrayList<>();
    }
    this.analyticCharacteristic.add(analyticCharacteristicItem);
    return this;
  }

  /**
   * Get analyticCharacteristic
   * @return analyticCharacteristic
   */
  @Valid 
  @Schema(name = "analyticCharacteristic", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("analyticCharacteristic")
  public List<@Valid Characteristic> getAnalyticCharacteristic() {
    return analyticCharacteristic;
  }

  @JsonProperty("analyticCharacteristic")
  public void setAnalyticCharacteristic(List<@Valid Characteristic> analyticCharacteristic) {
    this.analyticCharacteristic = analyticCharacteristic;
  }

  public Event event(@Nullable Object event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
   */
  
  @Schema(name = "event", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event")
  public @Nullable Object getEvent() {
    return event;
  }

  @JsonProperty("event")
  public void setEvent(@Nullable Object event) {
    this.event = event;
  }

  public Event relatedParty(List<@Valid RelatedParty> relatedParty) {
    this.relatedParty = relatedParty;
    return this;
  }

  public Event addRelatedPartyItem(RelatedParty relatedPartyItem) {
    if (this.relatedParty == null) {
      this.relatedParty = new ArrayList<>();
    }
    this.relatedParty.add(relatedPartyItem);
    return this;
  }

  /**
   * Get relatedParty
   * @return relatedParty
   */
  @Valid 
  @Schema(name = "relatedParty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relatedParty")
  public List<@Valid RelatedParty> getRelatedParty() {
    return relatedParty;
  }

  @JsonProperty("relatedParty")
  public void setRelatedParty(List<@Valid RelatedParty> relatedParty) {
    this.relatedParty = relatedParty;
  }

  public Event reportingSystem(@Nullable EntityRef reportingSystem) {
    this.reportingSystem = reportingSystem;
    return this;
  }

  /**
   * Get reportingSystem
   * @return reportingSystem
   */
  @Valid 
  @Schema(name = "reportingSystem", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reportingSystem")
  public @Nullable EntityRef getReportingSystem() {
    return reportingSystem;
  }

  @JsonProperty("reportingSystem")
  public void setReportingSystem(@Nullable EntityRef reportingSystem) {
    this.reportingSystem = reportingSystem;
  }

  public Event source(@Nullable EntityRef source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  @Valid 
  @Schema(name = "source", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source")
  public @Nullable EntityRef getSource() {
    return source;
  }

  @JsonProperty("source")
  public void setSource(@Nullable EntityRef source) {
    this.source = source;
  }

  public Event atBaseType(@Nullable String atBaseType) {
    this.atBaseType = atBaseType;
    return this;
  }

  /**
   * When sub-classing, this defines the super-class
   * @return atBaseType
   */
  
  @Schema(name = "@baseType", description = "When sub-classing, this defines the super-class", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("@baseType")
  public @Nullable String getAtBaseType() {
    return atBaseType;
  }

  @JsonProperty("@baseType")
  public void setAtBaseType(@Nullable String atBaseType) {
    this.atBaseType = atBaseType;
  }

  public Event atSchemaLocation(@Nullable URI atSchemaLocation) {
    this.atSchemaLocation = atSchemaLocation;
    return this;
  }

  /**
   * A URI to a JSON-Schema file that defines additional attributes and relationships
   * @return atSchemaLocation
   */
  @Valid 
  @Schema(name = "@schemaLocation", description = "A URI to a JSON-Schema file that defines additional attributes and relationships", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("@schemaLocation")
  public @Nullable URI getAtSchemaLocation() {
    return atSchemaLocation;
  }

  @JsonProperty("@schemaLocation")
  public void setAtSchemaLocation(@Nullable URI atSchemaLocation) {
    this.atSchemaLocation = atSchemaLocation;
  }

  public Event atType(@Nullable String atType) {
    this.atType = atType;
    return this;
  }

  /**
   * When sub-classing, this defines the sub-class Extensible name
   * @return atType
   */
  
  @Schema(name = "@type", description = "When sub-classing, this defines the sub-class Extensible name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("@type")
  public @Nullable String getAtType() {
    return atType;
  }

  @JsonProperty("@type")
  public void setAtType(@Nullable String atType) {
    this.atType = atType;
  }

  @JsonIgnore
  public String getTopicId() {
    return topicId;
  }

  public void setTopicId(String topicId) {
    this.topicId = topicId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Event event = (Event) o;
    return Objects.equals(this.id, event.id) &&
        Objects.equals(this.href, event.href) &&
        Objects.equals(this.correlationId, event.correlationId) &&
        Objects.equals(this.description, event.description) &&
        Objects.equals(this.domain, event.domain) &&
        Objects.equals(this.eventId, event.eventId) &&
        Objects.equals(this.eventTime, event.eventTime) &&
        Objects.equals(this.eventType, event.eventType) &&
        Objects.equals(this.priority, event.priority) &&
        Objects.equals(this.timeOccurred, event.timeOccurred) &&
        Objects.equals(this.title, event.title) &&
        Objects.equals(this.analyticCharacteristic, event.analyticCharacteristic) &&
        Objects.equals(this.event, event.event) &&
        Objects.equals(this.relatedParty, event.relatedParty) &&
        Objects.equals(this.reportingSystem, event.reportingSystem) &&
        Objects.equals(this.source, event.source) &&
        Objects.equals(this.atBaseType, event.atBaseType) &&
        Objects.equals(this.atSchemaLocation, event.atSchemaLocation) &&
        Objects.equals(this.atType, event.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, correlationId, description, domain, eventId, eventTime, eventType, priority, timeOccurred, title, analyticCharacteristic, event, relatedParty, reportingSystem, source, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Event {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    correlationId: ").append(toIndentedString(correlationId)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    eventTime: ").append(toIndentedString(eventTime)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    timeOccurred: ").append(toIndentedString(timeOccurred)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    analyticCharacteristic: ").append(toIndentedString(analyticCharacteristic)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    relatedParty: ").append(toIndentedString(relatedParty)).append("\n");
    sb.append("    reportingSystem: ").append(toIndentedString(reportingSystem)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    atBaseType: ").append(toIndentedString(atBaseType)).append("\n");
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

