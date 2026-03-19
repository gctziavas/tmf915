package org.etsi.osl.controllers.tmf915.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.Characteristic;
import org.etsi.osl.controllers.tmf915.model.EntityRef;
import org.etsi.osl.controllers.tmf915.model.Feature;
import org.etsi.osl.controllers.tmf915.model.Note;
import org.etsi.osl.controllers.tmf915.model.RelatedEntityRefOrValue;
import org.etsi.osl.controllers.tmf915.model.RelatedParty;
import org.etsi.osl.controllers.tmf915.model.RelatedPlaceRefOrValue;
import org.etsi.osl.controllers.tmf915.model.RelatedServiceOrderItem;
import org.etsi.osl.controllers.tmf915.model.ResourceRef;
import org.etsi.osl.controllers.tmf915.model.ServiceRefOrValue;
import org.etsi.osl.controllers.tmf915.model.ServiceRelationship;
import org.etsi.osl.controllers.tmf915.model.ServiceSpecificationRef;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.etsi.osl.controllers.tmf915.model.SoftwareRef;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import org.etsi.osl.controllers.tmf915.mappers.converters.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AiModel is a base class for defining the AiModel hierarchy
 */

@Entity
@Table(name = "aim915_aim")
@Schema(name = "AiModel", description = "AiModel is a base class for defining the AiModel hierarchy")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class AiModel {

  @Id
  private @Nullable String id;

  @Convert(converter = UriToStringConverter.class)
  private @Nullable URI href;

  private @Nullable String category;

  private @Nullable String description;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endDate;

  private @Nullable Boolean hasStarted;

  private @Nullable Boolean isBundle;

  private @Nullable Boolean isServiceEnabled;

  private @Nullable Boolean isStateful;

  private @Nullable String name;

  private @Nullable String serviceDate;

  private @Nullable String serviceType;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startDate;

  private @Nullable String startMode;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ai_model_specification_id")
  private @Nullable AiModelSpecification aiModelSpecification;

  @Convert(converter = FeatureListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid Feature> feature = new ArrayList<>();

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "id",               column = @Column(name = "gpu_id")),
      @AttributeOverride(name = "href",             column = @Column(name = "gpu_href")),
      @AttributeOverride(name = "name",             column = @Column(name = "gpu_name")),
      @AttributeOverride(name = "atBaseType",       column = @Column(name = "gpu_base_type")),
      @AttributeOverride(name = "atSchemaLocation", column = @Column(name = "gpu_schema_loc")),
      @AttributeOverride(name = "atType",           column = @Column(name = "gpu_type")),
      @AttributeOverride(name = "atReferredType",   column = @Column(name = "gpu_referred_type"))
  })
  private @Nullable ResourceRef gpu;

  @Convert(converter = NoteListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid Note> note = new ArrayList<>();

  @Convert(converter = RelatedPlaceRefOrValueListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid RelatedPlaceRefOrValue> place = new ArrayList<>();

  @Convert(converter = RelatedEntityRefOrValueListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid RelatedEntityRefOrValue> relatedEntity = new ArrayList<>();

  @Convert(converter = RelatedPartyListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid RelatedParty> relatedParty = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "aim_id")
  @Valid
  private List<@Valid Characteristic> serviceCharacteristic = new ArrayList<>();

  @Convert(converter = RelatedServiceOrderItemListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid RelatedServiceOrderItem> serviceOrderItem = new ArrayList<>();

  @Convert(converter = ServiceRelationshipListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid ServiceRelationship> serviceRelationship = new ArrayList<>();

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "id",               column = @Column(name = "svc_spec_id")),
      @AttributeOverride(name = "href",             column = @Column(name = "svc_spec_href")),
      @AttributeOverride(name = "name",             column = @Column(name = "svc_spec_name")),
      @AttributeOverride(name = "version",          column = @Column(name = "svc_spec_version")),
      @AttributeOverride(name = "atBaseType",       column = @Column(name = "svc_spec_base_type")),
      @AttributeOverride(name = "atSchemaLocation", column = @Column(name = "svc_spec_schema_loc")),
      @AttributeOverride(name = "atType",           column = @Column(name = "svc_spec_type")),
      @AttributeOverride(name = "atReferredType",   column = @Column(name = "svc_spec_referred_type"))
  })
  private @Nullable ServiceSpecificationRef serviceSpecification;

  @Convert(converter = SoftwareRefListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid SoftwareRef> software = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  private @Nullable ServiceStateType state;

  @Convert(converter = ResourceRefListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid ResourceRef> supportingResource = new ArrayList<>();

  @Convert(converter = ServiceRefOrValueListConverter.class)
  @Column(columnDefinition = "TEXT")
  @Valid
  private List<@Valid ServiceRefOrValue> supportingService = new ArrayList<>();

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "id",               column = @Column(name = "td_id")),
      @AttributeOverride(name = "href",             column = @Column(name = "td_href")),
      @AttributeOverride(name = "name",             column = @Column(name = "td_name")),
      @AttributeOverride(name = "atBaseType",       column = @Column(name = "td_base_type")),
      @AttributeOverride(name = "atSchemaLocation", column = @Column(name = "td_schema_loc")),
      @AttributeOverride(name = "atType",           column = @Column(name = "td_type")),
      @AttributeOverride(name = "atReferredType",   column = @Column(name = "td_referred_type"))
  })
  private @Nullable EntityRef trainingData;

  private @Nullable String atBaseType;

  @Convert(converter = UriToStringConverter.class)
  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  public AiModel id(@Nullable String id) {
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

  public AiModel href(@Nullable URI href) {
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

  public AiModel category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Is it a customer facing or resource facing service
   * @return category
   */
  
  @Schema(name = "category", description = "Is it a customer facing or resource facing service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  @JsonProperty("category")
  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public AiModel description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Free-text description of the service
   * @return description
   */
  
  @Schema(name = "description", description = "Free-text description of the service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public AiModel endDate(@Nullable OffsetDateTime endDate) {
    this.endDate = endDate;
    return this;
  }

  /**
   * Date when the service ends
   * @return endDate
   */
  @Valid 
  @Schema(name = "endDate", description = "Date when the service ends", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endDate")
  public @Nullable OffsetDateTime getEndDate() {
    return endDate;
  }

  @JsonProperty("endDate")
  public void setEndDate(@Nullable OffsetDateTime endDate) {
    this.endDate = endDate;
  }

  public AiModel hasStarted(@Nullable Boolean hasStarted) {
    this.hasStarted = hasStarted;
    return this;
  }

  /**
   * If TRUE, this Service has already been started
   * @return hasStarted
   */
  
  @Schema(name = "hasStarted", description = "If TRUE, this Service has already been started", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hasStarted")
  public @Nullable Boolean getHasStarted() {
    return hasStarted;
  }

  @JsonProperty("hasStarted")
  public void setHasStarted(@Nullable Boolean hasStarted) {
    this.hasStarted = hasStarted;
  }

  public AiModel isBundle(@Nullable Boolean isBundle) {
    this.isBundle = isBundle;
    return this;
  }

  /**
   * If true, the service is a ServiceBundle which regroup a service hierachy. If false, the service is a 'atomic' service (hierachy leaf).
   * @return isBundle
   */
  
  @Schema(name = "isBundle", description = "If true, the service is a ServiceBundle which regroup a service hierachy. If false, the service is a 'atomic' service (hierachy leaf).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isBundle")
  public @Nullable Boolean getIsBundle() {
    return isBundle;
  }

  @JsonProperty("isBundle")
  public void setIsBundle(@Nullable Boolean isBundle) {
    this.isBundle = isBundle;
  }

  public AiModel isServiceEnabled(@Nullable Boolean isServiceEnabled) {
    this.isServiceEnabled = isServiceEnabled;
    return this;
  }

  /**
   * If FALSE and hasStarted is FALSE, this particular Service has NOT been enabled for use - if FALSE and hasStarted is TRUE then the service has failed 
   * @return isServiceEnabled
   */
  
  @Schema(name = "isServiceEnabled", description = "If FALSE and hasStarted is FALSE, this particular Service has NOT been enabled for use - if FALSE and hasStarted is TRUE then the service has failed ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isServiceEnabled")
  public @Nullable Boolean getIsServiceEnabled() {
    return isServiceEnabled;
  }

  @JsonProperty("isServiceEnabled")
  public void setIsServiceEnabled(@Nullable Boolean isServiceEnabled) {
    this.isServiceEnabled = isServiceEnabled;
  }

  public AiModel isStateful(@Nullable Boolean isStateful) {
    this.isStateful = isStateful;
    return this;
  }

  /**
   * If TRUE, this Service can be changed without affecting any other services
   * @return isStateful
   */
  
  @Schema(name = "isStateful", description = "If TRUE, this Service can be changed without affecting any other services", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isStateful")
  public @Nullable Boolean getIsStateful() {
    return isStateful;
  }

  @JsonProperty("isStateful")
  public void setIsStateful(@Nullable Boolean isStateful) {
    this.isStateful = isStateful;
  }

  public AiModel name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Name of the service
   * @return name
   */
  
  @Schema(name = "name", description = "Name of the service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(@Nullable String name) {
    this.name = name;
  }

  public AiModel serviceDate(@Nullable String serviceDate) {
    this.serviceDate = serviceDate;
    return this;
  }

  /**
   * Date when the service was created (whatever its status).
   * @return serviceDate
   */
  
  @Schema(name = "serviceDate", description = "Date when the service was created (whatever its status).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("serviceDate")
  public @Nullable String getServiceDate() {
    return serviceDate;
  }

  @JsonProperty("serviceDate")
  public void setServiceDate(@Nullable String serviceDate) {
    this.serviceDate = serviceDate;
  }

  public AiModel serviceType(@Nullable String serviceType) {
    this.serviceType = serviceType;
    return this;
  }

  /**
   * Business type of the service
   * @return serviceType
   */
  
  @Schema(name = "serviceType", description = "Business type of the service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("serviceType")
  public @Nullable String getServiceType() {
    return serviceType;
  }

  @JsonProperty("serviceType")
  public void setServiceType(@Nullable String serviceType) {
    this.serviceType = serviceType;
  }

  public AiModel startDate(@Nullable OffsetDateTime startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Date when the service starts
   * @return startDate
   */
  @Valid 
  @Schema(name = "startDate", description = "Date when the service starts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startDate")
  public @Nullable OffsetDateTime getStartDate() {
    return startDate;
  }

  @JsonProperty("startDate")
  public void setStartDate(@Nullable OffsetDateTime startDate) {
    this.startDate = startDate;
  }

  public AiModel startMode(@Nullable String startMode) {
    this.startMode = startMode;
    return this;
  }

  /**
   * This attribute is an enumerated integer that indicates how the Service is started, such as: 0: Unknown; 1: Automatically by the managed environment; 2: Automatically by the owning device; 3: Manually by the Provider of the Service; 4: Manually by a Customer of the Provider; 5: Any of the above
   * @return startMode
   */
  
  @Schema(name = "startMode", description = "This attribute is an enumerated integer that indicates how the Service is started, such as: 0: Unknown; 1: Automatically by the managed environment; 2: Automatically by the owning device; 3: Manually by the Provider of the Service; 4: Manually by a Customer of the Provider; 5: Any of the above", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startMode")
  public @Nullable String getStartMode() {
    return startMode;
  }

  @JsonProperty("startMode")
  public void setStartMode(@Nullable String startMode) {
    this.startMode = startMode;
  }

  public AiModel aiModelSpecification(@Nullable AiModelSpecification aiModelSpecification) {
    this.aiModelSpecification = aiModelSpecification;
    return this;
  }

  /**
   * Get aiModelSpecification
   * @return aiModelSpecification
   */
  @Valid 
  @Schema(name = "aiModelSpecification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aiModelSpecification")
  public @Nullable AiModelSpecification getAiModelSpecification() {
    return aiModelSpecification;
  }

  @JsonProperty("aiModelSpecification")
  public void setAiModelSpecification(@Nullable AiModelSpecification aiModelSpecification) {
    this.aiModelSpecification = aiModelSpecification;
  }

  public AiModel feature(List<@Valid Feature> feature) {
    this.feature = feature;
    return this;
  }

  public AiModel addFeatureItem(Feature featureItem) {
    if (this.feature == null) {
      this.feature = new ArrayList<>();
    }
    this.feature.add(featureItem);
    return this;
  }

  /**
   * A list of feature associated with this service 
   * @return feature
   */
  @Valid 
  @Schema(name = "feature", description = "A list of feature associated with this service ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("feature")
  public List<@Valid Feature> getFeature() {
    return feature;
  }

  @JsonProperty("feature")
  public void setFeature(List<@Valid Feature> feature) {
    this.feature = feature;
  }

  public AiModel gpu(@Nullable ResourceRef gpu) {
    this.gpu = gpu;
    return this;
  }

  /**
   * Get gpu
   * @return gpu
   */
  @Valid 
  @Schema(name = "gpu", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gpu")
  public @Nullable ResourceRef getGpu() {
    return gpu;
  }

  @JsonProperty("gpu")
  public void setGpu(@Nullable ResourceRef gpu) {
    this.gpu = gpu;
  }

  public AiModel note(List<@Valid Note> note) {
    this.note = note;
    return this;
  }

  public AiModel addNoteItem(Note noteItem) {
    if (this.note == null) {
      this.note = new ArrayList<>();
    }
    this.note.add(noteItem);
    return this;
  }

  /**
   * A list of notes made on this service
   * @return note
   */
  @Valid 
  @Schema(name = "note", description = "A list of notes made on this service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("note")
  public List<@Valid Note> getNote() {
    return note;
  }

  @JsonProperty("note")
  public void setNote(List<@Valid Note> note) {
    this.note = note;
  }

  public AiModel place(List<@Valid RelatedPlaceRefOrValue> place) {
    this.place = place;
    return this;
  }

  public AiModel addPlaceItem(RelatedPlaceRefOrValue placeItem) {
    if (this.place == null) {
      this.place = new ArrayList<>();
    }
    this.place.add(placeItem);
    return this;
  }

  /**
   * A list of places (Place [*]). Used to define a place useful for the service (for example a geographical place whre the service is installed)
   * @return place
   */
  @Valid 
  @Schema(name = "place", description = "A list of places (Place [*]). Used to define a place useful for the service (for example a geographical place whre the service is installed)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("place")
  public List<@Valid RelatedPlaceRefOrValue> getPlace() {
    return place;
  }

  @JsonProperty("place")
  public void setPlace(List<@Valid RelatedPlaceRefOrValue> place) {
    this.place = place;
  }

  public AiModel relatedEntity(List<@Valid RelatedEntityRefOrValue> relatedEntity) {
    this.relatedEntity = relatedEntity;
    return this;
  }

  public AiModel addRelatedEntityItem(RelatedEntityRefOrValue relatedEntityItem) {
    if (this.relatedEntity == null) {
      this.relatedEntity = new ArrayList<>();
    }
    this.relatedEntity.add(relatedEntityItem);
    return this;
  }

  /**
   * A list of related  entity in relationship with this service 
   * @return relatedEntity
   */
  @Valid 
  @Schema(name = "relatedEntity", description = "A list of related  entity in relationship with this service ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relatedEntity")
  public List<@Valid RelatedEntityRefOrValue> getRelatedEntity() {
    return relatedEntity;
  }

  @JsonProperty("relatedEntity")
  public void setRelatedEntity(List<@Valid RelatedEntityRefOrValue> relatedEntity) {
    this.relatedEntity = relatedEntity;
  }

  public AiModel relatedParty(List<@Valid RelatedParty> relatedParty) {
    this.relatedParty = relatedParty;
    return this;
  }

  public AiModel addRelatedPartyItem(RelatedParty relatedPartyItem) {
    if (this.relatedParty == null) {
      this.relatedParty = new ArrayList<>();
    }
    this.relatedParty.add(relatedPartyItem);
    return this;
  }

  /**
   * A list of related party references (RelatedParty [*]). A related party defines party or party role linked to a specific entity
   * @return relatedParty
   */
  @Valid 
  @Schema(name = "relatedParty", description = "A list of related party references (RelatedParty [*]). A related party defines party or party role linked to a specific entity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relatedParty")
  public List<@Valid RelatedParty> getRelatedParty() {
    return relatedParty;
  }

  @JsonProperty("relatedParty")
  public void setRelatedParty(List<@Valid RelatedParty> relatedParty) {
    this.relatedParty = relatedParty;
  }

  public AiModel serviceCharacteristic(List<@Valid Characteristic> serviceCharacteristic) {
    this.serviceCharacteristic = serviceCharacteristic;
    return this;
  }

  public AiModel addServiceCharacteristicItem(Characteristic serviceCharacteristicItem) {
    if (this.serviceCharacteristic == null) {
      this.serviceCharacteristic = new ArrayList<>();
    }
    this.serviceCharacteristic.add(serviceCharacteristicItem);
    return this;
  }

  /**
   * A list of characteristics that characterize this service (ServiceCharacteristic [*]) 
   * @return serviceCharacteristic
   */
  @Valid 
  @Schema(name = "serviceCharacteristic", description = "A list of characteristics that characterize this service (ServiceCharacteristic [*]) ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("serviceCharacteristic")
  public List<@Valid Characteristic> getServiceCharacteristic() {
    return serviceCharacteristic;
  }

  @JsonProperty("serviceCharacteristic")
  public void setServiceCharacteristic(List<@Valid Characteristic> serviceCharacteristic) {
    this.serviceCharacteristic = serviceCharacteristic;
  }

  public AiModel serviceOrderItem(List<@Valid RelatedServiceOrderItem> serviceOrderItem) {
    this.serviceOrderItem = serviceOrderItem;
    return this;
  }

  public AiModel addServiceOrderItemItem(RelatedServiceOrderItem serviceOrderItemItem) {
    if (this.serviceOrderItem == null) {
      this.serviceOrderItem = new ArrayList<>();
    }
    this.serviceOrderItem.add(serviceOrderItemItem);
    return this;
  }

  /**
   * A list of service order items related to this service
   * @return serviceOrderItem
   */
  @Valid 
  @Schema(name = "serviceOrderItem", description = "A list of service order items related to this service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("serviceOrderItem")
  public List<@Valid RelatedServiceOrderItem> getServiceOrderItem() {
    return serviceOrderItem;
  }

  @JsonProperty("serviceOrderItem")
  public void setServiceOrderItem(List<@Valid RelatedServiceOrderItem> serviceOrderItem) {
    this.serviceOrderItem = serviceOrderItem;
  }

  public AiModel serviceRelationship(List<@Valid ServiceRelationship> serviceRelationship) {
    this.serviceRelationship = serviceRelationship;
    return this;
  }

  public AiModel addServiceRelationshipItem(ServiceRelationship serviceRelationshipItem) {
    if (this.serviceRelationship == null) {
      this.serviceRelationship = new ArrayList<>();
    }
    this.serviceRelationship.add(serviceRelationshipItem);
    return this;
  }

  /**
   * A list of service relationships (ServiceRelationship [*]). Describes links with other service(s) in the inventory.
   * @return serviceRelationship
   */
  @Valid 
  @Schema(name = "serviceRelationship", description = "A list of service relationships (ServiceRelationship [*]). Describes links with other service(s) in the inventory.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("serviceRelationship")
  public List<@Valid ServiceRelationship> getServiceRelationship() {
    return serviceRelationship;
  }

  @JsonProperty("serviceRelationship")
  public void setServiceRelationship(List<@Valid ServiceRelationship> serviceRelationship) {
    this.serviceRelationship = serviceRelationship;
  }

  public AiModel serviceSpecification(@Nullable ServiceSpecificationRef serviceSpecification) {
    this.serviceSpecification = serviceSpecification;
    return this;
  }

  /**
   * Get serviceSpecification
   * @return serviceSpecification
   */
  @Valid 
  @Schema(name = "serviceSpecification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("serviceSpecification")
  public @Nullable ServiceSpecificationRef getServiceSpecification() {
    return serviceSpecification;
  }

  @JsonProperty("serviceSpecification")
  public void setServiceSpecification(@Nullable ServiceSpecificationRef serviceSpecification) {
    this.serviceSpecification = serviceSpecification;
  }

  public AiModel software(List<@Valid SoftwareRef> software) {
    this.software = software;
    return this;
  }

  public AiModel addSoftwareItem(SoftwareRef softwareItem) {
    if (this.software == null) {
      this.software = new ArrayList<>();
    }
    this.software.add(softwareItem);
    return this;
  }

  /**
   * Reference to Software in Inventory
   * @return software
   */
  @Valid 
  @Schema(name = "software", description = "Reference to Software in Inventory", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("software")
  public List<@Valid SoftwareRef> getSoftware() {
    return software;
  }

  @JsonProperty("software")
  public void setSoftware(List<@Valid SoftwareRef> software) {
    this.software = software;
  }

  public AiModel state(@Nullable ServiceStateType state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
   */
  @Valid 
  @Schema(name = "state", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  public @Nullable ServiceStateType getState() {
    return state;
  }

  @JsonProperty("state")
  public void setState(@Nullable ServiceStateType state) {
    this.state = state;
  }

  public AiModel supportingResource(List<@Valid ResourceRef> supportingResource) {
    this.supportingResource = supportingResource;
    return this;
  }

  public AiModel addSupportingResourceItem(ResourceRef supportingResourceItem) {
    if (this.supportingResource == null) {
      this.supportingResource = new ArrayList<>();
    }
    this.supportingResource.add(supportingResourceItem);
    return this;
  }

  /**
   * A list of supporting resources (SupportingResource [*]).Note: only Service of type RFS can be associated with Resources
   * @return supportingResource
   */
  @Valid 
  @Schema(name = "supportingResource", description = "A list of supporting resources (SupportingResource [*]).Note: only Service of type RFS can be associated with Resources", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("supportingResource")
  public List<@Valid ResourceRef> getSupportingResource() {
    return supportingResource;
  }

  @JsonProperty("supportingResource")
  public void setSupportingResource(List<@Valid ResourceRef> supportingResource) {
    this.supportingResource = supportingResource;
  }

  public AiModel supportingService(List<@Valid ServiceRefOrValue> supportingService) {
    this.supportingService = supportingService;
    return this;
  }

  public AiModel addSupportingServiceItem(ServiceRefOrValue supportingServiceItem) {
    if (this.supportingService == null) {
      this.supportingService = new ArrayList<>();
    }
    this.supportingService.add(supportingServiceItem);
    return this;
  }

  /**
   * A list of supporting services (SupportingService [*]). A collection of services that support this service (bundling, link CFS to RFS)
   * @return supportingService
   */
  @Valid 
  @Schema(name = "supportingService", description = "A list of supporting services (SupportingService [*]). A collection of services that support this service (bundling, link CFS to RFS)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("supportingService")
  public List<@Valid ServiceRefOrValue> getSupportingService() {
    return supportingService;
  }

  @JsonProperty("supportingService")
  public void setSupportingService(List<@Valid ServiceRefOrValue> supportingService) {
    this.supportingService = supportingService;
  }

  public AiModel trainingData(@Nullable EntityRef trainingData) {
    this.trainingData = trainingData;
    return this;
  }

  /**
   * Get trainingData
   * @return trainingData
   */
  @Valid 
  @Schema(name = "trainingData", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trainingData")
  public @Nullable EntityRef getTrainingData() {
    return trainingData;
  }

  @JsonProperty("trainingData")
  public void setTrainingData(@Nullable EntityRef trainingData) {
    this.trainingData = trainingData;
  }

  public AiModel atBaseType(@Nullable String atBaseType) {
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

  public AiModel atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public AiModel atType(@Nullable String atType) {
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
    AiModel aiModel = (AiModel) o;
    return Objects.equals(this.id, aiModel.id) &&
        Objects.equals(this.href, aiModel.href) &&
        Objects.equals(this.category, aiModel.category) &&
        Objects.equals(this.description, aiModel.description) &&
        Objects.equals(this.endDate, aiModel.endDate) &&
        Objects.equals(this.hasStarted, aiModel.hasStarted) &&
        Objects.equals(this.isBundle, aiModel.isBundle) &&
        Objects.equals(this.isServiceEnabled, aiModel.isServiceEnabled) &&
        Objects.equals(this.isStateful, aiModel.isStateful) &&
        Objects.equals(this.name, aiModel.name) &&
        Objects.equals(this.serviceDate, aiModel.serviceDate) &&
        Objects.equals(this.serviceType, aiModel.serviceType) &&
        Objects.equals(this.startDate, aiModel.startDate) &&
        Objects.equals(this.startMode, aiModel.startMode) &&
        Objects.equals(this.aiModelSpecification, aiModel.aiModelSpecification) &&
        Objects.equals(this.feature, aiModel.feature) &&
        Objects.equals(this.gpu, aiModel.gpu) &&
        Objects.equals(this.note, aiModel.note) &&
        Objects.equals(this.place, aiModel.place) &&
        Objects.equals(this.relatedEntity, aiModel.relatedEntity) &&
        Objects.equals(this.relatedParty, aiModel.relatedParty) &&
        Objects.equals(this.serviceCharacteristic, aiModel.serviceCharacteristic) &&
        Objects.equals(this.serviceOrderItem, aiModel.serviceOrderItem) &&
        Objects.equals(this.serviceRelationship, aiModel.serviceRelationship) &&
        Objects.equals(this.serviceSpecification, aiModel.serviceSpecification) &&
        Objects.equals(this.software, aiModel.software) &&
        Objects.equals(this.state, aiModel.state) &&
        Objects.equals(this.supportingResource, aiModel.supportingResource) &&
        Objects.equals(this.supportingService, aiModel.supportingService) &&
        Objects.equals(this.trainingData, aiModel.trainingData) &&
        Objects.equals(this.atBaseType, aiModel.atBaseType) &&
        Objects.equals(this.atSchemaLocation, aiModel.atSchemaLocation) &&
        Objects.equals(this.atType, aiModel.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, category, description, endDate, hasStarted, isBundle, isServiceEnabled, isStateful, name, serviceDate, serviceType, startDate, startMode, aiModelSpecification, feature, gpu, note, place, relatedEntity, relatedParty, serviceCharacteristic, serviceOrderItem, serviceRelationship, serviceSpecification, software, state, supportingResource, supportingService, trainingData, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AiModel {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    hasStarted: ").append(toIndentedString(hasStarted)).append("\n");
    sb.append("    isBundle: ").append(toIndentedString(isBundle)).append("\n");
    sb.append("    isServiceEnabled: ").append(toIndentedString(isServiceEnabled)).append("\n");
    sb.append("    isStateful: ").append(toIndentedString(isStateful)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    serviceDate: ").append(toIndentedString(serviceDate)).append("\n");
    sb.append("    serviceType: ").append(toIndentedString(serviceType)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    startMode: ").append(toIndentedString(startMode)).append("\n");
    sb.append("    aiModelSpecification: ").append(toIndentedString(aiModelSpecification)).append("\n");
    sb.append("    feature: ").append(toIndentedString(feature)).append("\n");
    sb.append("    gpu: ").append(toIndentedString(gpu)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
    sb.append("    place: ").append(toIndentedString(place)).append("\n");
    sb.append("    relatedEntity: ").append(toIndentedString(relatedEntity)).append("\n");
    sb.append("    relatedParty: ").append(toIndentedString(relatedParty)).append("\n");
    sb.append("    serviceCharacteristic: ").append(toIndentedString(serviceCharacteristic)).append("\n");
    sb.append("    serviceOrderItem: ").append(toIndentedString(serviceOrderItem)).append("\n");
    sb.append("    serviceRelationship: ").append(toIndentedString(serviceRelationship)).append("\n");
    sb.append("    serviceSpecification: ").append(toIndentedString(serviceSpecification)).append("\n");
    sb.append("    software: ").append(toIndentedString(software)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    supportingResource: ").append(toIndentedString(supportingResource)).append("\n");
    sb.append("    supportingService: ").append(toIndentedString(supportingService)).append("\n");
    sb.append("    trainingData: ").append(toIndentedString(trainingData)).append("\n");
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

