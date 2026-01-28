package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.sit.trafficking.engine.entities.AbstractEntity;
import com.sit.trafficking.engine.entities.DynamicEntity;
import com.sit.trafficking.engine.entities.StaticEntity;

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
    public void processCollisions(List<AbstractEntity> entities) {
        int size = entities.size();
        // Iterate all unique pairs (A vs B)
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                AbstractEntity a = entities.get(i);
                AbstractEntity b = entities.get(j);

                resolveCollision(a, b);
            }
        }
    }

    private void resolveCollision(AbstractEntity a, AbstractEntity b) {
        Rectangle rectA = a.getBounds();
        Rectangle rectB = b.getBounds();
        
        // Simple AABB overlap check
        if (Intersector.overlaps(rectA, rectB)) {
            
            // Calculate overlap rectangle
            Rectangle intersection = new Rectangle();
            Intersector.intersectRectangles(rectA, rectB, intersection);

            boolean collisionHandled = false;

            // Pattern Matching for resolution logic
            if (a instanceof DynamicEntity da && b instanceof StaticEntity sb) {
                resolveDynamicVsStatic(da, sb, intersection);
                collisionHandled = true;
            } else if (a instanceof StaticEntity sa && b instanceof DynamicEntity db) {
                resolveDynamicVsStatic(db, sa, intersection);
                collisionHandled = true;
            } else if (a instanceof DynamicEntity da && b instanceof DynamicEntity db) {
                resolveDynamicVsDynamic(da, db, intersection);
                collisionHandled = true;
            }

            if (collisionHandled) {
                // Play sound based on impact intensity (using relative velocity magnitude approximation)
                float intensity = a.getVelocity().len() + b.getVelocity().len();
                SoundManager.getInstance().playImpact(intensity);

                // Flash dynamic entities on impact
                if (a instanceof DynamicEntity) {
                    ((DynamicEntity) a).setColor(com.badlogic.gdx.graphics.Color.YELLOW);
                }
                if (b instanceof DynamicEntity) {
                    ((DynamicEntity) b).setColor(com.badlogic.gdx.graphics.Color.YELLOW);
                }
            }
        }
    }

    private void resolveDynamicVsStatic(DynamicEntity d, StaticEntity s, Rectangle intersection) {
        float restitution = 0.6f; // greater energy loss
        float postCollisionDamping = 0.85f;

        float vx = d.getVelocity().x;
        float vy = d.getVelocity().y;

        // 1. Determine Axis (Horizontal vs Vertical)
        if (intersection.width < intersection.height) {
            // --- Horizontal Collision ---
            float centerD = d.getPosition().x + d.getWidth() / 2f;
            float centerS = s.getPosition().x + s.getWidth() / 2f;

            // 2. Separation (Move dynamic entity out of static)
            if (centerD < centerS) {
                d.getPosition().x -= intersection.width;
            } else {
                d.getPosition().x += intersection.width;
            }

            // 3. Velocity Reflection (Flip X only)
            vx = -vx * restitution;

        } else {
            // --- Vertical Collision ---
            float centerD = d.getPosition().y + d.getHeight() / 2f;
            float centerS = s.getPosition().y + s.getHeight() / 2f;

            // 2. Separation (Move dynamic entity out of static)
            if (centerD < centerS) {
                d.getPosition().y -= intersection.height;
            } else {
                d.getPosition().y += intersection.height;
            }

            // 3. Velocity Reflection (Flip Y only)
            vy = -vy * restitution;
        }

        d.setVelocity(vx * postCollisionDamping, vy * postCollisionDamping);
    }

    private void resolveDynamicVsDynamic(DynamicEntity a, DynamicEntity b, Rectangle intersection) {
        // Equal-mass elastic collision with separation
        float velAx = a.getVelocity().x;
        float velAy = a.getVelocity().y;
        float velBx = b.getVelocity().x;
        float velBy = b.getVelocity().y;
        float impactDampingB = 0.6f; // additional loss for the impacted entity (b)

        // 1. Determine Axis (Horizontal vs Vertical)
        if (intersection.width < intersection.height) {
            // --- Horizontal Collision ---
            float separation = intersection.width / 2f;
            
            float centerA = a.getPosition().x + a.getWidth() / 2f;
            float centerB = b.getPosition().x + b.getWidth() / 2f;

            // 2. Separation (Move apart)
            if (centerA < centerB) {
                a.getPosition().x -= separation;
                b.getPosition().x += separation;
            } else {
                a.getPosition().x += separation;
                b.getPosition().x -= separation;
            }

            // 3. Velocity exchange along collision axis (equal mass elastic)
            float newAx = velBx;
            float newBx = velAx;
            a.setVelocity(newAx, velAy);
            b.setVelocity(newBx * impactDampingB, velBy * impactDampingB);

        } else {
            // --- Vertical Collision ---
            float separation = intersection.height / 2f;

            float centerA = a.getPosition().y + a.getHeight() / 2f;
            float centerB = b.getPosition().y + b.getHeight() / 2f;

            // 2. Separation (Move apart)
            if (centerA < centerB) {
                a.getPosition().y -= separation;
                b.getPosition().y += separation;
            } else {
                a.getPosition().y += separation;
                b.getPosition().y -= separation;
            }

            // 3. Velocity exchange along collision axis (equal mass elastic)
            float newAy = velBy;
            float newBy = velAy;
            a.setVelocity(velAx, newAy);
            b.setVelocity(velBx * impactDampingB, newBy * impactDampingB);
        }
    }
}
