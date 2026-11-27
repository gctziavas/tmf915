package org.etsi.osl.controllers.tmf915;

import org.etsi.osl.controllers.tmf915.catalog.CatalogSource;
import org.etsi.osl.controllers.tmf915.deployment.AiModelLifecycleService;
import org.etsi.osl.controllers.tmf915.deployment.PlatformDeployer;
import org.etsi.osl.controllers.tmf915.integrations.mlflow.MlflowClientService;
import org.etsi.osl.controllers.tmf915.integrations.mlflow.MlflowSpecificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Handoff 5.8: the application starts without MLflow ({@code mlflow.enabled=false}, the test
 * default), which is the obvious way to try the API without an MLflow server.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = "mlflow.enabled=false")
class MlflowDisabledStartupTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private AiModelLifecycleService lifecycleService;

    @Test
    void startsWithoutAnyMlflowBean() {
        assertEquals(0, context.getBeanNamesForType(MlflowClientService.class).length);
        assertEquals(0, context.getBeanNamesForType(MlflowSpecificationService.class).length);
        assertEquals(0, context.getBeanNamesForType(CatalogSource.class).length, "nothing to sync");
        assertEquals(0, context.getBeanNamesForType(PlatformDeployer.class).length,
                "no deployer: models can be catalogued and created, not deployed");
    }
}
