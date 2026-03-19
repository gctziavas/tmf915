package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.etsi.osl.controllers.tmf915.model.AffectedService;
import org.etsi.osl.controllers.tmf915.model.AlarmRef;
import org.etsi.osl.controllers.tmf915.model.AlarmType;
import org.etsi.osl.controllers.tmf915.model.AlarmedObject;
import org.etsi.osl.controllers.tmf915.model.Comment;
import org.etsi.osl.controllers.tmf915.model.CrossedThresholdInformation;
import org.etsi.osl.controllers.tmf915.model.PerceivedSeverity;
import org.etsi.osl.controllers.tmf915.model.RelatedPlaceRefOrValue;
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
 * This resource represents an alarm supporting the information model defined in ITU-T X.733. Skipped properties: id,href
 */

@Schema(name = "Alarm_Create", description = "This resource represents an alarm supporting the information model defined in ITU-T X.733. Skipped properties: id,href")
@JsonTypeName("Alarm_Create")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class AlarmCreate {

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
  private OffsetDateTime alarmRaisedTime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime alarmReportingTime;

  private @Nullable String alarmedObjectType;

  private @Nullable String clearSystemId;

  private @Nullable String clearUserId;

  private @Nullable String externalAlarmId;

  private @Nullable Boolean isRootCause;

  private @Nullable String plannedOutageIndicator;

  private String probableCause;

  private @Nullable String proposedRepairedActions;

  private @Nullable String reportingSystemId;

  private @Nullable Boolean serviceAffecting;

  private String sourceSystemId;

  private @Nullable String specificProblem;

  private String state;

  @Valid
  private List<@Valid AffectedService> affectedService = new ArrayList<>();

  private AlarmType alarmType;

  private AlarmedObject alarmedObject;

  @Valid
  private List<@Valid Comment> comment = new ArrayList<>();

  @Valid
  private List<@Valid AlarmRef> correlatedAlarm = new ArrayList<>();

  private @Nullable CrossedThresholdInformation crossedThresholdInformation;

  @Valid
  private List<@Valid AlarmRef> parentAlarm = new ArrayList<>();

  private PerceivedSeverity perceivedSeverity;

  @Valid
  private List<@Valid RelatedPlaceRefOrValue> place = new ArrayList<>();

  private @Nullable String atBaseType;

  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public AlarmCreate() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AlarmCreate(OffsetDateTime alarmRaisedTime, String probableCause, String sourceSystemId, String state, AlarmType alarmType, AlarmedObject alarmedObject, PerceivedSeverity perceivedSeverity) {
    this.alarmRaisedTime = alarmRaisedTime;
    this.probableCause = probableCause;
    this.sourceSystemId = sourceSystemId;
    this.state = state;
    this.alarmType = alarmType;
    this.alarmedObject = alarmedObject;
    this.perceivedSeverity = perceivedSeverity;
  }

  public AlarmCreate ackState(@Nullable String ackState) {
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

  public AlarmCreate ackSystemId(@Nullable String ackSystemId) {
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

  public AlarmCreate ackUserId(@Nullable String ackUserId) {
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

  public AlarmCreate alarmChangedTime(@Nullable OffsetDateTime alarmChangedTime) {
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

  public AlarmCreate alarmClearedTime(@Nullable OffsetDateTime alarmClearedTime) {
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

  public AlarmCreate alarmDetails(@Nullable String alarmDetails) {
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

  public AlarmCreate alarmEscalation(@Nullable Boolean alarmEscalation) {
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

  public AlarmCreate alarmRaisedTime(OffsetDateTime alarmRaisedTime) {
    this.alarmRaisedTime = alarmRaisedTime;
    return this;
  }

  /**
   * Indicates the time (as a date + time) at which the alarm occurred at its source.
   * @return alarmRaisedTime
   */
  @NotNull @Valid 
  @Schema(name = "alarmRaisedTime", description = "Indicates the time (as a date + time) at which the alarm occurred at its source.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("alarmRaisedTime")
  public OffsetDateTime getAlarmRaisedTime() {
    return alarmRaisedTime;
  }

  @JsonProperty("alarmRaisedTime")
  public void setAlarmRaisedTime(OffsetDateTime alarmRaisedTime) {
    this.alarmRaisedTime = alarmRaisedTime;
  }

  public AlarmCreate alarmReportingTime(@Nullable OffsetDateTime alarmReportingTime) {
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

  public AlarmCreate alarmedObjectType(@Nullable String alarmedObjectType) {
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

  public AlarmCreate clearSystemId(@Nullable String clearSystemId) {
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

  public AlarmCreate clearUserId(@Nullable String clearUserId) {
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

  public AlarmCreate externalAlarmId(@Nullable String externalAlarmId) {
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

  public AlarmCreate isRootCause(@Nullable Boolean isRootCause) {
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

  public AlarmCreate plannedOutageIndicator(@Nullable String plannedOutageIndicator) {
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

  public AlarmCreate probableCause(String probableCause) {
    this.probableCause = probableCause;
    return this;
  }

  /**
   * Provides the probable cause of the alarm. The values are consistent with ITU-T Recommendation X.733 or 3GPP TS 32.111-2 Annex B.
   * @return probableCause
   */
  @NotNull 
  @Schema(name = "probableCause", description = "Provides the probable cause of the alarm. The values are consistent with ITU-T Recommendation X.733 or 3GPP TS 32.111-2 Annex B.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("probableCause")
  public String getProbableCause() {
    return probableCause;
  }

  @JsonProperty("probableCause")
  public void setProbableCause(String probableCause) {
    this.probableCause = probableCause;
  }

  public AlarmCreate proposedRepairedActions(@Nullable String proposedRepairedActions) {
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

  public AlarmCreate reportingSystemId(@Nullable String reportingSystemId) {
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

  public AlarmCreate serviceAffecting(@Nullable Boolean serviceAffecting) {
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

  public AlarmCreate sourceSystemId(String sourceSystemId) {
    this.sourceSystemId = sourceSystemId;
    return this;
  }

  /**
   * Source system identity.
   * @return sourceSystemId
   */
  @NotNull 
  @Schema(name = "sourceSystemId", description = "Source system identity.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sourceSystemId")
  public String getSourceSystemId() {
    return sourceSystemId;
  }

  @JsonProperty("sourceSystemId")
  public void setSourceSystemId(String sourceSystemId) {
    this.sourceSystemId = sourceSystemId;
  }

  public AlarmCreate specificProblem(@Nullable String specificProblem) {
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

  public AlarmCreate state(String state) {
    this.state = state;
    return this;
  }

  /**
   * Defines the alarm state during its life cycle (raised | updated | cleared).
   * @return state
   */
  @NotNull 
  @Schema(name = "state", description = "Defines the alarm state during its life cycle (raised | updated | cleared).", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("state")
  public String getState() {
    return state;
  }

  @JsonProperty("state")
  public void setState(String state) {
    this.state = state;
  }

  public AlarmCreate affectedService(List<@Valid AffectedService> affectedService) {
    this.affectedService = affectedService;
    return this;
  }

  public AlarmCreate addAffectedServiceItem(AffectedService affectedServiceItem) {
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

  public AlarmCreate alarmType(AlarmType alarmType) {
    this.alarmType = alarmType;
    return this;
  }

  /**
   * Get alarmType
   * @return alarmType
   */
  @NotNull @Valid 
  @Schema(name = "alarmType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("alarmType")
  public AlarmType getAlarmType() {
    return alarmType;
  }

  @JsonProperty("alarmType")
  public void setAlarmType(AlarmType alarmType) {
    this.alarmType = alarmType;
  }

  public AlarmCreate alarmedObject(AlarmedObject alarmedObject) {
    this.alarmedObject = alarmedObject;
    return this;
  }

  /**
   * Get alarmedObject
   * @return alarmedObject
   */
  @NotNull @Valid 
  @Schema(name = "alarmedObject", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("alarmedObject")
  public AlarmedObject getAlarmedObject() {
    return alarmedObject;
  }

  @JsonProperty("alarmedObject")
  public void setAlarmedObject(AlarmedObject alarmedObject) {
    this.alarmedObject = alarmedObject;
  }

  public AlarmCreate comment(List<@Valid Comment> comment) {
    this.comment = comment;
    return this;
  }

  public AlarmCreate addCommentItem(Comment commentItem) {
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

  public AlarmCreate correlatedAlarm(List<@Valid AlarmRef> correlatedAlarm) {
    this.correlatedAlarm = correlatedAlarm;
    return this;
  }

  public AlarmCreate addCorrelatedAlarmItem(AlarmRef correlatedAlarmItem) {
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

  public AlarmCreate crossedThresholdInformation(@Nullable CrossedThresholdInformation crossedThresholdInformation) {
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

  public AlarmCreate parentAlarm(List<@Valid AlarmRef> parentAlarm) {
    this.parentAlarm = parentAlarm;
    return this;
  }

  public AlarmCreate addParentAlarmItem(AlarmRef parentAlarmItem) {
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

  public AlarmCreate perceivedSeverity(PerceivedSeverity perceivedSeverity) {
    this.perceivedSeverity = perceivedSeverity;
    return this;
  }

  /**
   * Get perceivedSeverity
   * @return perceivedSeverity
   */
  @NotNull @Valid 
  @Schema(name = "perceivedSeverity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("perceivedSeverity")
  public PerceivedSeverity getPerceivedSeverity() {
    return perceivedSeverity;
  }

  @JsonProperty("perceivedSeverity")
  public void setPerceivedSeverity(PerceivedSeverity perceivedSeverity) {
    this.perceivedSeverity = perceivedSeverity;
  }

  public AlarmCreate place(List<@Valid RelatedPlaceRefOrValue> place) {
    this.place = place;
    return this;
  }

  public AlarmCreate addPlaceItem(RelatedPlaceRefOrValue placeItem) {
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

  public AlarmCreate atBaseType(@Nullable String atBaseType) {
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

  public AlarmCreate atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public AlarmCreate atType(@Nullable String atType) {
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
    AlarmCreate alarmCreate = (AlarmCreate) o;
    return Objects.equals(this.ackState, alarmCreate.ackState) &&
        Objects.equals(this.ackSystemId, alarmCreate.ackSystemId) &&
        Objects.equals(this.ackUserId, alarmCreate.ackUserId) &&
        Objects.equals(this.alarmChangedTime, alarmCreate.alarmChangedTime) &&
        Objects.equals(this.alarmClearedTime, alarmCreate.alarmClearedTime) &&
        Objects.equals(this.alarmDetails, alarmCreate.alarmDetails) &&
        Objects.equals(this.alarmEscalation, alarmCreate.alarmEscalation) &&
        Objects.equals(this.alarmRaisedTime, alarmCreate.alarmRaisedTime) &&
        Objects.equals(this.alarmReportingTime, alarmCreate.alarmReportingTime) &&
        Objects.equals(this.alarmedObjectType, alarmCreate.alarmedObjectType) &&
        Objects.equals(this.clearSystemId, alarmCreate.clearSystemId) &&
        Objects.equals(this.clearUserId, alarmCreate.clearUserId) &&
        Objects.equals(this.externalAlarmId, alarmCreate.externalAlarmId) &&
        Objects.equals(this.isRootCause, alarmCreate.isRootCause) &&
        Objects.equals(this.plannedOutageIndicator, alarmCreate.plannedOutageIndicator) &&
        Objects.equals(this.probableCause, alarmCreate.probableCause) &&
        Objects.equals(this.proposedRepairedActions, alarmCreate.proposedRepairedActions) &&
        Objects.equals(this.reportingSystemId, alarmCreate.reportingSystemId) &&
        Objects.equals(this.serviceAffecting, alarmCreate.serviceAffecting) &&
        Objects.equals(this.sourceSystemId, alarmCreate.sourceSystemId) &&
        Objects.equals(this.specificProblem, alarmCreate.specificProblem) &&
        Objects.equals(this.state, alarmCreate.state) &&
        Objects.equals(this.affectedService, alarmCreate.affectedService) &&
        Objects.equals(this.alarmType, alarmCreate.alarmType) &&
        Objects.equals(this.alarmedObject, alarmCreate.alarmedObject) &&
        Objects.equals(this.comment, alarmCreate.comment) &&
        Objects.equals(this.correlatedAlarm, alarmCreate.correlatedAlarm) &&
        Objects.equals(this.crossedThresholdInformation, alarmCreate.crossedThresholdInformation) &&
        Objects.equals(this.parentAlarm, alarmCreate.parentAlarm) &&
        Objects.equals(this.perceivedSeverity, alarmCreate.perceivedSeverity) &&
        Objects.equals(this.place, alarmCreate.place) &&
        Objects.equals(this.atBaseType, alarmCreate.atBaseType) &&
        Objects.equals(this.atSchemaLocation, alarmCreate.atSchemaLocation) &&
        Objects.equals(this.atType, alarmCreate.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ackState, ackSystemId, ackUserId, alarmChangedTime, alarmClearedTime, alarmDetails, alarmEscalation, alarmRaisedTime, alarmReportingTime, alarmedObjectType, clearSystemId, clearUserId, externalAlarmId, isRootCause, plannedOutageIndicator, probableCause, proposedRepairedActions, reportingSystemId, serviceAffecting, sourceSystemId, specificProblem, state, affectedService, alarmType, alarmedObject, comment, correlatedAlarm, crossedThresholdInformation, parentAlarm, perceivedSeverity, place, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AlarmCreate {\n");
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

