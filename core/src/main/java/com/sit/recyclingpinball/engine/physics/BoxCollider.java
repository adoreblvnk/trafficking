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
        return other.checkCollision(this).invert();
    }

    @Override
    public CollisionResult checkCollision(CircleCollider other) {
        return other.checkCollision(this).invert();
    }

    @Override
    public CollisionResult checkCollision(BoxCollider other) {
        return SATMathUtils.getAABBvsAABB(this.bounds, other.getAABB());
    }

    @Override
    public CollisionResult checkCollision(OBBCollider other) {
        return SATMathUtils.getMTV(other, this).invert();
    }

    @Override
    public boolean contains(float x, float y) {
        return this.bounds.contains(x, y);
    }
}
