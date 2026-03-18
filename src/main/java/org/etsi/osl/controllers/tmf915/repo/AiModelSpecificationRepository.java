package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;

import java.util.List;
import java.util.Optional;

public interface AiModelSpecificationRepository {

    Optional<AiModelSpecification> findByUuid(String uuid);

    Optional<AiModelSpecification> findByNameAndVersion(String name, String version);

    Optional<AiModelSpecification> findByName(String name);

    List<AiModelSpecification> findAll();

    AiModelSpecification save(AiModelSpecification spec);

    void delete(AiModelSpecification spec);
}
