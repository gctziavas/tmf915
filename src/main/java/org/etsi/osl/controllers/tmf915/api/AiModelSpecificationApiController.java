package org.etsi.osl.controllers.tmf915.api;

import java.util.List;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationUpdate;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${openapi.aiManagement.base-path:/tmf-api/AiM/v4}")
public class AiModelSpecificationApiController implements AiModelSpecificationApi {

    private final AiModelSpecificationRepositoryService service;

    public AiModelSpecificationApiController(AiModelSpecificationRepositoryService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<AiModelSpecification> createAiModelSpecification(AiModelSpecificationCreate aiModelSpecification) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createAiModelSpecification(aiModelSpecification));
    }

    @Override
    public ResponseEntity<Void> deleteAiModelSpecification(String id) {
        service.deleteAiModelSpecification(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<AiModelSpecification>> listAiModelSpecification(@Nullable String fields,
                                                                                @Nullable Integer offset,
                                                                                @Nullable Integer limit) {
        return ResponseEntity.ok(service.findAllAiModelSpecifications());
    }

    @Override
    public ResponseEntity<AiModelSpecification> patchAiModelSpecification(String id,
                                                                           AiModelSpecificationUpdate aiModelSpecification) {
        AiModelSpecification updated = service.updateAiModelSpecification(id, aiModelSpecification);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<AiModelSpecification> retrieveAiModelSpecification(String id, @Nullable String fields) {
        AiModelSpecification found = service.findAiModelSpecificationById(id);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(found);
    }
}

