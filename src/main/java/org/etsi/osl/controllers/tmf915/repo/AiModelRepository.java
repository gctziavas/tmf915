package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiModelRepository extends CrudRepository<AiModel, String>, PagingAndSortingRepository<AiModel, String> {
    Optional<AiModel> findByUuid(String uuid);
    
    List<AiModel> findByNameStartingWith(String namePrefix);
    
    List<AiModel> findByState(ServiceStateType state);
}
