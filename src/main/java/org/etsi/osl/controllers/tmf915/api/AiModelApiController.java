package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelUpdate;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelRepositoryService;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("${openapi.aiManagement.base-path:/tmf-api/AiM/v4}")
public class AiModelApiController implements AiModelApi {

    private final AiModelRepositoryService service;

    public AiModelApiController(AiModelRepositoryService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<AiModel> createAiModel(AiModelCreate aiModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createAiModel(aiModel));
    }

    @Override
    public ResponseEntity<Void> deleteAiModel(String id) {
        service.deleteAiModel(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<AiModel>> listAiModel(@Nullable String fields,
                                                      @Nullable Integer offset,
                                                      @Nullable Integer limit) {
        return ResponseEntity.ok(service.findAllAiModels());
    }

    @Override
    public ResponseEntity<AiModel> patchAiModel(String id, AiModelUpdate aiModel) {
        AiModel updated = service.updateAiModel(id, aiModel);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<AiModel> retrieveAiModel(String id, @Nullable String fields) {
        AiModel found = service.findAiModelById(id);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(found);
    }
}

