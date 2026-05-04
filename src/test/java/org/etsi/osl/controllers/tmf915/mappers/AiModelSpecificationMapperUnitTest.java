package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationUpdate;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AiModelSpecificationMapperUnitTest {

    @Test
    void fromCreate_allFieldsSet_mapsCorrectly() {
        AiModelSpecificationCreate src = new AiModelSpecificationCreate();
        src.setName("BERT-Spec");
        src.setVersion("2.0");
        src.setDescription("BERT specification");
        src.setIsBundle(false);
        src.setLifecycleStatus("Active");
        src.setDeploymentRecord("Deployed v2");
        src.setInheritedModel("GPT-Base");
        src.setModelContractVersionHistory("v1->v2");
        src.setModelDataSheet("sheet-ref");
        src.setModelEvaluationData("eval-data");
        src.setModelSpecificationHistory("history");
        src.setModelTrainingData("training-data");
        OffsetDateTime now = OffsetDateTime.now();
        src.setLastUpdate(now);

        AiModelSpecification result = AiModelSpecificationMapper.fromCreate(src);

        assertEquals("BERT-Spec", result.getName());
        assertEquals("2.0", result.getVersion());
        assertEquals("BERT specification", result.getDescription());
        assertFalse(result.getIsBundle());
        assertEquals("Active", result.getLifecycleStatus());
        assertEquals("Deployed v2", result.getDeploymentRecord());
        assertEquals("GPT-Base", result.getInheritedModel());
        assertEquals(now, result.getLastUpdate());
    }

    @Test
    void fromCreate_nullFields_mapsNulls() {
        AiModelSpecificationCreate src = new AiModelSpecificationCreate();

        AiModelSpecification result = AiModelSpecificationMapper.fromCreate(src);

        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getVersion());
        assertNull(result.getDescription());
    }

    @Test
    void applyUpdate_updatesOnlyNonNullFields() {
        AiModelSpecification target = new AiModelSpecification();
        target.setName("Original");
        target.setVersion("1.0");
        target.setDescription("Old");
        target.setLifecycleStatus("Draft");

        AiModelSpecificationUpdate src = new AiModelSpecificationUpdate();
        src.setName("Updated");
        src.setLifecycleStatus("Active");

        AiModelSpecificationMapper.applyUpdate(target, src);

        assertEquals("Updated", target.getName());
        assertEquals("Active", target.getLifecycleStatus());
        assertEquals("1.0", target.getVersion());
        assertEquals("Old", target.getDescription());
    }

    @Test
    void applyUpdate_allNull_nothingChanges() {
        AiModelSpecification target = new AiModelSpecification();
        target.setName("Kept");
        target.setVersion("3.0");

        AiModelSpecificationUpdate src = new AiModelSpecificationUpdate();

        AiModelSpecificationMapper.applyUpdate(target, src);

        assertEquals("Kept", target.getName());
        assertEquals("3.0", target.getVersion());
    }

    @Test
    void applyUpdate_overwriteVersion() {
        AiModelSpecification target = new AiModelSpecification();
        target.setVersion("1.0");

        AiModelSpecificationUpdate src = new AiModelSpecificationUpdate();
        src.setVersion("2.0");

        AiModelSpecificationMapper.applyUpdate(target, src);

        assertEquals("2.0", target.getVersion());
    }
}
