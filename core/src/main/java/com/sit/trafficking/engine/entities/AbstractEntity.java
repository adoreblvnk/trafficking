package com.sit.trafficking.engine.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sit.trafficking.engine.interfaces.CollisionListener;
import com.sit.trafficking.engine.interfaces.ICollidable;
import com.sit.trafficking.engine.interfaces.providers.IGraphicsProvider;

/**
 * Abstract base class for all game entities.
 * Defines common properties like position, size, rendering, and collision behavior.
 * Rendering is now platform-independent via IGraphicsProvider.
 */
public abstract class AbstractEntity implements ICollidable {

    private String id;
    private Vector2 position;
    private float width;
    private float height;
    private float r = 1.0f;
    private float g = 1.0f;
    private float b = 1.0f;
    private float a = 1.0f;
    private int zIndex = 0;
    private CollisionListener collisionListener;

    private Rectangle bounds;

    //every entity requires a position and size to exist
    public AbstractEntity(String id, float x, float y, float w, float h) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Entity ID cannot be null or empty");
        }
        if (Float.isNaN(x) || Float.isNaN(y) || Float.isInfinite(x) || Float.isInfinite(y)) {
            throw new IllegalArgumentException("Position cannot be NaN or Infinite: (" + x + ", " + y + ")");
        }
        if (w <= 0 || h <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive: " + w + "x" + h);
        }

        this.id = id;
        this.position = new Vector2(x, y);
        this.width = w;
        this.height = h;
        this.r = 1.0f;
        this.g = 1.0f;
        this.b = 1.0f;
        this.a = 1.0f;
        this.bounds = new Rectangle(x, y, w, h);
    }

    //make sure collision bounding is in sync whenever entity position updates
    public void update(float dt) {
        bounds.set(position.x, position.y, width, height);
    }

    /**
     * Renders the entity using the provided graphics provider (platform-independent).
     *
     * @param graphics the graphics provider
     */
    public abstract void render(IGraphicsProvider graphics);

    //shows current AABB for collision checks
    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    public String getId() {
        return id;
    }

    //gets and return current world position
    @Override
    public Vector2 getPosition() {
        return position;
    }

    //updates both position and bounding box to prevent desync whenever moving the entity
    @Override
    public void setPosition(float x, float y) {
        if (Float.isNaN(x) || Float.isNaN(y) || Float.isInfinite(x) || Float.isInfinite(y)) {
            return;
        }
        this.position.set(x, y);
        this.bounds.setPosition(x, y);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    /**
     * Sets the color as RGBA components.
     *
     * @param r red (0.0 to 1.0)
     * @param g green (0.0 to 1.0)
     * @param b blue (0.0 to 1.0)
     * @param a alpha (0.0 to 1.0)
     */
    public void setColor(float r, float g, float b, float a) {
        this.r = Math.max(0, Math.min(1, r));
        this.g = Math.max(0, Math.min(1, g));
        this.b = Math.max(0, Math.min(1, b));
        this.a = Math.max(0, Math.min(1, a));
    }

    /**
     * Gets the red component of the entity's color.
     */
    public float getRed() {
        return r;
    }

    /**
     * Gets the green component of the entity's color.
     */
    public float getGreen() {
        return g;
    }

    /**
     * Gets the blue component of the entity's color.
     */
    public float getBlue() {
        return b;
    }

    /**
     * Gets the alpha component of the entity's color.
     */
    public float getAlpha() {
        return a;
    }

    public int getZIndex() {
        return zIndex;
    }

    //updates the entity's render layer
    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }


    public void setCollisionListener(CollisionListener collisionListener) {
        this.collisionListener = collisionListener;
    }

    public CollisionListener getCollisionListener() {
        return collisionListener;
    }

    //activates when an overlap with each other occurs
    @Override
    public void onCollision(ICollidable other) {
        if (collisionListener != null) {
            collisionListener.onCollide(this, other);
        }
    }
}
