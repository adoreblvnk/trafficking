package com.sit.trafficking.engine.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sit.trafficking.engine.EngineConstants;
import com.sit.trafficking.engine.interfaces.Movable;

public class DynamicEntity extends AbstractEntity implements Movable {

    private Vector2 velocity;
    private float friction = EngineConstants.DEFAULT_FRICTION;

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        if (friction < 0 || friction > 1) {
            Gdx.app.log("DynamicEntity", "Friction clamped to [0,1]: " + friction + " -> " + MathUtils.clamp(friction, 0, 1));
        }
        this.friction = MathUtils.clamp(friction, 0, 1);
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
    public void render(ShapeRenderer sr) {
        sr.setColor(getColor());
        sr.rect(getPosition().x, getPosition().y, getWidth(), getHeight());
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
            Gdx.app.error("DynamicEntity", "Invalid velocity rejected: (" + x + ", " + y + ")");
            return;
        }
        this.velocity.set(x, y);
    }

    //update position based on current velocity and keep bounds aligned with updated position
    @Override
    public void updatePosition(float dt) {
        getPosition().mulAdd(velocity, dt);
        getBounds().setPosition(getPosition().x, getPosition().y);
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
