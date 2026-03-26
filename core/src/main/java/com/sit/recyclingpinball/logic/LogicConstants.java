package com.sit.recyclingpinball.logic;

public class LogicConstants {
    private LogicConstants() {
    }

    // GAME CONFIGURATION
    public static final int STARTING_BALLS = 3;

    // PATHS & DIRECTORIES
    public static final String DIR_LEVELS = "levels";
    public static final String PATH_BASE_LEVEL = "levels/base.json";

    // TAGS & IDs
    public static final String TAG_PINBALL = "pinball";
    public static final String TAG_TRASH = "trash";
    public static final String TAG_SHOOTER = "shooter";
    public static final String TAG_FLIPPER = "flipper";

    public static final String ID_WALL_PREFIX = "wall_";
    public static final String ID_FLIPPER_L_PREFIX = "flipper_l_";
    public static final String ID_FLIPPER_R_PREFIX = "flipper_r_";
    public static final String ID_SHOOTER_ROD_PREFIX = "shooter_rod_";
    public static final String ID_TRASH_PREFIX = "trash_";

    // ASSETS (Textures)
    public static final String TEX_DIRTY_BEACH = "textures/dirty_beach.png";
    public static final String TEX_BUTTON_RECT_DEPTH_FLAT = "textures/button_rectangle_depth_flat.png";
    public static final String TEX_BEACH_BACKGROUND = "textures/beach_background.png";
    public static final String TEX_UI_PANEL_BG = "textures/ui_panel_bg.png";
    public static final String TEX_STAR = "textures/star.png";
    public static final String TEX_PINBALL_DEFAULT = "textures/pinball_default.png";
    public static final String TEX_SLIDE_VERTICAL_GREY = "textures/slide_vertical_grey.png";
    public static final String TEX_BALL_BLUE_LARGE = "textures/ball_blue_large.png";
    public static final String TEX_FLIPPER = "textures/flipper.png";
    public static final String TEX_TRASH_PLASTIC = "textures/trash_plastic.png";
    public static final String TEX_TRASH_PAPER = "textures/trash_paper.png";
    public static final String TEX_TRASH_GLASS = "textures/trash_glass.png";

    public static final String[] TEXTURE_ASSETS = {TEX_DIRTY_BEACH, TEX_BUTTON_RECT_DEPTH_FLAT, TEX_BEACH_BACKGROUND,
            TEX_UI_PANEL_BG, TEX_STAR, TEX_PINBALL_DEFAULT, TEX_SLIDE_VERTICAL_GREY, TEX_BALL_BLUE_LARGE, TEX_FLIPPER,
            TEX_TRASH_PLASTIC, TEX_TRASH_PAPER, TEX_TRASH_GLASS};

    // ASSETS (Sounds)
    public static final String SOUND_CLICK = "sounds/click.mp3";
    public static final String SOUND_WIN = "sounds/win.mp3";
    public static final String SOUND_LOSE = "sounds/lose.mp3";
    public static final String SOUND_STRETCH = "sounds/stretch.mp3";
    public static final String SOUND_COLLECT = "sounds/collect.mp3";

    public static final String[] SOUND_ASSETS = {SOUND_CLICK, SOUND_COLLECT, SOUND_LOSE, SOUND_WIN, SOUND_STRETCH};
    public static final float VOLUME_DEFAULT = 1.0f;

    // ASSETS (Fonts)
    public static final String FONT_GEIST_BOLD = "fonts/Geist-Bold.ttf";
    public static final int FONT_SIZE_SMALL = 24;

    // ENTITIES (Pinball)
    public static final float PINBALL_SIZE = 48f;
    public static final float PINBALL_FRICTION = 0.999f;
    public static final float[] PINBALL_START = {1810f, 400f};

    // ENTITIES (Shooter Rod)
    public static final float[] SHOOTER_SIZE = {64f, 160f};
    public static final float SHOOTER_MAX_PULL = 100f;
    public static final float SHOOTER_KEY_PULL_SPEED = 150f;
    public static final float SHOOTER_LAUNCH_MULTIPLIER = 20f;
    public static final float[] SHOOTER_SHAFT_SIZE = {16f, 96f};
    public static final float SHOOTER_KNOB_SIZE = 64f;
    public static final float[] SHOOTER_SHAFT_OFFSET = {24f, 0f};
    public static final float[] SHOOTER_KNOB_OFFSET = {0f, 96f};

    // ENTITIES (Flipper)
    public static final float[] FLIPPER_SIZE = {180f, 40f};
    public static final float[] FLIPPER_PIVOT = {20f, 20f};
    public static final float FLIPPER_ROT_VELOCITY = 512f;
    public static final float[] FLIPPER_BOOST = {0.5f, 1.5f};

    public static final float FLIPPER_LEFT_START_ANGLE = -10f;
    public static final float FLIPPER_LEFT_MAX_ANGLE = 45f;
    public static final float FLIPPER_RIGHT_START_ANGLE = 190f;
    public static final float FLIPPER_RIGHT_MAX_ANGLE = 135f;

    // ENTITIES (Trash)
    public static final float TRASH_SIZE = 64f;

    // UI LAYOUT (Coords & Dims)
    public static final int[] SCENE_SIZE = {1900, 1000};

    public static final float UI_BTN_WIDTH_DEFAULT = 480f;
    public static final float UI_BTN_WIDTH_SMALL = 384f;
    public static final float UI_BTN_HEIGHT_DEFAULT = 64f;
    public static final float UI_BTN_HEIGHT_LARGE = 80f;

    public static final float[] UI_SCORE_POS = {50f, 900f};
    public static final float[] UI_BALLS_POS = {50f, 850f};
    public static final float[] UI_DESC_POS = {50f, 800f};
    public static final float UI_DESC_WIDTH = 300f;

    public static final float[] UI_STAR_START = {60f, 200f};
    public static final float UI_STAR_SPACING = 70f;
    public static final float[] UI_RESULT_STARS_POS = {950f, 480f};
    public static final float[] UI_RESULT_SCORE_POS = {850f, 470f};

    public static final float[] UI_LEVEL_SELECT_TITLE_POS = {950f, 670f};
    public static final float[] UI_LEVEL_SELECT_BTN_START_POS = {950f, 570f};
    public static final float UI_LEVEL_SELECT_BTN_SPACING = 90f;

    public static final float[] UI_MENU_TITLE_POS = {950f, 560f};
    public static final float[] UI_MENU_START_BTN_POS = {950f, 460f};
    public static final float[] UI_MENU_QUIT_BTN_POS = {950f, 380f};

    public static final float[] UI_RESULT_TITLE_POS = {950f, 600f};
    public static final float[] UI_RESULT_MENU_BTN_POS = {950f, 370f};
    public static final float[] UI_RESULT_RETRY_BTN_POS = {950f, 280f};

    public static final float[] UI_PAUSE_TITLE_POS = {950f, 580f};
    public static final float[] UI_PAUSE_RESUME_BTN_POS = {950f, 490f};
    public static final float[] UI_PAUSE_MENU_BTN_POS = {950f, 410f};

    // UI COLORS
    public static final float[] COLOR_BG = {0.1f, 0.1f, 0.1f};
    public static final float[] COLOR_SIM_BG = {0.8f, 0.9f, 1.0f};
    public static final float[] COLOR_DIM = {0f, 0f, 0f};
    public static final float COLOR_DIM_PAUSED_A = 0.6f;
    public static final float COLOR_DIM_OVERLAY_A = 0.7f;

    public static final float[] COLOR_TEXT_DARK = {0.2f, 0.15f, 0.1f, 1f};
    public static final float[] COLOR_TEXT_LIGHT = {1f, 1f, 1f, 1f};

    // UI STRINGS
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
    public static final String TEXT_TRASH_COLLECTED_PREFIX = "Collected: ";
    public static final String TEXT_TRASH_DIVIDER = " / ";
}
