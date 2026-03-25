package com.sit.recyclingpinball.engine.physics;

import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle;

public interface ICollider {
    PlatformRectangle getAABB();
    boolean intersects(ICollider other);
    boolean contains(float x, float y);
    void setPosition(float x, float y);
    default void setRotation(float angle) {
    }

    CollisionResult checkCollision(ICollider other);

    CollisionResult checkCollisionWith(CircleCollider other);
    CollisionResult checkCollisionWith(BoxCollider other);
    CollisionResult checkCollisionWith(OBBCollider other);
}
