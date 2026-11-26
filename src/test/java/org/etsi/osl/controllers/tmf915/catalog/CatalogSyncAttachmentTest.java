package org.etsi.osl.controllers.tmf915.catalog;

import org.etsi.osl.controllers.tmf915.artifacts.ArtifactCharacteristics;
import org.etsi.osl.controllers.tmf915.artifacts.InMemoryArtifactStore;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The sync's platform-neutral fields with the real download components (MLflow locator,
 * allow-list, size limit) and an in-memory {@code s3://} store: a source with
 * {@code platform=mlflow} gets the same attachment the download endpoint would serve.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
// A plain class, not a nested @TestConfiguration: see DeploymentSchedulerSpringTest.
@Import(CatalogSyncAttachmentTest.Store.class)
class CatalogSyncAttachmentTest {

    static class Store {
        @Bean
        InMemoryArtifactStore catalogTestArtifactStore() {
            return new InMemoryArtifactStore()
                    .put("s3://mlflow/1/models/m-1/artifacts", "MLmodel", new byte[100])
                    .put("s3://mlflow/1/models/m-1/artifacts", "model.pkl", new byte[1900])
                    .put("s3://elsewhere/m-1", "model.pkl", new byte[10]);
        }
    }

    @Autowired
    private CatalogSyncService sync;

    private final FakeCatalogSource mlflowLike = new FakeCatalogSource("mlflow") {
        @Override
        public String artifactFormat() {
            return "mlflow-model";
        }
    };

    private AiModelSpecificationCreate specification(String artifactUri, String downloadable) {
        AiModelSpecificationCreate spec = new AiModelSpecificationCreate();
        spec.setName("iris");
        spec.setVersion("m-1");
        spec.addSpecCharacteristicItem(ArtifactCharacteristics.of("artifactUri", "", artifactUri));
        if (downloadable != null) {
            spec.addSpecCharacteristicItem(ArtifactCharacteristics.of("downloadable", "", downloadable));
        }
        sync.addPlatformNeutralFields(mlflowLike, spec);
        return spec;
    }

    @Test
    void downloadableModel_getsTheAttachmentWithItsSize() {
        AiModelSpecificationCreate spec = specification("s3://mlflow/1/models/m-1/artifacts", null);

        assertEquals(1, spec.getAttachment().size());
        assertEquals("mlflow-model", spec.getAttachment().get(0).getAttachmentType());
        assertEquals(2000f, spec.getAttachment().get(0).getSize().getAmount());
    }

    @Test
    void optedOutOrOutsideTheAllowList_getsNone() {
        assertTrue(specification("s3://mlflow/1/models/m-1/artifacts", "false").getAttachment().isEmpty());
        assertTrue(specification("s3://elsewhere/m-1", null).getAttachment().isEmpty());
        assertTrue(specification("s3://mlflow/nothing-here", null).getAttachment().isEmpty());
    }
}
