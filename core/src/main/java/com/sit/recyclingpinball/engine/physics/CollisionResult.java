package com.sit.recyclingpinball.engine.physics;

import com.badlogic.gdx.math.Vector2;

public class CollisionResult {
    public boolean intersects;
    public Vector2 normal;
    public float depth;

    public CollisionResult(boolean intersects, Vector2 normal, float depth) {
        this.intersects = intersects;
        this.normal = normal;
        this.depth = depth;
    }
}
