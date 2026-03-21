package com.sit.recyclingpinball.logic.events;

public class ShooterRodMovedEvent implements IPinballEvent {
    private final float rodY;

    public ShooterRodMovedEvent(float rodY) {
        this.rodY = rodY;
    }

    public float getRodY() {
        return rodY;
    }

    @Override
    public void accept(PinballEventVisitor visitor) {
        visitor.visit(this);
    }
}
