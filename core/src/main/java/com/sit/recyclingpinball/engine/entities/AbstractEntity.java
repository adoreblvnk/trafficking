package com.sit.recyclingpinball.engine.entities;

import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformVector2;
import com.sit.recyclingpinball.engine.interfaces.ICollidable;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import com.sit.recyclingpinball.engine.physics.ICollider;
import com.sit.recyclingpinball.engine.physics.BoxCollider;

/**
 * Abstract base class for all game entities. Defines common properties like
 * position, size, rendering, and collision behavior. Rendering is now
 * platform-independent via IGraphicsProvider.
 */
public abstract class AbstractEntity implements ICollidable {

    private String id;
    private PlatformVector2 position;
    private float width;
    private float height;
    private int zIndex = 0;
    private String tag = "";

    private ICollider collider;
    private boolean collisionEnabled = true;

    // every entity requires a position and size to exist
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
        this.position = new PlatformVector2(x, y);
        this.width = w;
        this.height = h;
        this.collider = new BoxCollider(x, y, w, h);
    }

    protected void setCollider(ICollider collider) {
        this.collider = collider;
    }

    // make sure collision bounding is in sync whenever entity position updates
    public void update(float dt) {
        if (collider != null) {
            collider.setPosition(position.getX(), position.getY());
        }
    }

    /**
     * Renders the entity using the provided graphics provider
     * (platform-independent).
     *
     * @param graphics
     *            the graphics provider
     */
    public abstract void render(IGraphicsProvider graphics);

    // shows current AABB for collision checks
    @Override
    public ICollider getCollider() {
        return collider;
    }

    @Override
    public boolean isCollisionEnabled() {
        return collisionEnabled;
    }

    @Override
    public void setCollisionEnabled(boolean enabled) {
        this.collisionEnabled = enabled;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag != null ? tag : "";
    }

    // gets and return current world position
    @Override
    public PlatformVector2 getPosition() {
        return position;
    }

    // updates both position and bounding box to prevent desync whenever moving the
    // entity
    @Override
    public void setPosition(float x, float y) {
        if (Float.isNaN(x) || Float.isNaN(y) || Float.isInfinite(x) || Float.isInfinite(y)) {
            return;
        }
        this.position.set(x, y);
        if (this.collider != null) {
            this.collider.setPosition(x, y);
        }
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
     * @param r
     *            red (0.0 to 1.0)
     * @param g
     *            green (0.0 to 1.0)
     * @param b
     *            blue (0.0 to 1.0)
     * @param a
     *            alpha (0.0 to 1.0)
     */
    /**
     * Gets the red component of the entity's color.
     */
    /**
     * Gets the green component of the entity's color.
     */
    /**
     * Gets the blue component of the entity's color.
     */
    /**
     * Gets the alpha component of the entity's color.
     */
    public int getZIndex() {
        return zIndex;
    }

    // activates when an overlap with each other occurs
    @Override
    public void onCollision(ICollidable other) {
    }

    @Override
    public void applyBounce(float normalX, float normalY) {
        // Default empty implementation, meant to be overridden by dynamic entities.
    }
}
