package com.sit.trafficking.engine.interfaces;

import com.badlogic.gdx.math.Vector2;

public interface Movable {
    Vector2 getVelocity();

    void setVelocity(float x, float y);

    void updatePosition(float dt);
}
