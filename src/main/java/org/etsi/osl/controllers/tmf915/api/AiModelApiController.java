package org.etsi.osl.controllers.tmf915.api;

import java.util.List;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelUpdate;
import org.etsi.osl.controllers.tmf915.deployment.AiModelLifecycleService;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelRepositoryService;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("AiManagementApiController915")
@RequestMapping("${openapi.aiManagement.base-path:/tmf-api/AiM/v4}")
public class AiModelApiController implements AiModelApi {

    private final AiModelRepositoryService repoService;
    private final AiModelLifecycleService lifecycleService;

    public AiModelApiController(AiModelRepositoryService repoService,
                                AiModelLifecycleService lifecycleService) {
        this.repoService = repoService;
        this.lifecycleService = lifecycleService;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<AiModel> createAiModel(AiModelCreate aiModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lifecycleService.createAiModel(aiModel));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Void> deleteAiModel(String id) {
        lifecycleService.deleteAiModel(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<List<AiModel>> listAiModel(@Nullable String fields,
                                                      @Nullable Integer offset,
                                                      @Nullable Integer limit) {
        return ResponseEntity.ok(repoService.findAllAiModels());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<AiModel> patchAiModel(String id, AiModelUpdate aiModel) {
        return ResponseEntity.ok(lifecycleService.updateAiModel(id, aiModel));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<AiModel> retrieveAiModel(String id, @Nullable String fields) {
        AiModel found = repoService.findAiModelById(id);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(found);
    }
}

