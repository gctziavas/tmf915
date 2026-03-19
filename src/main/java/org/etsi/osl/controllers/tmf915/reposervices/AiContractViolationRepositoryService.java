package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.mappers.AiContractViolationMapper;
import org.etsi.osl.controllers.tmf915.model.AiContractViolation;
import org.etsi.osl.controllers.tmf915.model.AiContractViolationCreate;
import org.etsi.osl.controllers.tmf915.repo.AiContractViolationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AiContractViolationRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(AiContractViolationRepositoryService.class);

    private final AiContractViolationRepository aiContractViolationRepository;

    public AiContractViolationRepositoryService(AiContractViolationRepository aiContractViolationRepository) {
        this.aiContractViolationRepository = aiContractViolationRepository;
    }

    public List<AiContractViolation> findAllAiContractViolations() {
        log.info("AiContractViolations LIST");
        List<AiContractViolation> result = new ArrayList<>();
        aiContractViolationRepository.findAll().forEach(result::add);
        return result;
    }

    public AiContractViolation findAiContractViolationById(String id) {
        log.info("AiContractViolation FIND BY ID: {}", id);
        return aiContractViolationRepository.findById(id).orElse(null);
    }

    public AiContractViolation createAiContractViolation(AiContractViolationCreate src) {
        log.info("AiContractViolation CREATE: {}", src);
        AiContractViolation violation = AiContractViolationMapper.fromCreate(src);
        violation.setId(UUID.randomUUID().toString());
        return aiContractViolationRepository.save(violation);
    }
}
