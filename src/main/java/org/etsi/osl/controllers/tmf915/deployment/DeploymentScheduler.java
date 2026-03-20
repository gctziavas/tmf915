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
    private final TransactionTemplate txTemplate;

    public DeploymentScheduler(List<PlatformDeployer> deployers,
                               AiModelRepositoryService repoService,
                               PlatformTransactionManager txManager) {
        this.deployers = deployers;
        this.repoService = repoService;
        this.txTemplate = new TransactionTemplate(txManager);
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
            // Deploy now
            log.info("Deploying AiModel {} immediately (startDate={})", id, startDate);
            AiModel deployed = executeDeployment(id);
            if (endDate != null) {
                scheduleUndeploy(id, endDate);
            }
            return deployed != null ? deployed : aiModel;
        }

        // startDate is in the future → schedule
        long deployDelay = Duration.between(now, startDate).toMillis();
        log.info("Scheduling deployment of AiModel {} in {}ms (startDate={})", id, deployDelay, startDate);

        cancelExisting(deployTasks, id);
        ScheduledFuture<?> deployFuture = executor.schedule(
                () -> {
                    deployTasks.remove(id);
                    executeDeployment(id);
                    if (endDate != null) {
                        scheduleUndeploy(id, endDate);
                    }
                },
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

    private AiModel executeDeployment(String aiModelId) {
        return txTemplate.execute(status -> {
            AiModel aiModel = repoService.findAiModelById(aiModelId);
            if (aiModel == null) {
                log.warn("AiModel {} no longer exists – skipping deployment", aiModelId);
                return null;
            }

            PlatformDeployer deployer = findDeployer(aiModel);
            if (deployer == null) {
                log.warn("No deployer supports AiModel {} – skipping deployment", aiModelId);
                return aiModel;
            }

            try {
                deployer.deploy(aiModel);
                return repoService.findAiModelById(aiModelId);
            } catch (IOException e) {
                log.error("Scheduled deployment failed for AiModel {}: {}", aiModelId, e.getMessage());
                repoService.updateAiModelState(aiModelId, ServiceStateType.DESIGNED);
                return aiModel;
            }
        });
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
                () -> {
                    undeployTasks.remove(aiModelId);
                    executeUndeploy(aiModelId);
                },
                delay, TimeUnit.MILLISECONDS);

        undeployTasks.put(aiModelId, future);
    }

    private void executeUndeploy(String aiModelId) {
        txTemplate.executeWithoutResult(status -> {
            AiModel aiModel = repoService.findAiModelById(aiModelId);
            if (aiModel == null) {
                log.warn("AiModel {} no longer exists – skipping undeployment", aiModelId);
                return;
            }
            if (aiModel.getState() != ServiceStateType.ACTIVE) {
                log.info("AiModel {} is not ACTIVE (state={}) – skipping undeployment", aiModelId, aiModel.getState());
                return;
            }

            PlatformDeployer deployer = findDeployer(aiModel);
            if (deployer != null) {
                deployer.undeploy(aiModel);
            } else {
                log.warn("No deployer supports AiModel {} – cannot undeploy cleanly", aiModelId);
            }

            repoService.updateAiModelState(aiModelId, ServiceStateType.TERMINATED);
            log.info("AiModel {} undeployed and set to TERMINATED (endDate reached)", aiModelId);
        });
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
}
