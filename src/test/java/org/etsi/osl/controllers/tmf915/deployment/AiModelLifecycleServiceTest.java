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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private AiModelLifecycleService service;

    @BeforeEach
    public void setup() {
        service = new AiModelLifecycleService(repoService, specRepoService, scheduler, Collections.emptyList(), transactionManager);
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
}
