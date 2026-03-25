package com.sit.recyclingpinball.engine.platform.libgdx.math;

import com.badlogic.gdx.math.Circle;

public class PlatformCircle {
    private final Circle delegate;

    public PlatformCircle(float x, float y, float radius) {
        this.delegate = new Circle(x, y, radius);
    }

    public float getX() {
        return delegate.x;
    }
    public void setX(float x) {
        delegate.x = x;
    }

    public float getY() {
        return delegate.y;
    }
    public void setY(float y) {
        delegate.y = y;
    }

    public float getRadius() {
        return delegate.radius;
    }
    public void setRadius(float radius) {
        delegate.radius = radius;
    }

    public void set(float x, float y, float radius) {
        delegate.set(x, y, radius);
    }

    public boolean contains(float x, float y) {
        return delegate.contains(x, y);
    }

    public boolean contains(PlatformVector2 point) {
        return delegate.contains(point.getX(), point.getY());
    }

    public boolean overlaps(PlatformCircle c) {
        return delegate.overlaps(c.delegate);
    }

    public boolean overlaps(PlatformRectangle r) {
        return com.badlogic.gdx.math.Intersector.overlaps(this.delegate, r.getDelegate());
    }

    public void setPosition(float x, float y) {
        delegate.setPosition(x, y);
    }
}
