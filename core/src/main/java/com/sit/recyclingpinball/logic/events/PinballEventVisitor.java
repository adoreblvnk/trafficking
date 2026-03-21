package com.sit.recyclingpinball.logic.events;

public interface PinballEventVisitor {
    default void visit(BallDrainedEvent event) {}
    default void visit(BallLaunchedEvent event) {}
    default void visit(ShooterRodMovedEvent event) {}
    default void visit(TrashCollectedEvent event) {}
}
