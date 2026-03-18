package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Categorizes the alarm (X.733 8.1.1, 3GPP TS 32.111-2 Annex A)
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public enum AlarmType {
  
  COMMUNICATIONS_ALARM("communicationsAlarm"),
  
  PROCESSING_ERROR_ALARM("processingErrorAlarm"),
  
  ENVIRONMENTAL_ALARM("environmentalAlarm"),
  
  QUALITY_OF_SERVICE_ALARM("qualityOfServiceAlarm"),
  
  EQUIPMENT_ALARM("equipmentAlarm"),
  
  INTEGRITY_VIOLATION("integrityViolation"),
  
  OPERATIONAL_VIOLATION("operationalViolation"),
  
  PHYSICAL_VIOLATION("physicalViolation"),
  
  SECURITY_SERVICE("securityService"),
  
  MECHANISM_VIOLATION("mechanismViolation"),
  
  TIME_DOMAIN_VIOLATION("timeDomainViolation");

  private final String value;

  AlarmType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static AlarmType fromValue(String value) {
    for (AlarmType b : AlarmType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

