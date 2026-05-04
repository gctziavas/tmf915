package org.etsi.osl.controllers.tmf915.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AiModelSpecificationUpdateUnitTest {

    @Test
    public void testAiModelSpecificationUpdate() {
        AiModelSpecificationUpdate update = new AiModelSpecificationUpdate();
        
        update.setName("New Spec Update");
        update.setDescription("Test update setup");
        update.setVersion("1.0.1");
        update.setIsBundle(true);
        update.setLifecycleStatus("Obsolete");
        
        assertEquals("New Spec Update", update.getName());
        assertEquals("Test update setup", update.getDescription());
        assertEquals("1.0.1", update.getVersion());
        assertTrue(update.getIsBundle());
        assertEquals("Obsolete", update.getLifecycleStatus());
        
        CharacteristicSpecification charSpec = new CharacteristicSpecification().name("spec-2");
        update.addSpecCharacteristicItem(charSpec);
        assertEquals(1, update.getSpecCharacteristic().size());
        assertEquals("spec-2", update.getSpecCharacteristic().get(0).getName());
    }
}
