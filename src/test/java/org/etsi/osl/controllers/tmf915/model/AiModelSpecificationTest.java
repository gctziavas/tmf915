package org.etsi.osl.controllers.tmf915.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AiModelSpecificationTest {

    @Test
    public void testAiModelSpecification() {
        AiModelSpecification spec = new AiModelSpecification();
        
        spec.setId("spec-xyz");
        spec.setName("Test AiModel Specification");
        spec.setDescription("A sample specification");
        spec.setVersion("1.0.0");
        spec.setIsBundle(false);
        spec.setLifecycleStatus("Active");
        
        assertEquals("spec-xyz", spec.getId());
        assertEquals("Test AiModel Specification", spec.getName());
        assertEquals("A sample specification", spec.getDescription());
        assertEquals("1.0.0", spec.getVersion());
        assertFalse(spec.getIsBundle());
        assertEquals("Active", spec.getLifecycleStatus());
    }

    @Test
    public void testSetAndGetCharacteristics() {
        AiModelSpecification spec = new AiModelSpecification();
        
        CharacteristicSpecification charSpec = new CharacteristicSpecification();
        charSpec.setName("model_file_exists");
        charSpec.setValueType("boolean");
        
        List<CharacteristicSpecification> characteristics = new ArrayList<>();
        characteristics.add(charSpec);
        
        spec.setSpecCharacteristic(characteristics);
        
        assertNotNull(spec.getSpecCharacteristic());
        assertEquals(1, spec.getSpecCharacteristic().size());
        assertEquals("model_file_exists", spec.getSpecCharacteristic().get(0).getName());
        assertEquals("boolean", spec.getSpecCharacteristic().get(0).getValueType());
        
        // Test add methods
        CharacteristicSpecification charSpec2 = new CharacteristicSpecification().name("new_char");
        spec.addSpecCharacteristicItem(charSpec2);
        
        assertEquals(2, spec.getSpecCharacteristic().size());
        assertEquals("new_char", spec.getSpecCharacteristic().get(1).getName());
    }
}
