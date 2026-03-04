package com.sit.covid26.engine;

//constants are strictly for the Abstract Engine alone
//defines both physics defaults and unit scales
public class EngineConstants {

    //pixels per meter - 1:1 ratio
    public static final float PPM = 1.0f;

    //physics Defaults
    public static final float DEFAULT_BOUNCE = 0.8f;
    public static final float DEFAULT_FRICTION = 0.98f;
    public static final float PUSH_OUT_FACTOR = 0.5f;

    //entity types for serialisation
    public static final String ENTITY_TYPE_STATIC = "STATIC";
    public static final String ENTITY_TYPE_DYNAMIC = "DYNAMIC";

    //quadtree spatial partitioning
    public static final int QUADTREE_MAX_OBJECTS = 10;
    public static final int QUADTREE_MAX_LEVELS = 5;

    //assets
    public static final String DEFAULT_FONT_PATH = "fonts/Geist-Bold.ttf";
}
