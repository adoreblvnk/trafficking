package com.sit.recyclingpinball.logic.events;

public class TrashCollectedEvent implements IPinballEvent {
    private final String entityId;

    public TrashCollectedEvent(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityId() {
        return entityId;
    }

    @Override
    public void accept(PinballEventVisitor visitor) {
        visitor.visit(this);
    }
}
