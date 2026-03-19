package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelUpdate;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.etsi.osl.controllers.tmf915.repo.AiModelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiModelRepositoryServiceTest {

    @Mock
    private AiModelRepository aiModelRepository;

    @InjectMocks
    private AiModelRepositoryService service;

    @Test
    void findAllAiModels_returnsList() {
        AiModel m1 = new AiModel();
        m1.setId("1");
        m1.setName("Model Alpha");
        AiModel m2 = new AiModel();
        m2.setId("2");
        m2.setName("Model Beta");

        when(aiModelRepository.findAll()).thenReturn(List.of(m1, m2));

        List<AiModel> result = service.findAllAiModels();

        assertEquals(2, result.size());
        verify(aiModelRepository).findAll();
    }

    @Test
    void findAiModelById_found_returnsEntity() {
        AiModel model = new AiModel();
        model.setId("model-1");
        model.setName("My Model");

        when(aiModelRepository.findById("model-1")).thenReturn(Optional.of(model));

        AiModel result = service.findAiModelById("model-1");

        assertNotNull(result);
        assertEquals("model-1", result.getId());
        assertEquals("My Model", result.getName());
    }

    @Test
    void findAiModelById_notFound_returnsNull() {
        when(aiModelRepository.findById("missing")).thenReturn(Optional.empty());

        AiModel result = service.findAiModelById("missing");

        assertNull(result);
    }

    @Test
    void createAiModel_assignsIdAndSaves() {
        AiModelCreate create = new AiModelCreate();
        create.setName("New Model");
        create.setCategory("nlp");
        create.setState(ServiceStateType.INACTIVE);

        when(aiModelRepository.save(any(AiModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        AiModel result = service.createAiModel(create);

        assertNotNull(result.getId());
        assertEquals("New Model", result.getName());
        assertEquals("nlp", result.getCategory());
        assertEquals(ServiceStateType.INACTIVE, result.getState());
        verify(aiModelRepository).save(any(AiModel.class));
    }

    @Test
    void updateAiModel_found_appliesUpdateAndSaves() {
        AiModel existing = new AiModel();
        existing.setId("m-x");
        existing.setName("Old Name");
        existing.setDescription("Old desc");

        AiModelUpdate update = new AiModelUpdate();
        update.setName("New Name");
        update.setDescription("New desc");

        when(aiModelRepository.findById("m-x")).thenReturn(Optional.of(existing));
        when(aiModelRepository.save(any(AiModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        AiModel result = service.updateAiModel("m-x", update);

        assertEquals("New Name", result.getName());
        assertEquals("New desc", result.getDescription());
        verify(aiModelRepository).save(existing);
    }

    @Test
    void updateAiModel_notFound_throwsIllegalArgumentException() {
        when(aiModelRepository.findById("none")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.updateAiModel("none", new AiModelUpdate()));
    }

    @Test
    void deleteAiModel_found_deletesEntity() {
        AiModel model = new AiModel();
        model.setId("del-1");

        when(aiModelRepository.findById("del-1")).thenReturn(Optional.of(model));

        service.deleteAiModel("del-1");

        verify(aiModelRepository).delete(model);
    }

    @Test
    void deleteAiModel_notFound_throwsIllegalArgumentException() {
        when(aiModelRepository.findById("none")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.deleteAiModel("none"));
    }

    @Test
    void updateAiModelState_found_updatesStateAndSaves() {
        AiModel model = new AiModel();
        model.setId("m-state");
        model.setState(ServiceStateType.INACTIVE);

        when(aiModelRepository.findById("m-state")).thenReturn(Optional.of(model));
        when(aiModelRepository.save(any(AiModel.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        AiModel result = service.updateAiModelState("m-state", ServiceStateType.ACTIVE);

        assertEquals(ServiceStateType.ACTIVE, result.getState());
        verify(aiModelRepository).save(model);
    }

    @Test
    void updateAiModelState_notFound_throwsIllegalArgumentException() {
        when(aiModelRepository.findById("none")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.updateAiModelState("none", ServiceStateType.ACTIVE));
    }

    @Test
    void findByNameStartingWith_delegatesToRepository() {
        AiModel m = new AiModel();
        m.setId("m1");
        m.setName("Alpha Model");

        when(aiModelRepository.findByNameStartingWith("Alpha")).thenReturn(List.of(m));

        List<AiModel> result = service.findByNameStartingWith("Alpha");

        assertEquals(1, result.size());
        assertEquals("Alpha Model", result.get(0).getName());
        verify(aiModelRepository).findByNameStartingWith("Alpha");
    }

    @Test
    void findByState_delegatesToRepository() {
        AiModel m1 = new AiModel();
        m1.setId("a1");
        m1.setState(ServiceStateType.ACTIVE);
        AiModel m2 = new AiModel();
        m2.setId("a2");
        m2.setState(ServiceStateType.ACTIVE);

        when(aiModelRepository.findByState(ServiceStateType.ACTIVE)).thenReturn(List.of(m1, m2));

        List<AiModel> result = service.findByState(ServiceStateType.ACTIVE);

        assertEquals(2, result.size());
        verify(aiModelRepository).findByState(ServiceStateType.ACTIVE);
    }
}
