package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mlflow.api.proto.ModelRegistry.ModelVersion;
import org.mlflow.api.proto.ModelRegistry.RegisteredModel;
import org.mlflow.api.proto.Service.Experiment;
import org.mlflow.api.proto.Service.Run;
import org.mlflow.tracking.MlflowClient;
import org.mlflow.tracking.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@ConditionalOnProperty(name = "mlflow.enabled", havingValue = "true")
public class MlflowClientService {

    private static final Logger log = LoggerFactory.getLogger(MlflowClientService.class);

    private final MlflowClient mlflowClient;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public MlflowClientService(MlflowClient mlflowClient,
                                @Qualifier("mlflowRestTemplate") RestTemplate restTemplate) {
        this.mlflowClient = mlflowClient;
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    // ── Logged Models (new MLflow 2.x API) ──────────────────────────────

    /**
     * Searches for logged models across the given experiments.
     * Uses the {@code POST /api/2.0/mlflow/logged-models/search} endpoint.
     */
    public List<LoggedModel> searchLoggedModels(List<String> experimentIds) {
        log.debug("Searching logged models for experiments: {}", experimentIds);
        List<LoggedModel> allModels = new ArrayList<>();
        String pageToken = null;

        do {
            Map<String, Object> body = new java.util.LinkedHashMap<>();
            body.put("experiment_ids", experimentIds);
            if (pageToken != null) {
                body.put("page_token", pageToken);
            }

            String jsonBody;
            try {
                jsonBody = objectMapper.writeValueAsString(body);
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize search request body", e);
                break;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

            try {
                ResponseEntity<LoggedModelsSearchResponse> response = restTemplate.exchange(
                        "/api/2.0/mlflow/logged-models/search",
                        HttpMethod.POST,
                        request,
                        new ParameterizedTypeReference<LoggedModelsSearchResponse>() {});

                LoggedModelsSearchResponse searchResponse = response.getBody();
                if (searchResponse != null && searchResponse.models != null) {
                    allModels.addAll(searchResponse.models);
                    pageToken = searchResponse.nextPageToken;
                } else {
                    pageToken = null;
                }
            } catch (RestClientException e) {
                log.warn("Failed to search logged models: {}", e.getMessage());
                pageToken = null;
            }
        } while (pageToken != null && !pageToken.isEmpty());

        log.debug("Found {} logged models", allModels.size());
        return allModels;
    }

    /**
     * Searches for logged models across all experiments.
     */
    public List<LoggedModel> searchAllLoggedModels() {
        log.debug("Fetching all logged models across all experiments");
        List<String> experimentIds = listExperimentIds();
        if (experimentIds.isEmpty()) {
            log.debug("No experiments found in MLflow");
            return List.of();
        }
        return searchLoggedModels(experimentIds);
    }

    /**
     * Gets a specific logged model by its ID.
     */
    public Optional<LoggedModel> getLoggedModel(String modelId) {
        log.debug("Fetching logged model: {}", modelId);
        try {
            ResponseEntity<LoggedModelGetResponse> response = restTemplate.exchange(
                    "/api/2.0/mlflow/logged-models/{modelId}",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<LoggedModelGetResponse>() {},
                    modelId);
            LoggedModelGetResponse body = response.getBody();
            if (body != null && body.model != null) {
                return Optional.of(body.model);
            }
            return Optional.empty();
        } catch (RestClientException e) {
            log.warn("Logged model not found: {}", modelId, e);
            return Optional.empty();
        }
    }

    /**
     * Lists all experiment IDs from the MLflow server.
     */
    public List<String> listExperimentIds() {
        log.debug("Listing all experiment IDs");
        List<String> ids = new ArrayList<>();
        try {
            Page<Experiment> page = mlflowClient.searchExperiments();
            page.getItems().forEach(exp -> ids.add(exp.getExperimentId()));
            while (page.hasNextPage()) {
                page = page.getNextPage();
                page.getItems().forEach(exp -> ids.add(exp.getExperimentId()));
            }
        } catch (Exception e) {
            log.warn("Failed to list experiments: {}", e.getMessage());
        }
        return ids;
    }

    // ── Registered Models (classic Model Registry) ──────────────────────

    public Optional<RegisteredModel> getRegisteredModel(String name) {
        log.debug("Fetching registered model: {}", name);
        try {
            return Optional.ofNullable(mlflowClient.getRegisteredModel(name));
        } catch (Exception e) {
            log.warn("Registered model not found: {}", name, e);
            return Optional.empty();
        }
    }

    // ── Model Versions ──────────────────────────────────────────────────

    public List<ModelVersion> searchModelVersions(String filter) {
        log.debug("Searching model versions with filter: {}", filter);
        List<ModelVersion> allVersions = new ArrayList<>();
        Page<ModelVersion> page = mlflowClient.searchModelVersions(filter);
        page.getItems().forEach(allVersions::add);
        while (page.hasNextPage()) {
            page = page.getNextPage();
            page.getItems().forEach(allVersions::add);
        }
        return allVersions;
    }

    /**
     * Returns all model versions across all registered models.
     */
    public List<ModelVersion> searchAllModelVersions() {
        log.debug("Fetching all model versions");
        List<ModelVersion> allVersions = new ArrayList<>();
        Page<ModelVersion> page = mlflowClient.searchModelVersions();
        page.getItems().forEach(allVersions::add);
        while (page.hasNextPage()) {
            page = page.getNextPage();
            page.getItems().forEach(allVersions::add);
        }
        return allVersions;
    }

    public List<ModelVersion> getLatestVersions(String modelName) {
        log.debug("Fetching latest versions for model: {}", modelName);
        return mlflowClient.getLatestVersions(modelName);
    }

    public Optional<ModelVersion> getModelVersion(String modelName, String version) {
        log.debug("Fetching model version: {} v{}", modelName, version);
        try {
            return Optional.ofNullable(mlflowClient.getModelVersion(modelName, version));
        } catch (Exception e) {
            log.warn("Model version not found: {} v{}", modelName, version, e);
            return Optional.empty();
        }
    }

    // ── Experiments ─────────────────────────────────────────────────────

    public Optional<Experiment> getExperiment(String experimentId) {
        log.debug("Fetching experiment: {}", experimentId);
        try {
            return Optional.ofNullable(mlflowClient.getExperiment(experimentId));
        } catch (Exception e) {
            log.warn("Experiment not found: {}", experimentId, e);
            return Optional.empty();
        }
    }

    // ── Runs ────────────────────────────────────────────────────────────

    public Optional<Run> getRun(String runId) {
        log.debug("Fetching run: {}", runId);
        try {
            return Optional.ofNullable(mlflowClient.getRun(runId));
        } catch (Exception e) {
            log.warn("Run not found: {}", runId, e);
            return Optional.empty();
        }
    }

    // ── Artifacts ───────────────────────────────────────────────────────

    public String getModelVersionDownloadUri(String modelName, String version) {
        log.debug("Fetching download URI for model: {} v{}", modelName, version);
        return mlflowClient.getModelVersionDownloadUri(modelName, version);
    }

    // ── Internal response DTOs ──────────────────────────────────────────

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class LoggedModelsSearchResponse {
        @JsonProperty("models")
        List<LoggedModel> models;

        @JsonProperty("next_page_token")
        String nextPageToken;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class LoggedModelGetResponse {
        @JsonProperty("model")
        LoggedModel model;
    }
}
