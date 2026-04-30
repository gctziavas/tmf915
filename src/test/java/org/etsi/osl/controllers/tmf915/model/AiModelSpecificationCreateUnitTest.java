package org.etsi.osl.controllers.tmf915.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AiModelSpecificationCreateUnitTest {

    @Test
    public void testAiModelSpecificationCreate() {
        AiModelSpecificationCreate create = new AiModelSpecificationCreate();
        
        create.setName("New Spec Create");
        create.setDescription("Test create setup");
        create.setVersion("1.0.0");
        create.setIsBundle(false);
        create.setLifecycleStatus("Active");
        
        assertEquals("New Spec Create", create.getName());
        assertEquals("Test create setup", create.getDescription());
        assertEquals("1.0.0", create.getVersion());
        assertFalse(create.getIsBundle());
        assertEquals("Active", create.getLifecycleStatus());
        
        CharacteristicSpecification charSpec = new CharacteristicSpecification().name("spec-1");
        create.addSpecCharacteristicItem(charSpec);
        assertEquals(1, create.getSpecCharacteristic().size());
        assertEquals("spec-1", create.getSpecCharacteristic().get(0).getName());
    }
}
