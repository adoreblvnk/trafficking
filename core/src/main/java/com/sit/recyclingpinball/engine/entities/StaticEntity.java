package com.sit.recyclingpinball.engine.entities;

import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;

/**
 * Static entity that does not move or update.
 * Renders as a filled rectangle with its color.
 */
public class StaticEntity extends AbstractEntity {

    public StaticEntity(String id, float x, float y, float w, float h, float r, float g, float b) {
        super(id, x, y, w, h);
        setColor(r, g, b, 1.0f);
    }

    //skipped as static entities don't change between frames
    @Override
    public void update(float dt) {
        // Do nothing
    }

    //default rendering - subclasses can override
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
