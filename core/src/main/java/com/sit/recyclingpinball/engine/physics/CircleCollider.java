package com.sit.recyclingpinball.engine.physics;

import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle;
import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformCircle;

public class CircleCollider implements ICollider {
    private PlatformCircle circle;
    private PlatformRectangle aabb;

    public CircleCollider(float x, float y, float radius) {
        this.circle = new PlatformCircle(x, y, radius);
        this.aabb = new PlatformRectangle(x - radius, y - radius, radius * 2, radius * 2);
    }

    public void setPosition(float x, float y) {
        this.circle.setPosition(x, y);
        this.aabb.setPosition(x - circle.getRadius(), y - circle.getRadius());
    }

    public PlatformCircle getCircle() {
        return this.circle;
    }

    @Override
    public PlatformRectangle getAABB() {
        return this.aabb;
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
        boolean overlaps = this.circle.overlaps(other.getCircle());
        if (!overlaps)
            return new CollisionResult(false, null, 0);

        float dx = this.circle.getX() - other.getCircle().getX();
        float dy = this.circle.getY() - other.getCircle().getY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        float overlap = (this.circle.getRadius() + other.getCircle().getRadius()) - dist;

        com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformVector2 normal;
        if (dist > 0) {
            normal = new com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformVector2(dx / dist, dy / dist);
        } else {
            normal = new com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformVector2(1, 0);
        }
        return new CollisionResult(true, normal, overlap);
    }

    @Override
    public CollisionResult checkCollision(BoxCollider other) {
        return SATMathUtils.getAABBvsAABB(this.aabb, other.getAABB());
    }

    @Override
    public CollisionResult checkCollision(OBBCollider other) {
        return SATMathUtils.getMTV(other, this).invert();
    }

    @Override
    public boolean contains(float x, float y) {
        return this.circle.contains(x, y);
    }
}
