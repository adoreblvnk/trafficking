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
     * Resolves collision through double dispatch without downcasting.
     *
     * This project treats collider primitives as a practically closed set
     * (circle, AABB, OBB). We accept the theoretical OCP trade-off of adding
     * one new overload per new collider type so collision resolution remains
     * fully polymorphic and type-safe at compile time.
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
