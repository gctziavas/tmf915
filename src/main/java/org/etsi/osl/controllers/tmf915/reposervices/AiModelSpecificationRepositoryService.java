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

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Service
public class AiModelSpecificationRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(AiModelSpecificationRepositoryService.class);
    private static final String AI_MANAGEMENT_BASE_PATH = "/tmf-api/AiM/v4";
    private static final String AI_MODEL_SPECIFICATION_TYPE = "AIModelSpecification";
    private static final String AI_MODEL_SPECIFICATION_BASE_TYPE = "ServiceSpecification";

    private final AiModelSpecificationRepository aiModelSpecificationRepository;

    public AiModelSpecificationRepositoryService(AiModelSpecificationRepository aiModelSpecificationRepository) {
        this.aiModelSpecificationRepository = aiModelSpecificationRepository;
    }

    private static final int DEFAULT_LIMIT = 50;

    public List<AiModelSpecification> findAllAiModelSpecifications(Integer offset, Integer limit) {
        log.info("AiModelSpecifications LIST (offset={}, limit={})", offset, limit);
        int page = (offset != null && offset >= 0) ? offset : 0;
        int size = (limit != null && limit > 0) ? limit : DEFAULT_LIMIT;
        return aiModelSpecificationRepository.findAll(PageRequest.of(page / size, size)).getContent().stream()
                .map(this::normalize)
                .toList();
    }

    public AiModelSpecification findAiModelSpecificationById(String id) {
        log.info("AiModelSpecification FIND BY ID: {}", id);
        return aiModelSpecificationRepository.findById(id)
                .map(this::normalize)
                .orElse(null);
    }

    public AiModelSpecification findAiModelSpecificationByNameAndVersion(String name, String version) {
        log.info("AiModelSpecification FIND BY name/version: {}/{}", name, version);
        return aiModelSpecificationRepository.findByNameAndVersion(name, version)
                .map(this::normalize)
                .orElse(null);
    }

    public AiModelSpecification findAiModelSpecificationByName(String name) {
        log.info("AiModelSpecification FIND BY name: {}", name);
        return aiModelSpecificationRepository.findByName(name)
                .map(this::normalize)
                .orElse(null);
    }

    public AiModelSpecification createAiModelSpecification(AiModelSpecificationCreate aiModelSpecCreate) {
        log.info("AiModelSpecification CREATE: {}", aiModelSpecCreate);
        AiModelSpecification spec = AiModelSpecificationMapper.fromCreate(aiModelSpecCreate);
        spec.setId(UUID.randomUUID().toString());
        normalize(spec);
        return normalize(aiModelSpecificationRepository.save(spec));
    }

    public AiModelSpecification updateAiModelSpecification(String id, AiModelSpecificationUpdate aiModelSpecUpdate) {
        log.info("AiModelSpecification UPDATE with ID: {}", id);
        AiModelSpecification existing = aiModelSpecificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No AiModelSpecification with ID: " + id));
        AiModelSpecificationMapper.applyUpdate(existing, aiModelSpecUpdate);
        normalize(existing);
        return normalize(aiModelSpecificationRepository.save(existing));
    }

    public void deleteAiModelSpecification(String id) {
        log.info("AiModelSpecification DELETE with ID: {}", id);
        AiModelSpecification spec = aiModelSpecificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No AiModelSpecification with ID: " + id));
        aiModelSpecificationRepository.delete(spec);
    }

    private AiModelSpecification normalize(AiModelSpecification specification) {
        if (specification == null) {
            return null;
        }

        if (specification.getAtType() == null) {
            specification.setAtType(AI_MODEL_SPECIFICATION_TYPE);
        }
        if (specification.getAtBaseType() == null) {
            specification.setAtBaseType(AI_MODEL_SPECIFICATION_BASE_TYPE);
        }
        if (specification.getId() != null && specification.getHref() == null) {
            specification.setHref(URI.create(AI_MANAGEMENT_BASE_PATH + "/aiModelSpecification/" + specification.getId()));
        }

        return specification;
    }
}

