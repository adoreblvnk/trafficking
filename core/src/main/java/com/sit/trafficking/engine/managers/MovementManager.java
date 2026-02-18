package com.sit.trafficking.engine.managers;

import com.sit.trafficking.engine.entities.AbstractEntity;
import com.sit.trafficking.engine.interfaces.Movable;
import java.util.List;

/**
 * Applies position updates to movable entities based on their velocity and delta time.
 */
public class MovementManager {

    public MovementManager() {
    }

    public void processMovement(List<AbstractEntity> entities, float dt) {
        for (AbstractEntity e : entities) {
            if (e instanceof Movable) {
                ((Movable) e).updatePosition(dt);
            }
        }
    }
}
