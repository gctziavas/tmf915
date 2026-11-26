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

import java.io.IOException;
import java.util.Collections;

import org.etsi.osl.controllers.tmf915.model.AiModelUpdate;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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

    // ── 5.12: failure notes carry a short reason, not the raw error ──────

    private String noteAfterFailedDeployment(Exception failure) throws Exception {
        AiModel model = new AiModel();
        model.setId("model-fail");
        when(repoService.findAiModelById("model-fail")).thenReturn(model);
        when(deployer.supports(any())).thenReturn(true);
        doThrow(failure).when(deployer).deploy(any());

        scheduler.scheduleDeploy(model);

        ArgumentCaptor<AiModelUpdate> update = ArgumentCaptor.forClass(AiModelUpdate.class);
        verify(repoService, timeout(5000)).updateAiModel(eq("model-fail"), update.capture());
        assertEquals(ServiceStateType.DESIGNED, update.getValue().getState());
        return update.getValue().getNote().get(update.getValue().getNote().size() - 1).getText();
    }

    @Test
    public void failedDeployment_noteShowsOnlyThePublicReason() throws Exception {
        String internal = "Build failed (exit 1): botocore NoSuchBucket s3://internal-bucket/secret/path "
                + "http://172.16.100.94:9010 /home/runner/.cache/pip";
        String note = noteAfterFailedDeployment(
                new DeploymentException("Image build failed", new IOException(internal)));

        assertTrue(note.startsWith("Image build failed (reference "), note);
        assertTrue(note.contains("details are in the server log"), note);
        assertFalse(note.contains("s3://"), note);
        assertFalse(note.contains("172.16"), note);
        assertFalse(note.contains("exit 1"), note);
    }

    @Test
    public void failedDeployment_unclassifiedError_isGeneric() throws Exception {
        String note = noteAfterFailedDeployment(new IllegalStateException("NPE in jdbc:mysql://mysql:3306/aim915db"));
        assertTrue(note.startsWith("Deployment failed (reference "), note);
        assertFalse(note.contains("mysql"), note);
    }
}
