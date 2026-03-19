package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.mappers.AiModelMapper;
import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelUpdate;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.etsi.osl.controllers.tmf915.repo.AiModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AiModelRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(AiModelRepositoryService.class);

    private final AiModelRepository aiModelRepository;

    public AiModelRepositoryService(AiModelRepository aiModelRepository) {
        this.aiModelRepository = aiModelRepository;
    }

    public List<AiModel> findAllAiModels() {
        log.info("AiModels LIST");
        List<AiModel> result = new ArrayList<>();
        aiModelRepository.findAll().forEach(result::add);
        return result;
    }

    public AiModel findAiModelById(String id) {
        log.info("AiModel FIND BY ID: {}", id);
        return aiModelRepository.findById(id).orElse(null);
    }

    public AiModel createAiModel(AiModelCreate aiModelCreate) {
        log.info("AiModel CREATE: {}", aiModelCreate);
        AiModel aiModel = AiModelMapper.fromCreate(aiModelCreate);
        aiModel.setId(UUID.randomUUID().toString());
        return aiModelRepository.save(aiModel);
    }

    public AiModel updateAiModel(String id, AiModelUpdate aiModelUpdate) {
        log.info("AiModel UPDATE with ID: {}", id);
        AiModel existing = aiModelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No AiModel with ID: " + id));
        AiModelMapper.applyUpdate(existing, aiModelUpdate);
        return aiModelRepository.save(existing);
    }

    public void deleteAiModel(String id) {
        log.info("AiModel DELETE with ID: {}", id);
        AiModel aiModel = aiModelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No AiModel with ID: " + id));
        aiModelRepository.delete(aiModel);
    }

    public AiModel updateAiModelState(String id, ServiceStateType state) {
        log.info("AiModel UPDATE STATE with ID: {} to {}", id, state);
        AiModel aiModel = aiModelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No AiModel with ID: " + id));
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

}
