package com.sit.covid26.engine.interfaces;

import com.badlogic.gdx.math.Vector2;

/**
 * Enables entities to move with velocity-based movement.
 */
public interface Movable {
    Vector2 getVelocity();
    void setVelocity(float x, float y);
    void updatePosition(float dt);
}
