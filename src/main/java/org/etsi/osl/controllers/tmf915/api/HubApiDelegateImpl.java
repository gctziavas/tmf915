package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.model.Hub;
import org.etsi.osl.controllers.tmf915.model.HubCreate;
import org.etsi.osl.controllers.tmf915.reposervices.HubRepositoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public class HubApiDelegateImpl implements HubApiDelegate {

    private final HubRepositoryService hubRepositoryService;

    public HubApiDelegateImpl(HubRepositoryService hubRepositoryService) {
        this.hubRepositoryService = hubRepositoryService;
    }

    @Override
    public ResponseEntity<Hub> createHub(String topicId, HubCreate hub) {
        Hub created = hubRepositoryService.createHub(topicId, hub);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<Void> deleteHub(String topicId, String id) {
        hubRepositoryService.deleteHub(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<Hub>> listHub(String topicId, String fields, Integer offset, Integer limit) {
        return ResponseEntity.ok(hubRepositoryService.findHubsByTopicId(topicId));
    }

    @Override
    public ResponseEntity<Hub> retrieveHub(String topicId, String id, String fields) {
        Hub hub = hubRepositoryService.findHubById(id);
        if (hub == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(hub);
    }
}
