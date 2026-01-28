package com.sit.trafficking.engine.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Base abstract class for all entities in the simulation.
 * Enforces strict OOP inheritance and encapsulation of core properties.
 */
public abstract class AbstractEntity {
    
    protected final String id;
    protected final Vector2 position;
    protected final Vector2 velocity;
    protected float width;
    protected float height;
    protected Color color;

    /**
     * @param id Unique identifier for the entity.
     * @param x Initial X position.
     * @param y Initial Y position.
     * @param width Entity width.
     * @param height Entity height.
     */
    public AbstractEntity(String id, float x, float y, float width, float height) {
        this.id = id;
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0); // Default stationary
        this.width = width;
        this.height = height;
        this.color = Color.WHITE; // Default color
    }

    /**
     * Updates the entity's state.
     * Basic Euler integration: pos += vel * dt.
     * @param dt Delta time.
     */
    public void update(float dt) {
        position.mulAdd(velocity, dt);
    }

    /**
     * Renders the entity using the provided ShapeRenderer.
     * Must be implemented by concrete subclasses.
     * @param sr The ShapeRenderer context.
     */
    public abstract void render(ShapeRenderer sr);

    /**
     * @return The Axis Aligned Bounding Box (AABB) for collision detection.
     */
    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, width, height);
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(float vx, float vy) {
        this.velocity.set(vx, vy);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
}
