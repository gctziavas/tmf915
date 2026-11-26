package org.etsi.osl.controllers.tmf915.artifacts;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AttachmentRefOrValue;
import org.etsi.osl.controllers.tmf915.model.Quantity;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The {@code attachment}s that advertise a specification's downloads, one per format, the
 * TMF-idiomatic way: {@code href} is the download endpoint with {@code ?format=}, 
 * {@code attachmentType} the format, {@code mimeType} {@code application/zip}, {@code size} the
 * files' total in bytes. They are told apart from attachments clients add by their
 * {@code @type}, {@value #TYPE}.
 */
public final class DownloadAttachments {

    public static final String TYPE = "ModelArtifactAttachment";
    public static final String BASE_TYPE = "Attachment";
    public static final String MIME_TYPE = "application/zip";
    public static final String BASE_PATH = "/tmf-api/AiM/v4/aiModelSpecification/";

    private DownloadAttachments() {
    }

    /** The download endpoint of a specification, for one format ({@code null}: the primary). */
    public static URI href(String specificationId, String format) {
        String href = BASE_PATH + specificationId + "/artifacts";
        if (format != null && !format.isBlank()) {
            href = href + "?format=" + URLEncoder.encode(format, StandardCharsets.UTF_8);
        }
        return URI.create(href);
    }

    /** {@code <name>-<version>.zip}, limited to characters that are safe in any file system. */
    public static String fileName(String name, String version) {
        return fileName(name, version, null);
    }

    /**
     * {@code <name>-<version>-<format>.zip}, or {@code <name>-<version>.zip} without a format;
     * limited to characters that are safe in any file system.
     */
    public static String fileName(String name, String version, String format) {
        String base = sanitize(name == null || name.isBlank() ? "model" : name);
        if (version != null && !version.isBlank()) {
            base = base + "-" + sanitize(version);
        }
        if (format != null && !format.isBlank()) {
            base = base + "-" + sanitize(format);
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
        attachment.setName(fileName(specName, specVersion, artifact.format()));
        attachment.setSize(new Quantity().amount((float) artifact.totalBytes()).units("bytes"));
        int files = artifact.files().size();
        attachment.setDescription("Model files (format " + artifact.format() + ", " + files
                + (files == 1 ? " file, " : " files, ") + artifact.totalBytes() + " bytes), downloaded as a zip");
        return attachment;
    }

    public static boolean isDownloadAttachment(AttachmentRefOrValue attachment) {
        return attachment != null && TYPE.equals(attachment.getAtType());
    }

    /** The first download attachment, if any. */
    public static Optional<AttachmentRefOrValue> find(List<AttachmentRefOrValue> attachments) {
        return findAll(attachments).stream().findFirst();
    }

    /** All download attachments, one per format. */
    public static List<AttachmentRefOrValue> findAll(List<AttachmentRefOrValue> attachments) {
        return attachments == null ? List.of()
                : attachments.stream().filter(DownloadAttachments::isDownloadAttachment).toList();
    }

    /**
     * Brings the download attachments in line with the specification: removes them if the
     * specification opts out ({@code downloadable=false}), otherwise points each {@code href} at
     * this specification's download endpoint for its format, and names it after the
     * specification. Called whenever a specification is stored or read.
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
        for (AttachmentRefOrValue a : findAll(attachments)) {
            if (specification.getId() != null) {
                a.setHref(href(specification.getId(), a.getAttachmentType()));
            }
            a.setName(fileName(specification.getName(), specification.getVersion(), a.getAttachmentType()));
        }
    }
}
