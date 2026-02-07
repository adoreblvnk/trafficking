package com.sit.trafficking.engine.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sit.trafficking.engine.interfaces.ICollidable;

public abstract class AbstractEntity implements ICollidable {
    
    protected String id;
    protected Vector2 position;
    protected float width;
    protected float height;
    protected Color color;

    public AbstractEntity(String id, float x, float y, float w, float h) {
        this.id = id;
        this.position = new Vector2(x, y);
        this.width = w;
        this.height = h;
        this.color = Color.WHITE;
    }

    public void update(float dt) {
        // Base update logic if any
    }

    public abstract void render(ShapeRenderer sr);

    @Override
    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, width, height);
    }

    public String getId() {
        return id;
    }

    public Vector2 getPosition() {
        return position;
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
