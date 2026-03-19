package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.Alarm;
import org.etsi.osl.controllers.tmf915.model.AlarmCreate;
import org.etsi.osl.controllers.tmf915.model.AlarmUpdate;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AlarmMapperTest {

    @Test
    void fromCreate_allFieldsSet_mapsCorrectly() {
        AlarmCreate src = new AlarmCreate();
        src.setAckState("acknowledged");
        src.setAckSystemId("sys-1");
        src.setAckUserId("user-1");
        src.setAlarmDetails("Some details");
        src.setExternalAlarmId("ext-123");
        src.setIsRootCause(true);
        src.setProbableCause("overload");
        src.setSpecificProblem("CPU overload");
        src.setState("raised");
        src.setAtBaseType("BaseEntity");
        src.setAtType("Alarm");
        src.setAtSchemaLocation(URI.create("http://example.com"));
        OffsetDateTime now = OffsetDateTime.now();
        src.setAlarmRaisedTime(now);
        src.setAlarmReportingTime(now);

        Alarm result = AlarmMapper.fromCreate(src);

        assertEquals("acknowledged", result.getAckState());
        assertEquals("sys-1", result.getAckSystemId());
        assertEquals("user-1", result.getAckUserId());
        assertEquals("Some details", result.getAlarmDetails());
        assertEquals("ext-123", result.getExternalAlarmId());
        assertTrue(result.getIsRootCause());
        assertEquals("overload", result.getProbableCause());
        assertEquals("CPU overload", result.getSpecificProblem());
        assertEquals("raised", result.getState());
        assertEquals(now, result.getAlarmRaisedTime());
        assertEquals(now, result.getAlarmReportingTime());
    }

    @Test
    void fromCreate_nullFields_mapsNulls() {
        AlarmCreate src = new AlarmCreate();

        Alarm result = AlarmMapper.fromCreate(src);

        assertNotNull(result);
        assertNull(result.getAckState());
        assertNull(result.getState());
        assertNull(result.getAlarmRaisedTime());
    }

    @Test
    void applyUpdate_updatesOnlyNonNullFields() {
        Alarm target = new Alarm();
        target.setAckState("unacknowledged");
        target.setState("raised");
        target.setSpecificProblem("Old problem");

        AlarmUpdate src = new AlarmUpdate();
        src.setAckState("acknowledged");
        src.setAckUserId("admin");

        AlarmMapper.applyUpdate(target, src);

        assertEquals("acknowledged", target.getAckState());
        assertEquals("admin", target.getAckUserId());
        assertEquals("raised", target.getState());
        assertEquals("Old problem", target.getSpecificProblem());
    }

    @Test
    void applyUpdate_allNull_nothingChanges() {
        Alarm target = new Alarm();
        target.setState("cleared");

        AlarmUpdate src = new AlarmUpdate();

        AlarmMapper.applyUpdate(target, src);

        assertEquals("cleared", target.getState());
    }
}
