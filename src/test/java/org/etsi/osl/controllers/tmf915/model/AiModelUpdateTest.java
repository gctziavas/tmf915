package org.etsi.osl.controllers.tmf915.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AiModelUpdateTest {

    @Test
    public void testAiModelUpdate() {
        AiModelUpdate update = new AiModelUpdate();
        update.setName("Updated AiModel");
        update.setDescription("Update DTO test");
        update.setIsBundle(false);
        update.setState(ServiceStateType.ACTIVE);
        
        assertEquals("Updated AiModel", update.getName());
        assertEquals("Update DTO test", update.getDescription());
        assertFalse(update.getIsBundle());
        assertEquals(ServiceStateType.ACTIVE, update.getState());
        
        Note note = new Note().text("Update note");
        update.addNoteItem(note);
        assertEquals(1, update.getNote().size());
        assertEquals("Update note", update.getNote().get(0).getText());
        
        Characteristic characteristic = new Characteristic().name("c1");
        update.addServiceCharacteristicItem(characteristic);
        assertEquals(1, update.getServiceCharacteristic().size());
        assertEquals("c1", update.getServiceCharacteristic().get(0).getName());
    }
}
