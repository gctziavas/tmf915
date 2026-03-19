package org.etsi.osl.controllers.tmf915.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.etsi.osl.controllers.tmf915.mappers.converters.CharacteristicSpecificationRelationshipListConverter;
import org.etsi.osl.controllers.tmf915.mappers.converters.CharacteristicValueSpecificationListConverter;
import org.etsi.osl.controllers.tmf915.mappers.converters.UriToStringConverter;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class defines a characteristic specification.
 */

@Entity
@Table(name = "aim915_charspec")
@Schema(name = "CharacteristicSpecification", description = "This class defines a characteristic specification.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class CharacteristicSpecification {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private @Nullable String id;

  private @Nullable Boolean configurable;

  private @Nullable String description;

  private @Nullable Boolean extensible;

  private @Nullable Boolean isUnique;

  private @Nullable Integer maxCardinality;

  private @Nullable Integer minCardinality;

  private @Nullable String name;

  private @Nullable String regex;

  private @Nullable String valueType;

  @Convert(converter = CharacteristicSpecificationRelationshipListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid CharacteristicSpecificationRelationship> charSpecRelationship = new ArrayList<>();

  @Convert(converter = CharacteristicValueSpecificationListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid CharacteristicValueSpecification> characteristicValueSpecification = new ArrayList<>();

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "startDateTime", column = @Column(name = "valid_for_start")),
      @AttributeOverride(name = "endDateTime",   column = @Column(name = "valid_for_end"))
  })
  private @Nullable TimePeriod validFor;

  private @Nullable String atBaseType;

  @Convert(converter = UriToStringConverter.class)
  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  private @Nullable String atValueSchemaLocation;

  public CharacteristicSpecification id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * Unique ID for the characteristic
   * @return id
   */
  
  @Schema(name = "id", description = "Unique ID for the characteristic", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(@Nullable String id) {
    this.id = id;
  }

  public CharacteristicSpecification configurable(@Nullable Boolean configurable) {
    this.configurable = configurable;
    return this;
  }

  /**
   * If true, the Boolean indicates that the target Characteristic is configurable
   * @return configurable
   */
  
  @Schema(name = "configurable", description = "If true, the Boolean indicates that the target Characteristic is configurable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("configurable")
  public @Nullable Boolean getConfigurable() {
    return configurable;
  }

  @JsonProperty("configurable")
  public void setConfigurable(@Nullable Boolean configurable) {
    this.configurable = configurable;
  }

  public CharacteristicSpecification description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * A narrative that explains the CharacteristicSpecification.
   * @return description
   */
  
  @Schema(name = "description", description = "A narrative that explains the CharacteristicSpecification.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public CharacteristicSpecification extensible(@Nullable Boolean extensible) {
    this.extensible = extensible;
    return this;
  }

  /**
   * An indicator that specifies that the values for the characteristic can be extended by adding new values when instantiating a characteristic for a resource.
   * @return extensible
   */
  
  @Schema(name = "extensible", description = "An indicator that specifies that the values for the characteristic can be extended by adding new values when instantiating a characteristic for a resource.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("extensible")
  public @Nullable Boolean getExtensible() {
    return extensible;
  }

  @JsonProperty("extensible")
  public void setExtensible(@Nullable Boolean extensible) {
    this.extensible = extensible;
  }

  public CharacteristicSpecification isUnique(@Nullable Boolean isUnique) {
    this.isUnique = isUnique;
    return this;
  }

  /**
   * An indicator that specifies if a value is unique for the specification. Possible values are; \"unique while value is in effect\" and \"unique whether value is in effect or not\"
   * @return isUnique
   */
  
  @Schema(name = "isUnique", description = "An indicator that specifies if a value is unique for the specification. Possible values are; \"unique while value is in effect\" and \"unique whether value is in effect or not\"", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isUnique")
  public @Nullable Boolean getIsUnique() {
    return isUnique;
  }

  @JsonProperty("isUnique")
  public void setIsUnique(@Nullable Boolean isUnique) {
    this.isUnique = isUnique;
  }

  public CharacteristicSpecification maxCardinality(@Nullable Integer maxCardinality) {
    this.maxCardinality = maxCardinality;
    return this;
  }

  /**
   * The maximum number of instances a CharacteristicValue can take on. For example, zero to five phone numbers in a group calling plan, where five is the value for the maxCardinality.
   * @return maxCardinality
   */
  
  @Schema(name = "maxCardinality", description = "The maximum number of instances a CharacteristicValue can take on. For example, zero to five phone numbers in a group calling plan, where five is the value for the maxCardinality.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxCardinality")
  public @Nullable Integer getMaxCardinality() {
    return maxCardinality;
  }

  @JsonProperty("maxCardinality")
  public void setMaxCardinality(@Nullable Integer maxCardinality) {
    this.maxCardinality = maxCardinality;
  }

  public CharacteristicSpecification minCardinality(@Nullable Integer minCardinality) {
    this.minCardinality = minCardinality;
    return this;
  }

  /**
   * The minimum number of instances a CharacteristicValue can take on. For example, zero to five phone numbers in a group calling plan, where zero is the value for the minCardinality.
   * @return minCardinality
   */
  
  @Schema(name = "minCardinality", description = "The minimum number of instances a CharacteristicValue can take on. For example, zero to five phone numbers in a group calling plan, where zero is the value for the minCardinality.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minCardinality")
  public @Nullable Integer getMinCardinality() {
    return minCardinality;
  }

  @JsonProperty("minCardinality")
  public void setMinCardinality(@Nullable Integer minCardinality) {
    this.minCardinality = minCardinality;
  }

  public CharacteristicSpecification name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * A word, term, or phrase by which this characteristic specification is known and distinguished from other characteristic specifications.
   * @return name
   */
  
  @Schema(name = "name", description = "A word, term, or phrase by which this characteristic specification is known and distinguished from other characteristic specifications.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(@Nullable String name) {
    this.name = name;
  }

  public CharacteristicSpecification regex(@Nullable String regex) {
    this.regex = regex;
    return this;
  }

  /**
   * A rule or principle represented in regular expression used to derive the value of a characteristic value.
   * @return regex
   */
  
  @Schema(name = "regex", description = "A rule or principle represented in regular expression used to derive the value of a characteristic value.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("regex")
  public @Nullable String getRegex() {
    return regex;
  }

  @JsonProperty("regex")
  public void setRegex(@Nullable String regex) {
    this.regex = regex;
  }

  public CharacteristicSpecification valueType(@Nullable String valueType) {
    this.valueType = valueType;
    return this;
  }

  /**
   * A kind of value that the characteristic can take on, such as numeric, text and so forth
   * @return valueType
   */
  
  @Schema(name = "valueType", description = "A kind of value that the characteristic can take on, such as numeric, text and so forth", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("valueType")
  public @Nullable String getValueType() {
    return valueType;
  }

  @JsonProperty("valueType")
  public void setValueType(@Nullable String valueType) {
    this.valueType = valueType;
  }

  public CharacteristicSpecification charSpecRelationship(List<@Valid CharacteristicSpecificationRelationship> charSpecRelationship) {
    this.charSpecRelationship = charSpecRelationship;
    return this;
  }

  public CharacteristicSpecification addCharSpecRelationshipItem(CharacteristicSpecificationRelationship charSpecRelationshipItem) {
    if (this.charSpecRelationship == null) {
      this.charSpecRelationship = new ArrayList<>();
    }
    this.charSpecRelationship.add(charSpecRelationshipItem);
    return this;
  }

  /**
   * An aggregation, migration, substitution, dependency or exclusivity relationship between/among Specification Characteristics.
   * @return charSpecRelationship
   */
  @Valid 
  @Schema(name = "charSpecRelationship", description = "An aggregation, migration, substitution, dependency or exclusivity relationship between/among Specification Characteristics.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("charSpecRelationship")
  public List<@Valid CharacteristicSpecificationRelationship> getCharSpecRelationship() {
    return charSpecRelationship;
  }

  @JsonProperty("charSpecRelationship")
  public void setCharSpecRelationship(List<@Valid CharacteristicSpecificationRelationship> charSpecRelationship) {
    this.charSpecRelationship = charSpecRelationship;
  }

  public CharacteristicSpecification characteristicValueSpecification(List<@Valid CharacteristicValueSpecification> characteristicValueSpecification) {
    this.characteristicValueSpecification = characteristicValueSpecification;
    return this;
  }

  public CharacteristicSpecification addCharacteristicValueSpecificationItem(CharacteristicValueSpecification characteristicValueSpecificationItem) {
    if (this.characteristicValueSpecification == null) {
      this.characteristicValueSpecification = new ArrayList<>();
    }
    this.characteristicValueSpecification.add(characteristicValueSpecificationItem);
    return this;
  }

  /**
   * A CharacteristicValueSpecification object is used to define a set of attributes, each of which can be assigned to a corresponding set of attributes in a CharacteristicSpecification object. The values of the attributes in the CharacteristicValueSpecification object describe the values of the attributes that a corresponding Characteristic object can take on.
   * @return characteristicValueSpecification
   */
  @Valid 
  @Schema(name = "characteristicValueSpecification", description = "A CharacteristicValueSpecification object is used to define a set of attributes, each of which can be assigned to a corresponding set of attributes in a CharacteristicSpecification object. The values of the attributes in the CharacteristicValueSpecification object describe the values of the attributes that a corresponding Characteristic object can take on.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("characteristicValueSpecification")
  public List<@Valid CharacteristicValueSpecification> getCharacteristicValueSpecification() {
    return characteristicValueSpecification;
  }

  @JsonProperty("characteristicValueSpecification")
  public void setCharacteristicValueSpecification(List<@Valid CharacteristicValueSpecification> characteristicValueSpecification) {
    this.characteristicValueSpecification = characteristicValueSpecification;
  }

  public CharacteristicSpecification validFor(@Nullable TimePeriod validFor) {
    this.validFor = validFor;
    return this;
  }

  /**
   * Get validFor
   * @return validFor
   */
  @Valid 
  @Schema(name = "validFor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("validFor")
  public @Nullable TimePeriod getValidFor() {
    return validFor;
  }

  @JsonProperty("validFor")
  public void setValidFor(@Nullable TimePeriod validFor) {
    this.validFor = validFor;
  }

  public CharacteristicSpecification atBaseType(@Nullable String atBaseType) {
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

  public CharacteristicSpecification atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public CharacteristicSpecification atType(@Nullable String atType) {
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

  public CharacteristicSpecification atValueSchemaLocation(@Nullable String atValueSchemaLocation) {
    this.atValueSchemaLocation = atValueSchemaLocation;
    return this;
  }

  /**
   * This (optional) field provides a link to the schema describing the value type.
   * @return atValueSchemaLocation
   */
  
  @Schema(name = "@valueSchemaLocation", description = "This (optional) field provides a link to the schema describing the value type.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("@valueSchemaLocation")
  public @Nullable String getAtValueSchemaLocation() {
    return atValueSchemaLocation;
  }

  @JsonProperty("@valueSchemaLocation")
  public void setAtValueSchemaLocation(@Nullable String atValueSchemaLocation) {
    this.atValueSchemaLocation = atValueSchemaLocation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacteristicSpecification characteristicSpecification = (CharacteristicSpecification) o;
    return Objects.equals(this.id, characteristicSpecification.id) &&
        Objects.equals(this.configurable, characteristicSpecification.configurable) &&
        Objects.equals(this.description, characteristicSpecification.description) &&
        Objects.equals(this.extensible, characteristicSpecification.extensible) &&
        Objects.equals(this.isUnique, characteristicSpecification.isUnique) &&
        Objects.equals(this.maxCardinality, characteristicSpecification.maxCardinality) &&
        Objects.equals(this.minCardinality, characteristicSpecification.minCardinality) &&
        Objects.equals(this.name, characteristicSpecification.name) &&
        Objects.equals(this.regex, characteristicSpecification.regex) &&
        Objects.equals(this.valueType, characteristicSpecification.valueType) &&
        Objects.equals(this.charSpecRelationship, characteristicSpecification.charSpecRelationship) &&
        Objects.equals(this.characteristicValueSpecification, characteristicSpecification.characteristicValueSpecification) &&
        Objects.equals(this.validFor, characteristicSpecification.validFor) &&
        Objects.equals(this.atBaseType, characteristicSpecification.atBaseType) &&
        Objects.equals(this.atSchemaLocation, characteristicSpecification.atSchemaLocation) &&
        Objects.equals(this.atType, characteristicSpecification.atType) &&
        Objects.equals(this.atValueSchemaLocation, characteristicSpecification.atValueSchemaLocation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, configurable, description, extensible, isUnique, maxCardinality, minCardinality, name, regex, valueType, charSpecRelationship, characteristicValueSpecification, validFor, atBaseType, atSchemaLocation, atType, atValueSchemaLocation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacteristicSpecification {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    configurable: ").append(toIndentedString(configurable)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    extensible: ").append(toIndentedString(extensible)).append("\n");
    sb.append("    isUnique: ").append(toIndentedString(isUnique)).append("\n");
    sb.append("    maxCardinality: ").append(toIndentedString(maxCardinality)).append("\n");
    sb.append("    minCardinality: ").append(toIndentedString(minCardinality)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    regex: ").append(toIndentedString(regex)).append("\n");
    sb.append("    valueType: ").append(toIndentedString(valueType)).append("\n");
    sb.append("    charSpecRelationship: ").append(toIndentedString(charSpecRelationship)).append("\n");
    sb.append("    characteristicValueSpecification: ").append(toIndentedString(characteristicValueSpecification)).append("\n");
    sb.append("    validFor: ").append(toIndentedString(validFor)).append("\n");
    sb.append("    atBaseType: ").append(toIndentedString(atBaseType)).append("\n");
    sb.append("    atSchemaLocation: ").append(toIndentedString(atSchemaLocation)).append("\n");
    sb.append("    atType: ").append(toIndentedString(atType)).append("\n");
    sb.append("    atValueSchemaLocation: ").append(toIndentedString(atValueSchemaLocation)).append("\n");
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

