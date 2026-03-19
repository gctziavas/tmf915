package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.mappers.AiModelSpecificationMapper;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationUpdate;
import org.etsi.osl.controllers.tmf915.repo.AiModelSpecificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AiModelSpecificationRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(AiModelSpecificationRepositoryService.class);

    private final AiModelSpecificationRepository aiModelSpecificationRepository;

    public AiModelSpecificationRepositoryService(AiModelSpecificationRepository aiModelSpecificationRepository) {
        this.aiModelSpecificationRepository = aiModelSpecificationRepository;
    }

    public List<AiModelSpecification> findAllAiModelSpecifications() {
        log.info("AiModelSpecifications LIST");
        List<AiModelSpecification> result = new ArrayList<>();
        aiModelSpecificationRepository.findAll().forEach(result::add);
        return result;
    }

    public AiModelSpecification findAiModelSpecificationByUuid(String uuid) {
        log.info("AiModelSpecification FIND BY UUID: {}", uuid);
        return aiModelSpecificationRepository.findByUuid(uuid).orElse(null);
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

    public AiModelSpecification updateAiModelSpecification(String uuid, AiModelSpecificationUpdate aiModelSpecUpdate) {
        log.info("AiModelSpecification UPDATE with UUID: {}", uuid);
        AiModelSpecification existing = aiModelSpecificationRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("No AiModelSpecification with UUID: " + uuid));
        AiModelSpecificationMapper.applyUpdate(existing, aiModelSpecUpdate);
        return aiModelSpecificationRepository.save(existing);
    }

    public void deleteAiModelSpecification(String uuid) {
        log.info("AiModelSpecification DELETE with UUID: {}", uuid);
        AiModelSpecification spec = aiModelSpecificationRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("No AiModelSpecification with UUID: " + uuid));
        aiModelSpecificationRepository.delete(spec);
    }
}

