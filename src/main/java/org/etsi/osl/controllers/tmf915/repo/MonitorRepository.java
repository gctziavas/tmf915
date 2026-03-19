package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.Monitor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorRepository extends CrudRepository<Monitor, String>, PagingAndSortingRepository<Monitor, String> {
}
