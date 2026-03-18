package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationUpdate;
import org.etsi.osl.controllers.tmf915.repo.AiModelSpecificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AiModelSpecificationRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(AiModelSpecificationRepositoryService.class);

    private final AiModelSpecificationRepository aiModelSpecificationRepository;

    @Autowired
    public AiModelSpecificationRepositoryService(AiModelSpecificationRepository aiModelSpecificationRepository) {
        this.aiModelSpecificationRepository = aiModelSpecificationRepository;
    }

    public List<AiModelSpecification> findAllAiModelSpecifications() {
        log.info("AiModelSpecifications LIST");
        return aiModelSpecificationRepository.findAll();
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
        AiModelSpecification spec = fromCreate(aiModelSpecCreate);
        spec.setId(UUID.randomUUID().toString());
        return aiModelSpecificationRepository.save(spec);
    }

    public AiModelSpecification updateAiModelSpecification(String uuid, AiModelSpecificationUpdate aiModelSpecUpdate) {
        log.info("AiModelSpecification UPDATE with UUID: {}", uuid);
        AiModelSpecification existing = aiModelSpecificationRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("No AiModelSpecification with UUID: " + uuid));
        applyUpdate(existing, aiModelSpecUpdate);
        return aiModelSpecificationRepository.save(existing);
    }

    public void deleteAiModelSpecification(String uuid) {
        log.info("AiModelSpecification DELETE with UUID: {}", uuid);
        AiModelSpecification spec = aiModelSpecificationRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("No AiModelSpecification with UUID: " + uuid));
        aiModelSpecificationRepository.delete(spec);
    }

    private AiModelSpecification fromCreate(AiModelSpecificationCreate src) {
        AiModelSpecification spec = new AiModelSpecification();
        spec.setDeploymentRecord(src.getDeploymentRecord());
        spec.setDescription(src.getDescription());
        spec.setInheritedModel(src.getInheritedModel());
        spec.setIsBundle(src.getIsBundle());
        spec.setLastUpdate(src.getLastUpdate());
        spec.setLifecycleStatus(src.getLifecycleStatus());
        spec.setModelContractVersionHistory(src.getModelContractVersionHistory());
        spec.setModelDataSheet(src.getModelDataSheet());
        spec.setModelEvaluationData(src.getModelEvaluationData());
        spec.setModelSpecificationHistory(src.getModelSpecificationHistory());
        spec.setModelTrainingData(src.getModelTrainingData());
        spec.setName(src.getName());
        spec.setVersion(src.getVersion());
        spec.setAttachment(src.getAttachment());
        spec.setConstraint(src.getConstraint());
        spec.setEntitySpecRelationship(src.getEntitySpecRelationship());
        spec.setFeatureSpecification(src.getFeatureSpecification());
        spec.setRelatedParty(src.getRelatedParty());
        spec.setResourceSpecification(src.getResourceSpecification());
        spec.setServiceLevelSpecification(src.getServiceLevelSpecification());
        spec.setServiceSpecRelationship(src.getServiceSpecRelationship());
        spec.setSpecCharacteristic(src.getSpecCharacteristic());
        return spec;
    }

    private void applyUpdate(AiModelSpecification target, AiModelSpecificationUpdate src) {
        if (src.getDeploymentRecord() != null)              target.setDeploymentRecord(src.getDeploymentRecord());
        if (src.getDescription() != null)                   target.setDescription(src.getDescription());
        if (src.getInheritedModel() != null)                target.setInheritedModel(src.getInheritedModel());
        if (src.getIsBundle() != null)                      target.setIsBundle(src.getIsBundle());
        if (src.getLifecycleStatus() != null)               target.setLifecycleStatus(src.getLifecycleStatus());
        if (src.getModelContractVersionHistory() != null)   target.setModelContractVersionHistory(src.getModelContractVersionHistory());
        if (src.getModelDataSheet() != null)                target.setModelDataSheet(src.getModelDataSheet());
        if (src.getModelEvaluationData() != null)           target.setModelEvaluationData(src.getModelEvaluationData());
        if (src.getModelSpecificationHistory() != null)     target.setModelSpecificationHistory(src.getModelSpecificationHistory());
        if (src.getModelTrainingData() != null)             target.setModelTrainingData(src.getModelTrainingData());
        if (src.getName() != null)                          target.setName(src.getName());
        if (src.getVersion() != null)                       target.setVersion(src.getVersion());
        if (src.getAttachment() != null)                    target.setAttachment(src.getAttachment());
        if (src.getConstraint() != null)                    target.setConstraint(src.getConstraint());
        if (src.getEntitySpecRelationship() != null)        target.setEntitySpecRelationship(src.getEntitySpecRelationship());
        if (src.getFeatureSpecification() != null)          target.setFeatureSpecification(src.getFeatureSpecification());
        if (src.getRelatedParty() != null)                  target.setRelatedParty(src.getRelatedParty());
        if (src.getResourceSpecification() != null)         target.setResourceSpecification(src.getResourceSpecification());
        if (src.getServiceLevelSpecification() != null)     target.setServiceLevelSpecification(src.getServiceLevelSpecification());
        if (src.getServiceSpecRelationship() != null)       target.setServiceSpecRelationship(src.getServiceSpecRelationship());
        if (src.getSpecCharacteristic() != null)            target.setSpecCharacteristic(src.getSpecCharacteristic());
    }
}
