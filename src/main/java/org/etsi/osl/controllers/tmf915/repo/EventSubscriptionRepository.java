package org.etsi.osl.controllers.tmf915.repo;

import org.etsi.osl.controllers.tmf915.model.EventSubscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventSubscriptionRepository extends CrudRepository<EventSubscription, String>, PagingAndSortingRepository<EventSubscription, String> {
}
