package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.mappers.AiContractMapper;
import org.etsi.osl.controllers.tmf915.model.AiContract;
import org.etsi.osl.controllers.tmf915.model.AiContractCreate;
import org.etsi.osl.controllers.tmf915.model.AiContractUpdate;
import org.etsi.osl.controllers.tmf915.repo.AiContractRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AiContractRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(AiContractRepositoryService.class);

    private final AiContractRepository aiContractRepository;

    public AiContractRepositoryService(AiContractRepository aiContractRepository) {
        this.aiContractRepository = aiContractRepository;
    }

    public List<AiContract> findAllAiContracts() {
        log.info("AiContracts LIST");
        List<AiContract> result = new ArrayList<>();
        aiContractRepository.findAll().forEach(result::add);
        return result;
    }

    public AiContract findAiContractById(String id) {
        log.info("AiContract FIND BY ID: {}", id);
        return aiContractRepository.findById(id).orElse(null);
    }

    public AiContract createAiContract(AiContractCreate aiContractCreate) {
        log.info("AiContract CREATE: {}", aiContractCreate);
        AiContract aiContract = AiContractMapper.fromCreate(aiContractCreate);
        aiContract.setId(UUID.randomUUID().toString());
        return aiContractRepository.save(aiContract);
    }

    public AiContract updateAiContract(String id, AiContractUpdate aiContractUpdate) {
        log.info("AiContract UPDATE with ID: {}", id);
        AiContract existing = aiContractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No AiContract with ID: " + id));
        AiContractMapper.applyUpdate(existing, aiContractUpdate);
        return aiContractRepository.save(existing);
    }

    public void deleteAiContract(String id) {
        log.info("AiContract DELETE with ID: {}", id);
        AiContract aiContract = aiContractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No AiContract with ID: " + id));
        aiContractRepository.delete(aiContract);
    }
}
