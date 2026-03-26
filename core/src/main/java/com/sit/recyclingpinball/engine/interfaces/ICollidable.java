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
    void applyBounce(float normalX, float normalY);

    /**
     * Resolves a collision with a {@code DynamicEntity} using double dispatch.
     *
     * This method is intentionally coupled to {@code DynamicEntity} instead of
     * accepting a generic {@code ICollidable}. In this engine, gameplay collisions
     * are designed around the pinball (dynamic body) interacting with static or
     * scripted objects. Keeping this contract narrow avoids introducing a fully
     * generalized two-body impulse solver (mass-vs-mass, restitution blending,
     * iterative stabilization), which is outside project scope and can reduce
     * gameplay stability when implemented incorrectly.
     *
     * The trade-off is deliberate: prioritize robust, predictable, and testable
     * collision response for the current game architecture.
     *
     * @param entity
     *            The dynamic entity this collidable is interacting with.
     */
    default void resolveCollision(com.sit.recyclingpinball.engine.entities.DynamicEntity entity) {
    }
}
