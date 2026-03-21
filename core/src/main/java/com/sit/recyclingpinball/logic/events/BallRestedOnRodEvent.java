package com.sit.recyclingpinball.logic.events;

public class BallRestedOnRodEvent implements IPinballEvent {
    @Override
    public void accept(PinballEventVisitor visitor) {
        visitor.visit(this);
    }
}
