package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.mappers.TopicMapper;
import org.etsi.osl.controllers.tmf915.model.Topic;
import org.etsi.osl.controllers.tmf915.model.TopicCreate;
import org.etsi.osl.controllers.tmf915.repo.TopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TopicRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(TopicRepositoryService.class);

    private final TopicRepository topicRepository;

    public TopicRepositoryService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> findAllTopics() {
        log.info("Topics LIST");
        List<Topic> result = new ArrayList<>();
        topicRepository.findAll().forEach(result::add);
        return result;
    }

    public Topic findTopicById(String id) {
        log.info("Topic FIND BY ID: {}", id);
        return topicRepository.findById(id).orElse(null);
    }

    public Topic createTopic(TopicCreate src) {
        log.info("Topic CREATE: {}", src);
        Topic topic = TopicMapper.fromCreate(src);
        topic.setId(UUID.randomUUID().toString());
        return topicRepository.save(topic);
    }

    public void deleteTopic(String id) {
        log.info("Topic DELETE with ID: {}", id);
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Topic with ID: " + id));
        topicRepository.delete(topic);
    }
}
