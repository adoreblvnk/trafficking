package com.sit.trafficking.engine.entities;

public interface ICollidable {
    void onCollision(Entity other, float intensity);
}
