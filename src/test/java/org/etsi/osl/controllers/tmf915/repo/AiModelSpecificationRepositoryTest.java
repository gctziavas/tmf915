package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class AiModelSpecificationRepositoryTest {

    @Autowired
    private AiModelSpecificationRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        AiModelSpecification s1 = new AiModelSpecification();
        s1.setId(UUID.randomUUID().toString());
        s1.setName("BERT");
        s1.setVersion("1.0");
        repository.save(s1);

        AiModelSpecification s2 = new AiModelSpecification();
        s2.setId(UUID.randomUUID().toString());
        s2.setName("BERT");
        s2.setVersion("2.0");
        repository.save(s2);

        AiModelSpecification s3 = new AiModelSpecification();
        s3.setId(UUID.randomUUID().toString());
        s3.setName("GPT4");
        s3.setVersion("1.0");
        repository.save(s3);
    }

    // --- findByNameAndVersion ---

    @Test
    void findByNameAndVersion_exactMatch_returnsSpec() {
        Optional<AiModelSpecification> result = repository.findByNameAndVersion("BERT", "1.0");
        assertTrue(result.isPresent());
        assertEquals("BERT", result.get().getName());
        assertEquals("1.0", result.get().getVersion());
    }

    @Test
    void findByNameAndVersion_differentVersion_returnsMatch() {
        Optional<AiModelSpecification> result = repository.findByNameAndVersion("BERT", "2.0");
        assertTrue(result.isPresent());
        assertEquals("2.0", result.get().getVersion());
    }

    @Test
    void findByNameAndVersion_unknownName_returnsEmpty() {
        Optional<AiModelSpecification> result = repository.findByNameAndVersion("Unknown", "1.0");
        assertFalse(result.isPresent());
    }

    @Test
    void findByNameAndVersion_unknownVersion_returnsEmpty() {
        Optional<AiModelSpecification> result = repository.findByNameAndVersion("BERT", "99.0");
        assertFalse(result.isPresent());
    }

    // --- findByName ---

    @Test
    void findByName_uniqueName_returnsSpec() {
        Optional<AiModelSpecification> result = repository.findByName("GPT4");
        assertTrue(result.isPresent());
        assertEquals("GPT4", result.get().getName());
    }

    @Test
    void findByName_unknownName_returnsEmpty() {
        Optional<AiModelSpecification> result = repository.findByName("ResNet");
        assertFalse(result.isPresent());
    }
}
