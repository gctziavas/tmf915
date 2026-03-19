package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.Rule;
import org.etsi.osl.controllers.tmf915.model.RuleCreate;
import org.etsi.osl.controllers.tmf915.model.RuleUpdate;

public final class RuleMapper {

    private RuleMapper() {}

    public static Rule fromCreate(RuleCreate src) {
        Rule r = new Rule();
        r.setName(src.getName());
        r.setAtBaseType(src.getAtBaseType());
        r.setAtSchemaLocation(src.getAtSchemaLocation());
        r.setAtType(src.getAtType());
        return r;
    }

    public static void applyUpdate(Rule target, RuleUpdate src) {
        if (src.getName() != null)              target.setName(src.getName());
        if (src.getAtBaseType() != null)        target.setAtBaseType(src.getAtBaseType());
        if (src.getAtSchemaLocation() != null)  target.setAtSchemaLocation(src.getAtSchemaLocation());
        if (src.getAtType() != null)            target.setAtType(src.getAtType());
    }
}
