package org.etsi.osl.controllers.tmf915.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AiModelCreateTest {

    @Test
    public void testAiModelCreate() {
        AiModelCreate create = new AiModelCreate();
        create.setName("New AiModel");
        create.setDescription("Create DTO test");
        create.setIsBundle(true);
        create.setState(ServiceStateType.DESIGNED);
        
        assertEquals("New AiModel", create.getName());
        assertEquals("Create DTO test", create.getDescription());
        assertTrue(create.getIsBundle());
        assertEquals(ServiceStateType.DESIGNED, create.getState());
        
        Note note = new Note().text("Init note");
        create.addNoteItem(note);
        assertEquals(1, create.getNote().size());
        assertEquals("Init note", create.getNote().get(0).getText());
        
        Characteristic characteristic = new Characteristic().name("c1");
        create.addServiceCharacteristicItem(characteristic);
        assertEquals(1, create.getServiceCharacteristic().size());
        assertEquals("c1", create.getServiceCharacteristic().get(0).getName());
    }
}
