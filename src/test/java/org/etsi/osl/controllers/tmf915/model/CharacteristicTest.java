package org.etsi.osl.controllers.tmf915.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CharacteristicTest {

    @Test
    public void testCharacteristicGettersAndSetters() {
        Characteristic characteristic = new Characteristic();
        
        characteristic.setId("char-1");
        characteristic.setName("model_file_exists");
        characteristic.setValue("false");
        characteristic.setValueType("boolean");
        
        assertEquals("char-1", characteristic.getId());
        assertEquals("model_file_exists", characteristic.getName());
        assertEquals("false", characteristic.getValue());
        assertEquals("boolean", characteristic.getValueType());
    }

    @Test
    public void testCharacteristicBuilder() {
        Characteristic characteristic = new Characteristic()
            .name("platform")
            .value("mlflow")
            .valueType("string");
            
        assertEquals("platform", characteristic.getName());
        assertEquals("mlflow", characteristic.getValue());
        assertEquals("string", characteristic.getValueType());
    }
}
