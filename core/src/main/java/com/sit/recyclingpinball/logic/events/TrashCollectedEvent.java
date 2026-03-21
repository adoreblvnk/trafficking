package com.sit.recyclingpinball.logic.events;

import com.sit.recyclingpinball.logic.factories.TrashType;

public class TrashCollectedEvent implements IPinballEvent {
    private final TrashType type;
    public TrashCollectedEvent(TrashType type) { this.type = type; }
    public TrashType getType() { return type; }
    
    @Override
    public void accept(PinballEventVisitor visitor) {
        visitor.visit(this);
    }
}
