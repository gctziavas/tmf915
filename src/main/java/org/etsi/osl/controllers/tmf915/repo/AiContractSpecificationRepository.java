package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.AiContractSpecification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiContractSpecificationRepository extends CrudRepository<AiContractSpecification, String>, PagingAndSortingRepository<AiContractSpecification, String> {
}
