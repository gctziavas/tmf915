package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.AiContractViolation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiContractViolationRepository extends CrudRepository<AiContractViolation, String>, PagingAndSortingRepository<AiContractViolation, String> {
}
