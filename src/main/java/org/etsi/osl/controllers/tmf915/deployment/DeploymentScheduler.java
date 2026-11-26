package org.etsi.osl.controllers.tmf915.deployment;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.etsi.osl.controllers.tmf915.integrations.hub.AiModelDomainEvent;
import org.etsi.osl.controllers.tmf915.model.Characteristic;
import org.springframework.context.ApplicationEventPublisher;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Manages scheduled deployment and undeployment of AiModels using
 * their {@code startDate} and {@code endDate} fields.
 *
 * <p>Platform-agnostic — delegates actual deploy/undeploy work to
 * {@link PlatformDeployer} implementations discovered via Spring.
 *
 * <ul>
 *   <li>{@code startDate} in the future → schedules deployment for that instant</li>
 *   <li>{@code startDate} null or in the past → deploys immediately</li>
 *   <li>{@code endDate} reached → undeploys automatically</li>
 * </ul>
 */
@Service
public class DeploymentScheduler {

    private static final Logger log = LoggerFactory.getLogger(DeploymentScheduler.class);

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(4, r -> {
        Thread t = new Thread(r, "deploy-scheduler");
        t.setDaemon(true);
        return t;
    });

    /** Tracks pending tasks so they can be cancelled on delete or re-schedule. */
    private final Map<String, ScheduledFuture<?>> deployTasks = new ConcurrentHashMap<>();
    private final Map<String, ScheduledFuture<?>> undeployTasks = new ConcurrentHashMap<>();

    private final List<PlatformDeployer> deployers;
    private final AiModelRepositoryService repoService;
    private final org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService specRepoService;
    private final TransactionTemplate txTemplate;
    private final ApplicationEventPublisher eventPublisher;

    public DeploymentScheduler(List<PlatformDeployer> deployers,
                               AiModelRepositoryService repoService,
                               org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService specRepoService,
                               PlatformTransactionManager txManager,
                               ApplicationEventPublisher eventPublisher) {
        this.deployers = deployers;
        this.repoService = repoService;
        this.specRepoService = specRepoService;
        this.txTemplate = new TransactionTemplate(txManager);
        this.eventPublisher = eventPublisher;
    }

    // ── Startup recovery ────────────────────────────────────────────────

    /**
     * On application startup, re-schedules pending work that was lost when the process stopped:
     * <ul>
     *   <li>RESERVED models → re-schedule deployment (or deploy immediately if startDate has passed)</li>
     *   <li>ACTIVE models with an endDate → re-schedule teardown (or tear down immediately if endDate has passed)</li>
     * </ul>
     */
    @EventListener(ApplicationReadyEvent.class)
    void recoverOnStartup() {
        if (deployers.isEmpty()) {
            log.info("No platform deployers configured – skipping deployment recovery");
            return;
        }

        log.info("Recovering scheduled deployments after restart…");

        try {
            txTemplate.executeWithoutResult(status -> {
            List<AiModel> reserved = repoService.findByState(ServiceStateType.RESERVED);
            for (AiModel model : reserved) {
                if (findDeployer(model) != null) {
                    log.info("Re-scheduling deployment for RESERVED AiModel {} (startDate={})",
                            model.getId(), model.getStartDate());
                    scheduleDeploy(model);
                } else {
                    log.warn("RESERVED AiModel {} has no matching deployer – skipping recovery", model.getId());
                }
            }

            List<AiModel> active = repoService.findByState(ServiceStateType.ACTIVE);
            long teardowns = 0;
            for (AiModel model : active) {
                OffsetDateTime endDate = model.getEndDate();
                if (endDate == null) continue;
                log.info("Re-scheduling teardown for ACTIVE AiModel {} (endDate={})", model.getId(), endDate);
                scheduleUndeploy(model.getId(), endDate);
                teardowns++;
            }

            log.info("Recovery complete: {} RESERVED re-scheduled, {} ACTIVE teardowns re-scheduled",
                    reserved.size(), teardowns);
            });
        } catch (Exception e) {
            log.warn("Startup recovery skipped (database tables may not exist yet): {}", e.getMessage());
        }
    }

    // ── Public API ──────────────────────────────────────────────────────

    /**
     * Schedules (or immediately executes) deployment and subsequent undeployment.
     * Expects {@code startDate} and {@code endDate} to already be set on the entity.
     *
     * @param aiModel the persisted AiModel (already saved with startDate/endDate)
     * @return the AiModel — if deployed immediately the reloaded entity is returned,
     *         otherwise the original (still RESERVED) entity
     */
    public AiModel scheduleDeploy(AiModel aiModel) {
        String id = aiModel.getId();
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime startDate = aiModel.getStartDate();
        OffsetDateTime endDate = aiModel.getEndDate();

        if (startDate == null || !startDate.isAfter(now)) {
            // Deploy now in background to prevent HTTP blocking
            log.info("Scheduling immediate background deployment for AiModel {} (startDate={})", id, startDate);
            cancelExisting(deployTasks, id);
            ScheduledFuture<?> deployFuture = executor.schedule(
                    () -> runSafely("deployment", id, () -> {
                        deployTasks.remove(id);
                        executeDeployment(id);
                        if (endDate != null) {
                            scheduleUndeploy(id, endDate);
                        }
                    }), 0, TimeUnit.MILLISECONDS);
            deployTasks.put(id, deployFuture);
            return aiModel;
        }

        // startDate is in the future → schedule
        long deployDelay = Duration.between(now, startDate).toMillis();
        log.info("Scheduling deployment of AiModel {} in {}ms (startDate={})", id, deployDelay, startDate);

        cancelExisting(deployTasks, id);
        ScheduledFuture<?> deployFuture = executor.schedule(
                () -> runSafely("deployment", id, () -> {
                    deployTasks.remove(id);
                    executeDeployment(id);
                    if (endDate != null) {
                        scheduleUndeploy(id, endDate);
                    }
                }),
                deployDelay, TimeUnit.MILLISECONDS);

        deployTasks.put(id, deployFuture);
        return aiModel;
    }

    /**
     * Cancels any pending deploy/undeploy tasks for the given AiModel.
     */
    public void cancelScheduled(String aiModelId) {
        cancelExisting(deployTasks, aiModelId);
        cancelExisting(undeployTasks, aiModelId);
        log.info("Cancelled all scheduled tasks for AiModel {}", aiModelId);
    }

    @PreDestroy
    void shutdown() {
        executor.shutdownNow();
    }

    // ── Internal ────────────────────────────────────────────────────────

    /**
     * Runs a scheduled task and logs anything it throws. Without this, an exception escaping a
     * task is stored in its {@link ScheduledFuture}, which nobody reads, and disappears.
     */
    private void runSafely(String what, String aiModelId, Runnable task) {
        try {
            task.run();
        } catch (Throwable t) {
            log.error("Scheduled {} of AiModel {} failed unexpectedly", what, aiModelId, t);
        }
    }

    /**
     * Deploys a RESERVED model in three steps (handoff 5.6, 5.5, 4.1):
     * <ol>
     *   <li>read the model (its own short read-only transaction);</li>
     *   <li>deploy, <b>outside any transaction</b>: building an image can take minutes, and must
     *       not hold a database connection meanwhile;</li>
     *   <li>record the result in a short transaction, only if the model is still RESERVED. If it
     *       was stopped or deleted during step 2, the new deployment is undeployed instead.</li>
     * </ol>
     * State changes are published as {@code AiModelStateChangeEvent} after their transaction commits.
     */
    void executeDeployment(String aiModelId) {
        AiModel aiModel = repoService.findAiModelById(aiModelId);
        if (aiModel == null) {
            log.warn("AiModel {} no longer exists – skipping deployment", aiModelId);
            return;
        }
        if (aiModel.getState() != ServiceStateType.RESERVED) {
            log.info("AiModel {} is {} rather than RESERVED – skipping deployment", aiModelId, aiModel.getState());
            return;
        }
        PlatformDeployer deployer = findDeployer(aiModel);
        if (deployer == null) {
            log.warn("No deployer supports AiModel {} – skipping deployment", aiModelId);
            return;
        }

        List<Characteristic> recorded;
        try {
            recorded = deployer.deploy(aiModel);
        } catch (Exception e) {
            updateModelFileExistsCharacteristic(aiModel, false);
            recordFailure(aiModelId, e);
            return;
        }
        updateModelFileExistsCharacteristic(aiModel, true);

        List<Characteristic> result = recorded != null ? recorded : List.of();
        AiModel active = repoService.updateIfInState(aiModelId, ServiceStateType.RESERVED, model -> {
            setCharacteristics(model, result);
            model.setState(ServiceStateType.ACTIVE);
        });
        if (active == null) {
            // Stopped (PATCH inactive) or deleted while the image was building: nothing records
            // this deployment, so remove it rather than leave a container running on its port.
            log.info("AiModel {} was stopped or deleted during deployment – removing the new deployment", aiModelId);
            setCharacteristics(aiModel, result);
            try {
                deployer.undeploy(aiModel);
            } catch (Exception e) {
                log.error("Could not remove the deployment of AiModel {} made after it was stopped", aiModelId, e);
            }
            return;
        }
        log.info("AiModel {} is ACTIVE", aiModelId);
        eventPublisher.publishEvent(new AiModelDomainEvent(active, AiModelDomainEvent.EventKind.STATE_CHANGE));
    }

    /** Sets the model to DESIGNED with a short note, if it is still RESERVED. */
    private void recordFailure(String aiModelId, Exception e) {
        // The note is readable by every API user; it gets a short reason and a reference.
        // The full error (it can hold internal URLs and command output) is logged only.
        String reference = UUID.randomUUID().toString().substring(0, 8);
        log.error("Scheduled deployment failed for AiModel {} (reference {}): {}",
                aiModelId, reference, e.getMessage(), e);

        org.etsi.osl.controllers.tmf915.model.Note errorNote = new org.etsi.osl.controllers.tmf915.model.Note();
        errorNote.setText(publicReason(e) + " (reference " + reference + "; details are in the server log)");
        errorNote.setDate(OffsetDateTime.now());
        errorNote.setAuthor("TMF915_DeploymentScheduler");

        AiModel designed = repoService.updateIfInState(aiModelId, ServiceStateType.RESERVED, model -> {
            List<org.etsi.osl.controllers.tmf915.model.Note> notes =
                    new ArrayList<>(model.getNote() != null ? model.getNote() : List.of());
            notes.add(errorNote);
            model.setNote(notes);
            model.setState(ServiceStateType.DESIGNED);
        });
        if (designed != null) {
            eventPublisher.publishEvent(new AiModelDomainEvent(designed, AiModelDomainEvent.EventKind.STATE_CHANGE));
        } else {
            log.info("AiModel {} was stopped or deleted during the failed deployment – state left as is", aiModelId);
        }
    }

    /**
     * Sets each characteristic on the model, replacing an existing one with the same name (its
     * value is updated in place) and dropping further duplicates. The given objects are copied,
     * never attached, so detached entities do not end up in a managed collection.
     */
    static void setCharacteristics(AiModel model, List<Characteristic> values) {
        if (model.getServiceCharacteristic() == null) {
            model.setServiceCharacteristic(new ArrayList<>());
        }
        List<Characteristic> current = model.getServiceCharacteristic();
        for (Characteristic value : values) {
            Characteristic existing = null;
            Iterator<Characteristic> it = current.iterator();
            while (it.hasNext()) {
                Characteristic c = it.next();
                if (c != null && value.getName().equals(c.getName())) {
                    if (existing == null) {
                        existing = c;
                    } else {
                        it.remove();
                    }
                }
            }
            if (existing == null) {
                existing = new Characteristic();
                existing.setName(value.getName());
                current.add(existing);
            }
            existing.setValue(value.getValue());
            existing.setValueType(value.getValueType());
        }
    }

    private void scheduleUndeploy(String aiModelId, OffsetDateTime endDate) {
        long delay = Duration.between(OffsetDateTime.now(), endDate).toMillis();
        if (delay <= 0) {
            // Already past endDate — undeploy immediately
            executeUndeploy(aiModelId);
            return;
        }

        log.info("Scheduling undeployment of AiModel {} in {}ms (endDate={})", aiModelId, delay, endDate);
        cancelExisting(undeployTasks, aiModelId);
        ScheduledFuture<?> future = executor.schedule(
                () -> runSafely("undeployment", aiModelId, () -> {
                    undeployTasks.remove(aiModelId);
                    executeUndeploy(aiModelId);
                }),
                delay, TimeUnit.MILLISECONDS);

        undeployTasks.put(aiModelId, future);
    }

    /**
     * Tears down an ACTIVE model at its {@code endDate}, in the same three steps as
     * {@link #executeDeployment}: read, undeploy outside a transaction, then record TERMINATED
     * and remove the characteristics the deployer removed, only if the model is still ACTIVE.
     */
    void executeUndeploy(String aiModelId) {
        AiModel aiModel = repoService.findAiModelById(aiModelId);
        if (aiModel == null) {
            log.warn("AiModel {} no longer exists – skipping undeployment", aiModelId);
            return;
        }
        if (aiModel.getState() != ServiceStateType.ACTIVE) {
            log.info("AiModel {} is not ACTIVE (state={}) – skipping undeployment", aiModelId, aiModel.getState());
            return;
        }

        Set<String> before = characteristicNames(aiModel);
        PlatformDeployer deployer = findDeployer(aiModel);
        if (deployer != null) {
            try {
                deployer.undeploy(aiModel);
            } catch (Exception e) {
                // The container may still be running: keep the model ACTIVE rather than claim otherwise.
                log.error("Undeployment of AiModel {} at its endDate failed; it stays ACTIVE", aiModelId, e);
                return;
            }
        } else {
            log.warn("No deployer supports AiModel {} – cannot undeploy cleanly", aiModelId);
        }
        Set<String> removed = new HashSet<>(before);
        removed.removeAll(characteristicNames(aiModel));

        AiModel terminated = repoService.updateIfInState(aiModelId, ServiceStateType.ACTIVE, model -> {
            if (model.getServiceCharacteristic() != null) {
                model.getServiceCharacteristic().removeIf(c -> c != null && removed.contains(c.getName()));
            }
            model.setState(ServiceStateType.TERMINATED);
        });
        if (terminated != null) {
            log.info("AiModel {} undeployed and set to TERMINATED (endDate reached)", aiModelId);
            eventPublisher.publishEvent(new AiModelDomainEvent(terminated, AiModelDomainEvent.EventKind.STATE_CHANGE));
        }
    }

    private static Set<String> characteristicNames(AiModel model) {
        Set<String> names = new HashSet<>();
        if (model.getServiceCharacteristic() != null) {
            for (Characteristic c : model.getServiceCharacteristic()) {
                if (c != null && c.getName() != null) {
                    names.add(c.getName());
                }
            }
        }
        return names;
    }

    /** The client-safe part of a deployment failure; see {@link DeploymentException}. */
    static String publicReason(Throwable e) {
        return e instanceof DeploymentException de ? de.getPublicReason() : "Deployment failed";
    }

    private PlatformDeployer findDeployer(AiModel model) {
        return deployers.stream()
                .filter(d -> d.supports(model))
                .findFirst()
                .orElse(null);
    }

    private void cancelExisting(Map<String, ScheduledFuture<?>> map, String id) {
        ScheduledFuture<?> existing = map.remove(id);
        if (existing != null && !existing.isDone()) {
            existing.cancel(false);
        }
    }

    private void updateModelFileExistsCharacteristic(AiModel aiModel, boolean exists) {
        if (aiModel.getAiModelSpecification() != null && aiModel.getAiModelSpecification().getId() != null) {
            String specId = aiModel.getAiModelSpecification().getId();
            org.etsi.osl.controllers.tmf915.model.AiModelSpecification spec = specRepoService.findAiModelSpecificationById(specId);
            
            if (spec != null) {
                org.etsi.osl.controllers.tmf915.model.AiModelSpecificationUpdate specUpdate = new org.etsi.osl.controllers.tmf915.model.AiModelSpecificationUpdate();
                
                boolean characteristicFound = false;
                List<org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification> chars = spec.getSpecCharacteristic();
                
                if (chars != null) {
                    for (org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification cs : chars) {
                        if ("model_file_exists".equals(cs.getName())) {
                            characteristicFound = true;
                            break;
                        }
                    }
                }
                
                // Note: According to OpenAPI TMF specs, typically characteristics in update REPLACE or merge.
                // It depends on the application's mapper. Assuming we must provide the characteristic to add it:
                if (!characteristicFound) {
                    org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification newChar = new org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification();
                    newChar.setName("model_file_exists");
                    newChar.setValueType("boolean");
                    
                    org.etsi.osl.controllers.tmf915.model.CharacteristicValueSpecification valSpec = new org.etsi.osl.controllers.tmf915.model.CharacteristicValueSpecification();
                    valSpec.setValue(exists);
                    newChar.addCharacteristicValueSpecificationItem(valSpec);
                    
                    if (chars != null) {
                        // copy existing to update
                        List<org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification> updatedChars = new java.util.ArrayList<>(chars);
                        updatedChars.add(newChar);
                        specUpdate.setSpecCharacteristic(updatedChars);
                    } else {
                        specUpdate.addSpecCharacteristicItem(newChar);
                    }
                } else {
                    // Update existing
                    List<org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification> updatedChars = new java.util.ArrayList<>();
                    for (org.etsi.osl.controllers.tmf915.model.CharacteristicSpecification cs : chars) {
                        if ("model_file_exists".equals(cs.getName())) {
                            // Find and update value or add new one
                            List<org.etsi.osl.controllers.tmf915.model.CharacteristicValueSpecification> vals = cs.getCharacteristicValueSpecification();
                            if (vals != null && !vals.isEmpty()) {
                                vals.get(0).setValue(exists);
                            } else {
                                org.etsi.osl.controllers.tmf915.model.CharacteristicValueSpecification valSpec = new org.etsi.osl.controllers.tmf915.model.CharacteristicValueSpecification();
                                valSpec.setValue(exists);
                                cs.addCharacteristicValueSpecificationItem(valSpec);
                            }
                        }
                        updatedChars.add(cs);
                    }
                    specUpdate.setSpecCharacteristic(updatedChars);
                }
                
                try {
                    specRepoService.updateAiModelSpecification(specId, specUpdate);
                } catch (Exception e) {
                    log.warn("Failed to update model_file_exists characteristic on AiModelSpecification {}: {}", specId, e.getMessage());
                }
            }
        }
    }
}
