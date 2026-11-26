package org.etsi.osl.controllers.tmf915.deployment;

import org.etsi.osl.controllers.tmf915.integrations.hub.AiModelDomainEvent;
import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelUpdate;
import org.etsi.osl.controllers.tmf915.model.Characteristic;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.etsi.osl.controllers.tmf915.repo.AiModelRepository;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelRepositoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The three-step deployment on the real context and database (handoff 5.6, 5.5, 4.1), with a fake
 * platform so no Docker or MLflow is needed.
 */
@SpringBootTest
@RecordApplicationEvents
class DeploymentSchedulerSpringTest {

    /** A platform deployer driven by the test. Selected by the characteristic platform=fake. */
    static class FakeDeployer implements PlatformDeployer {
        volatile Boolean transactionActiveDuringDeploy;
        volatile Consumer<String> duringDeploy = id -> {};
        final List<List<String>> undeployedWith = new ArrayList<>();

        @Override
        public boolean supports(AiModel model) {
            return model.getServiceCharacteristic().stream()
                    .anyMatch(c -> "platform".equals(c.getName()) && "fake".equals(String.valueOf(c.getValue())));
        }

        @Override
        public List<Characteristic> deploy(AiModel model) {
            transactionActiveDuringDeploy = TransactionSynchronizationManager.isActualTransactionActive();
            duringDeploy.accept(model.getId());
            return List.of(characteristic("endpoint", "http://fake:3000/invocations"),
                    characteristic("containerId", "fake-container"));
        }

        @Override
        public void undeploy(AiModel model) {
            synchronized (undeployedWith) {
                undeployedWith.add(model.getServiceCharacteristic().stream().map(Characteristic::getName).toList());
            }
            model.getServiceCharacteristic().removeIf(c -> List.of("endpoint", "containerId").contains(c.getName()));
        }
    }

    @TestConfiguration
    static class FakePlatform {
        @Bean
        FakeDeployer fakeDeployer() {
            return new FakeDeployer();
        }
    }

    @Autowired private DeploymentScheduler scheduler;
    @Autowired private AiModelLifecycleService lifecycleService;
    @Autowired private AiModelRepositoryService repoService;
    @Autowired private AiModelRepository repository;
    @Autowired private FakeDeployer fake;
    @Autowired private ApplicationEvents events;

    @BeforeEach
    void reset() {
        fake.transactionActiveDuringDeploy = null;
        fake.duringDeploy = id -> {};
        fake.undeployedWith.clear();
    }

    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

    private static Characteristic characteristic(String name, String value) {
        Characteristic c = new Characteristic();
        c.setName(name);
        c.setValue(value);
        c.setValueType("string");
        return c;
    }

    private String storeModel(ServiceStateType state, Characteristic... chars) {
        AiModel model = new AiModel();
        model.setId(java.util.UUID.randomUUID().toString());
        model.setName("fake-model");
        model.setState(state);
        model.setServiceCharacteristic(new ArrayList<>(List.of(chars)));
        repository.save(model);
        return model.getId();
    }

    private static String value(AiModel model, String name) {
        return model.getServiceCharacteristic().stream().filter(c -> name.equals(c.getName()))
                .map(c -> String.valueOf(c.getValue())).findFirst().orElse(null);
    }

    private List<ServiceStateType> publishedStates() {
        return events.stream(AiModelDomainEvent.class)
                .filter(e -> e.kind() == AiModelDomainEvent.EventKind.STATE_CHANGE)
                .map(e -> e.aiModel().getState()).toList();
    }

    @Test
    void deployRunsOutsideATransaction_andTheResultIsStored() {
        String id = storeModel(ServiceStateType.RESERVED, characteristic("platform", "fake"));

        scheduler.executeDeployment(id);

        assertFalse(fake.transactionActiveDuringDeploy, "a transaction was open during deploy");
        AiModel stored = repoService.findAiModelById(id);
        assertEquals(ServiceStateType.ACTIVE, stored.getState());
        assertEquals("http://fake:3000/invocations", value(stored, "endpoint"));
        assertEquals("fake-container", value(stored, "containerId"));
        assertEquals(List.of(ServiceStateType.ACTIVE), publishedStates());
    }

    @Test
    void clientStopsTheModelDuringTheBuild_containerIsRemoved_stateStaysTerminated() {
        String id = storeModel(ServiceStateType.RESERVED, characteristic("platform", "fake"));
        fake.duringDeploy = modelId -> {
            AiModelUpdate stop = new AiModelUpdate();
            stop.setState(ServiceStateType.INACTIVE);
            lifecycleService.updateAiModel(modelId, stop);   // PATCH {"state":"inactive"} from a client
        };

        scheduler.executeDeployment(id);

        AiModel stored = repoService.findAiModelById(id);
        assertEquals(ServiceStateType.TERMINATED, stored.getState());
        assertNull(value(stored, "endpoint"), "a stopped model must not advertise the orphan endpoint");
        assertTrue(fake.undeployedWith.stream().anyMatch(names -> names.contains("containerId")),
                "the container started after the stop was not removed: " + fake.undeployedWith);
        assertFalse(publishedStates().contains(ServiceStateType.ACTIVE), publishedStates().toString());
    }

    @Test
    void teardownAtEndDate_isStoredAndPublished() {
        String id = storeModel(ServiceStateType.ACTIVE, characteristic("platform", "fake"),
                characteristic("endpoint", "http://fake:3000/invocations"), characteristic("containerId", "c1"));

        scheduler.executeUndeploy(id);

        AiModel stored = repoService.findAiModelById(id);
        assertEquals(ServiceStateType.TERMINATED, stored.getState());
        assertNull(value(stored, "endpoint"));
        assertNull(value(stored, "containerId"));
        assertEquals("fake", value(stored, "platform"));
        assertEquals(List.of(ServiceStateType.TERMINATED), publishedStates());
    }
}
