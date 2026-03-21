package com.sit.recyclingpinball.engine;

//constants are strictly for the Abstract Engine alone
//defines both physics defaults and unit scales
public class EngineConstants {

    // pixels per meter - 1:1 ratio
    public static final float PPM = 1.0f;

    // physics Defaults
    public static final float DEFAULT_BOUNCE = 0.8f;
    public static final float DEFAULT_FRICTION = 0.98f;
    public static final float PUSH_OUT_FACTOR = 0.5f;

    // entity types for serialisation
    public static final String ENTITY_TYPE_STATIC = "STATIC";
    public static final String ENTITY_TYPE_DYNAMIC = "DYNAMIC";

    // quadtree spatial partitioning
    public static final int QUADTREE_MAX_OBJECTS = 10;
    public static final int QUADTREE_MAX_LEVELS = 5;

    // assets
    public static final String DEFAULT_FONT_PATH = "fonts/Geist-Bold.ttf";
    public static final String FONTS_DIR = "fonts/";
    public static final String TEXTURES_DIR = "textures/";
    public static final String FONT_EXTENSION = ".ttf";
    public static final String TEXTURE_EXTENSION = ".png";
    public static final String SOUNDS_DIR = "sounds/";
    public static final String SOUND_EXTENSION = ".mp3";

    // sound IDs
    public static final String SOUND_CLICK = "click";
    public static final String SOUND_COLLECT = "collect";
    public static final String SOUND_LOSE = "lose";
    public static final String SOUND_WIN = "win";
    public static final String SOUND_BOUNCE = "bounce";
    public static final String SOUND_FLIP = "flip";
    public static final String SOUND_STRETCH = "stretch";

    // physics configuration
    public static final float COLLISION_EPSILON = 0.5f;
}
