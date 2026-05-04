package org.etsi.osl.controllers.tmf915.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CharacteristicSpecificationUnitTest {

    @Test
    public void testCharacteristicSpecification() {
        CharacteristicSpecification spec = new CharacteristicSpecification();
        
        spec.setId("spec-1");
        spec.setName("test-spec");
        spec.setDescription("test description");
        spec.setValueType("string");
        spec.setConfigurable(true);
        spec.setExtensible(false);
        spec.setIsUnique(true);
        
        assertEquals("spec-1", spec.getId());
        assertEquals("test-spec", spec.getName());
        assertEquals("test description", spec.getDescription());
        assertEquals("string", spec.getValueType());
        assertTrue(spec.getConfigurable());
        assertFalse(spec.getExtensible());
        assertTrue(spec.getIsUnique());
    }

    @Test
    public void testCharacteristicSpecificationBuilder() {
        CharacteristicSpecification spec = new CharacteristicSpecification()
            .name("platform")
            .valueType("string")
            .configurable(false);
            
        assertEquals("platform", spec.getName());
        assertEquals("string", spec.getValueType());
        assertFalse(spec.getConfigurable());
    }
}
