package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.Topic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends CrudRepository<Topic, String>, PagingAndSortingRepository<Topic, String> {
}
