package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.AiContractSpecification;
import org.etsi.osl.controllers.tmf915.model.AiContractSpecificationCreate;
import org.etsi.osl.controllers.tmf915.model.AiContractSpecificationUpdate;

public final class AiContractSpecificationMapper {

    private AiContractSpecificationMapper() {}

    public static AiContractSpecification fromCreate(AiContractSpecificationCreate src) {
        AiContractSpecification s = new AiContractSpecification();
        s.setDescription(src.getDescription());
        s.setIsBundle(src.getIsBundle());
        s.setLastUpdate(src.getLastUpdate());
        s.setLifecycleStatus(src.getLifecycleStatus());
        s.setName(src.getName());
        s.setVersion(src.getVersion());
        s.setAttachment(src.getAttachment());
        s.setConstraint(src.getConstraint());
        s.setEntitySpecRelationship(src.getEntitySpecRelationship());
        s.setRelatedParty(src.getRelatedParty());
        s.setSpecCharacteristic(src.getSpecCharacteristic());
        s.setTargetEntitySchema(src.getTargetEntitySchema());
        s.setValidFor(src.getValidFor());
        s.setAtBaseType(src.getAtBaseType());
        s.setAtSchemaLocation(src.getAtSchemaLocation());
        s.setAtType(src.getAtType());
        return s;
    }

    public static void applyUpdate(AiContractSpecification target, AiContractSpecificationUpdate src) {
        if (src.getDescription() != null)               target.setDescription(src.getDescription());
        if (src.getIsBundle() != null)                  target.setIsBundle(src.getIsBundle());
        if (src.getLifecycleStatus() != null)           target.setLifecycleStatus(src.getLifecycleStatus());
        if (src.getName() != null)                      target.setName(src.getName());
        if (src.getVersion() != null)                   target.setVersion(src.getVersion());
        if (src.getAttachment() != null)                target.setAttachment(src.getAttachment());
        if (src.getConstraint() != null)                target.setConstraint(src.getConstraint());
        if (src.getEntitySpecRelationship() != null)    target.setEntitySpecRelationship(src.getEntitySpecRelationship());
        if (src.getRelatedParty() != null)              target.setRelatedParty(src.getRelatedParty());
        if (src.getSpecCharacteristic() != null)        target.setSpecCharacteristic(src.getSpecCharacteristic());
        if (src.getTargetEntitySchema() != null)        target.setTargetEntitySchema(src.getTargetEntitySchema());
        if (src.getValidFor() != null)                  target.setValidFor(src.getValidFor());
        if (src.getAtBaseType() != null)                target.setAtBaseType(src.getAtBaseType());
        if (src.getAtSchemaLocation() != null)          target.setAtSchemaLocation(src.getAtSchemaLocation());
        if (src.getAtType() != null)                    target.setAtType(src.getAtType());
    }
}
