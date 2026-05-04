package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.AiModelSpecificationUpdate;
import org.etsi.osl.controllers.tmf915.repo.AiModelSpecificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiModelSpecificationRepositoryServiceUnitTest {

    @Mock
    private AiModelSpecificationRepository aiModelSpecificationRepository;

    @InjectMocks
    private AiModelSpecificationRepositoryService service;

    @Test
    void findAllAiModelSpecifications_returnsList() {
        AiModelSpecification s1 = new AiModelSpecification();
        s1.setId("1");
        AiModelSpecification s2 = new AiModelSpecification();
        s2.setId("2");

        when(aiModelSpecificationRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(s1, s2)));

        List<AiModelSpecification> result = service.findAllAiModelSpecifications(null, null);

        assertEquals(2, result.size());
        verify(aiModelSpecificationRepository).findAll(any(PageRequest.class));
    }

    @Test
    void findAiModelSpecificationById_found_returnsEntity() {
        AiModelSpecification spec = new AiModelSpecification();
        spec.setId("spec-1");
        spec.setName("GPT-4");

        when(aiModelSpecificationRepository.findById("spec-1")).thenReturn(Optional.of(spec));

        AiModelSpecification result = service.findAiModelSpecificationById("spec-1");

        assertNotNull(result);
        assertEquals("spec-1", result.getId());
        assertEquals("GPT-4", result.getName());
    }

    @Test
    void findAiModelSpecificationById_notFound_returnsNull() {
        when(aiModelSpecificationRepository.findById("missing")).thenReturn(Optional.empty());

        AiModelSpecification result = service.findAiModelSpecificationById("missing");

        assertNull(result);
    }

    @Test
    void findAiModelSpecificationByNameAndVersion_found_returnsEntity() {
        AiModelSpecification spec = new AiModelSpecification();
        spec.setId("spec-nv");
        spec.setName("BERT");
        spec.setVersion("2.0");

        when(aiModelSpecificationRepository.findByNameAndVersion("BERT", "2.0"))
                .thenReturn(Optional.of(spec));

        AiModelSpecification result = service.findAiModelSpecificationByNameAndVersion("BERT", "2.0");

        assertNotNull(result);
        assertEquals("BERT", result.getName());
        assertEquals("2.0", result.getVersion());
    }

    @Test
    void findAiModelSpecificationByNameAndVersion_notFound_returnsNull() {
        when(aiModelSpecificationRepository.findByNameAndVersion("Unknown", "1.0"))
                .thenReturn(Optional.empty());

        AiModelSpecification result = service.findAiModelSpecificationByNameAndVersion("Unknown", "1.0");

        assertNull(result);
    }

    @Test
    void findAiModelSpecificationByName_found_returnsEntity() {
        AiModelSpecification spec = new AiModelSpecification();
        spec.setId("spec-n");
        spec.setName("ResNet");

        when(aiModelSpecificationRepository.findByName("ResNet")).thenReturn(Optional.of(spec));

        AiModelSpecification result = service.findAiModelSpecificationByName("ResNet");

        assertNotNull(result);
        assertEquals("ResNet", result.getName());
    }

    @Test
    void findAiModelSpecificationByName_notFound_returnsNull() {
        when(aiModelSpecificationRepository.findByName("Unknown")).thenReturn(Optional.empty());

        AiModelSpecification result = service.findAiModelSpecificationByName("Unknown");

        assertNull(result);
    }

    @Test
    void createAiModelSpecification_assignsIdAndSaves() {
        AiModelSpecificationCreate create = new AiModelSpecificationCreate();
        create.setName("VGG16");
        create.setVersion("1.0");

        when(aiModelSpecificationRepository.save(any(AiModelSpecification.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        AiModelSpecification result = service.createAiModelSpecification(create);

        assertNotNull(result.getId());
        assertEquals("AIModelSpecification", result.getAtType());
        assertEquals("ServiceSpecification", result.getAtBaseType());
        assertEquals("/tmf-api/AiM/v4/aiModelSpecification/" + result.getId(), result.getHref().toString());
        verify(aiModelSpecificationRepository).save(any(AiModelSpecification.class));
    }

    @Test
    void findAiModelSpecificationById_normalizesReturnedSpecification() {
        AiModelSpecification spec = new AiModelSpecification();
        spec.setId("spec-1");

        when(aiModelSpecificationRepository.findById("spec-1")).thenReturn(Optional.of(spec));

        AiModelSpecification result = service.findAiModelSpecificationById("spec-1");

        assertEquals("AIModelSpecification", result.getAtType());
        assertEquals("ServiceSpecification", result.getAtBaseType());
        assertEquals("/tmf-api/AiM/v4/aiModelSpecification/spec-1", result.getHref().toString());
    }

    @Test
    void updateAiModelSpecification_found_appliesUpdateAndSaves() {
        AiModelSpecification existing = new AiModelSpecification();
        existing.setId("spec-upd");
        existing.setName("OldName");
        existing.setVersion("1.0");

        AiModelSpecificationUpdate update = new AiModelSpecificationUpdate();
        update.setName("NewName");
        update.setVersion("2.0");

        when(aiModelSpecificationRepository.findById("spec-upd")).thenReturn(Optional.of(existing));
        when(aiModelSpecificationRepository.save(any(AiModelSpecification.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        AiModelSpecification result = service.updateAiModelSpecification("spec-upd", update);

        assertEquals("NewName", result.getName());
        assertEquals("2.0", result.getVersion());
        verify(aiModelSpecificationRepository).save(existing);
    }

    @Test
    void updateAiModelSpecification_notFound_throwsIllegalArgumentException() {
        when(aiModelSpecificationRepository.findById("none")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.updateAiModelSpecification("none", new AiModelSpecificationUpdate()));
    }

    @Test
    void deleteAiModelSpecification_found_deletesEntity() {
        AiModelSpecification spec = new AiModelSpecification();
        spec.setId("del-1");

        when(aiModelSpecificationRepository.findById("del-1")).thenReturn(Optional.of(spec));

        service.deleteAiModelSpecification("del-1");

        verify(aiModelSpecificationRepository).delete(spec);
    }

    @Test
    void deleteAiModelSpecification_notFound_throwsIllegalArgumentException() {
        when(aiModelSpecificationRepository.findById("none")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.deleteAiModelSpecification("none"));
    }
}
