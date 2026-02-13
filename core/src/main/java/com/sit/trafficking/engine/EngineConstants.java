package com.sit.trafficking.engine;

/**
 * Constants strictly for the Abstract Engine.
 * Defines physics defaults and unit scales.
 */
public class EngineConstants {
    
    /** Pixels Per Meter - 1:1 ratio for simplicity in this abstract engine. */
    public static final float PPM = 1.0f;

    // Physics Defaults
    public static final float DEFAULT_BOUNCE = 0.8f;
    public static final float DEFAULT_FRICTION = 0.98f;
    public static final float PUSH_OUT_FACTOR = 0.5f;

    // Entity Types (for serialization)
    public static final String ENTITY_TYPE_STATIC = "STATIC";
    public static final String ENTITY_TYPE_DYNAMIC = "DYNAMIC";

    // Assets
    public static final String DEFAULT_FONT_PATH = "fonts/Geist-Bold.ttf";
}
