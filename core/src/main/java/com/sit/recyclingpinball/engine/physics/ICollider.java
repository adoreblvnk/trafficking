package com.sit.recyclingpinball.engine.physics;

import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle;

public interface ICollider {
    PlatformRectangle getAABB();
    boolean intersects(ICollider other);
    boolean contains(float x, float y);
    void setPosition(float x, float y);
    default void setRotation(float angle) {
    }
    default float getRotation() {
        return 0f;
    }

    CollisionResult checkCollision(ICollider other);
    CollisionResult checkCollision(CircleCollider other);
    CollisionResult checkCollision(BoxCollider other);
    CollisionResult checkCollision(OBBCollider other);
}
