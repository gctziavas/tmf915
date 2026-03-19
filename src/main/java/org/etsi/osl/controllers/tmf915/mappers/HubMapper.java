package org.etsi.osl.controllers.tmf915.mappers;

import org.etsi.osl.controllers.tmf915.model.Hub;
import org.etsi.osl.controllers.tmf915.model.HubCreate;

public final class HubMapper {

    private HubMapper() {}

    public static Hub fromCreate(HubCreate src) {
        Hub h = new Hub();
        h.setCallback(src.getCallback());
        h.setQuery(src.getQuery());
        h.setAtBaseType(src.getAtBaseType());
        h.setAtSchemaLocation(src.getAtSchemaLocation());
        h.setAtType(src.getAtType());
        return h;
    }
}
