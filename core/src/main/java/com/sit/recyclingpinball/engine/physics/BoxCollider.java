package com.sit.recyclingpinball.engine.physics;

import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle;

public class BoxCollider implements ICollider {
    private PlatformRectangle bounds;

    public BoxCollider(float x, float y, float width, float height) {
        this.bounds = new PlatformRectangle(x, y, width, height);
    }

    public void setPosition(float x, float y) {
        this.bounds.setPosition(x, y);
    }

    @Override
    public PlatformRectangle getAABB() {
        return this.bounds;
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
        return SATMathUtils.getAABBvsAABB(circle.getAABB(), this.getAABB());
    }

    @Override
    public CollisionResult collideWith(BoxCollider box) {
        return SATMathUtils.getAABBvsAABB(this.getAABB(), box.getAABB()).invert();
    }

    @Override
    public CollisionResult collideWith(OBBCollider obb) {
        return SATMathUtils.getMTV(obb, this);
    }

    @Override
    public boolean contains(float x, float y) {
        return this.bounds.contains(x, y);
    }
}
