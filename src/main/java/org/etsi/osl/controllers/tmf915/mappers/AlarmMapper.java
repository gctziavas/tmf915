package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.Alarm;
import org.etsi.osl.controllers.tmf915.model.AlarmCreate;
import org.etsi.osl.controllers.tmf915.model.AlarmUpdate;

public final class AlarmMapper {

    private AlarmMapper() {}

    public static Alarm fromCreate(AlarmCreate src) {
        Alarm a = new Alarm();
        a.setAckState(src.getAckState());
        a.setAckSystemId(src.getAckSystemId());
        a.setAckUserId(src.getAckUserId());
        a.setAlarmChangedTime(src.getAlarmChangedTime());
        a.setAlarmClearedTime(src.getAlarmClearedTime());
        a.setAlarmDetails(src.getAlarmDetails());
        a.setAlarmEscalation(src.getAlarmEscalation());
        a.setAlarmRaisedTime(src.getAlarmRaisedTime());
        a.setAlarmReportingTime(src.getAlarmReportingTime());
        a.setAlarmedObjectType(src.getAlarmedObjectType());
        a.setClearSystemId(src.getClearSystemId());
        a.setClearUserId(src.getClearUserId());
        a.setExternalAlarmId(src.getExternalAlarmId());
        a.setIsRootCause(src.getIsRootCause());
        a.setPlannedOutageIndicator(src.getPlannedOutageIndicator());
        a.setProbableCause(src.getProbableCause());
        a.setProposedRepairedActions(src.getProposedRepairedActions());
        a.setReportingSystemId(src.getReportingSystemId());
        a.setServiceAffecting(src.getServiceAffecting());
        a.setSourceSystemId(src.getSourceSystemId());
        a.setSpecificProblem(src.getSpecificProblem());
        a.setState(src.getState());
        a.setAffectedService(src.getAffectedService());
        a.setAlarmType(src.getAlarmType());
        a.setAlarmedObject(src.getAlarmedObject());
        a.setComment(src.getComment());
        a.setCorrelatedAlarm(src.getCorrelatedAlarm());
        a.setCrossedThresholdInformation(src.getCrossedThresholdInformation());
        a.setParentAlarm(src.getParentAlarm());
        a.setPerceivedSeverity(src.getPerceivedSeverity());
        a.setPlace(src.getPlace());
        a.setAtBaseType(src.getAtBaseType());
        a.setAtSchemaLocation(src.getAtSchemaLocation());
        a.setAtType(src.getAtType());
        return a;
    }

    public static void applyUpdate(Alarm target, AlarmUpdate src) {
        if (src.getAckState() != null)                  target.setAckState(src.getAckState());
        if (src.getAckSystemId() != null)               target.setAckSystemId(src.getAckSystemId());
        if (src.getAckUserId() != null)                 target.setAckUserId(src.getAckUserId());
        if (src.getAlarmChangedTime() != null)          target.setAlarmChangedTime(src.getAlarmChangedTime());
        if (src.getAlarmClearedTime() != null)          target.setAlarmClearedTime(src.getAlarmClearedTime());
        if (src.getAlarmDetails() != null)              target.setAlarmDetails(src.getAlarmDetails());
        if (src.getAlarmEscalation() != null)           target.setAlarmEscalation(src.getAlarmEscalation());
        if (src.getAlarmReportingTime() != null)        target.setAlarmReportingTime(src.getAlarmReportingTime());
        if (src.getAlarmedObjectType() != null)         target.setAlarmedObjectType(src.getAlarmedObjectType());
        if (src.getClearSystemId() != null)             target.setClearSystemId(src.getClearSystemId());
        if (src.getClearUserId() != null)               target.setClearUserId(src.getClearUserId());
        if (src.getExternalAlarmId() != null)           target.setExternalAlarmId(src.getExternalAlarmId());
        if (src.getIsRootCause() != null)               target.setIsRootCause(src.getIsRootCause());
        if (src.getPlannedOutageIndicator() != null)    target.setPlannedOutageIndicator(src.getPlannedOutageIndicator());
        if (src.getProbableCause() != null)             target.setProbableCause(src.getProbableCause());
        if (src.getProposedRepairedActions() != null)   target.setProposedRepairedActions(src.getProposedRepairedActions());
        if (src.getReportingSystemId() != null)         target.setReportingSystemId(src.getReportingSystemId());
        if (src.getServiceAffecting() != null)          target.setServiceAffecting(src.getServiceAffecting());
        if (src.getSpecificProblem() != null)           target.setSpecificProblem(src.getSpecificProblem());
        if (src.getState() != null)                     target.setState(src.getState());
        if (src.getAffectedService() != null)           target.setAffectedService(src.getAffectedService());
        if (src.getAlarmType() != null)                 target.setAlarmType(src.getAlarmType());
        if (src.getAlarmedObject() != null)             target.setAlarmedObject(src.getAlarmedObject());
        if (src.getComment() != null)                   target.setComment(src.getComment());
        if (src.getCorrelatedAlarm() != null)           target.setCorrelatedAlarm(src.getCorrelatedAlarm());
        if (src.getCrossedThresholdInformation() != null) target.setCrossedThresholdInformation(src.getCrossedThresholdInformation());
        if (src.getParentAlarm() != null)               target.setParentAlarm(src.getParentAlarm());
        if (src.getPerceivedSeverity() != null)         target.setPerceivedSeverity(src.getPerceivedSeverity());
        if (src.getPlace() != null)                     target.setPlace(src.getPlace());
        if (src.getAtBaseType() != null)                target.setAtBaseType(src.getAtBaseType());
        if (src.getAtSchemaLocation() != null)          target.setAtSchemaLocation(src.getAtSchemaLocation());
        if (src.getAtType() != null)                    target.setAtType(src.getAtType());
    }
}
