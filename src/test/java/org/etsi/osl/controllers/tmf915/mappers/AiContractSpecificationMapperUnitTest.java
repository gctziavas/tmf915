package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.AiContractSpecification;
import org.etsi.osl.controllers.tmf915.model.AiContractSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.AiContractSpecificationUpdate;
import org.etsi.osl.controllers.tmf915.model.TargetEntitySchema;
import org.etsi.osl.controllers.tmf915.model.TimePeriod;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class AiContractSpecificationMapperUnitTest {

    @Test
    void fromCreate_allFieldsSet_mapsCorrectly() {
        AiContractSpecificationCreate src = new AiContractSpecificationCreate();
        src.setName("Spec-A");
        src.setDescription("A specification");
        src.setVersion("1.0");
        src.setIsBundle(false);
        src.setLifecycleStatus("Active");
        src.setAtBaseType("BaseEntity");
        src.setAtType("AiContractSpecification");
        src.setAtSchemaLocation(URI.create("http://example.com"));
        TargetEntitySchema tes = new TargetEntitySchema();
        src.setTargetEntitySchema(tes);
        TimePeriod tp = new TimePeriod();
        src.setValidFor(tp);

        AiContractSpecification result = AiContractSpecificationMapper.fromCreate(src);

        assertEquals("Spec-A", result.getName());
        assertEquals("A specification", result.getDescription());
        assertEquals("1.0", result.getVersion());
        assertFalse(result.getIsBundle());
        assertEquals("Active", result.getLifecycleStatus());
        assertSame(tes, result.getTargetEntitySchema());
        assertSame(tp, result.getValidFor());
    }

    @Test
    void fromCreate_nullFields_mapsNulls() {
        AiContractSpecificationCreate src = new AiContractSpecificationCreate();

        AiContractSpecification result = AiContractSpecificationMapper.fromCreate(src);

        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getTargetEntitySchema());
    }

    @Test
    void applyUpdate_updatesOnlyNonNullFields() {
        AiContractSpecification target = new AiContractSpecification();
        target.setName("Original");
        target.setVersion("1.0");
        target.setLifecycleStatus("Draft");

        AiContractSpecificationUpdate src = new AiContractSpecificationUpdate();
        src.setName("Updated");
        src.setLifecycleStatus("Active");

        AiContractSpecificationMapper.applyUpdate(target, src);

        assertEquals("Updated", target.getName());
        assertEquals("Active", target.getLifecycleStatus());
        assertEquals("1.0", target.getVersion());
    }

    @Test
    void applyUpdate_allNull_nothingChanges() {
        AiContractSpecification target = new AiContractSpecification();
        target.setName("Kept");

        AiContractSpecificationUpdate src = new AiContractSpecificationUpdate();

        AiContractSpecificationMapper.applyUpdate(target, src);

        assertEquals("Kept", target.getName());
    }
}
