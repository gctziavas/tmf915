package org.etsi.osl.controllers.tmf915.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Generated;

/**
 * action to be performed on the product
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public enum OrderItemActionType {
  
  ADD("add"),
  
  MODIFY("modify"),
  
  DELETE("delete"),
  
  NO_CHANGE("noChange");

  private final String value;

  OrderItemActionType(String value) {
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
  public static OrderItemActionType fromValue(String value) {
    for (OrderItemActionType b : OrderItemActionType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

