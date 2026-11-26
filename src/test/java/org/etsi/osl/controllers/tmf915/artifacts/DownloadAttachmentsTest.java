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

        assertEquals(URI.create("/tmf-api/AiM/v4/aiModelSpecification/abc/artifacts?format=mlflow-model"),
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

    @Test
    void fileName_withFormat() {
        assertEquals("iris-m-1-onnx.zip", DownloadAttachments.fileName("iris", "m-1", "onnx"));
        assertEquals("iris-m-1.zip", DownloadAttachments.fileName("iris", "m-1", null));
    }

    @Test
    void complete_givesEachFormatItsOwnHrefAndName() {
        AiModelSpecification spec = spec("abc", false);
        spec.setName("iris v2");
        spec.setVersion("m-1");
        ModelArtifact onnx = new ModelArtifact(URI.create("s3://mlflow/x/extra_files"), "onnx",
                List.of(new ArtifactFile("model.onnx", 4)), 4);
        spec.getAttachment().add(DownloadAttachments.create("old name", "1", onnx));

        DownloadAttachments.complete(spec);

        List<AttachmentRefOrValue> downloads = DownloadAttachments.findAll(spec.getAttachment());
        assertEquals(2, downloads.size());
        assertEquals(URI.create("/tmf-api/AiM/v4/aiModelSpecification/abc/artifacts?format=mlflow-model"), downloads.get(0).getHref());
        assertEquals("iris_v2-m-1-mlflow-model.zip", downloads.get(0).getName());
        assertEquals(URI.create("/tmf-api/AiM/v4/aiModelSpecification/abc/artifacts?format=onnx"), downloads.get(1).getHref());
        assertEquals("iris_v2-m-1-onnx.zip", downloads.get(1).getName());
    }

    @Test
    void complete_onOptOut_removesEveryFormat() {
        AiModelSpecification spec = spec("abc", true);
        spec.getAttachment().add(DownloadAttachments.create("iris", "1", new ModelArtifact(URI.create("s3://mlflow/x"),
                "onnx", List.of(new ArtifactFile("model.onnx", 4)), 4)));

        DownloadAttachments.complete(spec);

        assertTrue(DownloadAttachments.findAll(spec.getAttachment()).isEmpty());
        assertEquals(1, spec.getAttachment().size());
    }
}
