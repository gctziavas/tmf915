package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.model.AiContractViolation;
import org.etsi.osl.controllers.tmf915.model.AiContractViolationCreate;
import org.etsi.osl.controllers.tmf915.reposervices.AiContractViolationRepositoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiContractViolationApiDelegateImpl implements AiContractViolationApiDelegate {

    private final AiContractViolationRepositoryService aiContractViolationRepositoryService;

    public AiContractViolationApiDelegateImpl(AiContractViolationRepositoryService aiContractViolationRepositoryService) {
        this.aiContractViolationRepositoryService = aiContractViolationRepositoryService;
    }

    @Override
    public ResponseEntity<AiContractViolation> createAiContractViolation(AiContractViolationCreate aiContractViolation) {
        AiContractViolation created = aiContractViolationRepositoryService.createAiContractViolation(aiContractViolation);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<List<AiContractViolation>> listAiContractViolation(String fields, Integer offset, Integer limit) {
        return ResponseEntity.ok(aiContractViolationRepositoryService.findAllAiContractViolations());
    }

    @Override
    public ResponseEntity<AiContractViolation> retrieveAiContractViolation(String id, String fields) {
        AiContractViolation violation = aiContractViolationRepositoryService.findAiContractViolationById(id);
        if (violation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(violation);
    }
}
