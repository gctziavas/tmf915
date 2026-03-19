package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.Hub;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HubRepository extends CrudRepository<Hub, String>, PagingAndSortingRepository<Hub, String> {
    List<Hub> findByTopicId(String topicId);
}
