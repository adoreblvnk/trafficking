package com.sit.recyclingpinball.engine.physics;

import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformVector2;
import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle;

public final class SATMathUtils {

    private SATMathUtils() {
    }

    public static float dotProduct(PlatformVector2 a, PlatformVector2 b) {
        return a.getX() * b.getX() + a.getY() * b.getY();
    }

    public static PlatformVector2 normalize(PlatformVector2 v) {
        float len = v.len();
        if (len == 0) return new PlatformVector2(0, 0);
        return new PlatformVector2(v.getX() / len, v.getY() / len);
    }

    public static PlatformVector2 perpendicular(PlatformVector2 v) {
        return new PlatformVector2(-v.getY(), v.getX());
    }

    public static float[] projectPolygon(PlatformVector2 axis, PlatformVector2[] vertices) {
        float min = dotProduct(axis, vertices[0]);
        float max = min;
        for (int i = 1; i < vertices.length; i++) {
            float projection = dotProduct(axis, vertices[i]);
            if (projection < min) {
                min = projection;
            } else if (projection > max) {
                max = projection;
            }
        }
        return new float[]{min, max};
    }

    public static float[] projectCircle(PlatformVector2 axis, PlatformVector2 center, float radius) {
        float projection = dotProduct(axis, center);
        return new float[]{projection - radius, projection + radius};
    }

    public static boolean overlap(float[] proj1, float[] proj2) {
        return proj1[0] <= proj2[1] && proj2[0] <= proj1[1];
    }

    public static float getOverlap(float[] proj1, float[] proj2) {
        if (!overlap(proj1, proj2)) {
            return 0;
        }
        return Math.min(proj1[1] - proj2[0], proj2[1] - proj1[0]);
    }

    public static CollisionResult getAABBvsAABB(PlatformRectangle rA, PlatformRectangle rB) {
        PlatformRectangle intersection = new PlatformRectangle();
        PlatformRectangle.intersectRectangles(rA, rB, intersection);

        if (intersection.getWidth() == 0 && intersection.getHeight() == 0) {
            return new CollisionResult(false, null, 0);
        }

        float overlapX = intersection.getWidth();
        float overlapY = intersection.getHeight();
        
        float pushX = 0;
        float pushY = 0;

        if (overlapX < overlapY) {
            if (rA.getX() + rA.getWidth() / 2 < rB.getX() + rB.getWidth() / 2)
                pushX = -overlapX;
            else
                pushX = overlapX;
        } else {
            if (rA.getY() + rA.getHeight() / 2 < rB.getY() + rB.getHeight() / 2)
                pushY = -overlapY;
            else
                pushY = overlapY;
        }

        PlatformVector2 normal = new PlatformVector2(pushX, pushY);
        float depth = normal.len();
        if (depth > 0) {
            normal.nor();
        }
        return new CollisionResult(true, normal, depth);
    }
    
    public static CollisionResult getMTV(OBBCollider obb, CircleCollider circle) {
        PlatformVector2[] vertices = obb.getVertices();
        PlatformVector2 center = new PlatformVector2(circle.getCircle().getX(), circle.getCircle().getY());
        float radius = circle.getCircle().getRadius();

        PlatformVector2[] axes = obb.getAxes();

        PlatformVector2 closestVertex = vertices[0];
        float minDst2 = center.dst2(vertices[0]);
        for (int i = 1; i < vertices.length; i++) {
            float dst2 = center.dst2(vertices[i]);
            if (dst2 < minDst2) {
                minDst2 = dst2;
                closestVertex = vertices[i];
            }
        }

        PlatformVector2 circleAxis = SATMathUtils.normalize(new PlatformVector2(center.getX() - closestVertex.getX(), center.getY() - closestVertex.getY()));

        PlatformVector2[] allAxes = new PlatformVector2[axes.length + 1];
        System.arraycopy(axes, 0, allAxes, 0, axes.length);
        allAxes[axes.length] = circleAxis;

        float minOverlap = Float.MAX_VALUE;
        PlatformVector2 mtvAxis = null;

        for (PlatformVector2 axis : allAxes) {
            if (axis.getX() == 0 && axis.getY() == 0) continue;
            float[] proj1 = SATMathUtils.projectPolygon(axis, vertices);
            float[] proj2 = SATMathUtils.projectCircle(axis, center, radius);

            if (!overlap(proj1, proj2)) {
                return new CollisionResult(false, null, 0);
            } else {
                float overlap = getOverlap(proj1, proj2);
                if (overlap < minOverlap) {
                    minOverlap = overlap;
                    mtvAxis = axis;
                }
            }
        }

        if (mtvAxis == null) return new CollisionResult(false, null, 0);

        PlatformVector2 centerA = getPolygonCenter(vertices);
        PlatformVector2 centerB = center;
        PlatformVector2 dir = new PlatformVector2(centerA.getX() - centerB.getX(), centerA.getY() - centerB.getY());
        if (dotProduct(mtvAxis, dir) < 0) {
            mtvAxis = new PlatformVector2(-mtvAxis.getX(), -mtvAxis.getY());
        }

        float epsilon = 0.5f;
        return new CollisionResult(true, mtvAxis, minOverlap + epsilon);
    }
    
    public static CollisionResult getMTV(OBBCollider obb, BoxCollider box) {
        com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle r = box.getAABB();
        PlatformVector2[] boxVertices = new PlatformVector2[]{
                new PlatformVector2(r.getX(), r.getY()),
                new PlatformVector2(r.getX() + r.getWidth(), r.getY()),
                new PlatformVector2(r.getX() + r.getWidth(), r.getY() + r.getHeight()),
                new PlatformVector2(r.getX(), r.getY() + r.getHeight())
        };
        PlatformVector2[] boxAxes = new PlatformVector2[]{
                new PlatformVector2(1, 0),
                new PlatformVector2(0, 1)
        };

        return getMTVPolygons(obb.getVertices(), obb.getAxes(), boxVertices, boxAxes);
    }
    
    public static CollisionResult getMTV(OBBCollider obb1, OBBCollider obb2) {
        return getMTVPolygons(obb1.getVertices(), obb1.getAxes(), obb2.getVertices(), obb2.getAxes());
    }

    private static CollisionResult getMTVPolygons(PlatformVector2[] verticesA, PlatformVector2[] axesA, PlatformVector2[] verticesB, PlatformVector2[] axesB) {
        PlatformVector2[] allAxes = new PlatformVector2[axesA.length + axesB.length];
        System.arraycopy(axesA, 0, allAxes, 0, axesA.length);
        System.arraycopy(axesB, 0, allAxes, axesA.length, axesB.length);

        float minOverlap = Float.MAX_VALUE;
        PlatformVector2 mtvAxis = null;

        for (PlatformVector2 axis : allAxes) {
            if (axis.getX() == 0 && axis.getY() == 0) continue;
            float[] proj1 = projectPolygon(axis, verticesA);
            float[] proj2 = projectPolygon(axis, verticesB);

            if (!overlap(proj1, proj2)) {
                return new CollisionResult(false, null, 0);
            } else {
                float overlap = getOverlap(proj1, proj2);
                if (overlap < minOverlap) {
                    minOverlap = overlap;
                    mtvAxis = axis;
                }
            }
        }

        if (mtvAxis == null) return new CollisionResult(false, null, 0);

        PlatformVector2 centerA = getPolygonCenter(verticesA);
        PlatformVector2 centerB = getPolygonCenter(verticesB);
        PlatformVector2 dir = new PlatformVector2(centerA.getX() - centerB.getX(), centerA.getY() - centerB.getY());
        if (dotProduct(mtvAxis, dir) < 0) {
            mtvAxis = new PlatformVector2(-mtvAxis.getX(), -mtvAxis.getY());
        }

        float epsilon = 0.5f;
        return new CollisionResult(true, mtvAxis, minOverlap + epsilon);
    }
    
    private static PlatformVector2 getPolygonCenter(PlatformVector2[] vertices) {
        float cx = 0, cy = 0;
        for (PlatformVector2 v : vertices) {
            cx += v.getX();
            cy += v.getY();
        }
        return new PlatformVector2(cx / vertices.length, cy / vertices.length);
    }
}
