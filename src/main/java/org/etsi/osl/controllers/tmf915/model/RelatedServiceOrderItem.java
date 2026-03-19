package org.etsi.osl.controllers.tmf915.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.Objects;

/**
 * RelatedServiceOrderItem (a ServiceOrder item) .The service order item which triggered service creation/change/termination.
 */

@Schema(name = "RelatedServiceOrderItem", description = "RelatedServiceOrderItem (a ServiceOrder item) .The service order item which triggered service creation/change/termination.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class RelatedServiceOrderItem {

  private @Nullable String id;

  private @Nullable URI href;

  private @Nullable String itemId;

  private @Nullable String role;

  private @Nullable String serviceOrderHref;

  private @Nullable String serviceOrderId;

  private @Nullable OrderItemActionType itemAction;

  private @Nullable String atBaseType;

  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  private @Nullable String atReferredType;

  public RelatedServiceOrderItem id(@Nullable String id) {
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

  public RelatedServiceOrderItem href(@Nullable URI href) {
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

  public RelatedServiceOrderItem itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Identifier of the order item where the service was managed
   * @return itemId
   */
  
  @Schema(name = "itemId", description = "Identifier of the order item where the service was managed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("itemId")
  public @Nullable String getItemId() {
    return itemId;
  }

  @JsonProperty("itemId")
  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public RelatedServiceOrderItem role(@Nullable String role) {
    this.role = role;
    return this;
  }

  /**
   * role of the service order item for this service
   * @return role
   */
  
  @Schema(name = "role", description = "role of the service order item for this service", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable String getRole() {
    return role;
  }

  @JsonProperty("role")
  public void setRole(@Nullable String role) {
    this.role = role;
  }

  public RelatedServiceOrderItem serviceOrderHref(@Nullable String serviceOrderHref) {
    this.serviceOrderHref = serviceOrderHref;
    return this;
  }

  /**
   * Reference of the related entity.
   * @return serviceOrderHref
   */
  
  @Schema(name = "serviceOrderHref", description = "Reference of the related entity.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("serviceOrderHref")
  public @Nullable String getServiceOrderHref() {
    return serviceOrderHref;
  }

  @JsonProperty("serviceOrderHref")
  public void setServiceOrderHref(@Nullable String serviceOrderHref) {
    this.serviceOrderHref = serviceOrderHref;
  }

  public RelatedServiceOrderItem serviceOrderId(@Nullable String serviceOrderId) {
    this.serviceOrderId = serviceOrderId;
    return this;
  }

  /**
   * Unique identifier of a related entity.
   * @return serviceOrderId
   */
  
  @Schema(name = "serviceOrderId", description = "Unique identifier of a related entity.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("serviceOrderId")
  public @Nullable String getServiceOrderId() {
    return serviceOrderId;
  }

  @JsonProperty("serviceOrderId")
  public void setServiceOrderId(@Nullable String serviceOrderId) {
    this.serviceOrderId = serviceOrderId;
  }

  public RelatedServiceOrderItem itemAction(@Nullable OrderItemActionType itemAction) {
    this.itemAction = itemAction;
    return this;
  }

  /**
   * Get itemAction
   * @return itemAction
   */
  @Valid 
  @Schema(name = "itemAction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("itemAction")
  public @Nullable OrderItemActionType getItemAction() {
    return itemAction;
  }

  @JsonProperty("itemAction")
  public void setItemAction(@Nullable OrderItemActionType itemAction) {
    this.itemAction = itemAction;
  }

  public RelatedServiceOrderItem atBaseType(@Nullable String atBaseType) {
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

  public RelatedServiceOrderItem atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public RelatedServiceOrderItem atType(@Nullable String atType) {
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

  public RelatedServiceOrderItem atReferredType(@Nullable String atReferredType) {
    this.atReferredType = atReferredType;
    return this;
  }

  /**
   * The actual type of the target instance when needed for disambiguation.
   * @return atReferredType
   */
  
  @Schema(name = "@referredType", description = "The actual type of the target instance when needed for disambiguation.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("@referredType")
  public @Nullable String getAtReferredType() {
    return atReferredType;
  }

  @JsonProperty("@referredType")
  public void setAtReferredType(@Nullable String atReferredType) {
    this.atReferredType = atReferredType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RelatedServiceOrderItem relatedServiceOrderItem = (RelatedServiceOrderItem) o;
    return Objects.equals(this.id, relatedServiceOrderItem.id) &&
        Objects.equals(this.href, relatedServiceOrderItem.href) &&
        Objects.equals(this.itemId, relatedServiceOrderItem.itemId) &&
        Objects.equals(this.role, relatedServiceOrderItem.role) &&
        Objects.equals(this.serviceOrderHref, relatedServiceOrderItem.serviceOrderHref) &&
        Objects.equals(this.serviceOrderId, relatedServiceOrderItem.serviceOrderId) &&
        Objects.equals(this.itemAction, relatedServiceOrderItem.itemAction) &&
        Objects.equals(this.atBaseType, relatedServiceOrderItem.atBaseType) &&
        Objects.equals(this.atSchemaLocation, relatedServiceOrderItem.atSchemaLocation) &&
        Objects.equals(this.atType, relatedServiceOrderItem.atType) &&
        Objects.equals(this.atReferredType, relatedServiceOrderItem.atReferredType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, itemId, role, serviceOrderHref, serviceOrderId, itemAction, atBaseType, atSchemaLocation, atType, atReferredType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RelatedServiceOrderItem {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    serviceOrderHref: ").append(toIndentedString(serviceOrderHref)).append("\n");
    sb.append("    serviceOrderId: ").append(toIndentedString(serviceOrderId)).append("\n");
    sb.append("    itemAction: ").append(toIndentedString(itemAction)).append("\n");
    sb.append("    atBaseType: ").append(toIndentedString(atBaseType)).append("\n");
    sb.append("    atSchemaLocation: ").append(toIndentedString(atSchemaLocation)).append("\n");
    sb.append("    atType: ").append(toIndentedString(atType)).append("\n");
    sb.append("    atReferredType: ").append(toIndentedString(atReferredType)).append("\n");
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

