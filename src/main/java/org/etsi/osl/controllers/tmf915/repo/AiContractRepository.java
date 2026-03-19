package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.AiContract;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiContractRepository extends CrudRepository<AiContract, String>, PagingAndSortingRepository<AiContract, String> {
}
