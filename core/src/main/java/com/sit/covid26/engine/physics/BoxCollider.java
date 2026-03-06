package com.sit.covid26.engine.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Circle;

public class BoxCollider implements ICollider {
    private Rectangle bounds;

    public BoxCollider(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void setPosition(float x, float y) {
        this.bounds.setPosition(x, y);
    }

    @Override
    public Rectangle getAABB() {
        return this.bounds;
    }

    @Override
    public boolean intersects(ICollider other) {
        if (other instanceof BoxCollider) {
            return this.bounds.overlaps(((BoxCollider) other).getAABB());
        } else if (other instanceof CircleCollider) {
            Circle circle = ((CircleCollider) other).getCircle();
            return Intersector.overlaps(circle, this.bounds);
        }
        return false;
    }

    @Override
    public boolean contains(float x, float y) {
        return this.bounds.contains(x, y);
    }
}