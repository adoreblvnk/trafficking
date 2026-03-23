package com.sit.recyclingpinball.engine;

//constants are strictly for the Abstract Engine alone
//defines both physics defaults and unit scales
public final class EngineConstants {

    private EngineConstants() {
    }

    // physics Defaults
    public static final float DEFAULT_BOUNCE = 0.8f;
    public static final float DEFAULT_FRICTION = 0.98f;
    public static final float PUSH_OUT_FACTOR = 0.5f;

    // quadtree spatial partitioning
    public static final int QUADTREE_MAX_OBJECTS = 10;
    public static final int QUADTREE_MAX_LEVELS = 5;
}
