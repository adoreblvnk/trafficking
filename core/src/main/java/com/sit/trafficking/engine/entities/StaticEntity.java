package com.sit.trafficking.engine.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class StaticEntity extends AbstractEntity {

    public StaticEntity(String id, float x, float y, float w, float h) {
        super(id, x, y, w, h);
        this.color = Color.GRAY;
    }

    //skipped as static entities don't change between frames
    @Override
    public void update(float dt) {
        // Do nothing
    }

    //default rendering - subclasses can override
    @Override
    public void render(ShapeRenderer sr) {
        sr.setColor(color);
        sr.rect(position.x, position.y, width, height);
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public boolean isTrigger() {
        return false;
    }
}
