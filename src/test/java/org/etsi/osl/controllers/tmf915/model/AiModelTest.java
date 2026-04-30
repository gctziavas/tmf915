package org.etsi.osl.controllers.tmf915.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AiModelTest {

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
}
