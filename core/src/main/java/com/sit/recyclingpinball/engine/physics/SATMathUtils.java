package com.sit.recyclingpinball.engine.physics;

import com.badlogic.gdx.math.Vector2;

public final class SATMathUtils {

    private SATMathUtils() {
    }

    public static float dotProduct(Vector2 a, Vector2 b) {
        return a.x * b.x + a.y * b.y;
    }

    public static Vector2 normalize(Vector2 v) {
        float len = v.len();
        if (len == 0) return new Vector2(0, 0);
        return new Vector2(v.x / len, v.y / len);
    }

    public static Vector2 perpendicular(Vector2 v) {
        return new Vector2(-v.y, v.x);
    }

    public static float[] projectPolygon(Vector2 axis, Vector2[] vertices) {
        float min = dotProduct(axis, vertices[0]);
        float max = min;
        for (int i = 1; i < vertices.length; i++) {
            float projection = dotProduct(axis, vertices[i]);
            if (projection < min) {
                min = projection;
            } else if (projection > max) {
                max = projection;
            }
        }
        return new float[]{min, max};
    }

    public static float[] projectCircle(Vector2 axis, Vector2 center, float radius) {
        float projection = dotProduct(axis, center);
        return new float[]{projection - radius, projection + radius};
    }

    public static boolean overlap(float[] proj1, float[] proj2) {
        return proj1[0] <= proj2[1] && proj2[0] <= proj1[1];
    }

    public static float getOverlap(float[] proj1, float[] proj2) {
        if (!overlap(proj1, proj2)) {
            return 0;
        }
        return Math.min(proj1[1] - proj2[0], proj2[1] - proj1[0]);
    }
}
