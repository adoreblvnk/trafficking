package com.sit.recyclingpinball.engine.entities;

import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;

/**
 * Static entity that does not move or update. Renders as a filled rectangle
 * with its color.
 */
public class StaticEntity extends AbstractEntity {
    private float r = 1.0f;
    private float g = 1.0f;
    private float b = 1.0f;
    private float a = 1.0f;

    public void setColor(float r, float g, float b, float a) {
        this.r = Math.max(0, Math.min(1, r));
        this.g = Math.max(0, Math.min(1, g));
        this.b = Math.max(0, Math.min(1, b));
        this.a = Math.max(0, Math.min(1, a));
    }

    public float getRed() { return r; }
    public float getGreen() { return g; }
    public float getBlue() { return b; }
    public float getAlpha() { return a; }


    public StaticEntity(String id, float x, float y, float w, float h, float r, float g, float b) {
        super(id, x, y, w, h);
        setColor(r, g, b, 1.0f);
    }

    // skipped as static entities don't change between frames
    @Override
    public void update(float dt) {
        // Do nothing
    }

    // default rendering - subclasses can override
    @Override
    public void render(IGraphicsProvider graphics) {
        graphics.setColor(getRed(), getGreen(), getBlue(), getAlpha());
        graphics.drawRect(getPosition().getX(), getPosition().getY(), getWidth(), getHeight());
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public boolean isTrigger() {
        return false;
    }

    @Override
    public float getInverseMass() {
        return 0f;
    }
}
