package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.AiModel;
import org.etsi.osl.controllers.tmf915.model.ServiceStateType;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiModelRepository extends CrudRepository<AiModel, String>, PagingAndSortingRepository<AiModel, String> {
    List<AiModel> findByNameStartingWith(String namePrefix);

    List<AiModel> findByState(ServiceStateType state);
}
