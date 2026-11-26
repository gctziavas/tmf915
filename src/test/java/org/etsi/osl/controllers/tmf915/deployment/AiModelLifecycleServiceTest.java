package org.etsi.osl.controllers.tmf915.deployment;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelUpdate;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelRepositoryService;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.etsi.osl.controllers.tmf915.model.Characteristic;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AiModelLifecycleServiceTest {

    @Mock
    private AiModelRepositoryService repoService;
    
    @Mock
    private AiModelSpecificationRepositoryService specRepoService;
    
    @Mock
    private DeploymentScheduler scheduler;
    
    @Mock
    private PlatformTransactionManager transactionManager;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private AiModelLifecycleService service;

    @BeforeEach
    public void setup() {
        service = new AiModelLifecycleService(repoService, specRepoService, scheduler, Collections.emptyList(), transactionManager, eventPublisher);
    }

    @Test
    public void testCreateWithForbiddenState() {
        AiModelCreate create = new AiModelCreate();
        create.setState(ServiceStateType.ACTIVE);
        
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            service.createAiModel(create);
        });
        assertEquals("Cannot create an AiModel in state active. Allowed states: feasibilityChecked, designed, reserved.", ex.getMessage());
    }
    
    @Test
    public void testCreateSimple() {
        AiModelCreate create = new AiModelCreate();
        create.setState(ServiceStateType.DESIGNED);
        create.setName("my-model");
        
        AiModel mockedModel = new AiModel();
        mockedModel.setId("model-1");
        
        lenient().when(repoService.createAiModel(any())).thenReturn(mockedModel);
        
        AiModel result = service.createAiModel(create);
        assertEquals("model-1", result.getId());
    }

    @Test
    public void testCreateReservedState() {
        AiModelCreate create = new AiModelCreate();
        create.setState(ServiceStateType.RESERVED);
        create.setName("my-model-reserved");

        AiModel mockedModel = new AiModel();
        mockedModel.setId("model-res");
        mockedModel.setState(ServiceStateType.RESERVED);

        lenient().when(repoService.createAiModel(any())).thenReturn(mockedModel);
        lenient().when(scheduler.scheduleDeploy(any())).thenReturn(mockedModel);

        TransactionStatus txStatus = mock(TransactionStatus.class);
        lenient().when(transactionManager.getTransaction(any())).thenReturn(txStatus);

        AiModel result = service.createAiModel(create);
        assertEquals("model-res", result.getId());
        verify(scheduler).scheduleDeploy(mockedModel);
    }

    @Test
    public void testUpdateStateToReserved() {
        AiModel existing = new AiModel();
        existing.setId("model-1");
        existing.setState(ServiceStateType.DESIGNED);

        AiModelUpdate update = new AiModelUpdate();
        update.setState(ServiceStateType.RESERVED);

        AiModel updated = new AiModel();
        updated.setId("model-1");
        updated.setState(ServiceStateType.RESERVED);

        lenient().when(repoService.findAiModelById("model-1")).thenReturn(existing);
        lenient().when(repoService.updateAiModel(eq("model-1"), any())).thenReturn(updated);
        lenient().when(scheduler.scheduleDeploy(any())).thenReturn(updated);

        TransactionStatus txStatus = mock(TransactionStatus.class);
        lenient().when(transactionManager.getTransaction(any())).thenReturn(txStatus);

        service.updateAiModel("model-1", update);

        verify(scheduler).scheduleDeploy(updated);
    }

    @Test
    public void testUpdateStateToInactiveAndTerminated() {
        AiModel existing = new AiModel();
        existing.setId("model-1");
        existing.setState(ServiceStateType.ACTIVE);

        AiModelUpdate updateInactive = new AiModelUpdate();
        updateInactive.setState(ServiceStateType.INACTIVE);

        AiModel updatedInactive = new AiModel();
        updatedInactive.setId("model-1");
        updatedInactive.setState(ServiceStateType.INACTIVE);

        lenient().when(repoService.findAiModelById("model-1")).thenReturn(existing);
        lenient().when(repoService.updateAiModel(eq("model-1"), any())).thenReturn(updatedInactive);

        TransactionStatus txStatus = mock(TransactionStatus.class);
        lenient().when(transactionManager.getTransaction(any())).thenReturn(txStatus);

        service.updateAiModel("model-1", updateInactive);
        verify(scheduler).cancelScheduled("model-1");
    }

    @Test
    public void testDeleteModel() {
        AiModel existing = new AiModel();
        existing.setId("model-1");
        
        lenient().when(repoService.findAiModelById("model-1")).thenReturn(existing);

        service.deleteAiModel("model-1");
        
        verify(scheduler).cancelScheduled("model-1");
        verify(repoService).deleteAiModel("model-1");
    }

    // ── 5.1: server-owned characteristics are dropped from client input ──

    private static Characteristic characteristic(String name, String value) {
        Characteristic c = new Characteristic();
        c.setName(name);
        c.setValue(value);
        return c;
    }

    private static List<String> names(List<Characteristic> characteristics) {
        return characteristics.stream().map(Characteristic::getName).toList();
    }

    @Test
    public void createDropsServerOwnedCharacteristics() {
        AiModelCreate create = new AiModelCreate();
        create.setState(ServiceStateType.DESIGNED);
        create.setName("m");
        create.setServiceCharacteristic(new ArrayList<>(List.of(
                characteristic("platform", "mlflow"),
                characteristic("containerId", "mysql"),
                characteristic("imageName", "keycloak"),
                characteristic("endpoint", "http://evil"))));
        AiModel created = new AiModel();
        created.setId("model-1");
        when(repoService.createAiModel(any())).thenReturn(created);

        service.createAiModel(create);

        ArgumentCaptor<AiModelCreate> stored = ArgumentCaptor.forClass(AiModelCreate.class);
        verify(repoService).createAiModel(stored.capture());
        assertEquals(List.of("platform"), names(stored.getValue().getServiceCharacteristic()));
    }

    @Test
    public void patchDropsServerOwnedCharacteristics() {
        AiModelUpdate update = new AiModelUpdate();
        update.setServiceCharacteristic(new ArrayList<>(List.of(
                characteristic("containerId", "mysql"),
                characteristic("hostPort", "3306"),
                characteristic("note", "kept"))));
        when(repoService.updateAiModel(eq("model-1"), any())).thenReturn(new AiModel());

        service.updateAiModel("model-1", update);

        ArgumentCaptor<AiModelUpdate> stored = ArgumentCaptor.forClass(AiModelUpdate.class);
        verify(repoService).updateAiModel(eq("model-1"), stored.capture());
        assertEquals(List.of("note"), names(stored.getValue().getServiceCharacteristic()));
    }

    @Test
    public void deployerValidationRejectsTheRequest() {
        PlatformDeployer strict = mock(PlatformDeployer.class);
        doThrow(new IllegalArgumentException("dockerHost not allowed"))
                .when(strict).validateCharacteristics(any());
        AiModelLifecycleService withDeployer = new AiModelLifecycleService(
                repoService, specRepoService, scheduler, List.of(strict), transactionManager, eventPublisher);

        AiModelCreate create = new AiModelCreate();
        create.setState(ServiceStateType.DESIGNED);
        create.setServiceCharacteristic(new ArrayList<>(List.of(characteristic("dockerHost", "tcp://evil:2375"))));

        assertThrows(IllegalArgumentException.class, () -> withDeployer.createAiModel(create));
        verify(repoService, never()).createAiModel(any());
    }

    // ── 5.13: only reserved and inactive may be requested, from sensible states ──

    private void currentState(ServiceStateType state) {
        AiModel existing = new AiModel();
        existing.setId("model-1");
        existing.setState(state);
        lenient().when(repoService.findAiModelById("model-1")).thenReturn(existing);
    }

    private void assertRejected(ServiceStateType from, ServiceStateType to) {
        currentState(from);
        AiModelUpdate update = new AiModelUpdate();
        update.setState(to);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.updateAiModel("model-1", update), from + " -> " + to);
        assertTrue(ex.getMessage().contains("Cannot change the state"), ex.getMessage());
        verify(repoService, never()).updateAiModel(any(), any());
        verifyNoInteractions(scheduler);
    }

    @Test
    public void patchToActiveIsRejected() {
        assertRejected(ServiceStateType.DESIGNED, ServiceStateType.ACTIVE);
    }

    @Test
    public void patchToTerminatedOnActiveModelIsRejected() {
        assertRejected(ServiceStateType.ACTIVE, ServiceStateType.TERMINATED);
    }

    @Test
    public void patchToDesignedOnActiveModelIsRejected() {
        assertRejected(ServiceStateType.ACTIVE, ServiceStateType.DESIGNED);
    }

    @Test
    public void patchToReservedOnActiveModelIsRejected() {
        // D6: no implicit redeploy; the client sets inactive first.
        assertRejected(ServiceStateType.ACTIVE, ServiceStateType.RESERVED);
    }

    @Test
    public void patchToInactiveOnDesignedModelIsRejected() {
        assertRejected(ServiceStateType.DESIGNED, ServiceStateType.INACTIVE);
    }

    @Test
    public void patchToReservedFromTerminatedIsAllowed() {
        currentState(ServiceStateType.TERMINATED);
        AiModel updated = new AiModel();
        updated.setId("model-1");
        when(repoService.updateAiModel(eq("model-1"), any())).thenReturn(updated);
        when(scheduler.scheduleDeploy(any())).thenReturn(updated);
        AiModelUpdate update = new AiModelUpdate();
        update.setState(ServiceStateType.RESERVED);

        service.updateAiModel("model-1", update);

        verify(scheduler).scheduleDeploy(updated);
    }

    @Test
    public void patchWithCurrentStateIsAnAttributeUpdate() {
        // A client that reads an active model and PATCHes it back unchanged must not get 400.
        currentState(ServiceStateType.ACTIVE);
        when(repoService.updateAiModel(eq("model-1"), any())).thenReturn(new AiModel());
        AiModelUpdate update = new AiModelUpdate();
        update.setState(ServiceStateType.ACTIVE);
        update.setDescription("changed");

        service.updateAiModel("model-1", update);

        ArgumentCaptor<AiModelUpdate> stored = ArgumentCaptor.forClass(AiModelUpdate.class);
        verify(repoService).updateAiModel(eq("model-1"), stored.capture());
        assertNull(stored.getValue().getState());
        assertEquals("changed", stored.getValue().getDescription());
        verifyNoInteractions(scheduler);
    }

    @Test
    public void patchStateOnUnknownModelIsRejected() {
        when(repoService.findAiModelById("missing")).thenReturn(null);
        AiModelUpdate update = new AiModelUpdate();
        update.setState(ServiceStateType.RESERVED);
        assertThrows(IllegalArgumentException.class, () -> service.updateAiModel("missing", update));
    }

    // ── 5.2: derived names never repeat ─────────────────────────────────

    @Test
    public void derivedNameSkipsNamesThatAreTaken() {
        org.etsi.osl.controllers.tmf915.model.AiModelSpecification spec =
                new org.etsi.osl.controllers.tmf915.model.AiModelSpecification();
        spec.setId("spec-1");
        spec.setName("fraud");
        spec.setVersion("m-8a1e");
        when(specRepoService.findAiModelSpecificationById("spec-1")).thenReturn(spec);
        // "-1" was deleted, "-2" still exists: count is 1, so the count alone would pick "-2" again.
        when(repoService.countByNamePrefix("fraud-v-m-8a1e-")).thenReturn(1L);
        when(repoService.existsByName("fraud-v-m-8a1e-2")).thenReturn(true);
        when(repoService.createAiModel(any())).thenAnswer(inv -> {
            AiModel m = new AiModel();
            m.setId("new");
            m.setName(((AiModelCreate) inv.getArgument(0)).getName());
            return m;
        });

        AiModelCreate create = new AiModelCreate();
        create.setState(ServiceStateType.DESIGNED);
        create.setAiModelSpecification(spec);

        assertEquals("fraud-v-m-8a1e-3", service.createAiModel(create).getName());
    }
}
