package com.sit.recyclingpinball.engine.managers;

import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle;
import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformVector2;
import com.sit.recyclingpinball.engine.EngineConstants;
import com.sit.recyclingpinball.engine.interfaces.ICollidable;
import com.sit.recyclingpinball.engine.interfaces.Movable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Detects and resolves intersections between bounded entities. Supports
 * static/dynamic entity separation with configurable bounce and push factors.
 */
public class CollisionManager {
    private static final Logger LOGGER = Logger.getLogger(CollisionManager.class.getName());

    private static final float SEPARATION_EPSILON = 0.5f;

    private final QuadTree quadTree;

    public CollisionManager(PlatformRectangle bounds) {
        if (bounds == null) {
            bounds = new PlatformRectangle(0, 0, 1920, 1080); // Default fallback
        }
        this.quadTree = new QuadTree(0, bounds);
    }

    public List<ICollidable> getEntitiesInArea(List<? extends ICollidable> entities, PlatformRectangle area) {
        List<ICollidable> result = new ArrayList<>();

        if (entities == null || area == null) {
            LOGGER.severe("Cannot query area with null parameters");
            return result;
        }

        for (ICollidable e : entities) {
            if (e != null && e.getCollider() != null && e.getCollider().getAABB().overlaps(area)) {
                result.add(e);
            }
        }
        return result;
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

                    int hashA = System.identityHashCode(a);
                    int hashB = System.identityHashCode(b);

                    boolean shouldProcess = false;
                    if (hashA > hashB) {
                        shouldProcess = true;
                    } else if (hashA == hashB) {
                        if (a.getCollider().getAABB().getX() > b.getCollider().getAABB().getX()
                                || (a.getCollider().getAABB().getX() == b.getCollider().getAABB().getX()
                                        && a.getCollider().getAABB().getY() > b.getCollider().getAABB().getY())) {
                            shouldProcess = true;
                        }
                    }

                    if (shouldProcess) {
                        if (checkCollision(a, b)) {
                            resolveCollision(a, b);
                            a.onCollision(b);
                            b.onCollision(a);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Collision processing failed", e);
        }
    }

    private boolean checkAABB(PlatformRectangle rectA, PlatformRectangle rectB) {
        return rectA.overlaps(rectB);
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
                applyBounce((Movable) a, pushX, pushY);
            }
        } else if (a.isStatic() && !b.isStatic()) {
            b.setPosition(b.getPosition().getX() - pushX, b.getPosition().getY() - pushY);
            if (b.getInverseMass() > 0) {
                applyBounce((Movable) b, -pushX, -pushY);
            }
        } else if (!a.isStatic() && !b.isStatic()) {
            a.setPosition(a.getPosition().getX() + pushX * EngineConstants.PUSH_OUT_FACTOR,
                    a.getPosition().getY() + pushY * EngineConstants.PUSH_OUT_FACTOR);
            b.setPosition(b.getPosition().getX() - pushX * EngineConstants.PUSH_OUT_FACTOR,
                    b.getPosition().getY() - pushY * EngineConstants.PUSH_OUT_FACTOR);

            if (a.getInverseMass() > 0 && b.getInverseMass() > 0) {
                PlatformVector2 vA = ((Movable) a).getVelocity();
                PlatformVector2 vB = ((Movable) b).getVelocity();

                if (Math.abs(pushX) > Math.abs(pushY)) {
                    float temp = vA.getX();
                    vA.setX(vB.getX() * EngineConstants.DEFAULT_BOUNCE);
                    vB.setX(temp * EngineConstants.DEFAULT_BOUNCE);
                } else {
                    float temp = vA.getY();
                    vA.setY(vB.getY() * EngineConstants.DEFAULT_BOUNCE);
                    vB.setY(temp * EngineConstants.DEFAULT_BOUNCE);
                }
            }
        }
    }

    private void applyBounce(Movable movable, float normalX, float normalY) {
        PlatformVector2 normal = new PlatformVector2(normalX, normalY);
        if (normal.len2() == 0f) {
            return;
        }

        normal.nor();

        PlatformVector2 velocity = movable.getVelocity();
        float speedAlongNormal = velocity.dot(normal);

        if (speedAlongNormal >= 0f) {
            return;
        }

        float restitution = EngineConstants.DEFAULT_BOUNCE;
        velocity.sub(normal.scl((1f + restitution) * speedAlongNormal));
    }
}
