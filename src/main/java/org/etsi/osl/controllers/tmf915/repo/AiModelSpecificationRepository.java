package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.AiModelSpecification;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AiModelSpecificationRepository extends CrudRepository<AiModelSpecification, String>, PagingAndSortingRepository<AiModelSpecification, String> {
    Optional<AiModelSpecification> findByNameAndVersion(String name, String version);

    Optional<AiModelSpecification> findByName(String name);
}
