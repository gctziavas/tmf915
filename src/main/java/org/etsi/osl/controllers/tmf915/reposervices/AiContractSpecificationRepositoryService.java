package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.mappers.AiContractSpecificationMapper;
import org.etsi.osl.controllers.tmf915.model.AiContractSpecification;
import org.etsi.osl.controllers.tmf915.model.AiContractSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.AiContractSpecificationUpdate;
import org.etsi.osl.controllers.tmf915.repo.AiContractSpecificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AiContractSpecificationRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(AiContractSpecificationRepositoryService.class);

    private final AiContractSpecificationRepository aiContractSpecificationRepository;

    public AiContractSpecificationRepositoryService(AiContractSpecificationRepository aiContractSpecificationRepository) {
        this.aiContractSpecificationRepository = aiContractSpecificationRepository;
    }

    public List<AiContractSpecification> findAllAiContractSpecifications() {
        log.info("AiContractSpecifications LIST");
        List<AiContractSpecification> result = new ArrayList<>();
        aiContractSpecificationRepository.findAll().forEach(result::add);
        return result;
    }

    public AiContractSpecification findAiContractSpecificationById(String id) {
        log.info("AiContractSpecification FIND BY ID: {}", id);
        return aiContractSpecificationRepository.findById(id).orElse(null);
    }

    public AiContractSpecification createAiContractSpecification(AiContractSpecificationCreate src) {
        log.info("AiContractSpecification CREATE: {}", src);
        AiContractSpecification spec = AiContractSpecificationMapper.fromCreate(src);
        spec.setId(UUID.randomUUID().toString());
        return aiContractSpecificationRepository.save(spec);
    }

    public AiContractSpecification updateAiContractSpecification(String id, AiContractSpecificationUpdate src) {
        log.info("AiContractSpecification UPDATE with ID: {}", id);
        AiContractSpecification existing = aiContractSpecificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No AiContractSpecification with ID: " + id));
        AiContractSpecificationMapper.applyUpdate(existing, src);
        return aiContractSpecificationRepository.save(existing);
    }

    public void deleteAiContractSpecification(String id) {
        log.info("AiContractSpecification DELETE with ID: {}", id);
        AiContractSpecification spec = aiContractSpecificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No AiContractSpecification with ID: " + id));
        aiContractSpecificationRepository.delete(spec);
    }
}
