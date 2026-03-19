package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.mappers.RuleMapper;
import org.etsi.osl.controllers.tmf915.model.Rule;
import org.etsi.osl.controllers.tmf915.model.RuleCreate;
import org.etsi.osl.controllers.tmf915.model.RuleUpdate;
import org.etsi.osl.controllers.tmf915.repo.RuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RuleRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(RuleRepositoryService.class);

    private final RuleRepository ruleRepository;

    public RuleRepositoryService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public List<Rule> findAllRules() {
        log.info("Rules LIST");
        List<Rule> result = new ArrayList<>();
        ruleRepository.findAll().forEach(result::add);
        return result;
    }

    public Rule findRuleById(String id) {
        log.info("Rule FIND BY ID: {}", id);
        return ruleRepository.findById(id).orElse(null);
    }

    public Rule createRule(RuleCreate src) {
        log.info("Rule CREATE: {}", src);
        Rule rule = RuleMapper.fromCreate(src);
        rule.setId(UUID.randomUUID().toString());
        return ruleRepository.save(rule);
    }

    public Rule updateRule(String id, RuleUpdate src) {
        log.info("Rule UPDATE with ID: {}", id);
        Rule existing = ruleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Rule with ID: " + id));
        RuleMapper.applyUpdate(existing, src);
        return ruleRepository.save(existing);
    }

    public void deleteRule(String id) {
        log.info("Rule DELETE with ID: {}", id);
        Rule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Rule with ID: " + id));
        ruleRepository.delete(rule);
    }
}
