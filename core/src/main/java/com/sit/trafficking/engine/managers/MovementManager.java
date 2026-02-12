package com.sit.trafficking.engine.managers;

import com.sit.trafficking.engine.entities.AbstractEntity;
import com.sit.trafficking.engine.interfaces.Movable;
import java.util.List;

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
