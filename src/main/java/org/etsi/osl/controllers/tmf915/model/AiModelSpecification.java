package org.etsi.osl.controllers.tmf915.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.etsi.osl.controllers.tmf915.mappers.converters.*;
import org.jspecify.annotations.Nullable;
import org.springframework.format.annotation.DateTimeFormat;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * AiModelSpecification is a class that offers characteristics to describe a type of service. Functionally, it acts as a template by which Services may be instantiated. By sharing the same  specification, these services would therefore share the same set of characteristics.
 */

@Entity
@Table(name = "aim915_aimspec")
@Schema(name = "AiModelSpecification", description = "AiModelSpecification is a class that offers characteristics to describe a type of service. Functionally, it acts as a template by which Services may be instantiated. By sharing the same  specification, these services would therefore share the same set of characteristics.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class AiModelSpecification {

  @Id
  private @Nullable String id;

  @Convert(converter = UriToStringConverter.class)
  private @Nullable URI href;

  @Convert(converter = ObjectToJsonConverter.class)
  @Column(columnDefinition = "TEXT")
  private @Nullable Object deploymentRecord;

  private @Nullable String description;

  @Convert(converter = ObjectToJsonConverter.class)
  @Column(columnDefinition = "TEXT")
  private @Nullable Object inheritedModel;

  private @Nullable Boolean isBundle;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastUpdate;

  private @Nullable String lifecycleStatus;

  @Convert(converter = ObjectToJsonConverter.class)
  @Column(columnDefinition = "TEXT")
  private @Nullable Object modelContractVersionHistory;

  @Convert(converter = ObjectToJsonConverter.class)
  @Column(columnDefinition = "TEXT")
  private @Nullable Object modelDataSheet;

  @Convert(converter = ObjectToJsonConverter.class)
  @Column(columnDefinition = "TEXT")
  private @Nullable Object modelEvaluationData;

  @Convert(converter = ObjectToJsonConverter.class)
  @Column(columnDefinition = "TEXT")
  private @Nullable Object modelSpecificationHistory;

  @Convert(converter = ObjectToJsonConverter.class)
  @Column(columnDefinition = "TEXT")
  private @Nullable Object modelTrainingData;

  private @Nullable String name;

  private @Nullable String version;

  @Convert(converter = AttachmentRefOrValueListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid AttachmentRefOrValue> attachment = new ArrayList<>();

  @Convert(converter = ConstraintRefListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid ConstraintRef> constraint = new ArrayList<>();

  @Convert(converter = EntitySpecificationRelationshipListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid EntitySpecificationRelationship> entitySpecRelationship = new ArrayList<>();

  @Convert(converter = FeatureSpecificationListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid FeatureSpecification> featureSpecification = new ArrayList<>();

  @Convert(converter = RelatedPartyListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid RelatedParty> relatedParty = new ArrayList<>();

  @Convert(converter = ResourceSpecificationRefListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid ResourceSpecificationRef> resourceSpecification = new ArrayList<>();

  @Convert(converter = ServiceLevelSpecificationRefListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid ServiceLevelSpecificationRef> serviceLevelSpecification = new ArrayList<>();

  @Convert(converter = ServiceSpecRelationshipListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid ServiceSpecRelationship> serviceSpecRelationship = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "aim_spec_id")
  @Valid
  private List<@Valid CharacteristicSpecification> specCharacteristic = new ArrayList<>();

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "atSchemaLocation", column = @Column(name = "tes_schema_loc")),
      @AttributeOverride(name = "atType",           column = @Column(name = "tes_type"))
  })
  private @Nullable TargetEntitySchema targetEntitySchema;

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

  public AiModelSpecification id(@Nullable String id) {
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

  public AiModelSpecification href(@Nullable URI href) {
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

  public AiModelSpecification deploymentRecord(@Nullable Object deploymentRecord) {
    this.deploymentRecord = deploymentRecord;
    return this;
  }

  /**
   * Get deploymentRecord
   * @return deploymentRecord
   */
  
  @Schema(name = "deploymentRecord", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deploymentRecord")
  public @Nullable Object getDeploymentRecord() {
    return deploymentRecord;
  }

  @JsonProperty("deploymentRecord")
  public void setDeploymentRecord(@Nullable Object deploymentRecord) {
    this.deploymentRecord = deploymentRecord;
  }

  public AiModelSpecification description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Description of the specification
   * @return description
   */
  
  @Schema(name = "description", description = "Description of the specification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public AiModelSpecification inheritedModel(@Nullable Object inheritedModel) {
    this.inheritedModel = inheritedModel;
    return this;
  }

  /**
   * Get inheritedModel
   * @return inheritedModel
   */
  
  @Schema(name = "inheritedModel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inheritedModel")
  public @Nullable Object getInheritedModel() {
    return inheritedModel;
  }

  @JsonProperty("inheritedModel")
  public void setInheritedModel(@Nullable Object inheritedModel) {
    this.inheritedModel = inheritedModel;
  }

  public AiModelSpecification isBundle(@Nullable Boolean isBundle) {
    this.isBundle = isBundle;
    return this;
  }

  /**
   * isBundle determines whether specification represents a single specification (false), or a bundle of specifications (true).
   * @return isBundle
   */
  
  @Schema(name = "isBundle", description = "isBundle determines whether specification represents a single specification (false), or a bundle of specifications (true).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isBundle")
  public @Nullable Boolean getIsBundle() {
    return isBundle;
  }

  @JsonProperty("isBundle")
  public void setIsBundle(@Nullable Boolean isBundle) {
    this.isBundle = isBundle;
  }

  public AiModelSpecification lastUpdate(@Nullable OffsetDateTime lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }

  /**
   * Date and time of the last update of the specification
   * @return lastUpdate
   */
  @Valid 
  @Schema(name = "lastUpdate", description = "Date and time of the last update of the specification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastUpdate")
  public @Nullable OffsetDateTime getLastUpdate() {
    return lastUpdate;
  }

  @JsonProperty("lastUpdate")
  public void setLastUpdate(@Nullable OffsetDateTime lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public AiModelSpecification lifecycleStatus(@Nullable String lifecycleStatus) {
    this.lifecycleStatus = lifecycleStatus;
    return this;
  }

  /**
   * Used to indicate the current lifecycle status of this catalog item
   * @return lifecycleStatus
   */
  
  @Schema(name = "lifecycleStatus", description = "Used to indicate the current lifecycle status of this catalog item", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lifecycleStatus")
  public @Nullable String getLifecycleStatus() {
    return lifecycleStatus;
  }

  @JsonProperty("lifecycleStatus")
  public void setLifecycleStatus(@Nullable String lifecycleStatus) {
    this.lifecycleStatus = lifecycleStatus;
  }

  public AiModelSpecification modelContractVersionHistory(@Nullable Object modelContractVersionHistory) {
    this.modelContractVersionHistory = modelContractVersionHistory;
    return this;
  }

  /**
   * Get modelContractVersionHistory
   * @return modelContractVersionHistory
   */
  
  @Schema(name = "modelContractVersionHistory", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modelContractVersionHistory")
  public @Nullable Object getModelContractVersionHistory() {
    return modelContractVersionHistory;
  }

  @JsonProperty("modelContractVersionHistory")
  public void setModelContractVersionHistory(@Nullable Object modelContractVersionHistory) {
    this.modelContractVersionHistory = modelContractVersionHistory;
  }

  public AiModelSpecification modelDataSheet(@Nullable Object modelDataSheet) {
    this.modelDataSheet = modelDataSheet;
    return this;
  }

  /**
   * Get modelDataSheet
   * @return modelDataSheet
   */
  
  @Schema(name = "modelDataSheet", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modelDataSheet")
  public @Nullable Object getModelDataSheet() {
    return modelDataSheet;
  }

  @JsonProperty("modelDataSheet")
  public void setModelDataSheet(@Nullable Object modelDataSheet) {
    this.modelDataSheet = modelDataSheet;
  }

  public AiModelSpecification modelEvaluationData(@Nullable Object modelEvaluationData) {
    this.modelEvaluationData = modelEvaluationData;
    return this;
  }

  /**
   * Get modelEvaluationData
   * @return modelEvaluationData
   */
  
  @Schema(name = "modelEvaluationData", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modelEvaluationData")
  public @Nullable Object getModelEvaluationData() {
    return modelEvaluationData;
  }

  @JsonProperty("modelEvaluationData")
  public void setModelEvaluationData(@Nullable Object modelEvaluationData) {
    this.modelEvaluationData = modelEvaluationData;
  }

  public AiModelSpecification modelSpecificationHistory(@Nullable Object modelSpecificationHistory) {
    this.modelSpecificationHistory = modelSpecificationHistory;
    return this;
  }

  /**
   * Get modelSpecificationHistory
   * @return modelSpecificationHistory
   */
  
  @Schema(name = "modelSpecificationHistory", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modelSpecificationHistory")
  public @Nullable Object getModelSpecificationHistory() {
    return modelSpecificationHistory;
  }

  @JsonProperty("modelSpecificationHistory")
  public void setModelSpecificationHistory(@Nullable Object modelSpecificationHistory) {
    this.modelSpecificationHistory = modelSpecificationHistory;
  }

  public AiModelSpecification modelTrainingData(@Nullable Object modelTrainingData) {
    this.modelTrainingData = modelTrainingData;
    return this;
  }

  /**
   * Get modelTrainingData
   * @return modelTrainingData
   */
  
  @Schema(name = "modelTrainingData", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modelTrainingData")
  public @Nullable Object getModelTrainingData() {
    return modelTrainingData;
  }

  @JsonProperty("modelTrainingData")
  public void setModelTrainingData(@Nullable Object modelTrainingData) {
    this.modelTrainingData = modelTrainingData;
  }

  public AiModelSpecification name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Name given to the specification
   * @return name
   */
  
  @Schema(name = "name", description = "Name given to the specification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(@Nullable String name) {
    this.name = name;
  }

  public AiModelSpecification version(@Nullable String version) {
    this.version = version;
    return this;
  }

  /**
   * specification version
   * @return version
   */
  
  @Schema(name = "version", description = "specification version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("version")
  public @Nullable String getVersion() {
    return version;
  }

  @JsonProperty("version")
  public void setVersion(@Nullable String version) {
    this.version = version;
  }

  public AiModelSpecification attachment(List<@Valid AttachmentRefOrValue> attachment) {
    this.attachment = attachment;
    return this;
  }

  public AiModelSpecification addAttachmentItem(AttachmentRefOrValue attachmentItem) {
    if (this.attachment == null) {
      this.attachment = new ArrayList<>();
    }
    this.attachment.add(attachmentItem);
    return this;
  }

  /**
   * Attachments that may be of relevance to this specification, such as picture, document, media
   * @return attachment
   */
  @Valid 
  @Schema(name = "attachment", description = "Attachments that may be of relevance to this specification, such as picture, document, media", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachment")
  public List<@Valid AttachmentRefOrValue> getAttachment() {
    return attachment;
  }

  @JsonProperty("attachment")
  public void setAttachment(List<@Valid AttachmentRefOrValue> attachment) {
    this.attachment = attachment;
  }

  public AiModelSpecification constraint(List<@Valid ConstraintRef> constraint) {
    this.constraint = constraint;
    return this;
  }

  public AiModelSpecification addConstraintItem(ConstraintRef constraintItem) {
    if (this.constraint == null) {
      this.constraint = new ArrayList<>();
    }
    this.constraint.add(constraintItem);
    return this;
  }

  /**
   * This is a list of constraint references applied to this specification
   * @return constraint
   */
  @Valid 
  @Schema(name = "constraint", description = "This is a list of constraint references applied to this specification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("constraint")
  public List<@Valid ConstraintRef> getConstraint() {
    return constraint;
  }

  @JsonProperty("constraint")
  public void setConstraint(List<@Valid ConstraintRef> constraint) {
    this.constraint = constraint;
  }

  public AiModelSpecification entitySpecRelationship(List<@Valid EntitySpecificationRelationship> entitySpecRelationship) {
    this.entitySpecRelationship = entitySpecRelationship;
    return this;
  }

  public AiModelSpecification addEntitySpecRelationshipItem(EntitySpecificationRelationship entitySpecRelationshipItem) {
    if (this.entitySpecRelationship == null) {
      this.entitySpecRelationship = new ArrayList<>();
    }
    this.entitySpecRelationship.add(entitySpecRelationshipItem);
    return this;
  }

  /**
   * Relationship to another specification
   * @return entitySpecRelationship
   */
  @Valid 
  @Schema(name = "entitySpecRelationship", description = "Relationship to another specification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entitySpecRelationship")
  public List<@Valid EntitySpecificationRelationship> getEntitySpecRelationship() {
    return entitySpecRelationship;
  }

  @JsonProperty("entitySpecRelationship")
  public void setEntitySpecRelationship(List<@Valid EntitySpecificationRelationship> entitySpecRelationship) {
    this.entitySpecRelationship = entitySpecRelationship;
  }

  public AiModelSpecification featureSpecification(List<@Valid FeatureSpecification> featureSpecification) {
    this.featureSpecification = featureSpecification;
    return this;
  }

  public AiModelSpecification addFeatureSpecificationItem(FeatureSpecification featureSpecificationItem) {
    if (this.featureSpecification == null) {
      this.featureSpecification = new ArrayList<>();
    }
    this.featureSpecification.add(featureSpecificationItem);
    return this;
  }

  /**
   * A list of Features for this specification.
   * @return featureSpecification
   */
  @Valid 
  @Schema(name = "featureSpecification", description = "A list of Features for this specification.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("featureSpecification")
  public List<@Valid FeatureSpecification> getFeatureSpecification() {
    return featureSpecification;
  }

  @JsonProperty("featureSpecification")
  public void setFeatureSpecification(List<@Valid FeatureSpecification> featureSpecification) {
    this.featureSpecification = featureSpecification;
  }

  public AiModelSpecification relatedParty(List<@Valid RelatedParty> relatedParty) {
    this.relatedParty = relatedParty;
    return this;
  }

  public AiModelSpecification addRelatedPartyItem(RelatedParty relatedPartyItem) {
    if (this.relatedParty == null) {
      this.relatedParty = new ArrayList<>();
    }
    this.relatedParty.add(relatedPartyItem);
    return this;
  }

  /**
   * Parties who manage or otherwise have an interest in this specification
   * @return relatedParty
   */
  @Valid 
  @Schema(name = "relatedParty", description = "Parties who manage or otherwise have an interest in this specification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relatedParty")
  public List<@Valid RelatedParty> getRelatedParty() {
    return relatedParty;
  }

  @JsonProperty("relatedParty")
  public void setRelatedParty(List<@Valid RelatedParty> relatedParty) {
    this.relatedParty = relatedParty;
  }

  public AiModelSpecification resourceSpecification(List<@Valid ResourceSpecificationRef> resourceSpecification) {
    this.resourceSpecification = resourceSpecification;
    return this;
  }

  public AiModelSpecification addResourceSpecificationItem(ResourceSpecificationRef resourceSpecificationItem) {
    if (this.resourceSpecification == null) {
      this.resourceSpecification = new ArrayList<>();
    }
    this.resourceSpecification.add(resourceSpecificationItem);
    return this;
  }

  /**
   * A list of resource specification references (ResourceSpecificationRef [*]). The ResourceSpecification is required for a service specification with type ResourceFacingServiceSpecification (RFSS).
   * @return resourceSpecification
   */
  @Valid 
  @Schema(name = "resourceSpecification", description = "A list of resource specification references (ResourceSpecificationRef [*]). The ResourceSpecification is required for a service specification with type ResourceFacingServiceSpecification (RFSS).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resourceSpecification")
  public List<@Valid ResourceSpecificationRef> getResourceSpecification() {
    return resourceSpecification;
  }

  @JsonProperty("resourceSpecification")
  public void setResourceSpecification(List<@Valid ResourceSpecificationRef> resourceSpecification) {
    this.resourceSpecification = resourceSpecification;
  }

  public AiModelSpecification serviceLevelSpecification(List<@Valid ServiceLevelSpecificationRef> serviceLevelSpecification) {
    this.serviceLevelSpecification = serviceLevelSpecification;
    return this;
  }

  public AiModelSpecification addServiceLevelSpecificationItem(ServiceLevelSpecificationRef serviceLevelSpecificationItem) {
    if (this.serviceLevelSpecification == null) {
      this.serviceLevelSpecification = new ArrayList<>();
    }
    this.serviceLevelSpecification.add(serviceLevelSpecificationItem);
    return this;
  }

  /**
   * A list of service level specifications related to this service specification, and which will need to be satisifiable for corresponding service instances; e.g. Gold, Platinum
   * @return serviceLevelSpecification
   */
  @Valid 
  @Schema(name = "serviceLevelSpecification", description = "A list of service level specifications related to this service specification, and which will need to be satisifiable for corresponding service instances; e.g. Gold, Platinum", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("serviceLevelSpecification")
  public List<@Valid ServiceLevelSpecificationRef> getServiceLevelSpecification() {
    return serviceLevelSpecification;
  }

  @JsonProperty("serviceLevelSpecification")
  public void setServiceLevelSpecification(List<@Valid ServiceLevelSpecificationRef> serviceLevelSpecification) {
    this.serviceLevelSpecification = serviceLevelSpecification;
  }

  public AiModelSpecification serviceSpecRelationship(List<@Valid ServiceSpecRelationship> serviceSpecRelationship) {
    this.serviceSpecRelationship = serviceSpecRelationship;
    return this;
  }

  public AiModelSpecification addServiceSpecRelationshipItem(ServiceSpecRelationship serviceSpecRelationshipItem) {
    if (this.serviceSpecRelationship == null) {
      this.serviceSpecRelationship = new ArrayList<>();
    }
    this.serviceSpecRelationship.add(serviceSpecRelationshipItem);
    return this;
  }

  /**
   * A list of service specifications related to this specification, e.g. migration, substitution, dependency or exclusivity relationship
   * @return serviceSpecRelationship
   */
  @Valid 
  @Schema(name = "serviceSpecRelationship", description = "A list of service specifications related to this specification, e.g. migration, substitution, dependency or exclusivity relationship", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("serviceSpecRelationship")
  public List<@Valid ServiceSpecRelationship> getServiceSpecRelationship() {
    return serviceSpecRelationship;
  }

  @JsonProperty("serviceSpecRelationship")
  public void setServiceSpecRelationship(List<@Valid ServiceSpecRelationship> serviceSpecRelationship) {
    this.serviceSpecRelationship = serviceSpecRelationship;
  }

  public AiModelSpecification specCharacteristic(List<@Valid CharacteristicSpecification> specCharacteristic) {
    this.specCharacteristic = specCharacteristic;
    return this;
  }

  public AiModelSpecification addSpecCharacteristicItem(CharacteristicSpecification specCharacteristicItem) {
    if (this.specCharacteristic == null) {
      this.specCharacteristic = new ArrayList<>();
    }
    this.specCharacteristic.add(specCharacteristicItem);
    return this;
  }

  /**
   * List of characteristics that the entity can take
   * @return specCharacteristic
   */
  @Valid 
  @Schema(name = "specCharacteristic", description = "List of characteristics that the entity can take", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("specCharacteristic")
  public List<@Valid CharacteristicSpecification> getSpecCharacteristic() {
    return specCharacteristic;
  }

  @JsonProperty("specCharacteristic")
  public void setSpecCharacteristic(List<@Valid CharacteristicSpecification> specCharacteristic) {
    this.specCharacteristic = specCharacteristic;
  }

  public AiModelSpecification targetEntitySchema(@Nullable TargetEntitySchema targetEntitySchema) {
    this.targetEntitySchema = targetEntitySchema;
    return this;
  }

  /**
   * Get targetEntitySchema
   * @return targetEntitySchema
   */
  @Valid 
  @Schema(name = "targetEntitySchema", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetEntitySchema")
  public @Nullable TargetEntitySchema getTargetEntitySchema() {
    return targetEntitySchema;
  }

  @JsonProperty("targetEntitySchema")
  public void setTargetEntitySchema(@Nullable TargetEntitySchema targetEntitySchema) {
    this.targetEntitySchema = targetEntitySchema;
  }

  public AiModelSpecification validFor(@Nullable TimePeriod validFor) {
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

  public AiModelSpecification atBaseType(@Nullable String atBaseType) {
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

  public AiModelSpecification atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public AiModelSpecification atType(@Nullable String atType) {
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
    AiModelSpecification aiModelSpecification = (AiModelSpecification) o;
    return Objects.equals(this.id, aiModelSpecification.id) &&
        Objects.equals(this.href, aiModelSpecification.href) &&
        Objects.equals(this.deploymentRecord, aiModelSpecification.deploymentRecord) &&
        Objects.equals(this.description, aiModelSpecification.description) &&
        Objects.equals(this.inheritedModel, aiModelSpecification.inheritedModel) &&
        Objects.equals(this.isBundle, aiModelSpecification.isBundle) &&
        Objects.equals(this.lastUpdate, aiModelSpecification.lastUpdate) &&
        Objects.equals(this.lifecycleStatus, aiModelSpecification.lifecycleStatus) &&
        Objects.equals(this.modelContractVersionHistory, aiModelSpecification.modelContractVersionHistory) &&
        Objects.equals(this.modelDataSheet, aiModelSpecification.modelDataSheet) &&
        Objects.equals(this.modelEvaluationData, aiModelSpecification.modelEvaluationData) &&
        Objects.equals(this.modelSpecificationHistory, aiModelSpecification.modelSpecificationHistory) &&
        Objects.equals(this.modelTrainingData, aiModelSpecification.modelTrainingData) &&
        Objects.equals(this.name, aiModelSpecification.name) &&
        Objects.equals(this.version, aiModelSpecification.version) &&
        Objects.equals(this.attachment, aiModelSpecification.attachment) &&
        Objects.equals(this.constraint, aiModelSpecification.constraint) &&
        Objects.equals(this.entitySpecRelationship, aiModelSpecification.entitySpecRelationship) &&
        Objects.equals(this.featureSpecification, aiModelSpecification.featureSpecification) &&
        Objects.equals(this.relatedParty, aiModelSpecification.relatedParty) &&
        Objects.equals(this.resourceSpecification, aiModelSpecification.resourceSpecification) &&
        Objects.equals(this.serviceLevelSpecification, aiModelSpecification.serviceLevelSpecification) &&
        Objects.equals(this.serviceSpecRelationship, aiModelSpecification.serviceSpecRelationship) &&
        Objects.equals(this.specCharacteristic, aiModelSpecification.specCharacteristic) &&
        Objects.equals(this.targetEntitySchema, aiModelSpecification.targetEntitySchema) &&
        Objects.equals(this.validFor, aiModelSpecification.validFor) &&
        Objects.equals(this.atBaseType, aiModelSpecification.atBaseType) &&
        Objects.equals(this.atSchemaLocation, aiModelSpecification.atSchemaLocation) &&
        Objects.equals(this.atType, aiModelSpecification.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, deploymentRecord, description, inheritedModel, isBundle, lastUpdate, lifecycleStatus, modelContractVersionHistory, modelDataSheet, modelEvaluationData, modelSpecificationHistory, modelTrainingData, name, version, attachment, constraint, entitySpecRelationship, featureSpecification, relatedParty, resourceSpecification, serviceLevelSpecification, serviceSpecRelationship, specCharacteristic, targetEntitySchema, validFor, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AiModelSpecification {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    deploymentRecord: ").append(toIndentedString(deploymentRecord)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    inheritedModel: ").append(toIndentedString(inheritedModel)).append("\n");
    sb.append("    isBundle: ").append(toIndentedString(isBundle)).append("\n");
    sb.append("    lastUpdate: ").append(toIndentedString(lastUpdate)).append("\n");
    sb.append("    lifecycleStatus: ").append(toIndentedString(lifecycleStatus)).append("\n");
    sb.append("    modelContractVersionHistory: ").append(toIndentedString(modelContractVersionHistory)).append("\n");
    sb.append("    modelDataSheet: ").append(toIndentedString(modelDataSheet)).append("\n");
    sb.append("    modelEvaluationData: ").append(toIndentedString(modelEvaluationData)).append("\n");
    sb.append("    modelSpecificationHistory: ").append(toIndentedString(modelSpecificationHistory)).append("\n");
    sb.append("    modelTrainingData: ").append(toIndentedString(modelTrainingData)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    attachment: ").append(toIndentedString(attachment)).append("\n");
    sb.append("    constraint: ").append(toIndentedString(constraint)).append("\n");
    sb.append("    entitySpecRelationship: ").append(toIndentedString(entitySpecRelationship)).append("\n");
    sb.append("    featureSpecification: ").append(toIndentedString(featureSpecification)).append("\n");
    sb.append("    relatedParty: ").append(toIndentedString(relatedParty)).append("\n");
    sb.append("    resourceSpecification: ").append(toIndentedString(resourceSpecification)).append("\n");
    sb.append("    serviceLevelSpecification: ").append(toIndentedString(serviceLevelSpecification)).append("\n");
    sb.append("    serviceSpecRelationship: ").append(toIndentedString(serviceSpecRelationship)).append("\n");
    sb.append("    specCharacteristic: ").append(toIndentedString(specCharacteristic)).append("\n");
    sb.append("    targetEntitySchema: ").append(toIndentedString(targetEntitySchema)).append("\n");
    sb.append("    validFor: ").append(toIndentedString(validFor)).append("\n");
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

