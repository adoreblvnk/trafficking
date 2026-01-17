package com.sit.trafficking.utils;

/**
 * Global constants for the application.
 */
public final class Constants {
    // Pixel-Per-Meter conversion
    public static final float PPM = 100f;

    // Box2D Collision Bits
    public static final short BIT_WALL = 2;
    public static final short BIT_PLAYER = 4;

    // Private constructor to prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
