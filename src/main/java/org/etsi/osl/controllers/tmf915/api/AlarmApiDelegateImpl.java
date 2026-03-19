package org.etsi.osl.controllers.tmf915.api;

import org.etsi.osl.controllers.tmf915.model.Alarm;
import org.etsi.osl.controllers.tmf915.model.AlarmCreate;
import org.etsi.osl.controllers.tmf915.model.AlarmUpdate;
import org.etsi.osl.controllers.tmf915.reposervices.AlarmRepositoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public class AlarmApiDelegateImpl implements AlarmApiDelegate {

    private final AlarmRepositoryService alarmRepositoryService;

    public AlarmApiDelegateImpl(AlarmRepositoryService alarmRepositoryService) {
        this.alarmRepositoryService = alarmRepositoryService;
    }

    @Override
    public ResponseEntity<Alarm> createAlarm(AlarmCreate alarm) {
        Alarm created = alarmRepositoryService.createAlarm(alarm);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<Void> deleteAlarm(String id) {
        alarmRepositoryService.deleteAlarm(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<Alarm>> listAlarm(String fields, Integer offset, Integer limit) {
        return ResponseEntity.ok(alarmRepositoryService.findAllAlarms());
    }

    @Override
    public ResponseEntity<Alarm> patchAlarm(String id, AlarmUpdate alarm) {
        Alarm updated = alarmRepositoryService.updateAlarm(id, alarm);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Alarm> retrieveAlarm(String id, String fields) {
        Alarm alarm = alarmRepositoryService.findAlarmById(id);
        if (alarm == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(alarm);
    }
}
