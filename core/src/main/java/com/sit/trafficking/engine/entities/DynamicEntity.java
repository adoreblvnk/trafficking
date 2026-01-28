package com.sit.trafficking.engine.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Represents moving objects in the simulation.
 * Renders as a Circle.
 */
public class DynamicEntity extends AbstractEntity {

    public DynamicEntity(String id, float x, float y, float width, float height) {
        super(id, x, y, width, height);
        this.color = com.badlogic.gdx.graphics.Color.SKY; // light blue
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        // Apply global friction so entities slow down naturally
        float friction = 0.5f; // retain ~50% speed per second
        velocity.scl((float) Math.pow(friction, dt));

        // Snap to full stop when very slow to prevent endless sliding
        if (velocity.len2() < 25f) { // speed < 5 units/sec
            velocity.setZero();
        }

        // Decay temporary flash back to light blue if needed (simple immediate reset)
        if (!color.equals(com.badlogic.gdx.graphics.Color.SKY)) {
            color = com.badlogic.gdx.graphics.Color.SKY;
        }
    }

    @Override
    public void render(ShapeRenderer sr) {
        sr.setColor(color);
        // Render as a circle centered at position + half dimensions
        // Note: AbstractEntity position is usually bottom-left for AABB, 
        // but for a circle we might want to treat pos as center or adjust.
        // Given AABB logic in getBounds (x,y,w,h), (x,y) is bottom-left corner.
        // Center is x + w/2, y + h/2. Radius is min(w, h) / 2.
        float radius = Math.min(width, height) / 2f;
        sr.circle(position.x + width / 2f, position.y + height / 2f, radius);
    }
}
