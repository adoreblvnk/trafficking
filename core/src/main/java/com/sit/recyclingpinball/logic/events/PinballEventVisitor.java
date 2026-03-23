package com.sit.recyclingpinball.logic.events;

/**
 * Visitor interface for pinball events.
 *
 * <p><b>Architecture Justification:</b> While expanding this interface for every new event 
 * violates the Interface Segregation Principle slightly, it is the inherent trade-off of the 
 * classic GoF Visitor Pattern to ensure exhaustiveness. By providing {@code default} empty 
 * implementations, this interface acts as an Adapter class, ensuring that concrete listeners 
 * only need to override the specific events they care about.</p>
 */
public interface PinballEventVisitor {
    default void visit(BallDrainedEvent event) {
    }
    default void visit(BallLaunchedEvent event) {
    }
    default void visit(ShooterRodMovedEvent event) {
    }
    default void visit(TrashCollectedEvent event) {
    }
    default void visit(BallRestedOnRodEvent event) {
    }
}
