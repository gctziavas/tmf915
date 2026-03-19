package org.etsi.osl.controllers.tmf915.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Generated;

/**
 * This datatype lists the possible severities that can be allocated to a Alarm. The values are consistent with ITU-T Recommendation X.733. This specification does not recommend the use of indeterminate.
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public enum PerceivedSeverity {
  
  CRITICAL("CRITICAL"),
  
  MAJOR("MAJOR"),
  
  MINOR("MINOR"),
  
  WARNING("WARNING"),
  
  INDETERMINATE("INDETERMINATE"),
  
  CLEARED("CLEARED");

  private final String value;

  PerceivedSeverity(String value) {
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
  public static PerceivedSeverity fromValue(String value) {
    for (PerceivedSeverity b : PerceivedSeverity.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

