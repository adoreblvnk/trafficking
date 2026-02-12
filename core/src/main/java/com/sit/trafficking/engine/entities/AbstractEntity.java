package com.sit.trafficking.engine.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sit.trafficking.engine.interfaces.CollisionListener;
import com.sit.trafficking.engine.interfaces.ICollidable;

public abstract class AbstractEntity implements ICollidable {

    protected String id;
    protected Vector2 position;
    protected float width;
    protected float height;
    protected Color color;
    protected int zIndex = 0;
    protected CollisionListener collisionListener;

    protected Rectangle bounds;

    public AbstractEntity(String id, float x, float y, float w, float h) {
        this.id = id;
        this.position = new Vector2(x, y);
        this.width = w;
        this.height = h;
        this.color = Color.WHITE;
        this.bounds = new Rectangle(x, y, w, h);
    }

    public void update(float dt) {
        bounds.set(position.x, position.y, width, height);
    }

    public abstract void render(ShapeRenderer sr);

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    public String getId() {
        return id;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public void setPosition(float x, float y) {
        this.position.set(x, y);
        this.bounds.setPosition(x, y);
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

    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public void setCollisionListener(CollisionListener collisionListener) {
        this.collisionListener = collisionListener;
    }

    @Override
    public void onCollision(ICollidable other) {
        // Now BOTH Static and Dynamic entities can react via logic injection
        if (collisionListener != null) {
            collisionListener.onCollide(this, other);
        }
    }
}
