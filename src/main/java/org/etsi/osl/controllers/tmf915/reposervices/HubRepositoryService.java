package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.mappers.HubMapper;
import org.etsi.osl.controllers.tmf915.model.Hub;
import org.etsi.osl.controllers.tmf915.model.HubCreate;
import org.etsi.osl.controllers.tmf915.repo.HubRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HubRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(HubRepositoryService.class);

    private final HubRepository hubRepository;

    public HubRepositoryService(HubRepository hubRepository) {
        this.hubRepository = hubRepository;
    }

    public List<Hub> findHubsByTopicId(String topicId) {
        log.info("Hubs LIST for topicId: {}", topicId);
        return hubRepository.findByTopicId(topicId);
    }

    public Hub findHubById(String id) {
        log.info("Hub FIND BY ID: {}", id);
        return hubRepository.findById(id).orElse(null);
    }

    public Hub createHub(String topicId, HubCreate src) {
        log.info("Hub CREATE for topicId: {}", topicId);
        Hub hub = HubMapper.fromCreate(src);
        hub.setId(UUID.randomUUID().toString());
        hub.setTopicId(topicId);
        return hubRepository.save(hub);
    }

    public void deleteHub(String id) {
        log.info("Hub DELETE with ID: {}", id);
        Hub hub = hubRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Hub with ID: " + id));
        hubRepository.delete(hub);
    }
}
