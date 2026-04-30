package org.openapitools.configuration;

import org.etsi.osl.controllers.tmf915.model.AlarmType;
import org.etsi.osl.controllers.tmf915.model.OrderItemActionType;
import org.etsi.osl.controllers.tmf915.model.PerceivedSeverity;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;

import static org.junit.jupiter.api.Assertions.*;

class EnumConverterConfigurationUnitTest {

    private EnumConverterConfiguration config;

    @BeforeEach
    void setUp() {
        config = new EnumConverterConfiguration();
    }

    // --- AlarmType ---

    @Test
    void alarmTypeConverter_validValue_returnsEnum() {
        Converter<String, AlarmType> converter = config.alarmTypeConverter();
        assertEquals(AlarmType.COMMUNICATIONS_ALARM, converter.convert("communicationsAlarm"));
    }

    @Test
    void alarmTypeConverter_anotherValidValue_returnsEnum() {
        Converter<String, AlarmType> converter = config.alarmTypeConverter();
        assertEquals(AlarmType.EQUIPMENT_ALARM, converter.convert("equipmentAlarm"));
    }

    @Test
    void alarmTypeConverter_invalidValue_throwsIllegalArgumentException() {
        Converter<String, AlarmType> converter = config.alarmTypeConverter();
        assertThrows(IllegalArgumentException.class, () -> converter.convert("unknown"));
    }

    // --- OrderItemActionType ---

    @Test
    void orderItemActionTypeConverter_validValue_returnsEnum() {
        Converter<String, OrderItemActionType> converter = config.orderItemActionTypeConverter();
        assertEquals(OrderItemActionType.ADD, converter.convert("add"));
    }

    @Test
    void orderItemActionTypeConverter_anotherValidValue_returnsEnum() {
        Converter<String, OrderItemActionType> converter = config.orderItemActionTypeConverter();
        assertEquals(OrderItemActionType.NO_CHANGE, converter.convert("noChange"));
    }

    @Test
    void orderItemActionTypeConverter_invalidValue_throwsIllegalArgumentException() {
        Converter<String, OrderItemActionType> converter = config.orderItemActionTypeConverter();
        assertThrows(IllegalArgumentException.class, () -> converter.convert("unknown"));
    }

    // --- PerceivedSeverity ---

    @Test
    void perceivedSeverityConverter_validValue_returnsEnum() {
        Converter<String, PerceivedSeverity> converter = config.perceivedSeverityConverter();
        assertEquals(PerceivedSeverity.CRITICAL, converter.convert("CRITICAL"));
    }

    @Test
    void perceivedSeverityConverter_anotherValidValue_returnsEnum() {
        Converter<String, PerceivedSeverity> converter = config.perceivedSeverityConverter();
        assertEquals(PerceivedSeverity.CLEARED, converter.convert("CLEARED"));
    }

    @Test
    void perceivedSeverityConverter_invalidValue_throwsIllegalArgumentException() {
        Converter<String, PerceivedSeverity> converter = config.perceivedSeverityConverter();
        assertThrows(IllegalArgumentException.class, () -> converter.convert("unknown"));
    }

    // --- ServiceStateType ---

    @Test
    void serviceStateTypeConverter_validValue_returnsEnum() {
        Converter<String, ServiceStateType> converter = config.serviceStateTypeConverter();
        assertEquals(ServiceStateType.ACTIVE, converter.convert("active"));
    }

    @Test
    void serviceStateTypeConverter_anotherValidValue_returnsEnum() {
        Converter<String, ServiceStateType> converter = config.serviceStateTypeConverter();
        assertEquals(ServiceStateType.TERMINATED, converter.convert("terminated"));
    }

    @Test
    void serviceStateTypeConverter_invalidValue_throwsIllegalArgumentException() {
        Converter<String, ServiceStateType> converter = config.serviceStateTypeConverter();
        assertThrows(IllegalArgumentException.class, () -> converter.convert("unknown"));
    }
}
