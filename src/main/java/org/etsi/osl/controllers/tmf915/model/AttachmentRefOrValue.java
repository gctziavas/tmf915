package org.etsi.osl.controllers.tmf915.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.Objects;

/**
 * An attachment by value or by reference. An attachment complements the description of an element, for example through a document, a video, a picture.
 */

@Schema(name = "AttachmentRefOrValue", description = "An attachment by value or by reference. An attachment complements the description of an element, for example through a document, a video, a picture.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-18T18:56:23.275173970Z[Etc/UTC]", comments = "Generator version: 7.21.0-SNAPSHOT")
public class AttachmentRefOrValue {

  private @Nullable String id;

  private @Nullable URI href;

  private @Nullable String attachmentType;

  private @Nullable String content;

  private @Nullable String description;

  private @Nullable String mimeType;

  private @Nullable String name;

  private @Nullable URI url;

  private @Nullable Quantity size;

  private @Nullable TimePeriod validFor;

  private @Nullable String atBaseType;

  private @Nullable URI atSchemaLocation;

  private @Nullable String atType;

  private @Nullable String atReferredType;

  public AttachmentRefOrValue id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * Unique identifier for this particular attachment
   * @return id
   */
  
  @Schema(name = "id", example = "4aafacbd-11ff-4dc8-b445-305f2215715f", description = "Unique identifier for this particular attachment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(@Nullable String id) {
    this.id = id;
  }

  public AttachmentRefOrValue href(@Nullable URI href) {
    this.href = href;
    return this;
  }

  /**
   * URI for this Attachment
   * @return href
   */
  @Valid 
  @Schema(name = "href", example = "http://host/Attachment/4aafacbd-11ff-4dc8-b445-305f2215715f", description = "URI for this Attachment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("href")
  public @Nullable URI getHref() {
    return href;
  }

  @JsonProperty("href")
  public void setHref(@Nullable URI href) {
    this.href = href;
  }

  public AttachmentRefOrValue attachmentType(@Nullable String attachmentType) {
    this.attachmentType = attachmentType;
    return this;
  }

  /**
   * Attachment type such as video, picture
   * @return attachmentType
   */
  
  @Schema(name = "attachmentType", example = "video", description = "Attachment type such as video, picture", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachmentType")
  public @Nullable String getAttachmentType() {
    return attachmentType;
  }

  @JsonProperty("attachmentType")
  public void setAttachmentType(@Nullable String attachmentType) {
    this.attachmentType = attachmentType;
  }

  public AttachmentRefOrValue content(@Nullable String content) {
    this.content = content;
    return this;
  }

  /**
   * The actual contents of the attachment object, if embedded, encoded as base64
   * @return content
   */
  
  @Schema(name = "content", description = "The actual contents of the attachment object, if embedded, encoded as base64", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("content")
  public @Nullable String getContent() {
    return content;
  }

  @JsonProperty("content")
  public void setContent(@Nullable String content) {
    this.content = content;
  }

  public AttachmentRefOrValue description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * A narrative text describing the content of the attachment
   * @return description
   */
  
  @Schema(name = "description", example = "Photograph of the Product", description = "A narrative text describing the content of the attachment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public AttachmentRefOrValue mimeType(@Nullable String mimeType) {
    this.mimeType = mimeType;
    return this;
  }

  /**
   * Attachment mime type such as extension file for video, picture and document
   * @return mimeType
   */
  
  @Schema(name = "mimeType", description = "Attachment mime type such as extension file for video, picture and document", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mimeType")
  public @Nullable String getMimeType() {
    return mimeType;
  }

  @JsonProperty("mimeType")
  public void setMimeType(@Nullable String mimeType) {
    this.mimeType = mimeType;
  }

  public AttachmentRefOrValue name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * The name of the attachment
   * @return name
   */
  
  @Schema(name = "name", description = "The name of the attachment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(@Nullable String name) {
    this.name = name;
  }

  public AttachmentRefOrValue url(@Nullable URI url) {
    this.url = url;
    return this;
  }

  /**
   * Uniform Resource Locator, is a web page address (a subset of URI)
   * @return url
   */
  @Valid 
  @Schema(name = "url", example = "http://host/Content/4aafacbd-11ff-4dc8-b445-305f2215715f", description = "Uniform Resource Locator, is a web page address (a subset of URI)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("url")
  public @Nullable URI getUrl() {
    return url;
  }

  @JsonProperty("url")
  public void setUrl(@Nullable URI url) {
    this.url = url;
  }

  public AttachmentRefOrValue size(@Nullable Quantity size) {
    this.size = size;
    return this;
  }

  /**
   * Get size
   * @return size
   */
  @Valid 
  @Schema(name = "size", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("size")
  public @Nullable Quantity getSize() {
    return size;
  }

  @JsonProperty("size")
  public void setSize(@Nullable Quantity size) {
    this.size = size;
  }

  public AttachmentRefOrValue validFor(@Nullable TimePeriod validFor) {
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

  public AttachmentRefOrValue atBaseType(@Nullable String atBaseType) {
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

  public AttachmentRefOrValue atSchemaLocation(@Nullable URI atSchemaLocation) {
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

  public AttachmentRefOrValue atType(@Nullable String atType) {
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

  public AttachmentRefOrValue atReferredType(@Nullable String atReferredType) {
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
    AttachmentRefOrValue attachmentRefOrValue = (AttachmentRefOrValue) o;
    return Objects.equals(this.id, attachmentRefOrValue.id) &&
        Objects.equals(this.href, attachmentRefOrValue.href) &&
        Objects.equals(this.attachmentType, attachmentRefOrValue.attachmentType) &&
        Objects.equals(this.content, attachmentRefOrValue.content) &&
        Objects.equals(this.description, attachmentRefOrValue.description) &&
        Objects.equals(this.mimeType, attachmentRefOrValue.mimeType) &&
        Objects.equals(this.name, attachmentRefOrValue.name) &&
        Objects.equals(this.url, attachmentRefOrValue.url) &&
        Objects.equals(this.size, attachmentRefOrValue.size) &&
        Objects.equals(this.validFor, attachmentRefOrValue.validFor) &&
        Objects.equals(this.atBaseType, attachmentRefOrValue.atBaseType) &&
        Objects.equals(this.atSchemaLocation, attachmentRefOrValue.atSchemaLocation) &&
        Objects.equals(this.atType, attachmentRefOrValue.atType) &&
        Objects.equals(this.atReferredType, attachmentRefOrValue.atReferredType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, attachmentType, content, description, mimeType, name, url, size, validFor, atBaseType, atSchemaLocation, atType, atReferredType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttachmentRefOrValue {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    attachmentType: ").append(toIndentedString(attachmentType)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    validFor: ").append(toIndentedString(validFor)).append("\n");
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

