package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class AiModelRepositoryTest {

    @Autowired
    private AiModelRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        AiModel m1 = new AiModel();
        m1.setId(UUID.randomUUID().toString());
        m1.setName("AlphaNet");
        m1.setState(ServiceStateType.ACTIVE);
        repository.save(m1);

        AiModel m2 = new AiModel();
        m2.setId(UUID.randomUUID().toString());
        m2.setName("AlphaBeta");
        m2.setState(ServiceStateType.INACTIVE);
        repository.save(m2);

        AiModel m3 = new AiModel();
        m3.setId(UUID.randomUUID().toString());
        m3.setName("GammaModel");
        m3.setState(ServiceStateType.ACTIVE);
        repository.save(m3);
    }

    // --- findByNameStartingWith ---

    @Test
    void findByNameStartingWith_matchingPrefix_returnsMatchingModels() {
        List<AiModel> result = repository.findByNameStartingWith("Alpha");
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(m -> m.getName().startsWith("Alpha")));
    }

    @Test
    void findByNameStartingWith_noMatch_returnsEmptyList() {
        List<AiModel> result = repository.findByNameStartingWith("Zeta");
        assertTrue(result.isEmpty());
    }

    @Test
    void findByNameStartingWith_exactPrefix_returnsSingleResult() {
        List<AiModel> result = repository.findByNameStartingWith("Gamma");
        assertEquals(1, result.size());
        assertEquals("GammaModel", result.get(0).getName());
    }

    // --- findByState ---

    @Test
    void findByState_active_returnsOnlyActiveModels() {
        List<AiModel> result = repository.findByState(ServiceStateType.ACTIVE);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(m -> m.getState() == ServiceStateType.ACTIVE));
    }

    @Test
    void findByState_inactive_returnsSingleModel() {
        List<AiModel> result = repository.findByState(ServiceStateType.INACTIVE);
        assertEquals(1, result.size());
        assertEquals(ServiceStateType.INACTIVE, result.get(0).getState());
    }

    @Test
    void findByState_noMatch_returnsEmptyList() {
        List<AiModel> result = repository.findByState(ServiceStateType.TERMINATED);
        assertTrue(result.isEmpty());
    }
}
