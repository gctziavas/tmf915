package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.etsi.osl.controllers.tmf915.mappers.converters.AffectedServiceListConverter;
import org.etsi.osl.controllers.tmf915.mappers.converters.AlarmRefListConverter;
import org.etsi.osl.controllers.tmf915.mappers.converters.AlarmedObjectJsonConverter;
import org.etsi.osl.controllers.tmf915.mappers.converters.CommentListConverter;
import org.etsi.osl.controllers.tmf915.mappers.converters.CrossedThresholdInformationJsonConverter;
import org.etsi.osl.controllers.tmf915.mappers.converters.RelatedPlaceRefOrValueListConverter;
import org.etsi.osl.controllers.tmf915.mappers.converters.UriToStringConverter;
import org.jspecify.annotations.Nullable;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;

/**
 * This resource represents an alarm supporting the information model defined in ITU-T X.733.
 */

@Entity
@Table(name = "aim915_alarm")
@Schema(name = "Alarm", description = "This resource represents an alarm supporting the information model defined in ITU-T X.733.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class Alarm {

  @Id
  private @Nullable String id;

  @Convert(converter = UriToStringConverter.class)
  private @Nullable URI href;

  private @Nullable String ackState;

  private @Nullable String ackSystemId;

  private @Nullable String ackUserId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime alarmChangedTime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime alarmClearedTime;

  private @Nullable String alarmDetails;

  private @Nullable Boolean alarmEscalation;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime alarmRaisedTime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime alarmReportingTime;

  private @Nullable String alarmedObjectType;

  private @Nullable String clearSystemId;

  private @Nullable String clearUserId;

  private @Nullable String externalAlarmId;

  private @Nullable Boolean isRootCause;

  private @Nullable String plannedOutageIndicator;

  private @Nullable String probableCause;

  private @Nullable String proposedRepairedActions;

  private @Nullable String reportingSystemId;

  private @Nullable Boolean serviceAffecting;

  private @Nullable String sourceSystemId;

  private @Nullable String specificProblem;

  private @Nullable String state;

  @Valid
  @Convert(converter = AffectedServiceListConverter.class)
  @Column(columnDefinition = "TEXT")
  private List<@Valid AffectedService> affectedService = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  private @Nullable AlarmType alarmType;

  @Convert(converter = AlarmedObjectJsonConverter.class)
  @Column(columnDefinition = "TEXT")
  private @Nullable AlarmedObject alarmedObject;

  @Valid
  @Convert(converter = CommentListConverter.class)
  @Column(columnDefinition = "TEXT")
  private List<@Valid Comment> comment = new ArrayList<>();

  @Valid
  @Convert(converter = AlarmRefListConverter.class)
  @Column(columnDefinition = "TEXT")
  private List<@Valid AlarmRef> correlatedAlarm = new ArrayList<>();

  @Convert(converter = CrossedThresholdInformationJsonConverter.class)
  @Column(columnDefinition = "TEXT")
  private @Nullable CrossedThresholdInformation crossedThresholdInformation;

  @Valid
  @Convert(converter = AlarmRefListConverter.class)
  @Column(name = "parent_alarm", columnDefinition = "TEXT")
  private List<@Valid AlarmRef> parentAlarm = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  private @Nullable PerceivedSeverity perceivedSeverity;

  @Valid
  @Convert(converter = RelatedPlaceRefOrValueListConverter.class)
  @Column(columnDefinition = "TEXT")
  private List<@Valid RelatedPlaceRefOrValue> place = new ArrayList<>();

  private @Nullable String atBaseType;

  @Convert(converter = UriToStringConverter.class)
  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public Alarm id(@Nullable String id) {
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

  public Alarm href(@Nullable URI href) {
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

  public Alarm ackState(@Nullable String ackState) {
    this.ackState = ackState;
    return this;
  }

  /**
   * Provides the Acknowledgement State of the alarm (unacknowledged | acknowledged).
   * @return ackState
   */
  
  @Schema(name = "ackState", description = "Provides the Acknowledgement State of the alarm (unacknowledged | acknowledged).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ackState")
  public @Nullable String getAckState() {
    return ackState;
  }

  @JsonProperty("ackState")
  public void setAckState(@Nullable String ackState) {
    this.ackState = ackState;
  }

  public Alarm ackSystemId(@Nullable String ackSystemId) {
    this.ackSystemId = ackSystemId;
    return this;
  }

  /**
   * Provides the name of the system that last changed the ackState of an alarm, i.e. acknowledged or unacknowledged the alarm.
   * @return ackSystemId
   */
  
  @Schema(name = "ackSystemId", description = "Provides the name of the system that last changed the ackState of an alarm, i.e. acknowledged or unacknowledged the alarm.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ackSystemId")
  public @Nullable String getAckSystemId() {
    return ackSystemId;
  }

  @JsonProperty("ackSystemId")
  public void setAckSystemId(@Nullable String ackSystemId) {
    this.ackSystemId = ackSystemId;
  }

  public Alarm ackUserId(@Nullable String ackUserId) {
    this.ackUserId = ackUserId;
    return this;
  }

  /**
   * Provides the id of the user who has last changed the ack state of the alarm, i.e. acknowledged or unacknowledged the alarm.
   * @return ackUserId
   */
  
  @Schema(name = "ackUserId", description = "Provides the id of the user who has last changed the ack state of the alarm, i.e. acknowledged or unacknowledged the alarm.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ackUserId")
  public @Nullable String getAckUserId() {
    return ackUserId;
  }

  @JsonProperty("ackUserId")
  public void setAckUserId(@Nullable String ackUserId) {
    this.ackUserId = ackUserId;
  }

  public Alarm alarmChangedTime(@Nullable OffsetDateTime alarmChangedTime) {
    this.alarmChangedTime = alarmChangedTime;
    return this;
  }

  /**
   * Indicates the last date and time when the alarm is changed on the alarm-owning system. Any change to the alarm whether coming from the alarmed resource, or triggered by a change from the client is changing this time.
   * @return alarmChangedTime
   */
  @Valid 
  @Schema(name = "alarmChangedTime", description = "Indicates the last date and time when the alarm is changed on the alarm-owning system. Any change to the alarm whether coming from the alarmed resource, or triggered by a change from the client is changing this time.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alarmChangedTime")
  public @Nullable OffsetDateTime getAlarmChangedTime() {
    return alarmChangedTime;
  }

  @JsonProperty("alarmChangedTime")
  public void setAlarmChangedTime(@Nullable OffsetDateTime alarmChangedTime) {
    this.alarmChangedTime = alarmChangedTime;
  }

  public Alarm alarmClearedTime(@Nullable OffsetDateTime alarmClearedTime) {
    this.alarmClearedTime = alarmClearedTime;
    return this;
  }

  /**
   * Indicates the time (as a date + time) at which the alarm is cleared at the source. 
   * @return alarmClearedTime
   */
  @Valid 
  @Schema(name = "alarmClearedTime", description = "Indicates the time (as a date + time) at which the alarm is cleared at the source. ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alarmClearedTime")
  public @Nullable OffsetDateTime getAlarmClearedTime() {
    return alarmClearedTime;
  }

  @JsonProperty("alarmClearedTime")
  public void setAlarmClearedTime(@Nullable OffsetDateTime alarmClearedTime) {
    this.alarmClearedTime = alarmClearedTime;
  }

  public Alarm alarmDetails(@Nullable String alarmDetails) {
    this.alarmDetails = alarmDetails;
    return this;
  }

  /**
   * Contains further information on the alarm.
   * @return alarmDetails
   */
  
  @Schema(name = "alarmDetails", description = "Contains further information on the alarm.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alarmDetails")
  public @Nullable String getAlarmDetails() {
    return alarmDetails;
  }

  @JsonProperty("alarmDetails")
  public void setAlarmDetails(@Nullable String alarmDetails) {
    this.alarmDetails = alarmDetails;
  }

  public Alarm alarmEscalation(@Nullable Boolean alarmEscalation) {
    this.alarmEscalation = alarmEscalation;
    return this;
  }

  /**
   * Indicates if this alarm has been escalated or not. 
   * @return alarmEscalation
   */
  
  @Schema(name = "alarmEscalation", description = "Indicates if this alarm has been escalated or not. ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alarmEscalation")
  public @Nullable Boolean getAlarmEscalation() {
    return alarmEscalation;
  }

  @JsonProperty("alarmEscalation")
  public void setAlarmEscalation(@Nullable Boolean alarmEscalation) {
    this.alarmEscalation = alarmEscalation;
  }

  public Alarm alarmRaisedTime(@Nullable OffsetDateTime alarmRaisedTime) {
    this.alarmRaisedTime = alarmRaisedTime;
    return this;
  }

  /**
   * Indicates the time (as a date + time) at which the alarm occurred at its source.
   * @return alarmRaisedTime
   */
  @Valid 
  @Schema(name = "alarmRaisedTime", description = "Indicates the time (as a date + time) at which the alarm occurred at its source.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alarmRaisedTime")
  public @Nullable OffsetDateTime getAlarmRaisedTime() {
    return alarmRaisedTime;
  }

  @JsonProperty("alarmRaisedTime")
  public void setAlarmRaisedTime(@Nullable OffsetDateTime alarmRaisedTime) {
    this.alarmRaisedTime = alarmRaisedTime;
  }

  public Alarm alarmReportingTime(@Nullable OffsetDateTime alarmReportingTime) {
    this.alarmReportingTime = alarmReportingTime;
    return this;
  }

  /**
   * Indicates the time (as a date + time) at which the alarm was reported by the owning OSS. It might be different from the alarmRaisedTime. For instance, if the alarm list is maintained by an EMS, the alarmRaisedtime would be the time the alarm   was detected by the NE, while the alarmReportingTime would be the time this alarm was stored in the alarm list of the EMS.
   * @return alarmReportingTime
   */
  @Valid 
  @Schema(name = "alarmReportingTime", description = "Indicates the time (as a date + time) at which the alarm was reported by the owning OSS. It might be different from the alarmRaisedTime. For instance, if the alarm list is maintained by an EMS, the alarmRaisedtime would be the time the alarm   was detected by the NE, while the alarmReportingTime would be the time this alarm was stored in the alarm list of the EMS.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alarmReportingTime")
  public @Nullable OffsetDateTime getAlarmReportingTime() {
    return alarmReportingTime;
  }

  @JsonProperty("alarmReportingTime")
  public void setAlarmReportingTime(@Nullable OffsetDateTime alarmReportingTime) {
    this.alarmReportingTime = alarmReportingTime;
  }

  public Alarm alarmedObjectType(@Nullable String alarmedObjectType) {
    this.alarmedObjectType = alarmedObjectType;
    return this;
  }

  /**
   * The type (class) of the managed object associated with the event.
   * @return alarmedObjectType
   */
  
  @Schema(name = "alarmedObjectType", description = "The type (class) of the managed object associated with the event.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alarmedObjectType")
  public @Nullable String getAlarmedObjectType() {
    return alarmedObjectType;
  }

  @JsonProperty("alarmedObjectType")
  public void setAlarmedObjectType(@Nullable String alarmedObjectType) {
    this.alarmedObjectType = alarmedObjectType;
  }

  public Alarm clearSystemId(@Nullable String clearSystemId) {
    this.clearSystemId = clearSystemId;
    return this;
  }

  /**
   * Provides the id of the system where the user who invoked the alarmCleared operation is located. 
   * @return clearSystemId
   */
  
  @Schema(name = "clearSystemId", description = "Provides the id of the system where the user who invoked the alarmCleared operation is located. ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clearSystemId")
  public @Nullable String getClearSystemId() {
    return clearSystemId;
  }

  @JsonProperty("clearSystemId")
  public void setClearSystemId(@Nullable String clearSystemId) {
    this.clearSystemId = clearSystemId;
  }

  public Alarm clearUserId(@Nullable String clearUserId) {
    this.clearUserId = clearUserId;
    return this;
  }

  /**
   * Provides the id of the user who invoked the alarmCleared operation
   * @return clearUserId
   */
  
  @Schema(name = "clearUserId", description = "Provides the id of the user who invoked the alarmCleared operation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clearUserId")
  public @Nullable String getClearUserId() {
    return clearUserId;
  }

  @JsonProperty("clearUserId")
  public void setClearUserId(@Nullable String clearUserId) {
    this.clearUserId = clearUserId;
  }

  public Alarm externalAlarmId(@Nullable String externalAlarmId) {
    this.externalAlarmId = externalAlarmId;
    return this;
  }

  /**
   * An identifier of the alarm in the source system.
   * @return externalAlarmId
   */
  
  @Schema(name = "externalAlarmId", description = "An identifier of the alarm in the source system.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("externalAlarmId")
  public @Nullable String getExternalAlarmId() {
    return externalAlarmId;
  }

  @JsonProperty("externalAlarmId")
  public void setExternalAlarmId(@Nullable String externalAlarmId) {
    this.externalAlarmId = externalAlarmId;
  }

  public Alarm isRootCause(@Nullable Boolean isRootCause) {
    this.isRootCause = isRootCause;
    return this;
  }

  /**
   * Indicates whether the alarm is a root cause alarm.. 
   * @return isRootCause
   */
  
  @Schema(name = "isRootCause", description = "Indicates whether the alarm is a root cause alarm.. ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isRootCause")
  public @Nullable Boolean getIsRootCause() {
    return isRootCause;
  }

  @JsonProperty("isRootCause")
  public void setIsRootCause(@Nullable Boolean isRootCause) {
    this.isRootCause = isRootCause;
  }

  public Alarm plannedOutageIndicator(@Nullable String plannedOutageIndicator) {
    this.plannedOutageIndicator = plannedOutageIndicator;
    return this;
  }

  /**
   * Indicates that the Managed Object (related to this alarm) is in planned outage (in planned maintenance, or out-of-service). 
   * @return plannedOutageIndicator
   */
  
  @Schema(name = "plannedOutageIndicator", description = "Indicates that the Managed Object (related to this alarm) is in planned outage (in planned maintenance, or out-of-service). ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("plannedOutageIndicator")
  public @Nullable String getPlannedOutageIndicator() {
    return plannedOutageIndicator;
  }

  @JsonProperty("plannedOutageIndicator")
  public void setPlannedOutageIndicator(@Nullable String plannedOutageIndicator) {
    this.plannedOutageIndicator = plannedOutageIndicator;
  }

  public Alarm probableCause(@Nullable String probableCause) {
    this.probableCause = probableCause;
    return this;
  }

  /**
   * Provides the probable cause of the alarm. The values are consistent with ITU-T Recommendation X.733 or 3GPP TS 32.111-2 Annex B.
   * @return probableCause
   */
  
  @Schema(name = "probableCause", description = "Provides the probable cause of the alarm. The values are consistent with ITU-T Recommendation X.733 or 3GPP TS 32.111-2 Annex B.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("probableCause")
  public @Nullable String getProbableCause() {
    return probableCause;
  }

  @JsonProperty("probableCause")
  public void setProbableCause(@Nullable String probableCause) {
    this.probableCause = probableCause;
  }

  public Alarm proposedRepairedActions(@Nullable String proposedRepairedActions) {
    this.proposedRepairedActions = proposedRepairedActions;
    return this;
  }

  /**
   * Indicates proposed repair actions, if known to the system emitting the alarm.
   * @return proposedRepairedActions
   */
  
  @Schema(name = "proposedRepairedActions", description = "Indicates proposed repair actions, if known to the system emitting the alarm.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("proposedRepairedActions")
  public @Nullable String getProposedRepairedActions() {
    return proposedRepairedActions;
  }

  @JsonProperty("proposedRepairedActions")
  public void setProposedRepairedActions(@Nullable String proposedRepairedActions) {
    this.proposedRepairedActions = proposedRepairedActions;
  }

  public Alarm reportingSystemId(@Nullable String reportingSystemId) {
    this.reportingSystemId = reportingSystemId;
    return this;
  }

  /**
   * Reporting system identity.
   * @return reportingSystemId
   */
  
  @Schema(name = "reportingSystemId", description = "Reporting system identity.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reportingSystemId")
  public @Nullable String getReportingSystemId() {
    return reportingSystemId;
  }

  @JsonProperty("reportingSystemId")
  public void setReportingSystemId(@Nullable String reportingSystemId) {
    this.reportingSystemId = reportingSystemId;
  }

  public Alarm serviceAffecting(@Nullable Boolean serviceAffecting) {
    this.serviceAffecting = serviceAffecting;
    return this;
  }

  /**
   * Indicates whether the alarm affects service or not.
   * @return serviceAffecting
   */
  
  @Schema(name = "serviceAffecting", description = "Indicates whether the alarm affects service or not.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("serviceAffecting")
  public @Nullable Boolean getServiceAffecting() {
    return serviceAffecting;
  }

  @JsonProperty("serviceAffecting")
  public void setServiceAffecting(@Nullable Boolean serviceAffecting) {
    this.serviceAffecting = serviceAffecting;
  }

  public Alarm sourceSystemId(@Nullable String sourceSystemId) {
    this.sourceSystemId = sourceSystemId;
    return this;
  }

  /**
   * Source system identity.
   * @return sourceSystemId
   */
  
  @Schema(name = "sourceSystemId", description = "Source system identity.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sourceSystemId")
  public @Nullable String getSourceSystemId() {
    return sourceSystemId;
  }

  @JsonProperty("sourceSystemId")
  public void setSourceSystemId(@Nullable String sourceSystemId) {
    this.sourceSystemId = sourceSystemId;
  }

  public Alarm specificProblem(@Nullable String specificProblem) {
    this.specificProblem = specificProblem;
    return this;
  }

  /**
   * Provides more specific information about the alarm.
   * @return specificProblem
   */
  
  @Schema(name = "specificProblem", description = "Provides more specific information about the alarm.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("specificProblem")
  public @Nullable String getSpecificProblem() {
    return specificProblem;
  }

  @JsonProperty("specificProblem")
  public void setSpecificProblem(@Nullable String specificProblem) {
    this.specificProblem = specificProblem;
  }

  public Alarm state(@Nullable String state) {
    this.state = state;
    return this;
  }

  /**
   * Defines the alarm state during its life cycle (raised | updated | cleared).
   * @return state
   */
  
  @Schema(name = "state", description = "Defines the alarm state during its life cycle (raised | updated | cleared).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  public @Nullable String getState() {
    return state;
  }

  @JsonProperty("state")
  public void setState(@Nullable String state) {
    this.state = state;
  }

  public Alarm affectedService(List<@Valid AffectedService> affectedService) {
    this.affectedService = affectedService;
    return this;
  }

  public Alarm addAffectedServiceItem(AffectedService affectedServiceItem) {
    if (this.affectedService == null) {
      this.affectedService = new ArrayList<>();
    }
    this.affectedService.add(affectedServiceItem);
    return this;
  }

  /**
   * Get affectedService
   * @return affectedService
   */
  @Valid 
  @Schema(name = "affectedService", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affectedService")
  public List<@Valid AffectedService> getAffectedService() {
    return affectedService;
  }

  @JsonProperty("affectedService")
  public void setAffectedService(List<@Valid AffectedService> affectedService) {
    this.affectedService = affectedService;
  }

  public Alarm alarmType(@Nullable AlarmType alarmType) {
    this.alarmType = alarmType;
    return this;
  }

  /**
   * Get alarmType
   * @return alarmType
   */
  @Valid 
  @Schema(name = "alarmType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alarmType")
  public @Nullable AlarmType getAlarmType() {
    return alarmType;
  }

  @JsonProperty("alarmType")
  public void setAlarmType(@Nullable AlarmType alarmType) {
    this.alarmType = alarmType;
  }

  public Alarm alarmedObject(@Nullable AlarmedObject alarmedObject) {
    this.alarmedObject = alarmedObject;
    return this;
  }

  /**
   * Get alarmedObject
   * @return alarmedObject
   */
  @Valid 
  @Schema(name = "alarmedObject", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alarmedObject")
  public @Nullable AlarmedObject getAlarmedObject() {
    return alarmedObject;
  }

  @JsonProperty("alarmedObject")
  public void setAlarmedObject(@Nullable AlarmedObject alarmedObject) {
    this.alarmedObject = alarmedObject;
  }

  public Alarm comment(List<@Valid Comment> comment) {
    this.comment = comment;
    return this;
  }

  public Alarm addCommentItem(Comment commentItem) {
    if (this.comment == null) {
      this.comment = new ArrayList<>();
    }
    this.comment.add(commentItem);
    return this;
  }

  /**
   * Get comment
   * @return comment
   */
  @Valid 
  @Schema(name = "comment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("comment")
  public List<@Valid Comment> getComment() {
    return comment;
  }

  @JsonProperty("comment")
  public void setComment(List<@Valid Comment> comment) {
    this.comment = comment;
  }

  public Alarm correlatedAlarm(List<@Valid AlarmRef> correlatedAlarm) {
    this.correlatedAlarm = correlatedAlarm;
    return this;
  }

  public Alarm addCorrelatedAlarmItem(AlarmRef correlatedAlarmItem) {
    if (this.correlatedAlarm == null) {
      this.correlatedAlarm = new ArrayList<>();
    }
    this.correlatedAlarm.add(correlatedAlarmItem);
    return this;
  }

  /**
   * Get correlatedAlarm
   * @return correlatedAlarm
   */
  @Valid 
  @Schema(name = "correlatedAlarm", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("correlatedAlarm")
  public List<@Valid AlarmRef> getCorrelatedAlarm() {
    return correlatedAlarm;
  }

  @JsonProperty("correlatedAlarm")
  public void setCorrelatedAlarm(List<@Valid AlarmRef> correlatedAlarm) {
    this.correlatedAlarm = correlatedAlarm;
  }

  public Alarm crossedThresholdInformation(@Nullable CrossedThresholdInformation crossedThresholdInformation) {
    this.crossedThresholdInformation = crossedThresholdInformation;
    return this;
  }

  /**
   * Get crossedThresholdInformation
   * @return crossedThresholdInformation
   */
  @Valid 
  @Schema(name = "crossedThresholdInformation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crossedThresholdInformation")
  public @Nullable CrossedThresholdInformation getCrossedThresholdInformation() {
    return crossedThresholdInformation;
  }

  @JsonProperty("crossedThresholdInformation")
  public void setCrossedThresholdInformation(@Nullable CrossedThresholdInformation crossedThresholdInformation) {
    this.crossedThresholdInformation = crossedThresholdInformation;
  }

  public Alarm parentAlarm(List<@Valid AlarmRef> parentAlarm) {
    this.parentAlarm = parentAlarm;
    return this;
  }

  public Alarm addParentAlarmItem(AlarmRef parentAlarmItem) {
    if (this.parentAlarm == null) {
      this.parentAlarm = new ArrayList<>();
    }
    this.parentAlarm.add(parentAlarmItem);
    return this;
  }

  /**
   * Get parentAlarm
   * @return parentAlarm
   */
  @Valid 
  @Schema(name = "parentAlarm", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("parentAlarm")
  public List<@Valid AlarmRef> getParentAlarm() {
    return parentAlarm;
  }

  @JsonProperty("parentAlarm")
  public void setParentAlarm(List<@Valid AlarmRef> parentAlarm) {
    this.parentAlarm = parentAlarm;
  }

  public Alarm perceivedSeverity(@Nullable PerceivedSeverity perceivedSeverity) {
    this.perceivedSeverity = perceivedSeverity;
    return this;
  }

  /**
   * Get perceivedSeverity
   * @return perceivedSeverity
   */
  @Valid 
  @Schema(name = "perceivedSeverity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("perceivedSeverity")
  public @Nullable PerceivedSeverity getPerceivedSeverity() {
    return perceivedSeverity;
  }

  @JsonProperty("perceivedSeverity")
  public void setPerceivedSeverity(@Nullable PerceivedSeverity perceivedSeverity) {
    this.perceivedSeverity = perceivedSeverity;
  }

  public Alarm place(List<@Valid RelatedPlaceRefOrValue> place) {
    this.place = place;
    return this;
  }

  public Alarm addPlaceItem(RelatedPlaceRefOrValue placeItem) {
    if (this.place == null) {
      this.place = new ArrayList<>();
    }
    this.place.add(placeItem);
    return this;
  }

  /**
   * Get place
   * @return place
   */
  @Valid 
  @Schema(name = "place", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("place")
  public List<@Valid RelatedPlaceRefOrValue> getPlace() {
    return place;
  }

  @JsonProperty("place")
  public void setPlace(List<@Valid RelatedPlaceRefOrValue> place) {
    this.place = place;
  }

  public Alarm atBaseType(@Nullable String atBaseType) {
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

  public Alarm atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public Alarm atType(@Nullable String atType) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Alarm alarm = (Alarm) o;
    return Objects.equals(this.id, alarm.id) &&
        Objects.equals(this.href, alarm.href) &&
        Objects.equals(this.ackState, alarm.ackState) &&
        Objects.equals(this.ackSystemId, alarm.ackSystemId) &&
        Objects.equals(this.ackUserId, alarm.ackUserId) &&
        Objects.equals(this.alarmChangedTime, alarm.alarmChangedTime) &&
        Objects.equals(this.alarmClearedTime, alarm.alarmClearedTime) &&
        Objects.equals(this.alarmDetails, alarm.alarmDetails) &&
        Objects.equals(this.alarmEscalation, alarm.alarmEscalation) &&
        Objects.equals(this.alarmRaisedTime, alarm.alarmRaisedTime) &&
        Objects.equals(this.alarmReportingTime, alarm.alarmReportingTime) &&
        Objects.equals(this.alarmedObjectType, alarm.alarmedObjectType) &&
        Objects.equals(this.clearSystemId, alarm.clearSystemId) &&
        Objects.equals(this.clearUserId, alarm.clearUserId) &&
        Objects.equals(this.externalAlarmId, alarm.externalAlarmId) &&
        Objects.equals(this.isRootCause, alarm.isRootCause) &&
        Objects.equals(this.plannedOutageIndicator, alarm.plannedOutageIndicator) &&
        Objects.equals(this.probableCause, alarm.probableCause) &&
        Objects.equals(this.proposedRepairedActions, alarm.proposedRepairedActions) &&
        Objects.equals(this.reportingSystemId, alarm.reportingSystemId) &&
        Objects.equals(this.serviceAffecting, alarm.serviceAffecting) &&
        Objects.equals(this.sourceSystemId, alarm.sourceSystemId) &&
        Objects.equals(this.specificProblem, alarm.specificProblem) &&
        Objects.equals(this.state, alarm.state) &&
        Objects.equals(this.affectedService, alarm.affectedService) &&
        Objects.equals(this.alarmType, alarm.alarmType) &&
        Objects.equals(this.alarmedObject, alarm.alarmedObject) &&
        Objects.equals(this.comment, alarm.comment) &&
        Objects.equals(this.correlatedAlarm, alarm.correlatedAlarm) &&
        Objects.equals(this.crossedThresholdInformation, alarm.crossedThresholdInformation) &&
        Objects.equals(this.parentAlarm, alarm.parentAlarm) &&
        Objects.equals(this.perceivedSeverity, alarm.perceivedSeverity) &&
        Objects.equals(this.place, alarm.place) &&
        Objects.equals(this.atBaseType, alarm.atBaseType) &&
        Objects.equals(this.atSchemaLocation, alarm.atSchemaLocation) &&
        Objects.equals(this.atType, alarm.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, ackState, ackSystemId, ackUserId, alarmChangedTime, alarmClearedTime, alarmDetails, alarmEscalation, alarmRaisedTime, alarmReportingTime, alarmedObjectType, clearSystemId, clearUserId, externalAlarmId, isRootCause, plannedOutageIndicator, probableCause, proposedRepairedActions, reportingSystemId, serviceAffecting, sourceSystemId, specificProblem, state, affectedService, alarmType, alarmedObject, comment, correlatedAlarm, crossedThresholdInformation, parentAlarm, perceivedSeverity, place, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Alarm {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    ackState: ").append(toIndentedString(ackState)).append("\n");
    sb.append("    ackSystemId: ").append(toIndentedString(ackSystemId)).append("\n");
    sb.append("    ackUserId: ").append(toIndentedString(ackUserId)).append("\n");
    sb.append("    alarmChangedTime: ").append(toIndentedString(alarmChangedTime)).append("\n");
    sb.append("    alarmClearedTime: ").append(toIndentedString(alarmClearedTime)).append("\n");
    sb.append("    alarmDetails: ").append(toIndentedString(alarmDetails)).append("\n");
    sb.append("    alarmEscalation: ").append(toIndentedString(alarmEscalation)).append("\n");
    sb.append("    alarmRaisedTime: ").append(toIndentedString(alarmRaisedTime)).append("\n");
    sb.append("    alarmReportingTime: ").append(toIndentedString(alarmReportingTime)).append("\n");
    sb.append("    alarmedObjectType: ").append(toIndentedString(alarmedObjectType)).append("\n");
    sb.append("    clearSystemId: ").append(toIndentedString(clearSystemId)).append("\n");
    sb.append("    clearUserId: ").append(toIndentedString(clearUserId)).append("\n");
    sb.append("    externalAlarmId: ").append(toIndentedString(externalAlarmId)).append("\n");
    sb.append("    isRootCause: ").append(toIndentedString(isRootCause)).append("\n");
    sb.append("    plannedOutageIndicator: ").append(toIndentedString(plannedOutageIndicator)).append("\n");
    sb.append("    probableCause: ").append(toIndentedString(probableCause)).append("\n");
    sb.append("    proposedRepairedActions: ").append(toIndentedString(proposedRepairedActions)).append("\n");
    sb.append("    reportingSystemId: ").append(toIndentedString(reportingSystemId)).append("\n");
    sb.append("    serviceAffecting: ").append(toIndentedString(serviceAffecting)).append("\n");
    sb.append("    sourceSystemId: ").append(toIndentedString(sourceSystemId)).append("\n");
    sb.append("    specificProblem: ").append(toIndentedString(specificProblem)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    affectedService: ").append(toIndentedString(affectedService)).append("\n");
    sb.append("    alarmType: ").append(toIndentedString(alarmType)).append("\n");
    sb.append("    alarmedObject: ").append(toIndentedString(alarmedObject)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    correlatedAlarm: ").append(toIndentedString(correlatedAlarm)).append("\n");
    sb.append("    crossedThresholdInformation: ").append(toIndentedString(crossedThresholdInformation)).append("\n");
    sb.append("    parentAlarm: ").append(toIndentedString(parentAlarm)).append("\n");
    sb.append("    perceivedSeverity: ").append(toIndentedString(perceivedSeverity)).append("\n");
    sb.append("    place: ").append(toIndentedString(place)).append("\n");
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

