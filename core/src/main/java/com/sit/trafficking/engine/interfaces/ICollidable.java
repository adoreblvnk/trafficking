package com.sit.trafficking.engine.interfaces;

import com.badlogic.gdx.math.Rectangle;

public interface ICollidable {
    Rectangle getBounds();

    boolean isStatic();

    boolean isTrigger();

    void onCollision(ICollidable other);
}
