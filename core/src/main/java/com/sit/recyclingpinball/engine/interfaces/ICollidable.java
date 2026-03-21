package com.sit.recyclingpinball.engine.interfaces;

import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformVector2;
import com.sit.recyclingpinball.engine.physics.ICollider;

/**
 * Enables entities to participate in collision detection and response.
 */
public interface ICollidable {
    ICollider getCollider();
    boolean isCollisionEnabled();
    void setCollisionEnabled(boolean enabled);
    PlatformVector2 getPosition();
    void setPosition(float x, float y);
    boolean isStatic();
    boolean isTrigger();
    void onCollision(ICollidable other);
    float getInverseMass();
    String getTag();
}
