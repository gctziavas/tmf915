package org.etsi.osl.controllers.tmf915.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceStateTypeUnitTest {

    @Test
    public void testEnumValues() {
        assertEquals("feasibilityChecked", ServiceStateType.FEASIBILITY_CHECKED.getValue());
        assertEquals("designed", ServiceStateType.DESIGNED.getValue());
        assertEquals("reserved", ServiceStateType.RESERVED.getValue());
        assertEquals("inactive", ServiceStateType.INACTIVE.getValue());
        assertEquals("active", ServiceStateType.ACTIVE.getValue());
        assertEquals("terminated", ServiceStateType.TERMINATED.getValue());
    }

    @Test
    public void testEnumFromString() {
        assertEquals(ServiceStateType.FEASIBILITY_CHECKED, ServiceStateType.fromValue("feasibilityChecked"));
        assertEquals(ServiceStateType.DESIGNED, ServiceStateType.fromValue("designed"));
        assertEquals(ServiceStateType.RESERVED, ServiceStateType.fromValue("reserved"));
        assertEquals(ServiceStateType.INACTIVE, ServiceStateType.fromValue("inactive"));
        assertEquals(ServiceStateType.ACTIVE, ServiceStateType.fromValue("active"));
        assertEquals(ServiceStateType.TERMINATED, ServiceStateType.fromValue("terminated"));
    }
    
    @Test
    public void testEnumFromStringInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            ServiceStateType.fromValue("invalid-state");
        });
    }
}
