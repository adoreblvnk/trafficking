package com.sit.recyclingpinball.engine.physics;

import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle;
import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformCircle;
import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformVector2;

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
        return CollisionDispatcher.dispatch(this, other);
    }

    /**
     * Circle-vs-circle collision logic.
     */
    CollisionResult collideCircle(CircleCollider other) {
        if (!this.circle.overlaps(other.getCircle())) {
            return new CollisionResult(false, null, 0);
        }

        PlatformVector2 posA = new PlatformVector2(this.circle.getX(), this.circle.getY());
        PlatformVector2 posB = new PlatformVector2(other.getCircle().getX(), other.getCircle().getY());
        PlatformVector2 diff = posA.cpy().sub(posB);
        float dist = diff.len();
        float overlap = (this.circle.getRadius() + other.getCircle().getRadius()) - dist;

        PlatformVector2 normal;
        if (dist > 0) {
            normal = diff.nor();
        } else {
            normal = new PlatformVector2(1, 0);
        }
        return new CollisionResult(true, normal, overlap);
    }

    @Override
    public boolean contains(float x, float y) {
        return this.circle.contains(x, y);
    }
}
