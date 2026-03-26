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

        PlatformVector2 p1 = new PlatformVector2(-originX, -originY).rotateDeg(rotationDegrees);
        PlatformVector2 p2 = new PlatformVector2(width - originX, -originY).rotateDeg(rotationDegrees);
        PlatformVector2 p3 = new PlatformVector2(width - originX, height - originY).rotateDeg(rotationDegrees);
        PlatformVector2 p4 = new PlatformVector2(-originX, height - originY).rotateDeg(rotationDegrees);

        float cx = x + originX;
        float cy = y + originY;

        vertices[0] = p1.add(cx, cy);
        vertices[1] = p2.add(cx, cy);
        vertices[2] = p3.add(cx, cy);
        vertices[3] = p4.add(cx, cy);

        return vertices;
    }

    public PlatformVector2[] getAxes() {
        PlatformVector2[] vertices = getVertices();
        PlatformVector2 axis1 = vertices[1].cpy().sub(vertices[0]).nor();
        PlatformVector2 axis2 = vertices[2].cpy().sub(vertices[1]).nor();
        return new PlatformVector2[]{axis1, axis2};
    }

    @Override
    public PlatformRectangle getAABB() {
        PlatformVector2[] vertices = getVertices();
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE;

        for (PlatformVector2 v : vertices) {
            minX = Math.min(minX, v.getX());
            minY = Math.min(minY, v.getY());
            maxX = Math.max(maxX, v.getX());
            maxY = Math.max(maxY, v.getY());
        }

        return new PlatformRectangle(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public boolean intersects(ICollider other) {
        return checkCollision(other).intersects();
    }

    @Override
    public CollisionResult checkCollision(ICollider other) {
        return other.collideWith(this);
    }

    @Override
    public CollisionResult collideWith(CircleCollider circle) {
        return SATMathUtils.getMTV(this, circle).invert();
    }

    @Override
    public CollisionResult collideWith(BoxCollider box) {
        return SATMathUtils.getMTV(this, box).invert();
    }

    @Override
    public CollisionResult collideWith(OBBCollider obb) {
        return SATMathUtils.getMTV(this, obb).invert();
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
}
