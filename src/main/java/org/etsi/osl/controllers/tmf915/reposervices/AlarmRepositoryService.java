package org.etsi.osl.controllers.tmf915.reposervices;

import org.etsi.osl.controllers.tmf915.mappers.AlarmMapper;
import org.etsi.osl.controllers.tmf915.model.Alarm;
import org.etsi.osl.controllers.tmf915.model.AlarmCreate;
import org.etsi.osl.controllers.tmf915.model.AlarmUpdate;
import org.etsi.osl.controllers.tmf915.repo.AlarmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AlarmRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(AlarmRepositoryService.class);

    private final AlarmRepository alarmRepository;

    public AlarmRepositoryService(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    public List<Alarm> findAllAlarms() {
        log.info("Alarms LIST");
        List<Alarm> result = new ArrayList<>();
        alarmRepository.findAll().forEach(result::add);
        return result;
    }

    public Alarm findAlarmById(String id) {
        log.info("Alarm FIND BY ID: {}", id);
        return alarmRepository.findById(id).orElse(null);
    }

    public Alarm createAlarm(AlarmCreate src) {
        log.info("Alarm CREATE: {}", src);
        Alarm alarm = AlarmMapper.fromCreate(src);
        alarm.setId(UUID.randomUUID().toString());
        return alarmRepository.save(alarm);
    }

    public Alarm updateAlarm(String id, AlarmUpdate src) {
        log.info("Alarm UPDATE with ID: {}", id);
        Alarm existing = alarmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Alarm with ID: " + id));
        AlarmMapper.applyUpdate(existing, src);
        return alarmRepository.save(existing);
    }

    public void deleteAlarm(String id) {
        log.info("Alarm DELETE with ID: {}", id);
        Alarm alarm = alarmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Alarm with ID: " + id));
        alarmRepository.delete(alarm);
    }
}
