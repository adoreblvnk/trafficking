package com.sit.recyclingpinball.engine.physics;

import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle;
import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformVector2;

public class OBBCollider implements ICollider {
    private float x;
    private float y;
    private float width;
    private float height;
    private float originX;
    private float originY;
    private float rotationDegrees;

    public OBBCollider(float x, float y, float width, float height, float originX, float originY,
            float rotationDegrees) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.originX = originX;
        this.originY = originY;
        this.rotationDegrees = rotationDegrees;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setRotation(float rotationDegrees) {
        this.rotationDegrees = rotationDegrees;
    }

    public PlatformVector2[] getVertices() {
        PlatformVector2[] vertices = new PlatformVector2[4];
        float rad = (float) Math.toRadians(rotationDegrees);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);

        float dx1 = -originX;
        float dy1 = -originY;
        float dx2 = width - originX;
        float dy2 = height - originY;

        float p1x = dx1 * cos - dy1 * sin;
        float p1y = dx1 * sin + dy1 * cos;

        float p2x = dx2 * cos - dy1 * sin;
        float p2y = dx2 * sin + dy1 * cos;

        float p3x = dx2 * cos - dy2 * sin;
        float p3y = dx2 * sin + dy2 * cos;

        float p4x = dx1 * cos - dy2 * sin;
        float p4y = dx1 * sin + dy2 * cos;

        vertices[0] = new PlatformVector2(x + originX + p1x, y + originY + p1y);
        vertices[1] = new PlatformVector2(x + originX + p2x, y + originY + p2y);
        vertices[2] = new PlatformVector2(x + originX + p3x, y + originY + p3y);
        vertices[3] = new PlatformVector2(x + originX + p4x, y + originY + p4y);

        return vertices;
    }

    public PlatformVector2[] getAxes() {
        PlatformVector2[] axes = new PlatformVector2[2];
        PlatformVector2[] vertices = getVertices();
        axes[0] = SATMathUtils.normalize(
                new PlatformVector2(vertices[1].getX() - vertices[0].getX(), vertices[1].getY() - vertices[0].getY()));
        axes[1] = SATMathUtils.normalize(
                new PlatformVector2(vertices[2].getX() - vertices[1].getX(), vertices[2].getY() - vertices[1].getY()));
        return axes;
    }

    @Override
    public PlatformRectangle getAABB() {
        PlatformVector2[] vertices = getVertices();
        float minX = vertices[0].getX();
        float minY = vertices[0].getY();
        float maxX = vertices[0].getX();
        float maxY = vertices[0].getY();

        for (int i = 1; i < vertices.length; i++) {
            if (vertices[i].getX() < minX)
                minX = vertices[i].getX();
            if (vertices[i].getY() < minY)
                minY = vertices[i].getY();
            if (vertices[i].getX() > maxX)
                maxX = vertices[i].getX();
            if (vertices[i].getY() > maxY)
                maxY = vertices[i].getY();
        }

        return new PlatformRectangle(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public boolean intersects(ICollider other) {
        return checkCollision(other).intersects();
    }

    @Override
    public CollisionResult checkCollision(ICollider other) {
        return other.checkCollision(this).invert();
    }

    @Override
    public CollisionResult checkCollision(CircleCollider other) {
        return SATMathUtils.getMTV(this, other);
    }

    @Override
    public CollisionResult checkCollision(BoxCollider other) {
        return SATMathUtils.getMTV(this, other);
    }

    @Override
    public CollisionResult checkCollision(OBBCollider other) {
        return SATMathUtils.getMTV(this, other);
    }

    @Override
    public boolean contains(float px, float py) {
        PlatformVector2[] vertices = getVertices();
        boolean result = false;
        for (int i = 0, j = vertices.length - 1; i < vertices.length; j = i++) {
            if ((vertices[i].getY() > py) != (vertices[j].getY() > py)
                    && (px < (vertices[j].getX() - vertices[i].getX()) * (py - vertices[i].getY())
                            / (vertices[j].getY() - vertices[i].getY()) + vertices[i].getX())) {
                result = !result;
            }
        }
        return result;
    }

    private boolean checkOBBVsCircle(OBBCollider obb, CircleCollider circle) {
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
        PlatformVector2 circleAxis = SATMathUtils.normalize(
                new PlatformVector2(closestVertex.getX() - center.getX(), closestVertex.getY() - center.getY()));

        PlatformVector2[] allAxes = new PlatformVector2[axes.length + 1];
        System.arraycopy(axes, 0, allAxes, 0, axes.length);
        allAxes[axes.length] = circleAxis;

        for (PlatformVector2 axis : allAxes) {
            float[] proj1 = SATMathUtils.projectPolygon(axis, vertices);
            float[] proj2 = SATMathUtils.projectCircle(axis, center, radius);

            if (!SATMathUtils.overlap(proj1, proj2)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkOBBVsBox(OBBCollider obb, BoxCollider box) {
        PlatformRectangle r = box.getAABB();
        PlatformVector2[] boxVertices = new PlatformVector2[]{new PlatformVector2(r.getX(), r.getY()),
                new PlatformVector2(r.getX() + r.getWidth(), r.getY()),
                new PlatformVector2(r.getX() + r.getWidth(), r.getY() + r.getHeight()),
                new PlatformVector2(r.getX(), r.getY() + r.getHeight())};
        PlatformVector2[] boxAxes = new PlatformVector2[]{new PlatformVector2(1, 0), new PlatformVector2(0, 1)};

        PlatformVector2[] obbVertices = obb.getVertices();
        PlatformVector2[] obbAxes = obb.getAxes();

        PlatformVector2[] allAxes = new PlatformVector2[boxAxes.length + obbAxes.length];
        System.arraycopy(boxAxes, 0, allAxes, 0, boxAxes.length);
        System.arraycopy(obbAxes, 0, allAxes, boxAxes.length, obbAxes.length);

        for (PlatformVector2 axis : allAxes) {
            float[] proj1 = SATMathUtils.projectPolygon(axis, obbVertices);
            float[] proj2 = SATMathUtils.projectPolygon(axis, boxVertices);

            if (!SATMathUtils.overlap(proj1, proj2)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkOBBVsOBB(OBBCollider obb1, OBBCollider obb2) {
        PlatformVector2[] vertices1 = obb1.getVertices();
        PlatformVector2[] vertices2 = obb2.getVertices();
        PlatformVector2[] axes1 = obb1.getAxes();
        PlatformVector2[] axes2 = obb2.getAxes();

        PlatformVector2[] allAxes = new PlatformVector2[axes1.length + axes2.length];
        System.arraycopy(axes1, 0, allAxes, 0, axes1.length);
        System.arraycopy(axes2, 0, allAxes, axes1.length, axes2.length);

        for (PlatformVector2 axis : allAxes) {
            float[] proj1 = SATMathUtils.projectPolygon(axis, vertices1);
            float[] proj2 = SATMathUtils.projectPolygon(axis, vertices2);

            if (!SATMathUtils.overlap(proj1, proj2)) {
                return false;
            }
        }
        return true;
    }
}
