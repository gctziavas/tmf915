package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.AiContractViolation;
import org.etsi.osl.controllers.tmf915.model.AiContractViolationCreate;
import org.etsi.osl.controllers.tmf915.model.EntityRef;
import org.etsi.osl.controllers.tmf915.model.Violation;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AiContractViolationMapperUnitTest {

    @Test
    void fromCreate_allFieldsSet_mapsCorrectly() {
        AiContractViolationCreate src = new AiContractViolationCreate();
        OffsetDateTime now = OffsetDateTime.now();
        src.setDate(now);
        EntityRef contract = new EntityRef();
        src.setAiContract(contract);
        Violation violation = new Violation();
        src.setViolation(violation);
        src.setAtBaseType("BaseEntity");
        src.setAtType("AiContractViolation");
        src.setAtSchemaLocation(URI.create("http://example.com/schema"));

        AiContractViolation result = AiContractViolationMapper.fromCreate(src);

        assertEquals(now, result.getDate());
        assertSame(contract, result.getAiContract());
        assertSame(violation, result.getViolation());
        assertEquals("BaseEntity", result.getAtBaseType());
        assertEquals("AiContractViolation", result.getAtType());
    }

    @Test
    void fromCreate_nullFields_mapsNulls() {
        AiContractViolationCreate src = new AiContractViolationCreate();

        AiContractViolation result = AiContractViolationMapper.fromCreate(src);

        assertNotNull(result);
        assertNull(result.getDate());
        assertNull(result.getAiContract());
        assertNull(result.getViolation());
    }
}
