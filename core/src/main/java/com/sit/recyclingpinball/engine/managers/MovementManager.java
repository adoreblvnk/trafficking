package com.sit.recyclingpinball.engine.managers;

import com.sit.recyclingpinball.engine.entities.AbstractEntity;
import com.sit.recyclingpinball.engine.interfaces.Movable;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Applies position updates to movable entities based on their velocity and delta time.
 */
public class MovementManager {
    private static final Logger LOGGER = Logger.getLogger(MovementManager.class.getName());

    public MovementManager() {
    }

    public void processMovement(List<AbstractEntity> entities, float dt) {
        if (entities == null) {
            LOGGER.severe("Cannot process movement on null entity list");
            return;
        }
        if (dt < 0) {
            LOGGER.severe("Negative delta time rejected: " + dt);
            return;
        }

        try {
            for (AbstractEntity e : entities) {
                e.updatePosition(dt);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Movement processing failed", e);
        }
    }
}
