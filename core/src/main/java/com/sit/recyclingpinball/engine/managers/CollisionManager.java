package com.sit.recyclingpinball.engine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sit.recyclingpinball.engine.EngineConstants;
import com.sit.recyclingpinball.engine.interfaces.ICollidable;
import com.sit.recyclingpinball.engine.interfaces.Movable;

import java.util.ArrayList;
import java.util.List;

/**
 * Detects and resolves intersections between bounded entities.
 * Supports static/dynamic entity separation with configurable bounce and push factors.
 */
public class CollisionManager {

    public CollisionManager() {
    }

    public List<ICollidable> getEntitiesInArea(List<? extends ICollidable> entities, Rectangle area) {
        List<ICollidable> result = new ArrayList<>();

        if (entities == null || area == null) {
            Gdx.app.error("CollisionManager", "Cannot query area with null parameters");
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
            Gdx.app.error("CollisionManager", "Cannot process collisions on null entity list");
            return;
        }

        if (entities.isEmpty()) return;

        try {
            float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE;
            float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE;

            for (ICollidable e : entities) {
                if (e == null || e.getCollider() == null) continue;
                Rectangle r = e.getCollider().getAABB();
                if (r.x < minX) minX = r.x;
                if (r.y < minY) minY = r.y;
                if (r.x + r.width > maxX) maxX = r.x + r.width;
                if (r.y + r.height > maxY) maxY = r.y + r.height;
            }

            if (minX == Float.MAX_VALUE) return;

            float width = Math.max(maxX - minX, 1);
            float height = Math.max(maxY - minY, 1);

            QuadTree quad = new QuadTree(0, new Rectangle(minX, minY, width, height));

            for (ICollidable e : entities) {
                if (e != null && e.getCollider() != null) {
                    quad.insert(e);
                }
            }

            List<ICollidable> returnObjects = new ArrayList<>();
            for (int i = 0; i < entities.size(); i++) {
                ICollidable a = entities.get(i);
                if (a == null || a.getCollider() == null) continue;

                returnObjects.clear();
                quad.retrieve(returnObjects, a.getCollider().getAABB());

                for (int j = 0; j < returnObjects.size(); j++) {
                    ICollidable b = returnObjects.get(j);

                    if (a == b) continue;
                    
                    if (!a.isCollisionEnabled() || !b.isCollisionEnabled()) continue;

                    int hashA = System.identityHashCode(a);
                    int hashB = System.identityHashCode(b);

                    boolean shouldProcess = false;
                    if (hashA > hashB) {
                        shouldProcess = true;
                    } else if (hashA == hashB) {
                        if (a.getCollider().getAABB().x > b.getCollider().getAABB().x || 
                           (a.getCollider().getAABB().x == b.getCollider().getAABB().x && a.getCollider().getAABB().y > b.getCollider().getAABB().y)) {
                            shouldProcess = true;
                        }
                    }

                    if (shouldProcess) {
                        if (a.getCollider().intersects(b.getCollider())) {
                            resolveCollision(a, b);
                            a.onCollision(b);
                            b.onCollision(a);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Gdx.app.error("CollisionManager", "Collision processing failed", e);
        }
    }

    private boolean checkAABB(Rectangle rectA, Rectangle rectB) {
        return rectA.overlaps(rectB);
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
        Rectangle rA = a.getCollider().getAABB();
        Rectangle rB = b.getCollider().getAABB();
        Rectangle intersection = new Rectangle();
        Intersector.intersectRectangles(rA, rB, intersection);

        float overlapX = intersection.width;
        float overlapY = intersection.height;
        float pushX = 0, pushY = 0;

        if (overlapX < overlapY) {
            if (rA.x < rB.x)
                pushX = -overlapX;
            else
                pushX = overlapX;
        } else {
            if (rA.y < rB.y)
                pushY = -overlapY;
            else
                pushY = overlapY;
        }

        if (!a.isStatic() && b.isStatic()) {
            a.setPosition(a.getPosition().x + pushX, a.getPosition().y + pushY);
            if (a instanceof Movable) {
                Vector2 vel = ((Movable) a).getVelocity();
                if (pushX != 0)
                    vel.x *= -EngineConstants.DEFAULT_BOUNCE;
                if (pushY != 0)
                    vel.y *= -EngineConstants.DEFAULT_BOUNCE;
            }
        } else if (a.isStatic() && !b.isStatic()) {
            b.setPosition(b.getPosition().x - pushX, b.getPosition().y - pushY);
            if (b instanceof Movable) {
                Vector2 vel = ((Movable) b).getVelocity();
                if (pushX != 0)
                    vel.x *= -EngineConstants.DEFAULT_BOUNCE;
                if (pushY != 0)
                    vel.y *= -EngineConstants.DEFAULT_BOUNCE;
            }
        } else if (!a.isStatic() && !b.isStatic()) {
            a.setPosition(a.getPosition().x + pushX * EngineConstants.PUSH_OUT_FACTOR,
                    a.getPosition().y + pushY * EngineConstants.PUSH_OUT_FACTOR);
            b.setPosition(b.getPosition().x - pushX * EngineConstants.PUSH_OUT_FACTOR,
                    b.getPosition().y - pushY * EngineConstants.PUSH_OUT_FACTOR);

            if (a instanceof Movable && b instanceof Movable) {
                Vector2 vA = ((Movable) a).getVelocity();
                Vector2 vB = ((Movable) b).getVelocity();

                if (pushX != 0) {
                    float temp = vA.x;
                    vA.x = vB.x * EngineConstants.DEFAULT_BOUNCE;
                    vB.x = temp * EngineConstants.DEFAULT_BOUNCE;
                }
                if (pushY != 0) {
                    float temp = vA.y;
                    vA.y = vB.y * EngineConstants.DEFAULT_BOUNCE;
                    vB.y = temp * EngineConstants.DEFAULT_BOUNCE;
                }
            }
        }
    }
}
