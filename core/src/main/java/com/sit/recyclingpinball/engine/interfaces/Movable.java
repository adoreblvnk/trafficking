package com.sit.recyclingpinball.engine.interfaces;

import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformVector2;

/**
 * Enables entities to move with velocity-based movement.
 */
public interface Movable {
    PlatformVector2 getVelocity();
    void setVelocity(float x, float y);
    void updatePosition(float dt);
}
