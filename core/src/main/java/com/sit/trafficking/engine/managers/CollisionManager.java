package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.sit.trafficking.engine.entities.AbstractEntity;
import com.sit.trafficking.engine.interfaces.ICollidable;

import java.util.List;

/**
 * Handles physics and collision resolution.
 * NOT a Singleton - instantiated per Scene.
 * Implements custom AABB collision logic without Box2D.
 */
public class CollisionManager {

    /**
     * Detects and resolves collisions between entities.
     * @param entities List of entities to check.
     */
    public void processCollisions(List<? extends ICollidable> entities) {
        int size = entities.size();
        // Iterate all unique pairs (A vs B)
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                ICollidable a = entities.get(i);
                ICollidable b = entities.get(j);

                resolveCollision(a, b);
            }
        }
    }

    private void resolveCollision(ICollidable a, ICollidable b) {
        Rectangle rectA = a.getBounds();
        Rectangle rectB = b.getBounds();
        
        // Simple AABB overlap check
        if (Intersector.overlaps(rectA, rectB)) {
            if (!a.isTrigger() && !b.isTrigger()) {
                resolvePhysics(a, b, rectA, rectB);
            }
            a.onCollision(b);
            b.onCollision(a);
        }
    }

    private void resolvePhysics(ICollidable a, ICollidable b, Rectangle rectA, Rectangle rectB) {
        if (a.isStatic() && b.isStatic()) {
            return;
        }

        AbstractEntity entA = (AbstractEntity) a;
        AbstractEntity entB = (AbstractEntity) b;

        Rectangle intersection = new Rectangle();
        Intersector.intersectRectangles(rectA, rectB, intersection);

        if (intersection.width < intersection.height) {
            resolveHorizontal(entA, entB, intersection.width);
        } else {
            resolveVertical(entA, entB, intersection.height);
        }
    }

    private void resolveHorizontal(AbstractEntity entA, AbstractEntity entB, float overlap) {
        float centerA = entA.getPosition().x + entA.getWidth() / 2f;
        float centerB = entB.getPosition().x + entB.getWidth() / 2f;

        if (entA.isStatic()) {
            applyStaticSeparationHorizontal(entB, entA, overlap, centerB < centerA);
            reflectHorizontal(entB, 0.6f, 0.85f);
            return;
        }

        if (entB.isStatic()) {
            applyStaticSeparationHorizontal(entA, entB, overlap, centerA < centerB);
            reflectHorizontal(entA, 0.6f, 0.85f);
            return;
        }

        float separation = overlap / 2f;
        if (centerA < centerB) {
            entA.getPosition().x -= separation;
            entB.getPosition().x += separation;
        } else {
            entA.getPosition().x += separation;
            entB.getPosition().x -= separation;
        }

        float velAx = entA.getVelocity().x;
        float velAy = entA.getVelocity().y;
        float velBx = entB.getVelocity().x;
        float velBy = entB.getVelocity().y;
        float impactDampingB = 0.6f;

        entA.setVelocity(velBx, velAy);
        entB.setVelocity(velAx * impactDampingB, velBy * impactDampingB);
    }

    private void resolveVertical(AbstractEntity entA, AbstractEntity entB, float overlap) {
        float centerA = entA.getPosition().y + entA.getHeight() / 2f;
        float centerB = entB.getPosition().y + entB.getHeight() / 2f;

        if (entA.isStatic()) {
            applyStaticSeparationVertical(entB, entA, overlap, centerB < centerA);
            reflectVertical(entB, 0.6f, 0.85f);
            return;
        }

        if (entB.isStatic()) {
            applyStaticSeparationVertical(entA, entB, overlap, centerA < centerB);
            reflectVertical(entA, 0.6f, 0.85f);
            return;
        }

        float separation = overlap / 2f;
        if (centerA < centerB) {
            entA.getPosition().y -= separation;
            entB.getPosition().y += separation;
        } else {
            entA.getPosition().y += separation;
            entB.getPosition().y -= separation;
        }

        float velAx = entA.getVelocity().x;
        float velAy = entA.getVelocity().y;
        float velBx = entB.getVelocity().x;
        float velBy = entB.getVelocity().y;
        float impactDampingB = 0.6f;

        entA.setVelocity(velAx, velBy);
        entB.setVelocity(velBx * impactDampingB, velAy * impactDampingB);
    }

    private void applyStaticSeparationHorizontal(AbstractEntity mover, AbstractEntity blocker, float overlap, boolean moverOnLeft) {
        if (moverOnLeft) {
            mover.getPosition().x -= overlap;
        } else {
            mover.getPosition().x += overlap;
        }
    }

    private void applyStaticSeparationVertical(AbstractEntity mover, AbstractEntity blocker, float overlap, boolean moverBelow) {
        if (moverBelow) {
            mover.getPosition().y -= overlap;
        } else {
            mover.getPosition().y += overlap;
        }
    }

    private void reflectHorizontal(AbstractEntity entity, float restitution, float damping) {
        float vx = entity.getVelocity().x;
        float vy = entity.getVelocity().y;
        entity.setVelocity(-vx * restitution * damping, vy * damping);
    }

    private void reflectVertical(AbstractEntity entity, float restitution, float damping) {
        float vx = entity.getVelocity().x;
        float vy = entity.getVelocity().y;
        entity.setVelocity(vx * damping, -vy * restitution * damping);
    }
}
