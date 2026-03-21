package com.sit.recyclingpinball.engine.platform.libgdx.math;

import com.badlogic.gdx.math.Rectangle;

public class PlatformRectangle {
    private final Rectangle delegate;

    public PlatformRectangle() {
        this.delegate = new Rectangle();
    }

    public PlatformRectangle(float x, float y, float width, float height) {
        this.delegate = new Rectangle(x, y, width, height);
    }

    public PlatformRectangle(PlatformRectangle rect) {
        this.delegate = new Rectangle(rect.delegate);
    }

    public float getX() { return delegate.x; }
    public void setX(float x) { delegate.x = x; }

    public float getY() { return delegate.y; }
    public void setY(float y) { delegate.y = y; }

    public float getWidth() { return delegate.width; }
    public void setWidth(float width) { delegate.width = width; }

    public float getHeight() { return delegate.height; }
    public void setHeight(float height) { delegate.height = height; }

    public PlatformRectangle set(float x, float y, float width, float height) {
        delegate.set(x, y, width, height);
        return this;
    }

    public PlatformRectangle set(PlatformRectangle rect) {
        delegate.set(rect.delegate);
        return this;
    }

    public boolean contains(float x, float y) {
        return delegate.contains(x, y);
    }

    public boolean contains(PlatformVector2 point) {
        return delegate.contains(point.getX(), point.getY());
    }

    public boolean contains(PlatformRectangle rectangle) {
        return delegate.contains(rectangle.delegate);
    }

    public boolean overlaps(PlatformRectangle r) {
        return delegate.overlaps(r.delegate);
    }
    
    public PlatformVector2 getCenter(PlatformVector2 vector) {
        com.badlogic.gdx.math.Vector2 temp = new com.badlogic.gdx.math.Vector2();
        delegate.getCenter(temp);
        vector.set(temp.x, temp.y);
        return vector;
    }
    
    public PlatformRectangle merge(PlatformRectangle rect) {
        delegate.merge(rect.delegate);
        return this;
    }

    public static boolean intersectRectangles(PlatformRectangle rectangle1, PlatformRectangle rectangle2, PlatformRectangle intersection) {
        return com.badlogic.gdx.math.Intersector.intersectRectangles(rectangle1.getDelegate(), rectangle2.getDelegate(), intersection.getDelegate());
    }

    public void setPosition(float x, float y) {
        delegate.setPosition(x, y);
    }

    public Rectangle getDelegate() {
        return delegate;
    }
}
