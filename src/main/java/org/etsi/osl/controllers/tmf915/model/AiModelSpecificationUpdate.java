package org.etsi.osl.controllers.tmf915.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * AiModelSpecification is a class that offers characteristics to describe a type of service. Functionally, it acts as a template by which Services may be instantiated. By sharing the same  specification, these services would therefore share the same set of characteristics. Skipped properties: id,href,lastUpdate
 */

@Schema(name = "AiModelSpecification_Update", description = "AiModelSpecification is a class that offers characteristics to describe a type of service. Functionally, it acts as a template by which Services may be instantiated. By sharing the same  specification, these services would therefore share the same set of characteristics. Skipped properties: id,href,lastUpdate")
@JsonTypeName("AiModelSpecification_Update")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class AiModelSpecificationUpdate {

  private @Nullable Object deploymentRecord;

  private @Nullable String description;

  private @Nullable Object inheritedModel;

  private @Nullable Boolean isBundle;

  private @Nullable String lifecycleStatus;

  private @Nullable Object modelContractVersionHistory;

  private @Nullable Object modelDataSheet;

  private @Nullable Object modelEvaluationData;

  private @Nullable Object modelSpecificationHistory;

  private @Nullable Object modelTrainingData;

  private @Nullable String name;

  private @Nullable String version;

  @Valid
  private List<@Valid AttachmentRefOrValue> attachment = new ArrayList<>();

  @Valid
  private List<@Valid ConstraintRef> constraint = new ArrayList<>();

  @Valid
  private List<@Valid EntitySpecificationRelationship> entitySpecRelationship = new ArrayList<>();

  @Valid
  private List<@Valid FeatureSpecification> featureSpecification = new ArrayList<>();

  @Valid
  private List<@Valid RelatedParty> relatedParty = new ArrayList<>();

  @Valid
  private List<@Valid ResourceSpecificationRef> resourceSpecification = new ArrayList<>();

  @Valid
  private List<@Valid ServiceLevelSpecificationRef> serviceLevelSpecification = new ArrayList<>();

  @Valid
  private List<@Valid ServiceSpecRelationship> serviceSpecRelationship = new ArrayList<>();

  @Valid
  private List<@Valid CharacteristicSpecification> specCharacteristic = new ArrayList<>();

  private @Nullable TargetEntitySchema targetEntitySchema;

  private @Nullable TimePeriod validFor;

  private @Nullable String atBaseType;

  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public AiModelSpecificationUpdate deploymentRecord(@Nullable Object deploymentRecord) {
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

  public AiModelSpecificationUpdate description(@Nullable String description) {
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

  public AiModelSpecificationUpdate inheritedModel(@Nullable Object inheritedModel) {
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

  public AiModelSpecificationUpdate isBundle(@Nullable Boolean isBundle) {
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

  public AiModelSpecificationUpdate lifecycleStatus(@Nullable String lifecycleStatus) {
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

  public AiModelSpecificationUpdate modelContractVersionHistory(@Nullable Object modelContractVersionHistory) {
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

  public AiModelSpecificationUpdate modelDataSheet(@Nullable Object modelDataSheet) {
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

  public AiModelSpecificationUpdate modelEvaluationData(@Nullable Object modelEvaluationData) {
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

  public AiModelSpecificationUpdate modelSpecificationHistory(@Nullable Object modelSpecificationHistory) {
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

  public AiModelSpecificationUpdate modelTrainingData(@Nullable Object modelTrainingData) {
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

  public AiModelSpecificationUpdate name(@Nullable String name) {
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

  public AiModelSpecificationUpdate version(@Nullable String version) {
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

  public AiModelSpecificationUpdate attachment(List<@Valid AttachmentRefOrValue> attachment) {
    this.attachment = attachment;
    return this;
  }

  public AiModelSpecificationUpdate addAttachmentItem(AttachmentRefOrValue attachmentItem) {
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

  public AiModelSpecificationUpdate constraint(List<@Valid ConstraintRef> constraint) {
    this.constraint = constraint;
    return this;
  }

  public AiModelSpecificationUpdate addConstraintItem(ConstraintRef constraintItem) {
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

  public AiModelSpecificationUpdate entitySpecRelationship(List<@Valid EntitySpecificationRelationship> entitySpecRelationship) {
    this.entitySpecRelationship = entitySpecRelationship;
    return this;
  }

  public AiModelSpecificationUpdate addEntitySpecRelationshipItem(EntitySpecificationRelationship entitySpecRelationshipItem) {
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

  public AiModelSpecificationUpdate featureSpecification(List<@Valid FeatureSpecification> featureSpecification) {
    this.featureSpecification = featureSpecification;
    return this;
  }

  public AiModelSpecificationUpdate addFeatureSpecificationItem(FeatureSpecification featureSpecificationItem) {
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

  public AiModelSpecificationUpdate relatedParty(List<@Valid RelatedParty> relatedParty) {
    this.relatedParty = relatedParty;
    return this;
  }

  public AiModelSpecificationUpdate addRelatedPartyItem(RelatedParty relatedPartyItem) {
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

  public AiModelSpecificationUpdate resourceSpecification(List<@Valid ResourceSpecificationRef> resourceSpecification) {
    this.resourceSpecification = resourceSpecification;
    return this;
  }

  public AiModelSpecificationUpdate addResourceSpecificationItem(ResourceSpecificationRef resourceSpecificationItem) {
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

  public AiModelSpecificationUpdate serviceLevelSpecification(List<@Valid ServiceLevelSpecificationRef> serviceLevelSpecification) {
    this.serviceLevelSpecification = serviceLevelSpecification;
    return this;
  }

  public AiModelSpecificationUpdate addServiceLevelSpecificationItem(ServiceLevelSpecificationRef serviceLevelSpecificationItem) {
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

  public AiModelSpecificationUpdate serviceSpecRelationship(List<@Valid ServiceSpecRelationship> serviceSpecRelationship) {
    this.serviceSpecRelationship = serviceSpecRelationship;
    return this;
  }

  public AiModelSpecificationUpdate addServiceSpecRelationshipItem(ServiceSpecRelationship serviceSpecRelationshipItem) {
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

  public AiModelSpecificationUpdate specCharacteristic(List<@Valid CharacteristicSpecification> specCharacteristic) {
    this.specCharacteristic = specCharacteristic;
    return this;
  }

  public AiModelSpecificationUpdate addSpecCharacteristicItem(CharacteristicSpecification specCharacteristicItem) {
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

  public AiModelSpecificationUpdate targetEntitySchema(@Nullable TargetEntitySchema targetEntitySchema) {
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

  public AiModelSpecificationUpdate validFor(@Nullable TimePeriod validFor) {
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

  public AiModelSpecificationUpdate atBaseType(@Nullable String atBaseType) {
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

  public AiModelSpecificationUpdate atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public AiModelSpecificationUpdate atType(@Nullable String atType) {
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
    AiModelSpecificationUpdate aiModelSpecificationUpdate = (AiModelSpecificationUpdate) o;
    return Objects.equals(this.deploymentRecord, aiModelSpecificationUpdate.deploymentRecord) &&
        Objects.equals(this.description, aiModelSpecificationUpdate.description) &&
        Objects.equals(this.inheritedModel, aiModelSpecificationUpdate.inheritedModel) &&
        Objects.equals(this.isBundle, aiModelSpecificationUpdate.isBundle) &&
        Objects.equals(this.lifecycleStatus, aiModelSpecificationUpdate.lifecycleStatus) &&
        Objects.equals(this.modelContractVersionHistory, aiModelSpecificationUpdate.modelContractVersionHistory) &&
        Objects.equals(this.modelDataSheet, aiModelSpecificationUpdate.modelDataSheet) &&
        Objects.equals(this.modelEvaluationData, aiModelSpecificationUpdate.modelEvaluationData) &&
        Objects.equals(this.modelSpecificationHistory, aiModelSpecificationUpdate.modelSpecificationHistory) &&
        Objects.equals(this.modelTrainingData, aiModelSpecificationUpdate.modelTrainingData) &&
        Objects.equals(this.name, aiModelSpecificationUpdate.name) &&
        Objects.equals(this.version, aiModelSpecificationUpdate.version) &&
        Objects.equals(this.attachment, aiModelSpecificationUpdate.attachment) &&
        Objects.equals(this.constraint, aiModelSpecificationUpdate.constraint) &&
        Objects.equals(this.entitySpecRelationship, aiModelSpecificationUpdate.entitySpecRelationship) &&
        Objects.equals(this.featureSpecification, aiModelSpecificationUpdate.featureSpecification) &&
        Objects.equals(this.relatedParty, aiModelSpecificationUpdate.relatedParty) &&
        Objects.equals(this.resourceSpecification, aiModelSpecificationUpdate.resourceSpecification) &&
        Objects.equals(this.serviceLevelSpecification, aiModelSpecificationUpdate.serviceLevelSpecification) &&
        Objects.equals(this.serviceSpecRelationship, aiModelSpecificationUpdate.serviceSpecRelationship) &&
        Objects.equals(this.specCharacteristic, aiModelSpecificationUpdate.specCharacteristic) &&
        Objects.equals(this.targetEntitySchema, aiModelSpecificationUpdate.targetEntitySchema) &&
        Objects.equals(this.validFor, aiModelSpecificationUpdate.validFor) &&
        Objects.equals(this.atBaseType, aiModelSpecificationUpdate.atBaseType) &&
        Objects.equals(this.atSchemaLocation, aiModelSpecificationUpdate.atSchemaLocation) &&
        Objects.equals(this.atType, aiModelSpecificationUpdate.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deploymentRecord, description, inheritedModel, isBundle, lifecycleStatus, modelContractVersionHistory, modelDataSheet, modelEvaluationData, modelSpecificationHistory, modelTrainingData, name, version, attachment, constraint, entitySpecRelationship, featureSpecification, relatedParty, resourceSpecification, serviceLevelSpecification, serviceSpecRelationship, specCharacteristic, targetEntitySchema, validFor, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AiModelSpecificationUpdate {\n");
    sb.append("    deploymentRecord: ").append(toIndentedString(deploymentRecord)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    inheritedModel: ").append(toIndentedString(inheritedModel)).append("\n");
    sb.append("    isBundle: ").append(toIndentedString(isBundle)).append("\n");
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

