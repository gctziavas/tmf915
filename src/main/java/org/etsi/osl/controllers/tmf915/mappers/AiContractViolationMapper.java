package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.AiContractViolation;
import org.etsi.osl.controllers.tmf915.model.AiContractViolationCreate;

public final class AiContractViolationMapper {

    private AiContractViolationMapper() {}

    public static AiContractViolation fromCreate(AiContractViolationCreate src) {
        AiContractViolation v = new AiContractViolation();
        v.setDate(src.getDate());
        v.setAiContract(src.getAiContract());
        v.setRelatedParty(src.getRelatedParty());
        v.setViolation(src.getViolation());
        v.setAtBaseType(src.getAtBaseType());
        v.setAtSchemaLocation(src.getAtSchemaLocation());
        v.setAtType(src.getAtType());
        return v;
    }
}
