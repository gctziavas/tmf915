package org.etsi.osl.controllers.tmf915.catalog;

import org.etsi.osl.controllers.tmf915.integrations.mlflow.MlflowCatalogSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * With MLflow and its sync enabled, the generic sync gets the MLflow source (injected as
 * {@code CatalogSource<?>}). MLflow itself is unreachable here and the schedule never fires.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {
        "mlflow.enabled=true",
        "mlflow.sync.enabled=true",
        "mlflow.tracking-uri=http://127.0.0.1:1",
        "aimodel.catalog.sync.interval-ms=999999999"
})
class CatalogSyncWiringTest {

    @Autowired
    private CatalogSyncService sync;

    @Test
    void mlflowSourceIsRegistered() {
        List<?> sources = (List<?>) ReflectionTestUtils.getField(sync, "sources");
        assertEquals(1, sources.size());
        assertInstanceOf(MlflowCatalogSource.class, sources.get(0));
    }
}
