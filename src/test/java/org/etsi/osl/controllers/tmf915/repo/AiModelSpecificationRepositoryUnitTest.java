package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiModelSpecificationRepositoryUnitTest {

    @Mock
    private AiModelSpecificationRepository repository;

    private AiModelSpecification spec(String name, String version) {
        AiModelSpecification s = new AiModelSpecification();
        s.setId(UUID.randomUUID().toString());
        s.setName(name);
        s.setVersion(version);
        return s;
    }

    // --- findByNameAndVersion ---

    @Test
    void findByNameAndVersion_exactMatch_returnsSpec() {
        AiModelSpecification s = spec("BERT", "1.0");
        when(repository.findByNameAndVersion("BERT", "1.0")).thenReturn(Optional.of(s));

        Optional<AiModelSpecification> result = repository.findByNameAndVersion("BERT", "1.0");
        assertTrue(result.isPresent());
        assertEquals("BERT", result.get().getName());
        assertEquals("1.0", result.get().getVersion());
    }

    @Test
    void findByNameAndVersion_differentVersion_returnsMatch() {
        AiModelSpecification s = spec("BERT", "2.0");
        when(repository.findByNameAndVersion("BERT", "2.0")).thenReturn(Optional.of(s));

        Optional<AiModelSpecification> result = repository.findByNameAndVersion("BERT", "2.0");
        assertTrue(result.isPresent());
        assertEquals("2.0", result.get().getVersion());
    }

    @Test
    void findByNameAndVersion_unknownName_returnsEmpty() {
        when(repository.findByNameAndVersion("Unknown", "1.0")).thenReturn(Optional.empty());

        Optional<AiModelSpecification> result = repository.findByNameAndVersion("Unknown", "1.0");
        assertFalse(result.isPresent());
    }

    @Test
    void findByNameAndVersion_unknownVersion_returnsEmpty() {
        when(repository.findByNameAndVersion("BERT", "99.0")).thenReturn(Optional.empty());

        Optional<AiModelSpecification> result = repository.findByNameAndVersion("BERT", "99.0");
        assertFalse(result.isPresent());
    }

    // --- findByName ---

    @Test
    void findByName_uniqueName_returnsSpec() {
        AiModelSpecification s = spec("GPT4", "1.0");
        when(repository.findByName("GPT4")).thenReturn(Optional.of(s));

        Optional<AiModelSpecification> result = repository.findByName("GPT4");
        assertTrue(result.isPresent());
        assertEquals("GPT4", result.get().getName());
    }

    @Test
    void findByName_unknownName_returnsEmpty() {
        when(repository.findByName("ResNet")).thenReturn(Optional.empty());

        Optional<AiModelSpecification> result = repository.findByName("ResNet");
        assertFalse(result.isPresent());
    }
}
