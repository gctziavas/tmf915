package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.mappers.AiModelSpecificationMapper;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationUpdate;
import org.etsi.osl.controllers.tmf915.repo.AiModelSpecificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AiModelSpecificationRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(AiModelSpecificationRepositoryService.class);

    private final AiModelSpecificationRepository aiModelSpecificationRepository;

    public AiModelSpecificationRepositoryService(AiModelSpecificationRepository aiModelSpecificationRepository) {
        this.aiModelSpecificationRepository = aiModelSpecificationRepository;
    }

    private static final int DEFAULT_LIMIT = 50;

    public List<AiModelSpecification> findAllAiModelSpecifications(Integer offset, Integer limit) {
        log.info("AiModelSpecifications LIST (offset={}, limit={})", offset, limit);
        int page = (offset != null && offset >= 0) ? offset : 0;
        int size = (limit != null && limit > 0) ? limit : DEFAULT_LIMIT;
        return aiModelSpecificationRepository.findAll(PageRequest.of(page / size, size)).getContent();
    }

    public AiModelSpecification findAiModelSpecificationById(String id) {
        log.info("AiModelSpecification FIND BY ID: {}", id);
        return aiModelSpecificationRepository.findById(id).orElse(null);
    }

    public AiModelSpecification findAiModelSpecificationByNameAndVersion(String name, String version) {
        log.info("AiModelSpecification FIND BY name/version: {}/{}", name, version);
        return aiModelSpecificationRepository.findByNameAndVersion(name, version).orElse(null);
    }

    public AiModelSpecification findAiModelSpecificationByName(String name) {
        log.info("AiModelSpecification FIND BY name: {}", name);
        return aiModelSpecificationRepository.findByName(name).orElse(null);
    }

    public AiModelSpecification createAiModelSpecification(AiModelSpecificationCreate aiModelSpecCreate) {
        log.info("AiModelSpecification CREATE: {}", aiModelSpecCreate);
        AiModelSpecification spec = AiModelSpecificationMapper.fromCreate(aiModelSpecCreate);
        spec.setId(UUID.randomUUID().toString());
        return aiModelSpecificationRepository.save(spec);
    }

    public AiModelSpecification updateAiModelSpecification(String id, AiModelSpecificationUpdate aiModelSpecUpdate) {
        log.info("AiModelSpecification UPDATE with ID: {}", id);
        AiModelSpecification existing = aiModelSpecificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No AiModelSpecification with ID: " + id));
        AiModelSpecificationMapper.applyUpdate(existing, aiModelSpecUpdate);
        return aiModelSpecificationRepository.save(existing);
    }

    public void deleteAiModelSpecification(String id) {
        log.info("AiModelSpecification DELETE with ID: {}", id);
        AiModelSpecification spec = aiModelSpecificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No AiModelSpecification with ID: " + id));
        aiModelSpecificationRepository.delete(spec);
    }
}

