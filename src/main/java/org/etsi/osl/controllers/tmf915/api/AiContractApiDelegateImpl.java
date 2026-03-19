package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.model.AiContract;
import org.etsi.osl.controllers.tmf915.model.AiContractCreate;
import org.etsi.osl.controllers.tmf915.model.AiContractUpdate;
import org.etsi.osl.controllers.tmf915.reposervices.AiContractRepositoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

public class AiContractApiDelegateImpl implements AiContractApiDelegate {

    private final AiContractRepositoryService aiContractRepositoryService;

    public AiContractApiDelegateImpl(AiContractRepositoryService aiContractRepositoryService) {
        this.aiContractRepositoryService = aiContractRepositoryService;
    }

    @Override
    public ResponseEntity<AiContract> createAiContract(AiContractCreate aiContract) {
        AiContract created = aiContractRepositoryService.createAiContract(aiContract);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<Void> deleteAiContract(String id) {
        aiContractRepositoryService.deleteAiContract(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<AiContract>> listAiContract(String fields, Integer offset, Integer limit) {
        return ResponseEntity.ok(aiContractRepositoryService.findAllAiContracts());
    }

    @Override
    public ResponseEntity<AiContract> patchAiContract(String id, AiContractUpdate aiContract) {
        AiContract updated = aiContractRepositoryService.updateAiContract(id, aiContract);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<AiContract> retrieveAiContract(String id, String fields) {
        AiContract aiContract = aiContractRepositoryService.findAiContractById(id);
        if (aiContract == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(aiContract);
    }
}
