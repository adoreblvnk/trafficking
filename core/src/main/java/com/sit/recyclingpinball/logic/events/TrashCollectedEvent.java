package com.sit.recyclingpinball.logic.events;

import com.sit.recyclingpinball.logic.factories.TrashType;

public class TrashCollectedEvent implements IPinballEvent {
    private final TrashType type;
    private final String entityId;

    public TrashCollectedEvent(TrashType type, String entityId) {
        this.type = type;
        this.entityId = entityId;
    }

    public TrashType getType() {
        return type;
    }

    public String getEntityId() {
        return entityId;
    }

    @Override
    public void accept(PinballEventVisitor visitor) {
        visitor.visit(this);
    }
}
