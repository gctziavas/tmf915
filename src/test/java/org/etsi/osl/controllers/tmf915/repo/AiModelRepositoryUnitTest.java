package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiModelRepositoryUnitTest {

    @Mock
    private AiModelRepository repository;

    private AiModel model(String name, ServiceStateType state) {
        AiModel m = new AiModel();
        m.setId(UUID.randomUUID().toString());
        m.setName(name);
        m.setState(state);
        return m;
    }

    // --- findByNameStartingWith ---

    @Test
    void findByNameStartingWith_matchingPrefix_returnsMatchingModels() {
        AiModel m1 = model("AlphaNet", ServiceStateType.ACTIVE);
        AiModel m2 = model("AlphaBeta", ServiceStateType.INACTIVE);
        when(repository.findByNameStartingWith("Alpha")).thenReturn(List.of(m1, m2));

        List<AiModel> result = repository.findByNameStartingWith("Alpha");
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(m -> m.getName().startsWith("Alpha")));
    }

    @Test
    void findByNameStartingWith_noMatch_returnsEmptyList() {
        when(repository.findByNameStartingWith("Zeta")).thenReturn(List.of());

        List<AiModel> result = repository.findByNameStartingWith("Zeta");
        assertTrue(result.isEmpty());
    }

    @Test
    void findByNameStartingWith_exactPrefix_returnsSingleResult() {
        AiModel m = model("GammaModel", ServiceStateType.ACTIVE);
        when(repository.findByNameStartingWith("Gamma")).thenReturn(List.of(m));

        List<AiModel> result = repository.findByNameStartingWith("Gamma");
        assertEquals(1, result.size());
        assertEquals("GammaModel", result.get(0).getName());
    }

    // --- findByState ---

    @Test
    void findByState_active_returnsOnlyActiveModels() {
        AiModel m1 = model("AlphaNet", ServiceStateType.ACTIVE);
        AiModel m2 = model("GammaModel", ServiceStateType.ACTIVE);
        when(repository.findByState(ServiceStateType.ACTIVE)).thenReturn(List.of(m1, m2));

        List<AiModel> result = repository.findByState(ServiceStateType.ACTIVE);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(m -> m.getState() == ServiceStateType.ACTIVE));
    }

    @Test
    void findByState_inactive_returnsSingleModel() {
        AiModel m = model("AlphaBeta", ServiceStateType.INACTIVE);
        when(repository.findByState(ServiceStateType.INACTIVE)).thenReturn(List.of(m));

        List<AiModel> result = repository.findByState(ServiceStateType.INACTIVE);
        assertEquals(1, result.size());
        assertEquals(ServiceStateType.INACTIVE, result.get(0).getState());
    }

    @Test
    void findByState_noMatch_returnsEmptyList() {
        when(repository.findByState(ServiceStateType.TERMINATED)).thenReturn(List.of());

        List<AiModel> result = repository.findByState(ServiceStateType.TERMINATED);
        assertTrue(result.isEmpty());
    }
}
