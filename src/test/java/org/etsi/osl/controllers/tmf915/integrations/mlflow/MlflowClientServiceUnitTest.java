package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.mlflow.tracking.MlflowClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;

import java.util.ArrayList;

import org.mlflow.api.proto.Service.Experiment;
import org.mlflow.tracking.Page;

import org.mlflow.tracking.ExperimentsPage;

@ExtendWith(MockitoExtension.class)
class MlflowClientServiceUnitTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MlflowClient mlflowClient;

    private MlflowClientService clientService;

    @BeforeEach
    void setUp() {
        clientService = new MlflowClientService(mlflowClient, restTemplate, "", "", "");
    }

    @Test
    void listExperimentIds_returnsIds() {
        // Arrange
        ExperimentsPage page = mock(ExperimentsPage.class);
        Experiment exp1 = Experiment.newBuilder().setExperimentId("1").build();
        Experiment exp2 = Experiment.newBuilder().setExperimentId("2").build();
        
        when(page.getItems()).thenReturn(List.of(exp1, exp2));
        when(page.hasNextPage()).thenReturn(false);
        
        doReturn(page).when(mlflowClient).searchExperiments();

        // Act
        List<String> result = clientService.listExperimentIds();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("1"));
        assertTrue(result.contains("2"));
        verify(mlflowClient).searchExperiments();
    }

    @Test
    void listLoggedModels_returnsLoggedModels() {
        // Arrange
        LoggedModel model = new LoggedModel();
        LoggedModel.LoggedModelInfo info = new LoggedModel.LoggedModelInfo();
        info.setModelId("m123");
        info.setExperimentId("exp_1");
        info.setArtifactUri("model_path");
        model.setInfo(info);

        MlflowClientService.LoggedModelsSearchResponse responseBody = new MlflowClientService.LoggedModelsSearchResponse();
        responseBody.models = List.of(model);

        ResponseEntity<MlflowClientService.LoggedModelsSearchResponse> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("/api/2.0/mlflow/logged-models/search"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class))
        ).thenReturn(responseEntity);

        // Act
        List<LoggedModel> result = clientService.searchLoggedModels(List.of("exp_1"));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("m123", result.get(0).getInfo().getModelId());
        assertEquals("exp_1", result.get(0).getInfo().getExperimentId());
        assertEquals("model_path", result.get(0).getInfo().getArtifactUri());
    }
}
