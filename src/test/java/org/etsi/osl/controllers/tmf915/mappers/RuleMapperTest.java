package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.Rule;
import org.etsi.osl.controllers.tmf915.model.RuleCreate;
import org.etsi.osl.controllers.tmf915.model.RuleUpdate;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class RuleMapperTest {

    @Test
    void fromCreate_allFieldsSet_mapsCorrectly() {
        RuleCreate src = new RuleCreate();
        src.setName("Rule-1");
        src.setAtBaseType("BaseEntity");
        src.setAtType("Rule");
        src.setAtSchemaLocation(URI.create("http://example.com"));

        Rule result = RuleMapper.fromCreate(src);

        assertEquals("Rule-1", result.getName());
        assertEquals("BaseEntity", result.getAtBaseType());
        assertEquals("Rule", result.getAtType());
        assertEquals(URI.create("http://example.com"), result.getAtSchemaLocation());
    }

    @Test
    void fromCreate_nullFields_mapsNulls() {
        RuleCreate src = new RuleCreate();

        Rule result = RuleMapper.fromCreate(src);

        assertNotNull(result);
        assertNull(result.getName());
    }

    @Test
    void applyUpdate_updatesOnlyNonNullFields() {
        Rule target = new Rule();
        target.setName("Original");
        target.setAtType("Rule");

        RuleUpdate src = new RuleUpdate();
        src.setName("Updated");

        RuleMapper.applyUpdate(target, src);

        assertEquals("Updated", target.getName());
        assertEquals("Rule", target.getAtType());
    }

    @Test
    void applyUpdate_allNull_nothingChanges() {
        Rule target = new Rule();
        target.setName("Kept");

        RuleUpdate src = new RuleUpdate();

        RuleMapper.applyUpdate(target, src);

        assertEquals("Kept", target.getName());
    }
}
