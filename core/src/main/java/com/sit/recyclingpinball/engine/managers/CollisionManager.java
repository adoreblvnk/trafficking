package com.sit.recyclingpinball.engine.managers;

import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle;
import com.sit.recyclingpinball.engine.EngineConstants;
import com.sit.recyclingpinball.engine.interfaces.ICollidable;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Detects and resolves intersections between bounded entities. Supports
 * static/dynamic entity separation with configurable bounce and push factors.
 */
public class CollisionManager {
    private static final Logger LOGGER = Logger.getLogger(CollisionManager.class.getName());

    private final QuadTree quadTree;

    public CollisionManager(PlatformRectangle bounds) {
        if (bounds == null) {
            bounds = new PlatformRectangle(0, 0, 1920, 1080); // Default fallback
        }
        this.quadTree = new QuadTree(0, bounds);
    }

    public void processCollisions(List<? extends ICollidable> entities) {
        if (entities == null) {
            LOGGER.severe("Cannot process collisions on null entity list");
            return;
        }

        if (entities.isEmpty())
            return;

        try {
            quadTree.clear();

            Map<ICollidable, Integer> entityOrder = new IdentityHashMap<>();
            for (int i = 0; i < entities.size(); i++) {
                ICollidable e = entities.get(i);
                if (e != null) {
                    entityOrder.put(e, i);
                }
            }

            for (ICollidable e : entities) {
                if (e != null && e.getCollider() != null) {
                    quadTree.insert(e);
                }
            }

            List<ICollidable> returnObjects = new ArrayList<>();
            for (int i = 0; i < entities.size(); i++) {
                ICollidable a = entities.get(i);
                if (a == null || a.getCollider() == null)
                    continue;

                returnObjects.clear();
                quadTree.retrieve(returnObjects, a.getCollider().getAABB());

                for (int j = 0; j < returnObjects.size(); j++) {
                    ICollidable b = returnObjects.get(j);

                    if (a == b)
                        continue;

                    if (!a.isCollisionEnabled() || !b.isCollisionEnabled())
                        continue;

                    Integer orderA = entityOrder.get(a);
                    Integer orderB = entityOrder.get(b);
                    if (orderA == null || orderB == null || orderA >= orderB)
                        continue;

                    if (checkCollision(a, b)) {
                        resolveCollision(a, b);
                        a.onCollision(b);
                        b.onCollision(a);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Collision processing failed", e);
        }
    }

    private boolean checkCollision(ICollidable a, ICollidable b) {
        return a.getCollider().intersects(b.getCollider());
    }

    private void resolveCollision(ICollidable a, ICollidable b) {
        if (a.isStatic() && b.isStatic())
            return;
        if (a.isTrigger() || b.isTrigger())
            return;

        resolvePhysics(a, b);
    }

    // Separates overlapping entities using minimum translation vector
    private void resolvePhysics(ICollidable a, ICollidable b) {
        com.sit.recyclingpinball.engine.physics.CollisionResult result = a.getCollider()
                .checkCollision(b.getCollider());
        if (result == null || !result.intersects() || result.normal() == null)
            return;

        float pushX = result.normal().getX() * result.depth();
        float pushY = result.normal().getY() * result.depth();

        if (!a.isStatic() && b.isStatic()) {
            a.setPosition(a.getPosition().getX() + pushX, a.getPosition().getY() + pushY);
            if (a.getInverseMass() > 0) {
                a.applyBounce(pushX, pushY);
            }
        } else if (a.isStatic() && !b.isStatic()) {
            b.setPosition(b.getPosition().getX() - pushX, b.getPosition().getY() - pushY);
            if (b.getInverseMass() > 0) {
                b.applyBounce(-pushX, -pushY);
            }
        } else if (!a.isStatic() && !b.isStatic()) {
            a.setPosition(a.getPosition().getX() + pushX * EngineConstants.PUSH_OUT_FACTOR,
                    a.getPosition().getY() + pushY * EngineConstants.PUSH_OUT_FACTOR);
            b.setPosition(b.getPosition().getX() - pushX * EngineConstants.PUSH_OUT_FACTOR,
                    b.getPosition().getY() - pushY * EngineConstants.PUSH_OUT_FACTOR);

            if (a.getInverseMass() > 0 && b.getInverseMass() > 0) {
                a.applyBounce(pushX, pushY);
                b.applyBounce(-pushX, -pushY);
            }
        }
    }
}
