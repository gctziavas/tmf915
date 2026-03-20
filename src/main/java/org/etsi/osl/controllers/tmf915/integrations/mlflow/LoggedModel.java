package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * POJO representing an MLflow Logged Model from the {@code /api/2.0/mlflow/logged-models} API.
 * This is the newer MLflow 2.x model tracking format, as opposed to the classic Model Registry.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoggedModel {

    private LoggedModelInfo info;
    private LoggedModelData data;

    public LoggedModelInfo getInfo() { return info; }
    public void setInfo(LoggedModelInfo info) { this.info = info; }
    public LoggedModelData getData() { return data; }
    public void setData(LoggedModelData data) { this.data = data; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoggedModelInfo {

        @JsonProperty("model_id")
        private String modelId;

        @JsonProperty("experiment_id")
        private String experimentId;

        private String name;

        @JsonProperty("creation_timestamp_ms")
        private long creationTimestampMs;

        @JsonProperty("last_updated_timestamp_ms")
        private long lastUpdatedTimestampMs;

        @JsonProperty("artifact_uri")
        private String artifactUri;

        private String status;

        @JsonProperty("model_type")
        private String modelType;

        @JsonProperty("source_run_id")
        private String sourceRunId;

        private List<Tag> tags;

        public String getModelId() { return modelId; }
        public void setModelId(String modelId) { this.modelId = modelId; }
        public String getExperimentId() { return experimentId; }
        public void setExperimentId(String experimentId) { this.experimentId = experimentId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public long getCreationTimestampMs() { return creationTimestampMs; }
        public void setCreationTimestampMs(long creationTimestampMs) { this.creationTimestampMs = creationTimestampMs; }
        public long getLastUpdatedTimestampMs() { return lastUpdatedTimestampMs; }
        public void setLastUpdatedTimestampMs(long lastUpdatedTimestampMs) { this.lastUpdatedTimestampMs = lastUpdatedTimestampMs; }
        public String getArtifactUri() { return artifactUri; }
        public void setArtifactUri(String artifactUri) { this.artifactUri = artifactUri; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getModelType() { return modelType; }
        public void setModelType(String modelType) { this.modelType = modelType; }
        public String getSourceRunId() { return sourceRunId; }
        public void setSourceRunId(String sourceRunId) { this.sourceRunId = sourceRunId; }
        public List<Tag> getTags() { return tags; }
        public void setTags(List<Tag> tags) { this.tags = tags; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoggedModelData {
        private List<Param> params;
        private List<Metric> metrics;

        public List<Param> getParams() { return params; }
        public void setParams(List<Param> params) { this.params = params; }
        public List<Metric> getMetrics() { return metrics; }
        public void setMetrics(List<Metric> metrics) { this.metrics = metrics; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tag {
        private String key;
        private String value;

        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Param {
        private String key;
        private String value;

        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Metric {
        private String key;
        private double value;
        private long timestamp;
        private int step;

        @JsonProperty("model_id")
        private String modelId;

        @JsonProperty("run_id")
        private String runId;

        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public double getValue() { return value; }
        public void setValue(double value) { this.value = value; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public int getStep() { return step; }
        public void setStep(int step) { this.step = step; }
        public String getModelId() { return modelId; }
        public void setModelId(String modelId) { this.modelId = modelId; }
        public String getRunId() { return runId; }
        public void setRunId(String runId) { this.runId = runId; }
    }
}
