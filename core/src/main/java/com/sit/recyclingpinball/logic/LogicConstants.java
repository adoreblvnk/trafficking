package com.sit.recyclingpinball.logic;

public class LogicConstants {

    public static final String DIR_LEVELS = "levels";
    public static final String PATH_BASE_LEVEL = "levels/base.json";

    // Textures
    public static final String TEX_DIRTY_BEACH = "textures/dirty_beach.png";
    public static final String TEX_BUTTON_RECT_DEPTH_FLAT = "textures/button_rectangle_depth_flat.png";
    public static final String TEX_BEACH_BACKGROUND = "textures/beach_background.png";
    public static final String TEX_UI_PANEL_BG = "textures/ui_panel_bg.png";
    public static final String TEX_STAR = "textures/star.png";
    public static final String TEX_PINBALL_DEFAULT = "textures/pinball_default.png";
    public static final String TEX_SLIDE_VERTICAL_GREY = "textures/slide_vertical_grey.png";
    public static final String TEX_BALL_BLUE_LARGE = "textures/ball_blue_large.png";
    public static final String TEX_FLIPPER = "textures/flipper.png";

    // Sounds
    public static final String SOUND_CLICK = "sounds/click.mp3";
    public static final String SOUND_WIN = "sounds/win.mp3";
    public static final String SOUND_LOSE = "sounds/lose.mp3";
    public static final String SOUND_STRETCH = "sounds/stretch.mp3";
    public static final String SOUND_COLLECT = "sounds/collect.mp3";
    public static final String SOUND_FLIP = "sounds/flip.mp3";
    public static final String SOUND_BOUNCE = "sounds/bounce.mp3";

    // Fonts
    public static final String FONT_GEIST_BOLD = "fonts/Geist-Bold.ttf";
    public static final int FONT_SIZE_SMALL = 24;

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

    public static final float UI_GAME_TEXT_X = 50f;
    public static final float UI_SCORE_Y = 900f;
    public static final float UI_BALLS_Y = 850f;
    public static final float UI_DESC_Y = 800f;
    public static final float UI_DESC_WIDTH = 300f;

    public static final float UI_STAR_START_X = 60f;
    public static final float UI_STAR_START_Y = 200f;
    public static final float UI_STAR_SPACING = 70f;
    public static final float UI_RESULT_STARS_Y = 480f;
    public static final float UI_RESULT_SCORE_Y = 470f;
    public static final float UI_RESULT_SCORE_X = 850f;

    public static final float UI_LEVEL_SELECT_TITLE_Y = 670f;
    public static final float UI_LEVEL_SELECT_BTN_START_Y = 570f;
    public static final float UI_LEVEL_SELECT_BTN_SPACING = 90f;

    public static final float UI_MENU_TITLE_Y = 560f;
    public static final float UI_MENU_START_BTN_Y = 460f;
    public static final float UI_MENU_QUIT_BTN_Y = 380f;

    public static final float UI_RESULT_TITLE_Y = 600f;
    public static final float UI_RESULT_MENU_BTN_Y = 370f;
    public static final float UI_RESULT_RETRY_BTN_Y = 280f;

    public static final float UI_PAUSE_TITLE_Y = 580f;
    public static final float UI_PAUSE_RESUME_BTN_Y = 490f;
    public static final float UI_PAUSE_MENU_BTN_Y = 410f;

    // UI Colors (R, G, B) or (R, G, B, A)
    public static final float[] COLOR_BG = {0.1f, 0.1f, 0.1f};
    public static final float[] COLOR_SIM_BG = {0.8f, 0.9f, 1.0f};
    public static final float[] COLOR_DIM = {0f, 0f, 0f};
    public static final float COLOR_DIM_PAUSED_A = 0.6f;
    public static final float COLOR_DIM_OVERLAY_A = 0.7f;

    public static final float[] COLOR_TEXT_DARK = {0.2f, 0.15f, 0.1f, 1f};
    public static final float[] COLOR_TEXT_LIGHT = {1f, 1f, 1f, 1f};

    // Entity IDs and Prefixes
    public static final String ID_WALL_PREFIX = "wall_";
    public static final String ID_FLIPPER_L_PREFIX = "flipper_l_";
    public static final String ID_FLIPPER_R_PREFIX = "flipper_r_";
    public static final String ID_SHOOTER_ROD_PREFIX = "shooter_rod_";
    public static final String ID_TRASH_PREFIX = "trash_";
    public static final String TEX_TRASH_PLASTIC = "textures/trash_plastic.png";
    public static final String TEX_TRASH_PAPER = "textures/trash_paper.png";
    public static final String TEX_TRASH_GLASS = "textures/trash_glass.png";

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

    public static final String TEXT_SCORE_PREFIX = "Score: ";
    public static final String TEXT_BALLS_PREFIX = "Balls: ";
    public static final String TEXT_TRASH_COLLECTED_PREFIX = "Collected ";
    public static final String TEXT_TRASH_COLLECTED_SUFFIX = " trash";
    public static final String TEXT_TRASH_DIVIDER = " / ";

    public static final int STARTING_BALLS = 3;
    public static final float VOLUME_DEFAULT = 1.0f;

    private LogicConstants() {
    }
}
