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

    //every entity requires a position and size to exist
    public AbstractEntity(String id, float x, float y, float w, float h) {
        this.id = id;
        this.position = new Vector2(x, y);
        this.width = w;
        this.height = h;
        this.color = Color.WHITE;
        this.bounds = new Rectangle(x, y, w, h);
    }

    //make sure collision bounding is in sync whenever entity position updates
    public void update(float dt) {
        bounds.set(position.x, position.y, width, height);
    }

    public abstract void render(ShapeRenderer sr);

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
    
    //updates the entity's render layer
    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }


    public void setCollisionListener(CollisionListener collisionListener) {
        this.collisionListener = collisionListener;
    }

    //activates when an overlap with each other occurs
    @Override
    public void onCollision(ICollidable other) {
        if (collisionListener != null) {
            collisionListener.onCollide(this, other);
        }
    }
}
