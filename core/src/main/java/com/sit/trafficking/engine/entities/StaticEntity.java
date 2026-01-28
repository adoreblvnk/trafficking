package com.sit.trafficking.engine.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Represents immutable obstacles like walls.
 * Velocity is permanently zero.
 * Renders as a Rectangle.
 */
public class StaticEntity extends AbstractEntity {

    public StaticEntity(String id, float x, float y, float width, float height) {
        super(id, x, y, width, height);
        this.velocity.set(0, 0);
    }

    @Override
    public void update(float dt) {
        // Static entities do not move.
        // Overridden to ensure velocity remains 0 if accidentally set? 
        // Or just leave empty to save comp time. 
        // Super update does pos += vel * dt. Since vel is 0, super.update is harmless but this is explicit.
    }

    @Override
    public void render(ShapeRenderer sr) {
        sr.setColor(color);
        sr.rect(position.x, position.y, width, height);
    }
}
