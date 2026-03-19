package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationUpdate;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("${openapi.aiManagement.base-path:/tmf-api/AiM/v4}")
public class AiModelSpecificationApiController implements AiModelSpecificationApi {

    private final AiModelSpecificationRepositoryService service;

    public AiModelSpecificationApiController(AiModelSpecificationRepositoryService service) {
        this.service = service;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<AiModelSpecification> createAiModelSpecification(AiModelSpecificationCreate aiModelSpecification) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createAiModelSpecification(aiModelSpecification));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Void> deleteAiModelSpecification(String id) {
        service.deleteAiModelSpecification(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<List<AiModelSpecification>> listAiModelSpecification(@Nullable String fields,
                                                                                @Nullable Integer offset,
                                                                                @Nullable Integer limit) {
        return ResponseEntity.ok(service.findAllAiModelSpecifications());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<AiModelSpecification> patchAiModelSpecification(String id,
                                                                           AiModelSpecificationUpdate aiModelSpecification) {
        AiModelSpecification updated = service.updateAiModelSpecification(id, aiModelSpecification);
        return ResponseEntity.ok(updated);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<AiModelSpecification> retrieveAiModelSpecification(String id, @Nullable String fields) {
        AiModelSpecification found = service.findAiModelSpecificationById(id);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(found);
    }
}

