package com.sit.recyclingpinball.engine.physics;

import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformVector2;

public record CollisionResult(boolean intersects, PlatformVector2 normal, float depth) {
    public CollisionResult invert() {
        if (this.normal != null) {
            return new CollisionResult(intersects, new PlatformVector2(-normal.getX(), -normal.getY()), depth);
        }
        return new CollisionResult(intersects, null, depth);
    }
}
