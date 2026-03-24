package com.sit.recyclingpinball.engine.interfaces;

import java.util.List;

public interface ICollisionManager {
    void processCollisions(List<? extends ICollidable> entities);
}
