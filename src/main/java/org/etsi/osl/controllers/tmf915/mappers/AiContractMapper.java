package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.AiContract;
import org.etsi.osl.controllers.tmf915.model.AiContractCreate;
import org.etsi.osl.controllers.tmf915.model.AiContractUpdate;

public final class AiContractMapper {

    private AiContractMapper() {}

    public static AiContract fromCreate(AiContractCreate src) {
        AiContract c = new AiContract();
        c.setApprovalDate(src.getApprovalDate());
        c.setApproved(src.getApproved());
        c.setDescription(src.getDescription());
        c.setName(src.getName());
        c.setState(src.getState());
        c.setVersion(src.getVersion());
        c.setAiContractSpecification(src.getAiContractSpecification());
        c.setAiModel(src.getAiModel());
        c.setCharacteristic(src.getCharacteristic());
        c.setRelatedParty(src.getRelatedParty());
        c.setRule(src.getRule());
        c.setTemplate(src.getTemplate());
        c.setValidFor(src.getValidFor());
        c.setAtBaseType(src.getAtBaseType());
        c.setAtSchemaLocation(src.getAtSchemaLocation());
        c.setAtType(src.getAtType());
        return c;
    }

    public static void applyUpdate(AiContract target, AiContractUpdate src) {
        if (src.getApprovalDate() != null)              target.setApprovalDate(src.getApprovalDate());
        if (src.getApproved() != null)                  target.setApproved(src.getApproved());
        if (src.getDescription() != null)               target.setDescription(src.getDescription());
        if (src.getName() != null)                      target.setName(src.getName());
        if (src.getState() != null)                     target.setState(src.getState());
        if (src.getVersion() != null)                   target.setVersion(src.getVersion());
        if (src.getAiContractSpecification() != null)   target.setAiContractSpecification(src.getAiContractSpecification());
        if (src.getAiModel() != null)                   target.setAiModel(src.getAiModel());
        if (src.getCharacteristic() != null)            target.setCharacteristic(src.getCharacteristic());
        if (src.getRelatedParty() != null)              target.setRelatedParty(src.getRelatedParty());
        if (src.getRule() != null)                      target.setRule(src.getRule());
        if (src.getTemplate() != null)                  target.setTemplate(src.getTemplate());
        if (src.getValidFor() != null)                  target.setValidFor(src.getValidFor());
        if (src.getAtBaseType() != null)                target.setAtBaseType(src.getAtBaseType());
        if (src.getAtSchemaLocation() != null)          target.setAtSchemaLocation(src.getAtSchemaLocation());
        if (src.getAtType() != null)                    target.setAtType(src.getAtType());
    }
}
