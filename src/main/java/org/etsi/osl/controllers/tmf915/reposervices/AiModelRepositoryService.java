package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.mappers.AiModelMapper;
import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelUpdate;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.etsi.osl.controllers.tmf915.model.ServiceSpecificationRef;
import org.etsi.osl.controllers.tmf915.repo.AiModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AiModelRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(AiModelRepositoryService.class);
    private static final String AI_MANAGEMENT_BASE_PATH = "/tmf-api/AiM/v4";
    private static final String AI_MODEL_TYPE = "AIModel";
    private static final String AI_MODEL_BASE_TYPE = "Service";
    private static final String AI_MODEL_SPECIFICATION_TYPE = "AIModelSpecification";
    private static final String AI_MODEL_SPECIFICATION_BASE_TYPE = "ServiceSpecification";
    private static final String SERVICE_SPECIFICATION_TYPE = "ServiceSpecification";

    private final AiModelRepository aiModelRepository;

    public AiModelRepositoryService(AiModelRepository aiModelRepository) {
        this.aiModelRepository = aiModelRepository;
    }

    public List<AiModel> findAllAiModels() {
        log.info("AiModels LIST");
        List<AiModel> result = new ArrayList<>();
        aiModelRepository.findAll().forEach(model -> result.add(normalize(model)));
        return result;
    }

    public AiModel findAiModelById(String id) {
        log.info("AiModel FIND BY ID: {}", id);
        return aiModelRepository.findById(id)
                .map(this::normalize)
                .orElse(null);
    }

    public AiModel createAiModel(AiModelCreate aiModelCreate) {
        log.info("AiModel CREATE: {}", aiModelCreate);
        AiModel aiModel = AiModelMapper.fromCreate(aiModelCreate);
        aiModel.setId(UUID.randomUUID().toString());
        normalize(aiModel);
        return normalize(aiModelRepository.save(aiModel));
    }

    public AiModel updateAiModel(String id, AiModelUpdate aiModelUpdate) {
        log.info("AiModel UPDATE with ID: {}", id);
        AiModel existing = aiModelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No AiModel with ID: " + id));
        AiModelMapper.applyUpdate(existing, aiModelUpdate);
        normalize(existing);
        return normalize(aiModelRepository.save(existing));
    }

    public void deleteAiModel(String id) {
        log.info("AiModel DELETE with ID: {}", id);
        AiModel aiModel = aiModelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No AiModel with ID: " + id));
        aiModelRepository.delete(aiModel);
    }

    public List<AiModel> findByNamePrefix(String prefix) {
        return aiModelRepository.findByNameStartingWith(prefix);
    }

    public AiModel updateAiModelState(String id, ServiceStateType state) {
        log.info("AiModel UPDATE STATE with ID: {} to {}", id, state);
        AiModel aiModel = aiModelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No AiModel with ID: " + id));
        aiModel.setState(state);
        normalize(aiModel);
        return normalize(aiModelRepository.save(aiModel));
    }

    public List<AiModel> findByNameStartingWith(String namePrefix) {
        return aiModelRepository.findByNameStartingWith(namePrefix).stream()
                .map(this::normalize)
                .toList();
    }

    public List<AiModel> findByState(ServiceStateType state) {
        log.info("AiModel FIND BY STATE: {}", state);
        return aiModelRepository.findByState(state).stream()
                .map(this::normalize)
                .toList();
    }

    private AiModel normalize(AiModel model) {
        if (model == null) {
            return null;
        }

        if (model.getAtType() == null) {
            model.setAtType(AI_MODEL_TYPE);
        }
        if (model.getAtBaseType() == null) {
            model.setAtBaseType(AI_MODEL_BASE_TYPE);
        }
        if (model.getId() != null && model.getHref() == null) {
            model.setHref(URI.create(AI_MANAGEMENT_BASE_PATH + "/aiModel/" + model.getId()));
        }

        AiModelSpecification specification = model.getAiModelSpecification();
        if (specification != null) {
            normalizeSpecification(specification);
            model.setServiceSpecification(toServiceSpecificationRef(specification, model.getServiceSpecification()));
        }

        return model;
    }

    private void normalizeSpecification(AiModelSpecification specification) {
        if (specification.getAtType() == null) {
            specification.setAtType(AI_MODEL_SPECIFICATION_TYPE);
        }
        if (specification.getAtBaseType() == null) {
            specification.setAtBaseType(AI_MODEL_SPECIFICATION_BASE_TYPE);
        }
        if (specification.getId() != null && specification.getHref() == null) {
            specification.setHref(URI.create(AI_MANAGEMENT_BASE_PATH + "/aiModelSpecification/" + specification.getId()));
        }
    }

    private ServiceSpecificationRef toServiceSpecificationRef(AiModelSpecification specification,
                                                              ServiceSpecificationRef existingRef) {
        ServiceSpecificationRef ref = existingRef != null ? existingRef : new ServiceSpecificationRef(specification.getId());
        if (ref.getId() == null) {
            ref.setId(specification.getId());
        }
        if (ref.getName() == null) {
            ref.setName(specification.getName());
        }
        if (ref.getVersion() == null) {
            ref.setVersion(specification.getVersion());
        }
        if (ref.getHref() == null && specification.getHref() != null) {
            ref.setHref(specification.getHref());
        }
        if (ref.getAtType() == null) {
            ref.setAtType(SERVICE_SPECIFICATION_TYPE);
        }
        if (ref.getAtBaseType() == null) {
            ref.setAtBaseType(AI_MODEL_SPECIFICATION_BASE_TYPE);
        }
        if (ref.getAtReferredType() == null) {
            ref.setAtReferredType(AI_MODEL_SPECIFICATION_TYPE);
        }
        return ref;
    }

}
