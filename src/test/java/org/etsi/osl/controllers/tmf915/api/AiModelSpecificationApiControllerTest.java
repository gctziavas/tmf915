package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationUpdate;
import org.etsi.osl.controllers.tmf915.reposervices.AiModelSpecificationRepositoryService;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class AiModelSpecificationApiControllerTest {

    @Mock
    private AiModelSpecificationRepositoryService service;

    @InjectMocks
    private AiModelSpecificationApiController controller;

    private AiModelSpecification aiModelSpecification;

    @BeforeEach
    public void setup() {
        aiModelSpecification = new AiModelSpecification();
        aiModelSpecification.setId("test-id");
    }

    @Test
    public void testCreate() {
        AiModelSpecificationCreate create = new AiModelSpecificationCreate();
        when(service.createAiModelSpecification(any())).thenReturn(aiModelSpecification);
        
        ResponseEntity<AiModelSpecification> response = controller.createAiModelSpecification(create);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(aiModelSpecification, response.getBody());
    }

    @Test
    public void testDelete() {
        doNothing().when(service).deleteAiModelSpecification("test-id");
        
        ResponseEntity<Void> response = controller.deleteAiModelSpecification("test-id");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testList() {
        when(service.findAllAiModelSpecifications(any(), any())).thenReturn(Collections.singletonList(aiModelSpecification));
        
        ResponseEntity<List<AiModelSpecification>> response = controller.listAiModelSpecification(null, null, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testPatch() {
        AiModelSpecificationUpdate update = new AiModelSpecificationUpdate();
        when(service.updateAiModelSpecification(eq("test-id"), any())).thenReturn(aiModelSpecification);
        
        ResponseEntity<AiModelSpecification> response = controller.patchAiModelSpecification("test-id", update);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(aiModelSpecification, response.getBody());
    }

    @Test
    public void testRetrieve_Found() {
        when(service.findAiModelSpecificationById("test-id")).thenReturn(aiModelSpecification);
        
        ResponseEntity<AiModelSpecification> response = controller.retrieveAiModelSpecification("test-id", null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(aiModelSpecification, response.getBody());
    }

    @Test
    public void testRetrieve_NotFound() {
        when(service.findAiModelSpecificationById("unknown")).thenReturn(null);
        
        ResponseEntity<AiModelSpecification> response = controller.retrieveAiModelSpecification("unknown", null);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /*
    @Test
    public void testHandleBadRequest() {
        ResponseEntity<String> response = controller.handleBadRequest(new IllegalArgumentException("test error"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("test error", response.getBody());
    }
    */
}
