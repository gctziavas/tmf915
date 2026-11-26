package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.mappers.AiModelMapper;
import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelUpdate;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.etsi.osl.controllers.tmf915.model.ServiceSpecificationRef;
import org.etsi.osl.controllers.tmf915.repo.AiModelRepository;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Write methods run in a transaction by default; finders are read-only. Lazy associations that
 * are serialized after the method returns are loaded in {@code normalize}, because
 * {@code spring.jpa.open-in-view} is off.
 */
@Service
@Transactional
public class AiModelRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(AiModelRepositoryService.class);
    private static final String AI_MANAGEMENT_BASE_PATH = "/tmf-api/AiM/v4";
    private static final String AI_MODEL_TYPE = "AIModel";
    private static final String AI_MODEL_BASE_TYPE = "Service";
    private static final String AI_MODEL_SPECIFICATION_TYPE = "AIModelSpecification";
    private static final String AI_MODEL_SPECIFICATION_BASE_TYPE = "ServiceSpecification";
    private static final String SERVICE_SPECIFICATION_TYPE = "ServiceSpecification";

    private final AiModelRepository aiModelRepository;
    private final AiModelSpecificationRepositoryService aiModelSpecificationRepositoryService;

    public AiModelRepositoryService(AiModelRepository aiModelRepository,
                                    AiModelSpecificationRepositoryService aiModelSpecificationRepositoryService) {
        this.aiModelRepository = aiModelRepository;
        this.aiModelSpecificationRepositoryService = aiModelSpecificationRepositoryService;
    }

    @Transactional(readOnly = true)
    public List<AiModel> findAllAiModels() {
        log.info("AiModels LIST");
        List<AiModel> result = new ArrayList<>();
        aiModelRepository.findAll().forEach(model -> result.add(normalize(model)));
        return result;
    }

    /**
     * One page of AiModels, ordered by id so consecutive pages do not overlap.
     *
     * @param offset zero-based row offset (null = 0)
     * @param limit  page size (null = {@link OffsetPageable#DEFAULT_LIMIT})
     */
    @Transactional(readOnly = true)
    public List<AiModel> findAiModels(Integer offset, Integer limit) {
        OffsetPageable page = OffsetPageable.of(offset, limit, Sort.by("id"));
        log.info("AiModels LIST (offset={}, limit={})", page.getOffset(), page.getPageSize());
        return aiModelRepository.findAllBy(page).stream()
                .map(this::normalize)
                .toList();
    }

    @Transactional(readOnly = true)
    public AiModel findAiModelById(String id) {
        log.info("AiModel FIND BY ID: {}", id);
        return aiModelRepository.findById(id)
                .map(this::normalize)
                .orElse(null);
    }

    public AiModel createAiModel(AiModelCreate aiModelCreate) {
        log.info("AiModel CREATE: name={}", aiModelCreate.getName());
        
        if (aiModelCreate.getServiceSpecification() != null && aiModelCreate.getServiceSpecification().getId() != null) {
            String specId = aiModelCreate.getServiceSpecification().getId();
            AiModelSpecification spec = aiModelSpecificationRepositoryService.findAiModelSpecificationById(specId);
            if (spec == null) {
                throw new IllegalArgumentException("No AiModelSpecification with ID: " + specId);
            }
        }
        
        AiModel aiModel = AiModelMapper.fromCreate(aiModelCreate);
        aiModel.setId(UUID.randomUUID().toString());
        normalize(aiModel);
        return normalize(aiModelRepository.save(aiModel));
    }

    public AiModel updateAiModel(String id, AiModelUpdate aiModelUpdate) {
        log.info("AiModel UPDATE with ID: {}", id);
        AiModel existing = aiModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No AiModel with ID: " + id));
        AiModelMapper.applyUpdate(existing, aiModelUpdate);
        normalize(existing);
        return normalize(aiModelRepository.save(existing));
    }

    public void deleteAiModel(String id) {
        log.info("AiModel DELETE with ID: {}", id);
        AiModel aiModel = aiModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No AiModel with ID: " + id));
        aiModelRepository.delete(aiModel);
    }

    @Transactional(readOnly = true)
    public long countByNamePrefix(String prefix) {
        return aiModelRepository.countByNameStartingWith(prefix);
    }

    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return aiModelRepository.existsByName(name);
    }

    public AiModel updateAiModelState(String id, ServiceStateType state) {
        log.info("AiModel UPDATE STATE with ID: {} to {}", id, state);
        AiModel aiModel = aiModelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No AiModel with ID: " + id));
        aiModel.setState(state);
        normalize(aiModel);
        return normalize(aiModelRepository.save(aiModel));
    }

    /**
     * Applies {@code change} to the model in one short transaction, but only if the model still
     * exists and is in {@code expectedState}. Used by background work, so that a result computed
     * outside a transaction (for example a finished deployment) is not written over a model that
     * a client stopped or deleted in the meantime.
     *
     * @return the updated model, or null if it no longer exists or is in another state
     */
    public AiModel updateIfInState(String id, ServiceStateType expectedState, Consumer<AiModel> change) {
        AiModel model = aiModelRepository.findById(id).orElse(null);
        if (model == null || model.getState() != expectedState) {
            return null;
        }
        change.accept(model);
        normalize(model);
        return normalize(aiModelRepository.save(model));
    }

    @Transactional(readOnly = true)
    public List<AiModel> findByNameStartingWith(String namePrefix) {
        return aiModelRepository.findByNameStartingWith(namePrefix).stream()
                .map(this::normalize)
                .toList();
    }

    @Transactional(readOnly = true)
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

        // Open-in-view is off: anything serialized after the transaction ends must be loaded now.
        // With @BatchSize, the first initialisation loads the characteristics of up to 50
        // models in the same session in one query.
        Hibernate.initialize(model.getServiceCharacteristic());

        AiModelSpecification specification = model.getAiModelSpecification();
        if (specification != null && !Hibernate.isInitialized(specification)
                && !hasStoredReference(model, specification)) {
            // Legacy row without a stored reference: load the specification once so the
            // reference can be built (and stored on the next write).
            Hibernate.initialize(specification);
        }
        if (specification != null && Hibernate.isInitialized(specification)) {
            normalizeSpecification(specification);
            model.setServiceSpecification(toServiceSpecificationRef(specification, model.getServiceSpecification()));
        }

        return model;
    }

    /** True when the stored reference matches the (possibly unloaded) specification. */
    private static boolean hasStoredReference(AiModel model, AiModelSpecification specification) {
        ServiceSpecificationRef stored = model.getServiceSpecification();
        // getId() on a Hibernate proxy returns the id without loading the entity.
        return stored != null && stored.getName() != null
                && specification.getId() != null && specification.getId().equals(stored.getId());
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
        // A stored reference to a different specification is stale (the model was re-pointed via
        // PATCH); rebuild it, since the JSON view of aiModelSpecification is served from it.
        boolean stale = existingRef != null && specification.getId() != null
                && !specification.getId().equals(existingRef.getId());
        ServiceSpecificationRef ref = (existingRef != null && !stale)
                ? existingRef
                : new ServiceSpecificationRef(specification.getId());
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
