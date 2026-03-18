package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelUpdate;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.etsi.osl.controllers.tmf915.repo.AiModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AiModelRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(AiModelRepositoryService.class);

    private final AiModelRepository aiModelRepository;

    @Autowired
    public AiModelRepositoryService(AiModelRepository aiModelRepository) {
        this.aiModelRepository = aiModelRepository;
    }

    public List<AiModel> findAllAiModels() {
        log.info("AiModels LIST");
        return aiModelRepository.findAll();
    }

    public AiModel findAiModelByUuid(String uuid) {
        log.info("AiModel FIND BY UUID: {}", uuid);
        return aiModelRepository.findByUuid(uuid).orElse(null);
    }

    public AiModel createAiModel(AiModelCreate aiModelCreate) {
        log.info("AiModel CREATE: {}", aiModelCreate);
        AiModel aiModel = fromCreate(aiModelCreate);
        aiModel.setId(UUID.randomUUID().toString());
        return aiModelRepository.save(aiModel);
    }

    public AiModel updateAiModel(String uuid, AiModelUpdate aiModelUpdate) {
        log.info("AiModel UPDATE with UUID: {}", uuid);
        AiModel existing = aiModelRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("No AiModel with UUID: " + uuid));
        applyUpdate(existing, aiModelUpdate);
        return aiModelRepository.save(existing);
    }

    public void deleteAiModel(String uuid) {
        log.info("AiModel DELETE with UUID: {}", uuid);
        AiModel aiModel = aiModelRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("No AiModel with UUID: " + uuid));
        aiModelRepository.delete(aiModel);
    }

    public AiModel updateAiModelState(String uuid, ServiceStateType state) {
        log.info("AiModel UPDATE STATE with UUID: {} to {}", uuid, state);
        AiModel aiModel = aiModelRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("No AiModel with UUID: " + uuid));
        aiModel.setState(state);
        return aiModelRepository.save(aiModel);
    }

    public List<AiModel> findByNameStartingWith(String namePrefix) {
        return aiModelRepository.findByNameStartingWith(namePrefix);
    }

    public List<AiModel> findByState(ServiceStateType state) {
        log.info("AiModel FIND BY STATE: {}", state);
        return aiModelRepository.findByState(state);
    }

    private AiModel fromCreate(AiModelCreate src) {
        AiModel model = new AiModel();
        model.setCategory(src.getCategory());
        model.setDescription(src.getDescription());
        model.setEndDate(src.getEndDate());
        model.setHasStarted(src.getHasStarted());
        model.setIsBundle(src.getIsBundle());
        model.setIsServiceEnabled(src.getIsServiceEnabled());
        model.setIsStateful(src.getIsStateful());
        model.setName(src.getName());
        model.setServiceDate(src.getServiceDate());
        model.setServiceType(src.getServiceType());
        model.setStartDate(src.getStartDate());
        model.setStartMode(src.getStartMode());
        model.setAiModelSpecification(src.getAiModelSpecification());
        model.setFeature(src.getFeature());
        model.setGpu(src.getGpu());
        model.setNote(src.getNote());
        model.setPlace(src.getPlace());
        model.setRelatedEntity(src.getRelatedEntity());
        model.setRelatedParty(src.getRelatedParty());
        model.setServiceCharacteristic(src.getServiceCharacteristic());
        model.setServiceOrderItem(src.getServiceOrderItem());
        model.setServiceRelationship(src.getServiceRelationship());
        model.setServiceSpecification(src.getServiceSpecification());
        model.setSoftware(src.getSoftware());
        model.setState(src.getState());
        model.setSupportingResource(src.getSupportingResource());
        model.setSupportingService(src.getSupportingService());
        model.setTrainingData(src.getTrainingData());
        return model;
    }

    private void applyUpdate(AiModel target, AiModelUpdate src) {
        if (src.getCategory() != null)              target.setCategory(src.getCategory());
        if (src.getDescription() != null)           target.setDescription(src.getDescription());
        if (src.getEndDate() != null)               target.setEndDate(src.getEndDate());
        if (src.getHasStarted() != null)            target.setHasStarted(src.getHasStarted());
        if (src.getIsBundle() != null)              target.setIsBundle(src.getIsBundle());
        if (src.getIsServiceEnabled() != null)      target.setIsServiceEnabled(src.getIsServiceEnabled());
        if (src.getIsStateful() != null)            target.setIsStateful(src.getIsStateful());
        if (src.getName() != null)                  target.setName(src.getName());
        if (src.getServiceType() != null)           target.setServiceType(src.getServiceType());
        if (src.getStartDate() != null)             target.setStartDate(src.getStartDate());
        if (src.getStartMode() != null)             target.setStartMode(src.getStartMode());
        if (src.getAiModelSpecification() != null)  target.setAiModelSpecification(src.getAiModelSpecification());
        if (src.getFeature() != null)               target.setFeature(src.getFeature());
        if (src.getGpu() != null)                   target.setGpu(src.getGpu());
        if (src.getNote() != null)                  target.setNote(src.getNote());
        if (src.getPlace() != null)                 target.setPlace(src.getPlace());
        if (src.getRelatedEntity() != null)         target.setRelatedEntity(src.getRelatedEntity());
        if (src.getRelatedParty() != null)          target.setRelatedParty(src.getRelatedParty());
        if (src.getServiceCharacteristic() != null) target.setServiceCharacteristic(src.getServiceCharacteristic());
        if (src.getServiceOrderItem() != null)      target.setServiceOrderItem(src.getServiceOrderItem());
        if (src.getServiceRelationship() != null)   target.setServiceRelationship(src.getServiceRelationship());
        if (src.getServiceSpecification() != null)  target.setServiceSpecification(src.getServiceSpecification());
        if (src.getSoftware() != null)              target.setSoftware(src.getSoftware());
        if (src.getState() != null)                 target.setState(src.getState());
        if (src.getSupportingResource() != null)    target.setSupportingResource(src.getSupportingResource());
        if (src.getSupportingService() != null)     target.setSupportingService(src.getSupportingService());
        if (src.getTrainingData() != null)          target.setTrainingData(src.getTrainingData());
    }
}
