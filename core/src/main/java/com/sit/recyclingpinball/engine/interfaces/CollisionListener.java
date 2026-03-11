package com.sit.recyclingpinball.engine.interfaces;

/**
 * Enables objects to observe and react to collision events.
 */
public interface CollisionListener {
    void onCollide(ICollidable source, ICollidable target);
}
