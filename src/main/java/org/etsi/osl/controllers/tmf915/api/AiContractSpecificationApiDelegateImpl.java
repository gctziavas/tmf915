package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.model.AiContractSpecification;
import org.etsi.osl.controllers.tmf915.model.AiContractSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.AiContractSpecificationUpdate;
import org.etsi.osl.controllers.tmf915.reposervices.AiContractSpecificationRepositoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public class AiContractSpecificationApiDelegateImpl implements AiContractSpecificationApiDelegate {

    private final AiContractSpecificationRepositoryService aiContractSpecificationRepositoryService;

    public AiContractSpecificationApiDelegateImpl(AiContractSpecificationRepositoryService aiContractSpecificationRepositoryService) {
        this.aiContractSpecificationRepositoryService = aiContractSpecificationRepositoryService;
    }

    @Override
    public ResponseEntity<AiContractSpecification> createAiContractSpecification(AiContractSpecificationCreate aiContractSpecification) {
        AiContractSpecification created = aiContractSpecificationRepositoryService.createAiContractSpecification(aiContractSpecification);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<Void> deleteAiContractSpecification(String id) {
        aiContractSpecificationRepositoryService.deleteAiContractSpecification(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<AiContractSpecification>> listAiContractSpecification(String fields, Integer offset, Integer limit) {
        return ResponseEntity.ok(aiContractSpecificationRepositoryService.findAllAiContractSpecifications());
    }

    @Override
    public ResponseEntity<AiContractSpecification> patchAiContractSpecification(String id, AiContractSpecificationUpdate aiContractSpecification) {
        AiContractSpecification updated = aiContractSpecificationRepositoryService.updateAiContractSpecification(id, aiContractSpecification);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<AiContractSpecification> retrieveAiContractSpecification(String id, String fields) {
        AiContractSpecification spec = aiContractSpecificationRepositoryService.findAiContractSpecificationById(id);
        if (spec == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spec);
    }
}
