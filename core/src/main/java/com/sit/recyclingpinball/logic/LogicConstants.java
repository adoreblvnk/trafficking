package com.sit.recyclingpinball.logic;

public class LogicConstants {

    // Textures
    public static final String TEX_DIRTY_BEACH = "dirty_beach";
    public static final String TEX_BUTTON_RECT_DEPTH_FLAT = "button_rectangle_depth_flat";
    public static final String TEX_BEACH_BACKGROUND = "beach_background";
    public static final String TEX_UI_PANEL_BG = "ui_panel_bg";
    public static final String TEX_STAR = "star";
    public static final String TEX_PINBALL_DEFAULT = "pinball_default";
    public static final String TEX_SLIDE_VERTICAL_GREY = "slide_vertical_grey";
    public static final String TEX_BALL_BLUE_LARGE = "ball_blue_large";
    public static final String TEX_FLIPPER = "flipper";

    // Sounds
    public static final String SOUND_CLICK = "click";
    public static final String SOUND_WIN = "win";
    public static final String SOUND_LOSE = "lose";
    public static final String SOUND_STRETCH = "stretch";
    public static final String SOUND_COLLECT = "collect";
    public static final String SOUND_FLIP = "flip";

    // Fonts
    public static final String FONT_GEIST_BOLD = "Geist-Bold";

    // Tags
    public static final String TAG_PINBALL = "pinball";
    public static final String TAG_TRASH = "trash";
    public static final String TAG_SHOOTER = "shooter";
    public static final String TAG_FLIPPER = "flipper";

    // Pinball Constants
    public static final float PINBALL_SIZE = 48f;
    public static final float PINBALL_RADIUS = 24f;
    public static final float PINBALL_FRICTION = 0.999f;

    // Shooter Rod Constants
    public static final float SHOOTER_WIDTH = 64f;
    public static final float SHOOTER_HEIGHT = 160f;
    public static final float SHOOTER_MAX_PULL = 100f;
    public static final float SHOOTER_KEY_PULL_SPEED = 150f;
    public static final float SHOOTER_LAUNCH_MULTIPLIER = 20f;
    public static final float SHOOTER_SHAFT_WIDTH = 16f;
    public static final float SHOOTER_SHAFT_HEIGHT = 96f;
    public static final float SHOOTER_KNOB_SIZE = 64f;
    public static final float SHOOTER_SHAFT_OFFSET_X = 24f;
    public static final float SHOOTER_KNOB_OFFSET_Y = 96f;

    // Flipper Constants
    public static final float FLIPPER_WIDTH = 180f;
    public static final float FLIPPER_HEIGHT = 40f;
    public static final float FLIPPER_PIVOT_X = 20f;
    public static final float FLIPPER_PIVOT_Y = 20f;
    public static final float FLIPPER_ROT_VELOCITY = 512f;
    public static final float FLIPPER_BOOST_Y = 1.5f;
    public static final float FLIPPER_BOOST_X = 0.5f;
    
    public static final float FLIPPER_LEFT_START_ANGLE = -10f;
    public static final float FLIPPER_LEFT_MAX_ANGLE = 45f;
    public static final float FLIPPER_RIGHT_START_ANGLE = 190f;
    public static final float FLIPPER_RIGHT_MAX_ANGLE = 135f;

    // Trash Constants
    public static final float TRASH_SIZE = 64f;
    public static final float TRASH_RADIUS = 32f;

    // UI Coordinates and Dimensions
    public static final int SCENE_WIDTH = 1900;
    public static final int SCENE_HEIGHT = 1000;
    
    public static final float UI_CENTER_X = 950f;
    public static final float UI_BTN_WIDTH_DEFAULT = 480f;
    public static final float UI_BTN_WIDTH_SMALL = 384f;
    public static final float UI_BTN_HEIGHT_DEFAULT = 64f;
    public static final float UI_BTN_HEIGHT_LARGE = 80f;

    // UI Colors
    public static final float COLOR_BG_R = 0.1f;
    public static final float COLOR_BG_G = 0.1f;
    public static final float COLOR_BG_B = 0.1f;
    public static final float COLOR_SIM_BG_R = 0.8f;
    public static final float COLOR_SIM_BG_G = 0.9f;
    public static final float COLOR_SIM_BG_B = 1.0f;
    
    public static final float COLOR_DIM_R = 0f;
    public static final float COLOR_DIM_G = 0f;
    public static final float COLOR_DIM_B = 0f;
    public static final float COLOR_DIM_PAUSED_A = 0.6f;
    public static final float COLOR_DIM_OVERLAY_A = 0.7f;
    
    public static final float COLOR_TEXT_DARK_R = 0.2f;
    public static final float COLOR_TEXT_DARK_G = 0.15f;
    public static final float COLOR_TEXT_DARK_B = 0.1f;
    public static final float COLOR_TEXT_LIGHT_R = 1f;
    public static final float COLOR_TEXT_LIGHT_G = 1f;
    public static final float COLOR_TEXT_LIGHT_B = 1f;
    public static final float COLOR_TEXT_A = 1f;

    // Entity IDs and Prefixes
    public static final String ID_WALL_PREFIX = "wall_";
    public static final String ID_FLIPPER_L_PREFIX = "flipper_l_";
    public static final String ID_FLIPPER_R_PREFIX = "flipper_r_";
    public static final String ID_SHOOTER_ROD_PREFIX = "shooter_rod_";
    public static final String ID_TRASH_PREFIX = "trash_";
    public static final String ID_TRASH_PLASTIC = "trash_plastic";
    public static final String ID_TRASH_PAPER = "trash_paper";
    public static final String ID_TRASH_GLASS = "trash_glass";

    public static final float PINBALL_START_X = 1810f;
    public static final float PINBALL_START_Y = 400f;
    
    public static final String TEXT_YOU_WIN = "YOU WIN!";
    public static final String TEXT_GAME_OVER = "GAME OVER!";
    public static final String TEXT_RECYCLING_PINBALL = "Recycling Pinball";
    public static final String TEXT_START_GAME = "Start Game";
    public static final String TEXT_QUIT = "Quit";
    public static final String TEXT_LEVEL_SELECT = "Level Select";
    public static final String TEXT_PAUSED = "PAUSED";
    public static final String TEXT_RESUME = "Resume";
    public static final String TEXT_MAIN_MENU = "Main Menu";
    public static final String TEXT_RETRY = "Retry";
    public static final String TEXT_BACK = "Back";

    public static final String TEXT_LEVEL_1 = "Level 1";
    public static final String TEXT_LEVEL_2 = "Level 2";
    public static final String TEXT_LEVEL_3 = "Level 3";
    public static final String TEXT_LEVEL_4 = "Level 4";
    public static final String TEXT_LEVEL_5 = "Level 5";

    public static final String TEXT_SCORE_PREFIX = "Score: ";
    public static final String TEXT_BALLS_PREFIX = "Balls: ";
    public static final String TEXT_TRASH_COLLECTED_PREFIX = "Collected ";
    public static final String TEXT_TRASH_COLLECTED_SUFFIX = " trash";
    public static final String TEXT_TRASH_DIVIDER = " / ";

    // Common Volume
    public static final float VOLUME_DEFAULT = 1.0f;
    
    private LogicConstants() {}
}
