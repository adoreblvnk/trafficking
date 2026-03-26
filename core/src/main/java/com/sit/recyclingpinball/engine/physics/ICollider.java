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
        * ARCHITECTURE JUSTIFICATION: 100% Downcast-Free Double Dispatch.
        *
        * We avoid instanceof and reflection for collider type selection, preserving
        * strictly polymorphic dispatch. This keeps the dispatcher closed for
        * modification and open for extension (OCP): new collider families can be
        * introduced without editing branching logic in collision dispatch paths.
     */
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
