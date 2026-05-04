package org.etsi.osl.controllers.tmf915.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AiModelUnitTest {

    @Test
    public void testAiModelGettersAndSetters() {
        AiModel model = new AiModel();
        
        model.setId("model-123");
        model.setName("Test Model");
        model.setState(ServiceStateType.DESIGNED);
        model.setIsBundle(false);
        
        assertEquals("model-123", model.getId());
        assertEquals("Test Model", model.getName());
        assertEquals(ServiceStateType.DESIGNED, model.getState());
        assertFalse(model.getIsBundle());
    }

    @Test
    public void testAddNoteItem() {
        AiModel model = new AiModel();
        
        assertNull(model.getNote() != null && !model.getNote().isEmpty() ? model.getNote() : null);
        
        Note note1 = new Note().text("Note 1");
        model.addNoteItem(note1);
        
        assertNotNull(model.getNote());
        assertEquals(1, model.getNote().size());
        assertEquals("Note 1", model.getNote().get(0).getText());
        
        Note note2 = new Note().text("Note 2");
        model.addNoteItem(note2);
        assertEquals(2, model.getNote().size());
        assertEquals("Note 2", model.getNote().get(1).getText());
    }
    
    @Test
    public void testSetAndGetCharacteristics() {
        AiModel model = new AiModel();
        
        Characteristic characteristic = new Characteristic();
        characteristic.setName("platform");
        characteristic.setValue("mlflow");
        
        List<Characteristic> characteristics = new ArrayList<>();
        characteristics.add(characteristic);
        
        model.setServiceCharacteristic(characteristics);
        
        assertEquals(1, model.getServiceCharacteristic().size());
        assertEquals("platform", model.getServiceCharacteristic().get(0).getName());
        assertEquals("mlflow", model.getServiceCharacteristic().get(0).getValue().toString());
    }

    @Test
    public void testAiModelSpecificationSerializesAsReference() throws Exception {
        AiModelSpecification specification = new AiModelSpecification();
        specification.setId("spec-123");
        specification.setHref(URI.create("/tmf-api/AiM/v4/aiModelSpecification/spec-123"));
        specification.setName("Spec Name");
        specification.setVersion("1.0");
        specification.setAtBaseType("ServiceSpecification");
        specification.setAtType("AIModelSpecification");
        specification.setDescription("This should not be serialized in AiModel responses");

        AiModel model = new AiModel();
        model.setId("model-1");
        model.setAiModelSpecification(specification);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(model);

        assertTrue(json.contains("\"aiModelSpecification\""));
        assertTrue(json.contains("\"id\":\"spec-123\""));
        assertTrue(json.contains("\"href\":\"/tmf-api/AiM/v4/aiModelSpecification/spec-123\""));
        assertTrue(json.contains("\"name\":\"Spec Name\""));
        assertTrue(json.contains("\"version\":\"1.0\""));
        assertTrue(json.contains("\"@baseType\":\"ServiceSpecification\""));
        assertTrue(json.contains("\"@type\":\"AIModelSpecification\""));
        assertFalse(json.contains("This should not be serialized in AiModel responses"));
        assertFalse(json.contains("deploymentRecord"));
        assertFalse(json.contains("specCharacteristic"));
    }
}
