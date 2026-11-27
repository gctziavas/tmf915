package org.etsi.osl.controllers.tmf915.catalog;

/**
 * A model found on a platform by a {@link CatalogSource}.
 *
 * @param key         identifies the model within its source; the sync's caches use it
 * @param name        the specification's {@code name}
 * @param version     the specification's {@code version}; with {@code name}, how the sync tells
 *                    whether the model is already in the catalog
 * @param createdAtMs creation time (epoch ms). The sync processes newer models first and asks
 *                    for models created after the newest one seen. {@code 0}: unknown, or the
 *                    source cannot search for this kind of model by time; such models never
 *                    start incremental searches, so the source is searched in full every tick
 * @param details     what the source needs to check and convert the model; opaque to the sync
 * @param status      whether the platform has finished writing the model. Only {@link Status#READY}
 *                    models are published; the others are checked again on every search
 * @param readyAtMs   when the platform finished writing the model, or last changed it (epoch ms).
 *                    The sync publishes the model only {@code aimodel.catalog.sync.settle-ms}
 *                    after this, so that what a publisher adds right after the model (MLflow:
 *                    the metrics linked to it) is in the specification, which is never updated
 *                    afterwards. {@code 0}: unknown, no wait
 * @param <T>         the source's own model type
 */
public record DiscoveredModel<T>(String key, String name, String version, long createdAtMs, T details,
                                 Status status, long readyAtMs) {

    /** How far the platform is with writing a model. */
    public enum Status {
        /** Finished, or the platform does not say. */
        READY,
        /** Still being written, for example while its files are uploaded. */
        PENDING,
        /** The platform gave up writing it, for example after a failed upload. */
        FAILED
    }

    public DiscoveredModel {
        if (status == null) {
            status = Status.READY;
        }
    }

    /** A model that is ready, since it was created: for platforms that report no status. */
    public DiscoveredModel(String key, String name, String version, long createdAtMs, T details) {
        this(key, name, version, createdAtMs, details, Status.READY, createdAtMs);
    }
}
