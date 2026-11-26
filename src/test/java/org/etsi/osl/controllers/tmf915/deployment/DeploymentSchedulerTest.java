package org.etsi.osl.controllers.tmf915.deployment;

import org.etsi.osl.controllers.tmf915.integrations.hub.AiModelDomainEvent;
import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.Characteristic;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelRepositoryService;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DeploymentSchedulerTest {

    @Mock
    private AiModelRepositoryService repoService;

    @Mock
    private AiModelSpecificationRepositoryService specRepoService;

    @Mock
    private PlatformTransactionManager txManager;

    @Mock
    private PlatformDeployer deployer;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private DeploymentScheduler scheduler;

    /** The stored model, as the in-memory stand-in for the database. Null when deleted. */
    private final AtomicReference<AiModel> stored = new AtomicReference<>();

    @BeforeEach
    public void setup() {
        scheduler = new DeploymentScheduler(Collections.singletonList(deployer), repoService, specRepoService,
                txManager, eventPublisher);
        when(deployer.supports(any())).thenReturn(true);
        when(repoService.findAiModelById(anyString())).thenAnswer(inv -> copy(stored.get()));
        when(repoService.updateIfInState(anyString(), any(), any())).thenAnswer(inv -> {
            AiModel current = stored.get();
            if (current == null || current.getState() != inv.getArgument(1)) {
                return null;
            }
            Consumer<AiModel> change = inv.getArgument(2);
            change.accept(current);
            return copy(current);
        });
    }

    private static AiModel copy(AiModel source) {
        if (source == null) return null;
        AiModel copy = new AiModel();
        copy.setId(source.getId());
        copy.setState(source.getState());
        copy.setNote(source.getNote() == null ? null : new ArrayList<>(source.getNote()));
        List<Characteristic> chars = new ArrayList<>();
        for (Characteristic c : source.getServiceCharacteristic()) {
            chars.add(characteristic(c.getName(), String.valueOf(c.getValue())));
        }
        copy.setServiceCharacteristic(chars);
        return copy;
    }

    private static Characteristic characteristic(String name, String value) {
        Characteristic c = new Characteristic();
        c.setName(name);
        c.setValue(value);
        c.setValueType("string");
        return c;
    }

    private void store(String id, ServiceStateType state, Characteristic... chars) {
        AiModel model = new AiModel();
        model.setId(id);
        model.setState(state);
        model.setServiceCharacteristic(new ArrayList<>(List.of(chars)));
        stored.set(model);
    }

    private static String value(AiModel model, String name) {
        return model.getServiceCharacteristic().stream().filter(c -> name.equals(c.getName()))
                .map(c -> String.valueOf(c.getValue())).findFirst().orElse(null);
    }

    private AiModelDomainEvent onlyEvent() {
        ArgumentCaptor<AiModelDomainEvent> event = ArgumentCaptor.forClass(AiModelDomainEvent.class);
        verify(eventPublisher).publishEvent(event.capture());
        return event.getValue();
    }

    // ── existing behaviour ──────────────────────────────────────────────

    @Test
    public void testRecoverPendingDeploymentsOnStartup() {
        AiModel model = new AiModel();
        model.setId("model-1");
        model.setState(ServiceStateType.RESERVED);
        when(repoService.findByState(ServiceStateType.RESERVED)).thenReturn(Collections.singletonList(model));
        when(repoService.findByState(ServiceStateType.ACTIVE)).thenReturn(Collections.emptyList());

        scheduler.recoverOnStartup();

        verify(repoService).findByState(ServiceStateType.RESERVED);
        verify(repoService).findByState(ServiceStateType.ACTIVE);
    }

    @Test
    public void testCancelScheduled() {
        scheduler.cancelScheduled("model-1");
    }

    // ── successful deployment (5.6, 4.1) ────────────────────────────────

    @Test
    public void deployment_recordsTheResultAndPublishesActive() throws Exception {
        store("m1", ServiceStateType.RESERVED,
                characteristic("platform", "mlflow"), characteristic("containerId", "stale"));
        when(deployer.deploy(any())).thenReturn(List.of(
                characteristic("containerId", "c-new"), characteristic("endpoint", "http://h:3000/invocations")));

        scheduler.executeDeployment("m1");

        AiModel model = stored.get();
        assertEquals(ServiceStateType.ACTIVE, model.getState());
        assertEquals("c-new", value(model, "containerId"));
        assertEquals(1, model.getServiceCharacteristic().stream().filter(c -> "containerId".equals(c.getName())).count());
        assertEquals("http://h:3000/invocations", value(model, "endpoint"));
        assertEquals("mlflow", value(model, "platform"));

        AiModelDomainEvent event = onlyEvent();
        assertEquals(AiModelDomainEvent.EventKind.STATE_CHANGE, event.kind());
        assertEquals(ServiceStateType.ACTIVE, event.aiModel().getState());
    }

    @Test
    public void deployment_ofAModelThatIsNoLongerReserved_doesNothing() throws Exception {
        store("m1", ServiceStateType.TERMINATED);
        scheduler.executeDeployment("m1");
        verify(deployer, never()).deploy(any());
        verifyNoInteractions(eventPublisher);
    }

    // ── failed deployment (5.12, 5.6, 4.1) ──────────────────────────────

    @Test
    public void failedDeployment_noteShowsOnlyThePublicReason_andDesignedIsPublished() throws Exception {
        store("m1", ServiceStateType.RESERVED);
        String internal = "Build failed (exit 1): botocore NoSuchBucket s3://internal-bucket/secret/path "
                + "http://172.16.100.94:9010 /home/runner/.cache/pip";
        when(deployer.deploy(any())).thenThrow(new DeploymentException("Image build failed", new IOException(internal)));

        scheduler.executeDeployment("m1");

        AiModel model = stored.get();
        assertEquals(ServiceStateType.DESIGNED, model.getState());
        String note = model.getNote().get(model.getNote().size() - 1).getText();
        assertTrue(note.startsWith("Image build failed (reference "), note);
        assertTrue(note.contains("details are in the server log"), note);
        assertFalse(note.contains("s3://"), note);
        assertFalse(note.contains("172.16"), note);
        assertEquals(ServiceStateType.DESIGNED, onlyEvent().aiModel().getState());
    }

    @Test
    public void failedDeployment_unclassifiedError_isGeneric() throws Exception {
        store("m1", ServiceStateType.RESERVED);
        when(deployer.deploy(any())).thenThrow(new IllegalStateException("NPE in jdbc:mysql://mysql:3306/aim915db"));

        scheduler.executeDeployment("m1");

        String note = stored.get().getNote().get(0).getText();
        assertTrue(note.startsWith("Deployment failed (reference "), note);
        assertFalse(note.contains("mysql"), note);
    }

    @Test
    public void failedDeployment_ofAModelStoppedMeanwhile_keepsItsState() throws Exception {
        store("m1", ServiceStateType.RESERVED);
        when(deployer.deploy(any())).thenAnswer(inv -> {
            stored.get().setState(ServiceStateType.TERMINATED);   // client PATCHed inactive during the build
            throw new DeploymentException("Image build failed", new IOException("x"));
        });

        scheduler.executeDeployment("m1");

        assertEquals(ServiceStateType.TERMINATED, stored.get().getState());
        verifyNoInteractions(eventPublisher);
    }

    // ── stopped or deleted during the build (5.5) ───────────────────────

    @Test
    public void modelStoppedDuringBuild_newDeploymentIsRemoved_andStateStays() throws Exception {
        store("m1", ServiceStateType.RESERVED, characteristic("platform", "mlflow"));
        when(deployer.deploy(any())).thenAnswer(inv -> {
            stored.get().setState(ServiceStateType.TERMINATED);
            return List.of(characteristic("containerId", "c-orphan"), characteristic("imageName", "img"));
        });

        scheduler.executeDeployment("m1");

        assertEquals(ServiceStateType.TERMINATED, stored.get().getState());
        assertNull(value(stored.get(), "containerId"), "the orphan deployment must not be recorded");
        ArgumentCaptor<AiModel> undeployed = ArgumentCaptor.forClass(AiModel.class);
        verify(deployer).undeploy(undeployed.capture());
        assertEquals("c-orphan", value(undeployed.getValue(), "containerId"));
        verifyNoInteractions(eventPublisher);
    }

    @Test
    public void modelDeletedDuringBuild_newDeploymentIsRemoved() throws Exception {
        store("m1", ServiceStateType.RESERVED);
        when(deployer.deploy(any())).thenAnswer(inv -> {
            stored.set(null);
            return List.of(characteristic("containerId", "c-orphan"));
        });

        scheduler.executeDeployment("m1");

        verify(deployer).undeploy(argThat(m -> "c-orphan".equals(value(m, "containerId"))));
        verifyNoInteractions(eventPublisher);
    }

    // ── teardown at endDate (5.6, 4.1) ──────────────────────────────────

    @Test
    public void undeployment_recordsTerminated_removesWhatTheDeployerRemoved_andPublishes() {
        store("m1", ServiceStateType.ACTIVE, characteristic("platform", "mlflow"),
                characteristic("containerId", "c1"), characteristic("endpoint", "http://h:3000/invocations"));
        doAnswer(inv -> {
            AiModel m = inv.getArgument(0);
            m.getServiceCharacteristic().removeIf(c -> List.of("containerId", "endpoint").contains(c.getName()));
            return null;
        }).when(deployer).undeploy(any());

        scheduler.executeUndeploy("m1");

        AiModel model = stored.get();
        assertEquals(ServiceStateType.TERMINATED, model.getState());
        assertNull(value(model, "containerId"));
        assertNull(value(model, "endpoint"));
        assertEquals("mlflow", value(model, "platform"));
        assertEquals(ServiceStateType.TERMINATED, onlyEvent().aiModel().getState());
    }

    @Test
    public void undeploymentFailure_keepsTheModelActive() {
        store("m1", ServiceStateType.ACTIVE, characteristic("containerId", "c1"));
        doThrow(new RuntimeException("docker daemon unreachable")).when(deployer).undeploy(any());

        scheduler.executeUndeploy("m1");

        assertEquals(ServiceStateType.ACTIVE, stored.get().getState());
        verifyNoInteractions(eventPublisher);
    }

    // ── scheduled tasks never die silently (5.6) ────────────────────────

    @Test
    public void anExceptionInOneScheduledTask_doesNotStopTheNextOne() throws Exception {
        store("m1", ServiceStateType.RESERVED);
        when(deployer.deploy(any())).thenReturn(List.of());
        when(repoService.updateIfInState(anyString(), any(), any()))
                .thenThrow(new RuntimeException("database unavailable"))
                .thenAnswer(inv -> {
                    Consumer<AiModel> change = inv.getArgument(2);
                    change.accept(stored.get());
                    return copy(stored.get());
                });
        AiModel request = new AiModel();
        request.setId("m1");
        request.setStartDate(OffsetDateTime.now().minusSeconds(1));

        scheduler.scheduleDeploy(request);        // throws inside the task: logged, swallowed
        verify(repoService, timeout(5000).times(1)).updateIfInState(anyString(), any(), any());
        scheduler.scheduleDeploy(request);        // the executor still runs tasks

        verify(repoService, timeout(5000).times(2)).updateIfInState(anyString(), any(), any());
        verify(deployer, times(2)).deploy(any());
    }
}
