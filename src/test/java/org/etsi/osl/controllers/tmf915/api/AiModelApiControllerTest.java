package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.deployment.AiModelLifecycleService;
import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelUpdate;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AiModelApiControllerTest {

    @Mock
    private AiModelRepositoryService repoService;

    @Mock
    private AiModelLifecycleService lifecycleService;

    @InjectMocks
    private AiModelApiController controller;

    private AiModel aiModel;

    @BeforeEach
    public void setup() {
        aiModel = new AiModel();
        aiModel.setId("test-id");
    }

    @Test
    public void testCreateAiModel() {
        AiModelCreate create = new AiModelCreate();
        when(lifecycleService.createAiModel(any())).thenReturn(aiModel);
        
        ResponseEntity<AiModel> response = controller.createAiModel(create);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(aiModel, response.getBody());
    }

    @Test
    public void testDeleteAiModel() {
        doNothing().when(lifecycleService).deleteAiModel("test-id");
        
        ResponseEntity<Void> response = controller.deleteAiModel("test-id");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testListAiModel() {
        when(repoService.findAllAiModels()).thenReturn(Collections.singletonList(aiModel));
        
        ResponseEntity<List<AiModel>> response = controller.listAiModel(null, null, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testPatchAiModel() {
        AiModelUpdate update = new AiModelUpdate();
        when(lifecycleService.updateAiModel(eq("test-id"), any())).thenReturn(aiModel);
        
        ResponseEntity<AiModel> response = controller.patchAiModel("test-id", update);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(aiModel, response.getBody());
    }

    @Test
    public void testRetrieveAiModel_Found() {
        when(repoService.findAiModelById("test-id")).thenReturn(aiModel);
        
        ResponseEntity<AiModel> response = controller.retrieveAiModel("test-id", null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(aiModel, response.getBody());
    }

    @Test
    public void testRetrieveAiModel_NotFound() {
        when(repoService.findAiModelById("unknown")).thenReturn(null);
        
        ResponseEntity<AiModel> response = controller.retrieveAiModel("unknown", null);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testHandleBadRequest() {
        ResponseEntity<String> response = controller.handleBadRequest(new IllegalArgumentException("test error"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("test error", response.getBody());
    }
}
