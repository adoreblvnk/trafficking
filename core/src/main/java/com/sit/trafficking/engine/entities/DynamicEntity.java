package com.sit.trafficking.engine.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.sit.trafficking.engine.EngineConstants;
import com.sit.trafficking.engine.interfaces.CollisionListener;
import com.sit.trafficking.engine.interfaces.ICollidable;
import com.sit.trafficking.engine.interfaces.Movable;

public class DynamicEntity extends AbstractEntity implements Movable {

    protected Vector2 velocity;
    protected float friction = EngineConstants.DEFAULT_FRICTION;

    public void setFriction(float friction) {
        this.friction = friction;
    }

    // Strategy Pattern: Logic is injected here
    protected CollisionListener collisionListener;

    public DynamicEntity(String id, float x, float y, float w, float h) {
        super(id, x, y, w, h);
        this.velocity = new Vector2(0, 0);
    }

    public void setCollisionListener(CollisionListener listener) {
        this.collisionListener = listener;
    }

    @Override
    public void update(float dt) {
        updatePosition(dt);
        velocity.scl(friction);
    }

    @Override
    public void render(ShapeRenderer sr) {
        sr.setColor(color);
        sr.rect(position.x, position.y, width, height);
    }

    @Override
    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    public void setVelocity(float x, float y) {
        this.velocity.set(x, y);
    }

    @Override
    public void updatePosition(float dt) {
        position.mulAdd(velocity, dt);
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean isTrigger() {
        return false;
    }

    @Override
    public void onCollision(ICollidable other) {
        // The Engine does NOT know about sounds.
        // It simply notifies the listener if one exists.
        if (collisionListener != null) {
            collisionListener.onCollide(this, other);
        }
    }
}
