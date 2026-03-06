package com.sit.covid26.engine.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

public class CircleCollider implements ICollider {
    private Circle circle;
    private Rectangle aabb;

    public CircleCollider(float x, float y, float radius) {
        this.circle = new Circle(x, y, radius);
        this.aabb = new Rectangle(x - radius, y - radius, radius * 2, radius * 2);
    }

    public void setPosition(float x, float y) {
        this.circle.setPosition(x, y);
        this.aabb.setPosition(x - circle.radius, y - circle.radius);
    }

    public Circle getCircle() {
        return this.circle;
    }

    @Override
    public Rectangle getAABB() {
        return this.aabb;
    }

    @Override
    public boolean intersects(ICollider other) {
        if (other instanceof CircleCollider) {
            return this.circle.overlaps(((CircleCollider) other).getCircle());
        } else if (other instanceof BoxCollider) {
            Rectangle rect = ((BoxCollider) other).getAABB();
            return Intersector.overlaps(this.circle, rect);
        }
        return false;
    }

    @Override
    public boolean contains(float x, float y) {
        return this.circle.contains(x, y);
    }
}