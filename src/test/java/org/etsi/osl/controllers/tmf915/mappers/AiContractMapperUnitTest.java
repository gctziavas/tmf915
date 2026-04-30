package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.AiContract;
import org.etsi.osl.controllers.tmf915.model.AiContractCreate;
import org.etsi.osl.controllers.tmf915.model.AiContractUpdate;
import org.etsi.osl.controllers.tmf915.model.TimePeriod;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class AiContractMapperUnitTest {

    @Test
    void fromCreate_allFieldsSet_mapsCorrectly() {
        AiContractCreate src = new AiContractCreate();
        src.setName("Contract-A");
        src.setDescription("Test contract");
        src.setVersion("1.0");
        src.setApproved(true);
        src.setState("active");
        src.setAtBaseType("BaseEntity");
        src.setAtType("AiContract");
        src.setAtSchemaLocation(URI.create("http://example.com/schema"));
        TimePeriod tp = new TimePeriod();
        src.setValidFor(tp);

        AiContract result = AiContractMapper.fromCreate(src);

        assertEquals("Contract-A", result.getName());
        assertEquals("Test contract", result.getDescription());
        assertEquals("1.0", result.getVersion());
        assertTrue(result.getApproved());
        assertEquals("active", result.getState());
        assertEquals("BaseEntity", result.getAtBaseType());
        assertEquals("AiContract", result.getAtType());
        assertEquals(URI.create("http://example.com/schema"), result.getAtSchemaLocation());
        assertSame(tp, result.getValidFor());
    }

    @Test
    void fromCreate_nullFields_mapsNulls() {
        AiContractCreate src = new AiContractCreate();

        AiContract result = AiContractMapper.fromCreate(src);

        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getApproved());
    }

    @Test
    void applyUpdate_updatesOnlyNonNullFields() {
        AiContract target = new AiContract();
        target.setName("Original");
        target.setDescription("Old");
        target.setState("draft");

        AiContractUpdate src = new AiContractUpdate();
        src.setName("Updated");
        src.setState("active");

        AiContractMapper.applyUpdate(target, src);

        assertEquals("Updated", target.getName());
        assertEquals("active", target.getState());
        assertEquals("Old", target.getDescription());
    }

    @Test
    void applyUpdate_allNull_nothingChanges() {
        AiContract target = new AiContract();
        target.setName("Kept");
        target.setVersion("2.0");

        AiContractUpdate src = new AiContractUpdate();

        AiContractMapper.applyUpdate(target, src);

        assertEquals("Kept", target.getName());
        assertEquals("2.0", target.getVersion());
    }
}
