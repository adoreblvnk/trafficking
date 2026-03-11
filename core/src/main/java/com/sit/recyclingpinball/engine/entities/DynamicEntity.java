package com.sit.recyclingpinball.engine.entities;

import com.badlogic.gdx.math.Vector2;
import com.sit.recyclingpinball.engine.EngineConstants;
import com.sit.recyclingpinball.engine.interfaces.Movable;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;

/**
 * Dynamic entity that moves with velocity-based motion.
 * Automatically applies friction to velocity each update.
 * Renders as a filled rectangle with its color.
 */
public class DynamicEntity extends AbstractEntity implements Movable {

    private Vector2 velocity;
    private float friction = EngineConstants.DEFAULT_FRICTION;

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = Math.max(0f, Math.min(1f, friction));
    }

    //the entity is movable with velocity-based motion
    public DynamicEntity(String id, float x, float y, float w, float h) {
        super(id, x, y, w, h);
        this.velocity = new Vector2(0, 0);
    }

    @Override
    public void update(float dt) {
        velocity.scl(friction);
        super.update(dt);
    }

    //default rendering - subclasses can override
    @Override
    public void render(IGraphicsProvider graphics) {
        graphics.setColor(getRed(), getGreen(), getBlue(), getAlpha());
        graphics.drawRect(getPosition().x, getPosition().y, getWidth(), getHeight());
    }

    //returning velocity for movement and collision responses
    @Override
    public Vector2 getVelocity() {
        return velocity;
    }

    //set velocity directly
    @Override
    public void setVelocity(float x, float y) {
        if (Float.isNaN(x) || Float.isNaN(y) || Float.isInfinite(x) || Float.isInfinite(y)) {
            return;
        }
        this.velocity.set(x, y);
    }

    //update position based on current velocity and keep bounds aligned with updated position
    @Override
    public void updatePosition(float dt) {
        getPosition().mulAdd(velocity, dt);
        if (collider instanceof com.sit.recyclingpinball.engine.physics.BoxCollider) {
            ((com.sit.recyclingpinball.engine.physics.BoxCollider) collider).setPosition(getPosition().x, getPosition().y);
        } else if (collider instanceof com.sit.recyclingpinball.engine.physics.CircleCollider) {
            ((com.sit.recyclingpinball.engine.physics.CircleCollider) collider).setPosition(getPosition().x, getPosition().y);
        }
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean isTrigger() {
        return false;
    }
}
