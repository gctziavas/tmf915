package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.model.Rule;
import org.etsi.osl.controllers.tmf915.model.RuleCreate;
import org.etsi.osl.controllers.tmf915.model.RuleUpdate;
import org.etsi.osl.controllers.tmf915.reposervices.RuleRepositoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleApiDelegateImpl implements RuleApiDelegate {

    private final RuleRepositoryService ruleRepositoryService;

    public RuleApiDelegateImpl(RuleRepositoryService ruleRepositoryService) {
        this.ruleRepositoryService = ruleRepositoryService;
    }

    @Override
    public ResponseEntity<Rule> createRule(RuleCreate rule) {
        Rule created = ruleRepositoryService.createRule(rule);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<Void> deleteRule(String id) {
        ruleRepositoryService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<Rule>> listRule(String fields, Integer offset, Integer limit) {
        return ResponseEntity.ok(ruleRepositoryService.findAllRules());
    }

    @Override
    public ResponseEntity<Rule> patchRule(String id, RuleUpdate rule) {
        Rule updated = ruleRepositoryService.updateRule(id, rule);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Rule> retrieveRule(String id, String fields) {
        Rule rule = ruleRepositoryService.findRuleById(id);
        if (rule == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rule);
    }
}
