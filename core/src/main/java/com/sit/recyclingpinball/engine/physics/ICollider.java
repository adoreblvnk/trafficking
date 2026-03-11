package com.sit.recyclingpinball.engine.physics;

import com.badlogic.gdx.math.Rectangle;

public interface ICollider {
    Rectangle getAABB();
    boolean intersects(ICollider other);
    boolean contains(float x, float y);
    void setPosition(float x, float y);
}