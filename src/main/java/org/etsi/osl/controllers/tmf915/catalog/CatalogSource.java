package org.etsi.osl.controllers.tmf915.catalog;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * Platform-specific part of the catalog sync: finds a platform's models and describes each as a
 * specification. One bean per enabled platform (MLflow today); {@link CatalogSyncService} runs
 * them all and does the rest: scheduling, full and incremental searches, de-duplication, the
 * back-off for models whose files are not stored yet, and the platform-neutral fields.
 *
 * @param <T> the source's own model type, carried in {@link DiscoveredModel#details()}
 */
public interface CatalogSource<T> {

    /** The {@code platform} characteristic of this source's specifications, e.g. {@code mlflow}. */
    String platform();

    /**
     * The {@code artifactFormat} of this platform's models (see the formats table in
     * {@code docs/INTEGRATION_GUIDE.md}), unless {@link #toSpecification} sets that
     * characteristic itself for a model.
     */
    String artifactFormat();

    /**
     * Lists the platform's models.
     *
     * @param createdAfterMs {@code null}: all models (a full search). Otherwise at least the models
     *                       created after this time (epoch ms); returning more is allowed.
     * @throws RuntimeException if the platform cannot be searched. On an incremental search, the
     *                          sync then stops searching this source incrementally and searches
     *                          it in full from then on.
     */
    List<DiscoveredModel<T>> discover(@Nullable Long createdAfterMs);

    /**
     * Whether the model's files are stored yet. A model without them is skipped and checked
     * again after a back-off.
     */
    boolean hasArtifacts(DiscoveredModel<T> model);

    /**
     * Describes the model as a new specification. Platform-neutral fields ({@code platform},
     * {@code artifactFormat}, the download {@code attachment}) are added by the sync.
     */
    AiModelSpecificationCreate toSpecification(DiscoveredModel<T> model) throws IOException;
}
