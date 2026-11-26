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
 * @param <T>         the source's own model type
 */
public record DiscoveredModel<T>(String key, String name, String version, long createdAtMs, T details) {
}
