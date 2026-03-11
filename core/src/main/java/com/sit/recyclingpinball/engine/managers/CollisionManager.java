package com.sit.recyclingpinball.engine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sit.recyclingpinball.engine.EngineConstants;
import com.sit.recyclingpinball.engine.interfaces.ICollidable;
import com.sit.recyclingpinball.engine.interfaces.Movable;

import com.sit.recyclingpinball.engine.physics.BoxCollider;
import com.sit.recyclingpinball.engine.physics.CircleCollider;
import com.sit.recyclingpinball.engine.physics.OBBCollider;
import com.sit.recyclingpinball.engine.physics.SATMathUtils;

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
                        if (checkCollision(a, b)) {
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
        float pushX = 0, pushY = 0;

        if (a.getCollider() instanceof OBBCollider || b.getCollider() instanceof OBBCollider) {
            Vector2 mtv = calculateSATMTV(a.getCollider(), b.getCollider());
            if (mtv == null) return;
            pushX = mtv.x;
            pushY = mtv.y;
        } else {
            Rectangle rA = a.getCollider().getAABB();
            Rectangle rB = b.getCollider().getAABB();
            Rectangle intersection = new Rectangle();
            Intersector.intersectRectangles(rA, rB, intersection);

            float overlapX = intersection.width;
            float overlapY = intersection.height;

            if (overlapX < overlapY) {
                if (rA.x + rA.width / 2 < rB.x + rB.width / 2)
                    pushX = -overlapX;
                else
                    pushX = overlapX;
            } else {
                if (rA.y + rA.height / 2 < rB.y + rB.height / 2)
                    pushY = -overlapY;
                else
                    pushY = overlapY;
            }
        }

        if (!a.isStatic() && b.isStatic()) {
            a.setPosition(a.getPosition().x + pushX, a.getPosition().y + pushY);
            if (a instanceof Movable) {
                Vector2 vel = ((Movable) a).getVelocity();
                if (Math.abs(pushX) > Math.abs(pushY)) vel.x *= -EngineConstants.DEFAULT_BOUNCE;
                else vel.y *= -EngineConstants.DEFAULT_BOUNCE;
            }
        } else if (a.isStatic() && !b.isStatic()) {
            b.setPosition(b.getPosition().x - pushX, b.getPosition().y - pushY);
            if (b instanceof Movable) {
                Vector2 vel = ((Movable) b).getVelocity();
                if (Math.abs(pushX) > Math.abs(pushY)) vel.x *= -EngineConstants.DEFAULT_BOUNCE;
                else vel.y *= -EngineConstants.DEFAULT_BOUNCE;
            }
        } else if (!a.isStatic() && !b.isStatic()) {
            a.setPosition(a.getPosition().x + pushX * EngineConstants.PUSH_OUT_FACTOR,
                    a.getPosition().y + pushY * EngineConstants.PUSH_OUT_FACTOR);
            b.setPosition(b.getPosition().x - pushX * EngineConstants.PUSH_OUT_FACTOR,
                    b.getPosition().y - pushY * EngineConstants.PUSH_OUT_FACTOR);

            if (a instanceof Movable && b instanceof Movable) {
                Vector2 vA = ((Movable) a).getVelocity();
                Vector2 vB = ((Movable) b).getVelocity();

                if (Math.abs(pushX) > Math.abs(pushY)) {
                    float temp = vA.x;
                    vA.x = vB.x * EngineConstants.DEFAULT_BOUNCE;
                    vB.x = temp * EngineConstants.DEFAULT_BOUNCE;
                } else {
                    float temp = vA.y;
                    vA.y = vB.y * EngineConstants.DEFAULT_BOUNCE;
                    vB.y = temp * EngineConstants.DEFAULT_BOUNCE;
                }
            }
        }
    }

    private Vector2 calculateSATMTV(com.sit.recyclingpinball.engine.physics.ICollider colA, com.sit.recyclingpinball.engine.physics.ICollider colB) {
        if (colA instanceof OBBCollider && colB instanceof CircleCollider) {
            return getMTV((OBBCollider) colA, (CircleCollider) colB, false);
        } else if (colA instanceof CircleCollider && colB instanceof OBBCollider) {
            return getMTV((OBBCollider) colB, (CircleCollider) colA, true);
        } else if (colA instanceof OBBCollider && colB instanceof BoxCollider) {
            return getMTV((OBBCollider) colA, (BoxCollider) colB, false);
        } else if (colA instanceof BoxCollider && colB instanceof OBBCollider) {
            return getMTV((OBBCollider) colB, (BoxCollider) colA, true);
        } else if (colA instanceof OBBCollider && colB instanceof OBBCollider) {
            return getMTV((OBBCollider) colA, (OBBCollider) colB, false);
        }
        return null; // AABB vs AABB shouldn't reach here usually
    }

    private Vector2 getMTV(OBBCollider obb, CircleCollider circle, boolean reverse) {
        Vector2[] vertices = obb.getVertices();
        Vector2 center = new Vector2(circle.getCircle().x, circle.getCircle().y);
        float radius = circle.getCircle().radius;

        Vector2[] axes = obb.getAxes();

        Vector2 closestVertex = vertices[0];
        float minDst2 = center.dst2(vertices[0]);
        for (int i = 1; i < vertices.length; i++) {
            float dst2 = center.dst2(vertices[i]);
            if (dst2 < minDst2) {
                minDst2 = dst2;
                closestVertex = vertices[i];
            }
        }

        Vector2 circleAxis = SATMathUtils.normalize(new Vector2(center.x - closestVertex.x, center.y - closestVertex.y));

        Vector2[] allAxes = new Vector2[axes.length + 1];
        System.arraycopy(axes, 0, allAxes, 0, axes.length);
        allAxes[axes.length] = circleAxis;

        float minOverlap = Float.MAX_VALUE;
        Vector2 mtvAxis = null;

        for (Vector2 axis : allAxes) {
            if (axis.x == 0 && axis.y == 0) continue;
            float[] proj1 = SATMathUtils.projectPolygon(axis, vertices);
            float[] proj2 = SATMathUtils.projectCircle(axis, center, radius);

            if (!SATMathUtils.overlap(proj1, proj2)) {
                return null; // Separating axis found
            } else {
                float overlap = SATMathUtils.getOverlap(proj1, proj2);
                if (overlap < minOverlap) {
                    minOverlap = overlap;
                    mtvAxis = axis;
                }
            }
        }

        if (mtvAxis == null) return null;

        // Ensure MTV points from A to B
        Vector2 centerA = getPolygonCenter(vertices);
        Vector2 centerB = center;
        Vector2 dir = new Vector2(centerB.x - centerA.x, centerB.y - centerA.y);
        if (SATMathUtils.dotProduct(mtvAxis, dir) < 0) {
            mtvAxis = new Vector2(-mtvAxis.x, -mtvAxis.y);
        }

        Vector2 mtv = new Vector2(mtvAxis.x * minOverlap, mtvAxis.y * minOverlap);
        if (reverse) {
            mtv.x = -mtv.x;
            mtv.y = -mtv.y;
        }
        return mtv;
    }

    private Vector2 getMTV(OBBCollider obb, BoxCollider box, boolean reverse) {
        Rectangle r = box.getAABB();
        Vector2[] boxVertices = new Vector2[]{
                new Vector2(r.x, r.y),
                new Vector2(r.x + r.width, r.y),
                new Vector2(r.x + r.width, r.y + r.height),
                new Vector2(r.x, r.y + r.height)
        };
        Vector2[] boxAxes = new Vector2[]{
                new Vector2(1, 0),
                new Vector2(0, 1)
        };

        return getMTVPolygons(obb.getVertices(), obb.getAxes(), boxVertices, boxAxes, reverse);
    }

    private Vector2 getMTV(OBBCollider obb1, OBBCollider obb2, boolean reverse) {
        return getMTVPolygons(obb1.getVertices(), obb1.getAxes(), obb2.getVertices(), obb2.getAxes(), reverse);
    }

    private Vector2 getMTVPolygons(Vector2[] verticesA, Vector2[] axesA, Vector2[] verticesB, Vector2[] axesB, boolean reverse) {
        Vector2[] allAxes = new Vector2[axesA.length + axesB.length];
        System.arraycopy(axesA, 0, allAxes, 0, axesA.length);
        System.arraycopy(axesB, 0, allAxes, axesA.length, axesB.length);

        float minOverlap = Float.MAX_VALUE;
        Vector2 mtvAxis = null;

        for (Vector2 axis : allAxes) {
            if (axis.x == 0 && axis.y == 0) continue;
            float[] proj1 = SATMathUtils.projectPolygon(axis, verticesA);
            float[] proj2 = SATMathUtils.projectPolygon(axis, verticesB);

            if (!SATMathUtils.overlap(proj1, proj2)) {
                return null;
            } else {
                float overlap = SATMathUtils.getOverlap(proj1, proj2);
                if (overlap < minOverlap) {
                    minOverlap = overlap;
                    mtvAxis = axis;
                }
            }
        }

        if (mtvAxis == null) return null;

        Vector2 centerA = getPolygonCenter(verticesA);
        Vector2 centerB = getPolygonCenter(verticesB);
        Vector2 dir = new Vector2(centerB.x - centerA.x, centerB.y - centerA.y);
        if (SATMathUtils.dotProduct(mtvAxis, dir) < 0) {
            mtvAxis = new Vector2(-mtvAxis.x, -mtvAxis.y);
        }

        Vector2 mtv = new Vector2(mtvAxis.x * minOverlap, mtvAxis.y * minOverlap);
        if (reverse) {
            mtv.x = -mtv.x;
            mtv.y = -mtv.y;
        }
        return mtv;
    }

    private Vector2 getPolygonCenter(Vector2[] vertices) {
        float cx = 0, cy = 0;
        for (Vector2 v : vertices) {
            cx += v.x;
            cy += v.y;
        }
        return new Vector2(cx / vertices.length, cy / vertices.length);
    }
}
