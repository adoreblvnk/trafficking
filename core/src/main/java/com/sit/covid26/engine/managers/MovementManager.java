package com.sit.covid26.engine.managers;

import com.badlogic.gdx.Gdx;
import com.sit.covid26.engine.entities.AbstractEntity;
import com.sit.covid26.engine.interfaces.Movable;

import java.util.List;

/**
 * Applies position updates to movable entities based on their velocity and delta time.
 */
public class MovementManager {

    public MovementManager() {
    }

    public void processMovement(List<AbstractEntity> entities, float dt) {
        if (entities == null) {
            Gdx.app.error("MovementManager", "Cannot process movement on null entity list");
            return;
        }
        if (dt < 0) {
            Gdx.app.error("MovementManager", "Negative delta time rejected: " + dt);
            return;
        }

        try {
            for (AbstractEntity e : entities) {
                if (e instanceof Movable) {
                    ((Movable) e).updatePosition(dt);
                }
            }
        } catch (Exception e) {
            Gdx.app.error("MovementManager", "Movement processing failed", e);
        }
    }
}
