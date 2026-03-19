package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.AiModelCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelUpdate;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AiModelMapperTest {

    @Test
    void fromCreate_allFieldsSet_mapsCorrectly() {
        AiModelCreate src = new AiModelCreate();
        src.setCategory("NLP");
        src.setDescription("A language model");
        src.setName("TestModel");
        src.setServiceType("inference");
        src.setStartMode("auto");
        src.setIsBundle(true);
        src.setIsServiceEnabled(true);
        src.setIsStateful(false);
        src.setHasStarted(true);
        src.setState(ServiceStateType.ACTIVE);
        OffsetDateTime now = OffsetDateTime.now();
        src.setStartDate(now);
        src.setEndDate(now.plusDays(30));
        src.setServiceDate("2025-01-01");

        AiModel result = AiModelMapper.fromCreate(src);

        assertEquals("NLP", result.getCategory());
        assertEquals("A language model", result.getDescription());
        assertEquals("TestModel", result.getName());
        assertEquals("inference", result.getServiceType());
        assertEquals("auto", result.getStartMode());
        assertTrue(result.getIsBundle());
        assertTrue(result.getIsServiceEnabled());
        assertFalse(result.getIsStateful());
        assertTrue(result.getHasStarted());
        assertEquals(ServiceStateType.ACTIVE, result.getState());
        assertEquals(now, result.getStartDate());
        assertEquals(now.plusDays(30), result.getEndDate());
        assertEquals("2025-01-01", result.getServiceDate());
    }

    @Test
    void fromCreate_nullFields_mapsNulls() {
        AiModelCreate src = new AiModelCreate();

        AiModel result = AiModelMapper.fromCreate(src);

        assertNotNull(result);
        assertNull(result.getCategory());
        assertNull(result.getName());
        assertNull(result.getDescription());
        assertNull(result.getState());
    }

    @Test
    void applyUpdate_updatesOnlyNonNullFields() {
        AiModel target = new AiModel();
        target.setName("Original");
        target.setCategory("CV");
        target.setDescription("Old description");
        target.setState(ServiceStateType.ACTIVE);

        AiModelUpdate src = new AiModelUpdate();
        src.setName("Updated");
        src.setDescription("New description");
        // category and state left null

        AiModelMapper.applyUpdate(target, src);

        assertEquals("Updated", target.getName());
        assertEquals("New description", target.getDescription());
        assertEquals("CV", target.getCategory());
        assertEquals(ServiceStateType.ACTIVE, target.getState());
    }

    @Test
    void applyUpdate_allNull_nothingChanges() {
        AiModel target = new AiModel();
        target.setName("Kept");
        target.setCategory("Audio");

        AiModelUpdate src = new AiModelUpdate();

        AiModelMapper.applyUpdate(target, src);

        assertEquals("Kept", target.getName());
        assertEquals("Audio", target.getCategory());
    }

    @Test
    void applyUpdate_overwriteState() {
        AiModel target = new AiModel();
        target.setState(ServiceStateType.ACTIVE);

        AiModelUpdate src = new AiModelUpdate();
        src.setState(ServiceStateType.INACTIVE);

        AiModelMapper.applyUpdate(target, src);

        assertEquals(ServiceStateType.INACTIVE, target.getState());
    }
}
