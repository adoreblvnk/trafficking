package com.sit.recyclingpinball.engine.platform.libgdx.math;

import com.badlogic.gdx.math.Vector2;

public class PlatformVector2 {
    private final Vector2 delegate;

    public PlatformVector2() {
        this.delegate = new Vector2();
    }

    public PlatformVector2(float x, float y) {
        this.delegate = new Vector2(x, y);
    }

    public PlatformVector2(PlatformVector2 v) {
        this.delegate = new Vector2(v.delegate);
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

    public PlatformVector2 set(float x, float y) {
        delegate.set(x, y);
        return this;
    }

    public PlatformVector2 set(PlatformVector2 v) {
        delegate.set(v.delegate);
        return this;
    }

    public PlatformVector2 add(PlatformVector2 v) {
        delegate.add(v.delegate);
        return this;
    }

    public PlatformVector2 add(float x, float y) {
        delegate.add(x, y);
        return this;
    }

    public PlatformVector2 sub(PlatformVector2 v) {
        delegate.sub(v.delegate);
        return this;
    }

    public PlatformVector2 sub(float x, float y) {
        delegate.sub(x, y);
        return this;
    }

    public PlatformVector2 scl(float scalar) {
        delegate.scl(scalar);
        return this;
    }

    public PlatformVector2 scl(float x, float y) {
        delegate.scl(x, y);
        return this;
    }

    public PlatformVector2 mulAdd(PlatformVector2 v, float scalar) {
        delegate.mulAdd(v.delegate, scalar);
        return this;
    }

    public float dot(PlatformVector2 v) {
        return delegate.dot(v.delegate);
    }

    public float len() {
        return delegate.len();
    }

    public float len2() {
        return delegate.len2();
    }

    public PlatformVector2 nor() {
        delegate.nor();
        return this;
    }

    public float dst(PlatformVector2 v) {
        return delegate.dst(v.delegate);
    }

    public float dst2(PlatformVector2 v) {
        return delegate.dst2(v.delegate);
    }

    public PlatformVector2 cpy() {
        return new PlatformVector2(delegate.x, delegate.y);
    }

    public boolean isZero() {
        return delegate.isZero();
    }

    public boolean isZero(float margin) {
        return delegate.isZero(margin);
    }

    public float angleDeg() {
        return delegate.angleDeg();
    }

    public float angleRad() {
        return delegate.angleRad();
    }

    public PlatformVector2 rotateRad(float radians) {
        delegate.rotateRad(radians);
        return this;
    }

    public PlatformVector2 rotateDeg(float degrees) {
        delegate.rotateDeg(degrees);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        PlatformVector2 that = (PlatformVector2) obj;
        return delegate.equals(that.delegate);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
