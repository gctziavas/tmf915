package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.model.Topic;
import org.etsi.osl.controllers.tmf915.model.TopicCreate;
import org.etsi.osl.controllers.tmf915.reposervices.TopicRepositoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public class TopicApiDelegateImpl implements TopicApiDelegate {

    private final TopicRepositoryService topicRepositoryService;

    public TopicApiDelegateImpl(TopicRepositoryService topicRepositoryService) {
        this.topicRepositoryService = topicRepositoryService;
    }

    @Override
    public ResponseEntity<Topic> createTopic(TopicCreate topic) {
        Topic created = topicRepositoryService.createTopic(topic);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<Void> deleteTopic(String id) {
        topicRepositoryService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<Topic>> listTopic(String fields, Integer offset, Integer limit) {
        return ResponseEntity.ok(topicRepositoryService.findAllTopics());
    }

    @Override
    public ResponseEntity<Topic> retrieveTopic(String id, String fields) {
        Topic topic = topicRepositoryService.findTopicById(id);
        if (topic == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(topic);
    }
}
