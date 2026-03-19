package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.model.Monitor;
import org.etsi.osl.controllers.tmf915.repo.MonitorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MonitorRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(MonitorRepositoryService.class);

    private final MonitorRepository monitorRepository;

    public MonitorRepositoryService(MonitorRepository monitorRepository) {
        this.monitorRepository = monitorRepository;
    }

    public List<Monitor> findAllMonitors() {
        log.info("Monitors LIST");
        List<Monitor> result = new ArrayList<>();
        monitorRepository.findAll().forEach(result::add);
        return result;
    }

    public Monitor findMonitorById(String id) {
        log.info("Monitor FIND BY ID: {}", id);
        return monitorRepository.findById(id).orElse(null);
    }
}
