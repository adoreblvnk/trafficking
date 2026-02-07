package com.sit.trafficking.logic;

import com.sit.trafficking.engine.EngineConstants;

/**
 * Constants specific to the "Trafficking" Application Logic.
 * Defines screen settings, gameplay tuning, and visual assets.
 */
public class LogicConstants {

    // System / Window
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    // Gameplay Physics
    public static final float MAX_VELOCITY = 2000f;
    public static final float SLINGSHOT_MULTIPLIER = 5.0f;
    public static final float VEHICLE_FRICTION = EngineConstants.DEFAULT_FRICTION;

    // Audio
    public static final float DEFAULT_VOLUME = 0.5f;
    public static final float AUDIO_THRESHOLD = 10000f;
    public static final float CRASH_SOUND_THRESHOLD = AUDIO_THRESHOLD;

    // UI & Visuals
    public static final float OVERLAY_ALPHA = 0.5f;
    public static final int FONT_SIZE_MENU = 32;
    public static final float VEHICLE_SIZE = 40f;
    public static final float NUDGE_OFFSET = 60f;
    
    // Menu Layout
    public static final float MENU_TITLE_X = 100f;
    public static final float MENU_TITLE_Y = 400f;
    public static final float MENU_SUBTITLE_Y = 350f;
}
