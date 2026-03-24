package com.sit.recyclingpinball.engine.interfaces;

import java.util.List;

public interface IMovementManager {
    void processMovement(List<Movable> entities, float dt);
}
