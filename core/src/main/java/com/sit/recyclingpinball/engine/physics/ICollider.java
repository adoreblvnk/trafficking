package com.sit.recyclingpinball.engine.physics;

import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle;

public interface ICollider {
    PlatformRectangle getAABB();
    boolean intersects(ICollider other);
    boolean contains(float x, float y);
    void setPosition(float x, float y);
    default void setRotation(float angle) {
    }

    /**
     */
    // Resolves collisions via double-dispatch to eliminate the need for instanceof checks.
    CollisionResult checkCollision(ICollider other);

    /**
     * Resolves this collider against a circle collider.
     *
     * Implementations return a normal oriented from this collider toward the
     * provided collider type, so callers can invert consistently via the
     * double-dispatch entry point.
     */
    CollisionResult collideWith(CircleCollider circle);

    CollisionResult collideWith(BoxCollider box);

    CollisionResult collideWith(OBBCollider obb);
}
