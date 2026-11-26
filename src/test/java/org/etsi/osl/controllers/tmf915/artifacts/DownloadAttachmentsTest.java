package org.etsi.osl.controllers.tmf915.artifacts;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AttachmentRefOrValue;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DownloadAttachmentsTest {

    private static final ModelArtifact ARTIFACT = new ModelArtifact(URI.create("s3://mlflow/x"), "mlflow-model",
            List.of(new ArtifactFile("MLmodel", 10)), 10);

    @Test
    void fileName_isSafeOnAnyFileSystem() {
        assertEquals("iris-m-1.zip", DownloadAttachments.fileName("iris", "m-1"));
        assertEquals("my_model_v2-1.0.zip", DownloadAttachments.fileName("my model/v2", "1.0"));
        assertEquals("model-1.zip", DownloadAttachments.fileName("..", "1"));
        assertEquals("_passwd.zip", DownloadAttachments.fileName("../passwd", null));
        assertEquals("model.zip", DownloadAttachments.fileName(null, " "));
        assertEquals("_.zip", DownloadAttachments.fileName("\"';", null));
    }

    private static AiModelSpecification spec(String id, boolean optOut) {
        AiModelSpecification spec = ArtifactServiceTest.spec(optOut ? new String[]{"downloadable", "false"} : new String[0]);
        spec.setId(id);
        AttachmentRefOrValue other = new AttachmentRefOrValue();
        other.setName("datasheet.pdf");
        spec.setAttachment(new ArrayList<>(List.of(other, DownloadAttachments.create("iris", "1", ARTIFACT))));
        return spec;
    }

    @Test
    void complete_pointsTheDownloadAttachmentAtTheEndpoint() {
        AiModelSpecification spec = spec("abc", false);

        DownloadAttachments.complete(spec);

        assertEquals(URI.create("/tmf-api/AiM/v4/aiModelSpecification/abc/artifacts"),
                DownloadAttachments.find(spec.getAttachment()).orElseThrow().getHref());
        assertEquals(null, spec.getAttachment().get(0).getHref(), "other attachments are left alone");
    }

    @Test
    void complete_removesTheDownloadAttachment_onOptOut_only() {
        AiModelSpecification spec = spec("abc", true);

        DownloadAttachments.complete(spec);

        assertEquals(1, spec.getAttachment().size());
        assertEquals("datasheet.pdf", spec.getAttachment().get(0).getName());
    }

    @Test
    void complete_withoutAttachments_doesNothing() {
        AiModelSpecification spec = new AiModelSpecification();
        DownloadAttachments.complete(spec);
        assertTrue(spec.getAttachment().isEmpty());
    }
}
