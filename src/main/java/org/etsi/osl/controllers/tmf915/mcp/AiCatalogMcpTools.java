package org.etsi.osl.controllers.tmf915.mcp;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelRepositoryService;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Component
public class AiCatalogMcpTools {

    private static final int DEFAULT_LIMIT = 50;

    private final AiModelRepositoryService aiModelRepositoryService;
    private final AiModelSpecificationRepositoryService specificationRepositoryService;

    public AiCatalogMcpTools(AiModelRepositoryService aiModelRepositoryService,
                             AiModelSpecificationRepositoryService specificationRepositoryService) {
        this.aiModelRepositoryService = aiModelRepositoryService;
        this.specificationRepositoryService = specificationRepositoryService;
    }

    @Tool(description = "List available AI model specifications")
    public List<AiModelSpecificationSummary> listAvailableAiModelSpecifications(
            @ToolParam(description = "Zero-based offset") Integer offset,
            @ToolParam(description = "Page size limit") Integer limit) {
        int safeOffset = offset != null && offset >= 0 ? offset : 0;
        int safeLimit = limit != null && limit > 0 ? limit : DEFAULT_LIMIT;

        return specificationRepositoryService.findAllAiModelSpecifications(safeOffset, safeLimit).stream()
                .map(AiCatalogMcpTools::toSummary)
                .toList();
    }

    @Tool(description = "Get details of an available AI model specification by id")
    public AiModelSpecificationSummary getAiModelSpecification(
            @ToolParam(description = "AI model specification id") String specificationId) {
        if (specificationId == null || specificationId.isBlank()) {
            return null;
        }
        AiModelSpecification spec = specificationRepositoryService.findAiModelSpecificationById(specificationId);
        return spec != null ? toSummary(spec) : null;
    }

    @Tool(description = "List available AI models")
    public List<AiModelSummary> listAvailableAiModels(
            @ToolParam(description = "Optional prefix for AI model name") String namePrefix) {
        List<AiModel> models = (namePrefix == null || namePrefix.isBlank())
                ? aiModelRepositoryService.findAllAiModels()
                : aiModelRepositoryService.findByNameStartingWith(namePrefix);

        return models.stream()
                .map(AiCatalogMcpTools::toSummary)
                .toList();
    }

    @Tool(description = "Get details of an available AI model by id")
    public AiModelSummary getAiModel(@ToolParam(description = "AI model id") String modelId) {
        if (modelId == null || modelId.isBlank()) {
            return null;
        }
        AiModel model = aiModelRepositoryService.findAiModelById(modelId);
        return model != null ? toSummary(model) : null;
    }

    private static AiModelSpecificationSummary toSummary(AiModelSpecification spec) {
        return new AiModelSpecificationSummary(
                spec.getId(),
                spec.getName(),
                spec.getVersion(),
                spec.getLifecycleStatus(),
                spec.getHref());
    }

    private static AiModelSummary toSummary(AiModel model) {
        return new AiModelSummary(
                model.getId(),
                model.getName(),
                model.getState() != null ? model.getState().getValue() : null,
                model.getServiceSpecification() != null ? model.getServiceSpecification().getVersion() : null,
                model.getServiceSpecification() != null ? model.getServiceSpecification().getId() : null,
                model.getHref());
    }

    public record AiModelSpecificationSummary(
            String id,
            String name,
            String version,
            String lifecycleStatus,
            URI href) {
    }

    public record AiModelSummary(
            String id,
            String name,
            String state,
            String version,
            String aiModelSpecificationId,
            URI href) {
    }
}
