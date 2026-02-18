package com.sit.trafficking.engine.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.sit.trafficking.engine.EngineConstants;
import com.sit.trafficking.engine.interfaces.Movable;

public class DynamicEntity extends AbstractEntity implements Movable {

    protected Vector2 velocity;
    protected float friction = EngineConstants.DEFAULT_FRICTION;

    public void setFriction(float friction) {
        this.friction = friction;
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
        sr.setColor(color);
        sr.rect(position.x, position.y, width, height);
    }

    //returning velocity for movement and collision responses
    @Override
    public Vector2 getVelocity() {
        return velocity;
    }

    //set velocity directly
    @Override
    public void setVelocity(float x, float y) {
        this.velocity.set(x, y);
    }

    //update position based on current velocity and keep bounds aligned with updated position
    @Override
    public void updatePosition(float dt) {
        position.mulAdd(velocity, dt);
        bounds.setPosition(position.x, position.y); 
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
