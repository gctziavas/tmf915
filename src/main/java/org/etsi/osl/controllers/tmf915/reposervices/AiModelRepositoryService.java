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

    public AiModel findAiModelByUuid(String uuid) {
        log.info("AiModel FIND BY UUID: {}", uuid);
        return aiModelRepository.findByUuid(uuid).orElse(null);
    }

    public AiModel createAiModel(AiModelCreate aiModelCreate) {
        log.info("AiModel CREATE: {}", aiModelCreate);
        AiModel aiModel = AiModelMapper.fromCreate(aiModelCreate);
        aiModel.setId(UUID.randomUUID().toString());
        return aiModelRepository.save(aiModel);
    }

    public AiModel updateAiModel(String uuid, AiModelUpdate aiModelUpdate) {
        log.info("AiModel UPDATE with UUID: {}", uuid);
        AiModel existing = aiModelRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("No AiModel with UUID: " + uuid));
        AiModelMapper.applyUpdate(existing, aiModelUpdate);
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

}
