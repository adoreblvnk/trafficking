package com.sit.recyclingpinball.logic.events;

public class BallDrainedEvent implements IPinballEvent {
    @Override
    public void accept(PinballEventVisitor visitor) {
        visitor.visit(this);
    }
}
