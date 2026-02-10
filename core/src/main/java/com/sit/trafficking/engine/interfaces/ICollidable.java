package com.sit.trafficking.engine.interfaces;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface ICollidable {
    Rectangle getBounds();

    Vector2 getPosition();

    void setPosition(float x, float y);

    boolean isStatic();

    boolean isTrigger();

    void onCollision(ICollidable other);
}
