package org.etsi.osl.controllers.tmf915.artifacts;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AttachmentRefOrValue;
import org.etsi.osl.controllers.tmf915.model.Quantity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The {@code attachment} that advertises a specification's download, the TMF-idiomatic way:
 * {@code href} is the download endpoint, {@code attachmentType} the format, {@code mimeType}
 * {@code application/zip}, {@code size} the files' total in bytes. It is told apart from
 * attachments clients add by its {@code @type}, {@value #TYPE}.
 */
public final class DownloadAttachments {

    public static final String TYPE = "ModelArtifactAttachment";
    public static final String BASE_TYPE = "Attachment";
    public static final String MIME_TYPE = "application/zip";
    public static final String BASE_PATH = "/tmf-api/AiM/v4/aiModelSpecification/";

    private DownloadAttachments() {
    }

    /** The download endpoint of a specification. */
    public static URI href(String specificationId) {
        return URI.create(BASE_PATH + specificationId + "/artifacts");
    }

    /** {@code <name>-<version>.zip}, limited to characters that are safe in any file system. */
    public static String fileName(String name, String version) {
        String base = sanitize(name == null || name.isBlank() ? "model" : name);
        if (version != null && !version.isBlank()) {
            base = base + "-" + sanitize(version);
        }
        return base + ".zip";
    }

    private static String sanitize(String s) {
        String cleaned = s.trim().replaceAll("[^A-Za-z0-9._-]+", "_");
        // No leading dots: not a hidden file, and never "." or "..".
        cleaned = cleaned.replaceAll("^\\.+", "");
        return cleaned.isEmpty() ? "model" : cleaned;
    }

    /**
     * A new download attachment. Its {@code href} is set by {@link #complete} once the
     * specification has an id.
     */
    public static AttachmentRefOrValue create(String specName, String specVersion, ModelArtifact artifact) {
        AttachmentRefOrValue attachment = new AttachmentRefOrValue();
        attachment.setAtType(TYPE);
        attachment.setAtBaseType(BASE_TYPE);
        attachment.setAttachmentType(artifact.format());
        attachment.setMimeType(MIME_TYPE);
        attachment.setName(fileName(specName, specVersion));
        attachment.setSize(new Quantity().amount((float) artifact.totalBytes()).units("bytes"));
        attachment.setDescription("Model files (format " + artifact.format() + ", " + artifact.files().size()
                + " files, " + artifact.totalBytes() + " bytes), downloaded as a zip");
        return attachment;
    }

    public static boolean isDownloadAttachment(AttachmentRefOrValue attachment) {
        return attachment != null && TYPE.equals(attachment.getAtType());
    }

    public static Optional<AttachmentRefOrValue> find(List<AttachmentRefOrValue> attachments) {
        return attachments == null ? Optional.empty()
                : attachments.stream().filter(DownloadAttachments::isDownloadAttachment).findFirst();
    }

    /**
     * Brings the download attachment in line with the specification: removes it if the
     * specification opts out ({@code downloadable=false}), otherwise points its {@code href} at
     * this specification's download endpoint. Called whenever a specification is stored.
     */
    public static void complete(AiModelSpecification specification) {
        List<AttachmentRefOrValue> attachments = specification.getAttachment();
        if (attachments == null || attachments.isEmpty()) {
            return;
        }
        if (ArtifactCharacteristics.optedOut(specification.getSpecCharacteristic())) {
            if (attachments.stream().anyMatch(DownloadAttachments::isDownloadAttachment)) {
                specification.setAttachment(new ArrayList<>(attachments.stream()
                        .filter(a -> !isDownloadAttachment(a)).toList()));
            }
            return;
        }
        if (specification.getId() != null) {
            URI href = href(specification.getId());
            attachments.stream().filter(DownloadAttachments::isDownloadAttachment).forEach(a -> a.setHref(href));
        }
    }
}
