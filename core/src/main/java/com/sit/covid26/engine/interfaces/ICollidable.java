package com.sit.covid26.engine.interfaces;

import com.badlogic.gdx.math.Vector2;
import com.sit.covid26.engine.physics.ICollider;

/**
 * Enables entities to participate in collision detection and response.
 */
public interface ICollidable {
    ICollider getCollider();
    boolean isCollisionEnabled();
    void setCollisionEnabled(boolean enabled);
    Vector2 getPosition();
    void setPosition(float x, float y);
    boolean isStatic();
    boolean isTrigger();
    void onCollision(ICollidable other);
}
