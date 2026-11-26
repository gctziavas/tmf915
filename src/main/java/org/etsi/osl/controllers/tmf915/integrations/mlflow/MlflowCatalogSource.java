package org.etsi.osl.controllers.tmf915.integrations.mlflow;

import org.etsi.osl.controllers.tmf915.catalog.CatalogSource;
import org.etsi.osl.controllers.tmf915.catalog.DiscoveredModel;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.jspecify.annotations.Nullable;
import org.mlflow.api.proto.ModelRegistry.ModelVersion;
import org.mlflow.api.proto.Service.Experiment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * MLflow's models for the catalog sync ({@code catalog.CatalogSyncService}).
 *
 * <ul>
 *   <li><b>Logged models</b> (MLflow 3): one specification per logged model, named after its
 *       experiment, with the model id as version. Incremental searches use MLflow's
 *       {@code creation_time} filter.</li>
 *   <li><b>Classic Model Registry</b>: searched only when a full search finds no logged models,
 *       one specification per registered model version. Registry versions carry no creation
 *       time for the sync, so a registry-only server is searched in full every tick.</li>
 * </ul>
 * The mapping to a specification is {@link MlflowSpecificationService}'s.
 *
 * <p>Active with {@code mlflow.enabled=true} and {@code mlflow.sync.enabled=true}.
 */
@Component
@ConditionalOnProperty(name = {"mlflow.enabled", "mlflow.sync.enabled"}, havingValue = "true")
public class MlflowCatalogSource implements CatalogSource<MlflowCatalogSource.MlflowModel> {

    private static final Logger log = LoggerFactory.getLogger(MlflowCatalogSource.class);

    /** What the source keeps about a discovered model. */
    public sealed interface MlflowModel permits LoggedModelEntry, RegistryVersion {
    }

    public record LoggedModelEntry(LoggedModel model) implements MlflowModel {
    }

    public record RegistryVersion(ModelVersion version) implements MlflowModel {
    }

    private final MlflowClientService mlflowClient;
    private final MlflowSpecificationService specificationService;

    @Value("${mlflow.sync.base-url:}")
    private String baseUrl = "";

    public MlflowCatalogSource(MlflowClientService mlflowClient, MlflowSpecificationService specificationService) {
        this.mlflowClient = mlflowClient;
        this.specificationService = specificationService;
    }

    @Override
    public String platform() {
        return MlflowArtifactLocator.PLATFORM;
    }

    @Override
    public String artifactFormat() {
        return MlflowArtifactLocator.FORMAT;
    }

    @Override
    public List<DiscoveredModel<MlflowModel>> discover(@Nullable Long createdAfterMs) {
        if (createdAfterMs != null) {
            // Logged models only, and no fallback: an empty answer means nothing new. Throws if
            // the server rejects the filter, so the sync falls back to full searches.
            return loggedModels(mlflowClient.searchAllLoggedModelsCreatedAfter(createdAfterMs));
        }
        List<LoggedModel> logged;
        try {
            logged = mlflowClient.searchAllLoggedModels();
        } catch (RuntimeException e) {
            log.warn("MLflow: failed to fetch logged models, will try the classic registry: {}", e.toString());
            logged = List.of();
        }
        if (!logged.isEmpty()) {
            return loggedModels(logged);
        }
        log.debug("MLflow: no logged models found, trying the classic registry");
        return registryVersions(mlflowClient.searchAllModelVersions());
    }

    private List<DiscoveredModel<MlflowModel>> loggedModels(List<LoggedModel> models) {
        Map<String, String> experimentNames = new HashMap<>();
        List<DiscoveredModel<MlflowModel>> discovered = new ArrayList<>();
        for (LoggedModel model : models) {
            LoggedModel.LoggedModelInfo info = model.getInfo();
            String experimentName = experimentNames.computeIfAbsent(info.getExperimentId(), this::experimentName);
            discovered.add(new DiscoveredModel<>(experimentName + ":" + info.getModelId(), experimentName,
                    info.getModelId(), info.getCreationTimestampMs(), new LoggedModelEntry(model)));
        }
        return discovered;
    }

    private List<DiscoveredModel<MlflowModel>> registryVersions(List<ModelVersion> versions) {
        // First occurrence per (name, version).
        Map<String, DiscoveredModel<MlflowModel>> unique = new LinkedHashMap<>();
        for (ModelVersion version : versions) {
            String key = "registry:" + version.getName() + ":" + version.getVersion();
            unique.putIfAbsent(key, new DiscoveredModel<>(key, version.getName(), version.getVersion(), 0,
                    new RegistryVersion(version)));
        }
        return new ArrayList<>(unique.values());
    }

    private String experimentName(String experimentId) {
        return mlflowClient.getExperiment(experimentId)
                .map(Experiment::getName)
                .orElse("experiment-" + experimentId);
    }

    @Override
    public boolean hasArtifacts(DiscoveredModel<MlflowModel> model) {
        if (model.details() instanceof LoggedModelEntry e) {
            return mlflowClient.hasArtifactsByUri(e.model().getInfo().getArtifactUri());
        }
        RegistryVersion v = (RegistryVersion) model.details();
        return mlflowClient.hasArtifactsByUri(registryArtifactUri(v.version().getSource()));
    }

    /**
     * A registry version's {@code source} as an S3 URI: as it is if it has a scheme, else relative
     * to the {@code mlflow} bucket.
     */
    static String registryArtifactUri(String source) {
        try {
            if (source != null && URI.create(source).getScheme() != null) {
                return source;
            }
        } catch (IllegalArgumentException e) {
            // Not a URI: treat as a relative path.
        }
        return "s3://mlflow/" + source;
    }

    @Override
    public AiModelSpecificationCreate toSpecification(DiscoveredModel<MlflowModel> model) throws IOException {
        if (model.details() instanceof LoggedModelEntry e) {
            return specificationService.createSpecificationFromLoggedModel(e.model(), model.name(), baseUrl);
        }
        return specificationService.createSpecificationFromMlflow(model.name(), model.version(), baseUrl);
    }
}
