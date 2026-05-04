package org.etsi.osl.controllers.tmf915.deployment;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelRepositoryService;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeploymentSchedulerTest {

    @Mock
    private AiModelRepositoryService repoService;
    
    @Mock
    private AiModelSpecificationRepositoryService specRepoService;
    
    @Mock
    private PlatformTransactionManager txManager;
    
    @Mock
    private PlatformDeployer deployer;
    
    private DeploymentScheduler scheduler;

    @BeforeEach
    public void setup() {
        scheduler = new DeploymentScheduler(Collections.singletonList(deployer), repoService, specRepoService, txManager);
    }
    
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

    @Test
    public void testScheduleDeploy() {
        AiModel model = new AiModel();
        model.setId("model-sched");
        scheduler.scheduleDeploy(model);
    }
}
