package org.etsi.osl.controllers.tmf915.mcp;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AttachmentRefOrValue;
import org.etsi.osl.controllers.tmf915.model.Quantity;
import org.etsi.osl.controllers.tmf915.model.ServiceSpecificationRef;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelRepositoryService;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiCatalogMcpToolsUnitTest {

    @Mock
    private AiModelRepositoryService aiModelRepositoryService;

    @Mock
    private AiModelSpecificationRepositoryService specificationRepositoryService;

    @InjectMocks
    private AiCatalogMcpTools tools;

    @Test
    void getAiModel_looksUpById_withoutListing() {
        AiModel model = new AiModel();
        model.setId("m1");
        model.setName("model-1");
        model.setState(ServiceStateType.ACTIVE);
        ServiceSpecificationRef ref = new ServiceSpecificationRef("spec-1");
        ref.setVersion("3");
        model.setServiceSpecification(ref);
        when(aiModelRepositoryService.findAiModelById("m1")).thenReturn(model);

        AiCatalogMcpTools.AiModelSummary summary = tools.getAiModel("m1");

        assertEquals("m1", summary.id());
        assertEquals("model-1", summary.name());
        assertEquals("active", summary.state());
        assertEquals("spec-1", summary.aiModelSpecificationId());
        assertEquals("3", summary.version());
        verify(aiModelRepositoryService, never()).findAllAiModels();
    }

    @Test
    void getAiModel_unknownOrBlankId_returnsNull() {
        when(aiModelRepositoryService.findAiModelById("missing")).thenReturn(null);
        assertNull(tools.getAiModel("missing"));
        assertNull(tools.getAiModel(" "));
        verify(aiModelRepositoryService, never()).findAllAiModels();
    }

    @Test
    void getAiModelSpecification_looksUpById_withoutPaging() {
        AiModelSpecification spec = new AiModelSpecification();
        spec.setId("spec-1");
        spec.setName("fraud");
        spec.setVersion("3");
        spec.setLifecycleStatus("Active");
        when(specificationRepositoryService.findAiModelSpecificationById("spec-1")).thenReturn(spec);

        AiCatalogMcpTools.AiModelSpecificationSummary summary = tools.getAiModelSpecification("spec-1");

        assertEquals("spec-1", summary.id());
        assertEquals("fraud", summary.name());
        assertEquals("3", summary.version());
        assertEquals("Active", summary.lifecycleStatus());
        verify(specificationRepositoryService, never()).findAllAiModelSpecifications(any(), any());
    }

    @Test
    void getAiModelSpecification_unknownOrBlankId_returnsNull() {
        when(specificationRepositoryService.findAiModelSpecificationById("missing")).thenReturn(null);
        assertNull(tools.getAiModelSpecification("missing"));
        assertNull(tools.getAiModelSpecification(""));
        verify(specificationRepositoryService, never()).findAllAiModelSpecifications(anyInt(), anyInt());
    }

    private static AttachmentRefOrValue attachment(String type, String format, String href, float size) {
        AttachmentRefOrValue a = new AttachmentRefOrValue();
        a.setAtType(type);
        a.setAttachmentType(format);
        a.setHref(URI.create(href));
        a.setSize(new Quantity().amount(size).units("bytes"));
        return a;
    }

    /** Download formats, with link and size; a client's own attachment is not one. */
    @Test
    void getAiModelSpecification_listsTheDownloadFormats() {
        AiModelSpecification spec = new AiModelSpecification();
        spec.setId("spec-1");
        String base = "/tmf-api/AiM/v4/aiModelSpecification/spec-1/artifacts?format=";
        spec.setAttachment(new java.util.ArrayList<>(List.of(
                attachment("ModelArtifactAttachment", "mlflow-model", base + "mlflow-model", 259846f),
                attachment("Attachment", "datasheet", "https://example.com/sheet.pdf", 10f),
                attachment("ModelArtifactAttachment", "onnx", base + "onnx", 80115f))));
        when(specificationRepositoryService.findAiModelSpecificationById("spec-1")).thenReturn(spec);

        AiCatalogMcpTools.AiModelSpecificationSummary summary = tools.getAiModelSpecification("spec-1");

        assertEquals(List.of(
                new AiCatalogMcpTools.DownloadSummary("mlflow-model", URI.create(base + "mlflow-model"), 259846L),
                new AiCatalogMcpTools.DownloadSummary("onnx", URI.create(base + "onnx"), 80115L)),
                summary.downloads());
    }

    @Test
    void listAvailableAiModelSpecifications_notDownloadable_hasNoDownloads() {
        AiModelSpecification spec = new AiModelSpecification();
        spec.setId("spec-2");
        when(specificationRepositoryService.findAllAiModelSpecifications(0, 50)).thenReturn(List.of(spec));

        List<AiCatalogMcpTools.AiModelSpecificationSummary> summaries = tools.listAvailableAiModelSpecifications(null, null);

        assertEquals(List.of(), summaries.get(0).downloads());
    }
}
