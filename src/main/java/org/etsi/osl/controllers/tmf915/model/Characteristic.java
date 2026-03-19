package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.etsi.osl.controllers.tmf915.model.CharacteristicRelationship;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import org.etsi.osl.controllers.tmf915.mappers.converters.CharacteristicRelationshipListConverter;
import org.etsi.osl.controllers.tmf915.mappers.converters.ObjectToJsonConverter;
import org.etsi.osl.controllers.tmf915.mappers.converters.UriToStringConverter;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Describes a given characteristic of an object or entity through a name/value pair.
 */

@Entity
@Table(name = "aim915_char")
@Schema(name = "Characteristic", description = "Describes a given characteristic of an object or entity through a name/value pair.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class Characteristic {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private @Nullable String id;

  private String name;

  private @Nullable String valueType;

  @Convert(converter = CharacteristicRelationshipListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid CharacteristicRelationship> characteristicRelationship = new ArrayList<>();

  @Convert(converter = ObjectToJsonConverter.class)
  @Column(columnDefinition = "TEXT")
  private Object value;

  private @Nullable String atBaseType;

  @Convert(converter = UriToStringConverter.class)
  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public Characteristic() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Characteristic(String name, Object value) {
    this.name = name;
    this.value = value;
  }

  public Characteristic id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * Unique identifier of the characteristic
   * @return id
   */
  
  @Schema(name = "id", description = "Unique identifier of the characteristic", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(@Nullable String id) {
    this.id = id;
  }

  public Characteristic name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Name of the characteristic
   * @return name
   */
  @NotNull 
  @Schema(name = "name", description = "Name of the characteristic", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  public Characteristic valueType(@Nullable String valueType) {
    this.valueType = valueType;
    return this;
  }

  /**
   * Data type of the value of the characteristic
   * @return valueType
   */
  
  @Schema(name = "valueType", description = "Data type of the value of the characteristic", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("valueType")
  public @Nullable String getValueType() {
    return valueType;
  }

  @JsonProperty("valueType")
  public void setValueType(@Nullable String valueType) {
    this.valueType = valueType;
  }

  public Characteristic characteristicRelationship(List<@Valid CharacteristicRelationship> characteristicRelationship) {
    this.characteristicRelationship = characteristicRelationship;
    return this;
  }

  public Characteristic addCharacteristicRelationshipItem(CharacteristicRelationship characteristicRelationshipItem) {
    if (this.characteristicRelationship == null) {
      this.characteristicRelationship = new ArrayList<>();
    }
    this.characteristicRelationship.add(characteristicRelationshipItem);
    return this;
  }

  /**
   * Get characteristicRelationship
   * @return characteristicRelationship
   */
  @Valid 
  @Schema(name = "characteristicRelationship", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("characteristicRelationship")
  public List<@Valid CharacteristicRelationship> getCharacteristicRelationship() {
    return characteristicRelationship;
  }

  @JsonProperty("characteristicRelationship")
  public void setCharacteristicRelationship(List<@Valid CharacteristicRelationship> characteristicRelationship) {
    this.characteristicRelationship = characteristicRelationship;
  }

  public Characteristic value(Object value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  @NotNull 
  @Schema(name = "value", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("value")
  public Object getValue() {
    return value;
  }

  @JsonProperty("value")
  public void setValue(Object value) {
    this.value = value;
  }

  public Characteristic atBaseType(@Nullable String atBaseType) {
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

  public Characteristic atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public Characteristic atType(@Nullable String atType) {
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
    Characteristic characteristic = (Characteristic) o;
    return Objects.equals(this.id, characteristic.id) &&
        Objects.equals(this.name, characteristic.name) &&
        Objects.equals(this.valueType, characteristic.valueType) &&
        Objects.equals(this.characteristicRelationship, characteristic.characteristicRelationship) &&
        Objects.equals(this.value, characteristic.value) &&
        Objects.equals(this.atBaseType, characteristic.atBaseType) &&
        Objects.equals(this.atSchemaLocation, characteristic.atSchemaLocation) &&
        Objects.equals(this.atType, characteristic.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, valueType, characteristicRelationship, value, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Characteristic {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    valueType: ").append(toIndentedString(valueType)).append("\n");
    sb.append("    characteristicRelationship: ").append(toIndentedString(characteristicRelationship)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

