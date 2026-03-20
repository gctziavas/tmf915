package org.etsi.osl.controllers.tmf915.deployment;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelUpdate;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

/**
 * Orchestrates AiModel CRUD with lifecycle state-change hooks.
 *
 * <p>Platform-agnostic — delegates actual deployment to {@link PlatformDeployer}
 * implementations via the {@link DeploymentScheduler}.
 *
 * <p>Lifecycle flow driven by the standard TMF 915 API:
 * <ol>
 *   <li>{@code POST /aiModel} → created in the posted state (ACTIVE, INACTIVE, TERMINATED are rejected)</li>
 *   <li>{@code POST /aiModel} with {@code state=reserved} → persists then schedules deployment</li>
 *   <li>{@code PATCH /aiModel/{id}} with {@code state=reserved} → schedules deployment</li>
 *   <li>{@code PATCH /aiModel/{id}} with {@code state=inactive} → stop container + remove image → TERMINATED</li>
 * </ol>
 *
 * <p>Deployment scheduling uses {@code startDate} and {@code endDate}:
 * <ul>
 *   <li>{@code startDate} null or in the past → deploys immediately, defaults to now</li>
 *   <li>{@code startDate} in the future → schedules deployment for that instant</li>
 *   <li>{@code endDate} null → defaults to {@code startDate + 1 day}</li>
 *   <li>{@code endDate} reached → automatic undeployment</li>
 * </ul>
 */
@Service
public class AiModelLifecycleService {

    private static final Logger log = LoggerFactory.getLogger(AiModelLifecycleService.class);

    private static final Set<ServiceStateType> FORBIDDEN_CREATE_STATES = Set.of(
            ServiceStateType.ACTIVE,
            ServiceStateType.INACTIVE,
            ServiceStateType.TERMINATED
    );

    private final AiModelRepositoryService repoService;
    private final DeploymentScheduler scheduler;
    private final List<PlatformDeployer> deployers;
    private final TransactionTemplate txTemplate;

    public AiModelLifecycleService(AiModelRepositoryService repoService,
                                   DeploymentScheduler scheduler,
                                   List<PlatformDeployer> deployers,
                                   PlatformTransactionManager txManager) {
        this.repoService = repoService;
        this.scheduler = scheduler;
        this.deployers = deployers;
        this.txTemplate = new TransactionTemplate(txManager);
    }

    /**
     * Creates an AiModel in the posted state.
     * Rejects ACTIVE, INACTIVE, and TERMINATED.
     * If the posted state is RESERVED, the model is persisted and deployment is scheduled.
     */
    public AiModel createAiModel(AiModelCreate aiModelCreate) {
        ServiceStateType requestedState = aiModelCreate.getState();

        if (requestedState != null && FORBIDDEN_CREATE_STATES.contains(requestedState)) {
            log.warn("Rejected create with forbidden state: {}", requestedState);
            throw new IllegalArgumentException(
                    "Cannot create an AiModel in state " + requestedState
                            + ". Allowed states: feasibilityChecked, designed, reserved.");
        }

        if (requestedState == ServiceStateType.RESERVED) {
            applyDefaultDates(aiModelCreate);
        }

        AiModel created = repoService.createAiModel(aiModelCreate);
        log.info("AiModel {} created in state {}", created.getId(), requestedState);

        if (requestedState == ServiceStateType.RESERVED) {
            log.info("AiModel {} is RESERVED – scheduling deployment", created.getId());
            return scheduler.scheduleDeploy(created);
        }

        return created;
    }

    /**
     * Updates an AiModel and reacts to state transitions.
     */
    public AiModel updateAiModel(String id, AiModelUpdate aiModelUpdate) {
        ServiceStateType requestedState = aiModelUpdate.getState();
        log.info("Updating AiModel {} (requestedState={})", id, requestedState);

        if (requestedState == ServiceStateType.RESERVED) {
            return handleDeploy(id, aiModelUpdate);
        }

        if (requestedState == ServiceStateType.INACTIVE) {
            return handleUndeploy(id, aiModelUpdate);
        }

        return repoService.updateAiModel(id, aiModelUpdate);
    }

    // ── Deploy: RESERVED → schedule build + start container → ACTIVE ────

    private AiModel handleDeploy(String id, AiModelUpdate aiModelUpdate) {
        applyDefaultDates(aiModelUpdate);
        AiModel aiModel = repoService.updateAiModel(id, aiModelUpdate);
        log.info("AiModel {} set to RESERVED – scheduling deployment (startDate={}, endDate={})",
                id, aiModel.getStartDate(), aiModel.getEndDate());
        return scheduler.scheduleDeploy(aiModel);
    }

    // ── Undeploy: INACTIVE → stop container + remove image → TERMINATED ─

    private AiModel handleUndeploy(String id, AiModelUpdate aiModelUpdate) {
        log.info("AiModel {} set to INACTIVE – undeploying", id);
        scheduler.cancelScheduled(id);

        txTemplate.executeWithoutResult(status -> {
            AiModel aiModel = repoService.findAiModelById(id);
            if (aiModel == null) {
                throw new IllegalArgumentException("No AiModel with ID: " + id);
            }

            PlatformDeployer deployer = findDeployer(aiModel);
            if (deployer != null) {
                deployer.undeploy(aiModel);
            } else {
                log.warn("No deployer supports AiModel {} – skipping platform-specific cleanup", id);
            }
        });

        log.info("AiModel {} undeployed and set to TERMINATED", id);
        return repoService.updateAiModelState(id, ServiceStateType.TERMINATED);
    }

    // ── Delegated read/delete operations ────────────────────────────────

    public List<AiModel> findAllAiModels() {
        return repoService.findAllAiModels();
    }

    public AiModel findAiModelById(String id) {
        return repoService.findAiModelById(id);
    }

    public void deleteAiModel(String id) {
        log.info("Deleting AiModel {}", id);
        scheduler.cancelScheduled(id);
        repoService.deleteAiModel(id);
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private void applyDefaultDates(AiModelCreate dto) {
        OffsetDateTime now = OffsetDateTime.now();
        if (dto.getStartDate() == null) {
            dto.setStartDate(now);
        }
        if (dto.getEndDate() == null) {
            dto.setEndDate(dto.getStartDate().plus(1, ChronoUnit.DAYS));
        }
    }

    private void applyDefaultDates(AiModelUpdate dto) {
        OffsetDateTime now = OffsetDateTime.now();
        if (dto.getStartDate() == null) {
            dto.setStartDate(now);
        }
        if (dto.getEndDate() == null) {
            dto.setEndDate(dto.getStartDate().plus(1, ChronoUnit.DAYS));
        }
    }

    private PlatformDeployer findDeployer(AiModel model) {
        return deployers.stream()
                .filter(d -> d.supports(model))
                .findFirst()
                .orElse(null);
    }
}
