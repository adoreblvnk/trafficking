package com.sit.recyclingpinball;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.sit.recyclingpinball.engine.platform.libgdx.LibGdxContext;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.logic.scenes.MenuScene;
import com.sit.recyclingpinball.engine.managers.AssetManager;

import com.sit.recyclingpinball.logic.LogicConstants;

public class Main extends Game {
    private SceneManager sceneManager;
    private LibGdxContext context;

    @Override
    public void create() {
        // ARCHITECTURE JUSTIFICATION: Composition Root Pattern
        // Main.java serves as the "Composition Root" for the entire application.
        // It is the only location where concrete implementations (LibGdxContext)
        // are instantiated and wired to their respective managers. This keeps
        // the core game engine and logic layers completely decoupled from
        // platform-specific bootstrapping.
        context = new LibGdxContext();

        // PERFORMANCE OPTIMIZATION: Asset Preloading & Flyweight Pattern
        // We initialize the AssetManager and explicitly load all resources upfront.
        // This ensures the Flyweight pattern is strictly enforced—multiple game
        // objects (e.g., trash, pinball) share single memory references for their
        // textures and sounds. Preloading also eliminates disk I/O "hitching"
        // during the gameplay loop, ensuring a consistent 60FPS render cycle.
        AssetManager assetManager = AssetManager.getInstance();
        assetManager.initialize(context.getAudio(), context.getGraphics());

        // Load Sounds
        assetManager.loadSound(LogicConstants.SOUNDS_DIR + LogicConstants.SOUND_CLICK + LogicConstants.SOUND_EXTENSION,
                LogicConstants.SOUND_CLICK);
        assetManager.loadSound(
                LogicConstants.SOUNDS_DIR + LogicConstants.SOUND_COLLECT + LogicConstants.SOUND_EXTENSION,
                LogicConstants.SOUND_COLLECT);
        assetManager.loadSound(LogicConstants.SOUNDS_DIR + LogicConstants.SOUND_LOSE + LogicConstants.SOUND_EXTENSION,
                LogicConstants.SOUND_LOSE);
        assetManager.loadSound(LogicConstants.SOUNDS_DIR + LogicConstants.SOUND_WIN + LogicConstants.SOUND_EXTENSION,
                LogicConstants.SOUND_WIN);
        assetManager.loadSound(LogicConstants.SOUNDS_DIR + LogicConstants.SOUND_BOUNCE + LogicConstants.SOUND_EXTENSION,
                LogicConstants.SOUND_BOUNCE);
        assetManager.loadSound(LogicConstants.SOUNDS_DIR + LogicConstants.SOUND_FLIP + LogicConstants.SOUND_EXTENSION,
                LogicConstants.SOUND_FLIP);
        assetManager.loadSound(
                LogicConstants.SOUNDS_DIR + LogicConstants.SOUND_STRETCH + LogicConstants.SOUND_EXTENSION,
                LogicConstants.SOUND_STRETCH);

        // Load Fonts
        assetManager.loadFont(LogicConstants.FONT_GEIST_BOLD, 24, LogicConstants.FONT_GEIST_BOLD);
        assetManager.loadFont(LogicConstants.FONT_GEIST_BOLD, 32, "font_32");
        assetManager.loadFont(LogicConstants.FONT_GEIST_BOLD, 48, "font_48");
        assetManager.loadFont(LogicConstants.FONT_GEIST_BOLD, 72, "font_72");

        // Load Textures
        assetManager.loadTexture(LogicConstants.TEX_DIRTY_BEACH, LogicConstants.TEX_DIRTY_BEACH);
        assetManager.loadTexture(LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT, LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT);
        assetManager.loadTexture(LogicConstants.TEX_BEACH_BACKGROUND, LogicConstants.TEX_BEACH_BACKGROUND);
        assetManager.loadTexture(LogicConstants.TEX_UI_PANEL_BG, LogicConstants.TEX_UI_PANEL_BG);
        assetManager.loadTexture(LogicConstants.TEX_STAR, LogicConstants.TEX_STAR);
        assetManager.loadTexture(LogicConstants.TEX_PINBALL_DEFAULT, LogicConstants.TEX_PINBALL_DEFAULT);
        assetManager.loadTexture(LogicConstants.TEX_SLIDE_VERTICAL_GREY, LogicConstants.TEX_SLIDE_VERTICAL_GREY);
        assetManager.loadTexture(LogicConstants.TEX_BALL_BLUE_LARGE, LogicConstants.TEX_BALL_BLUE_LARGE);
        assetManager.loadTexture(LogicConstants.TEX_FLIPPER, LogicConstants.TEX_FLIPPER);
        assetManager.loadTexture(LogicConstants.ID_TRASH_PLASTIC, LogicConstants.ID_TRASH_PLASTIC);
        assetManager.loadTexture(LogicConstants.ID_TRASH_PAPER, LogicConstants.ID_TRASH_PAPER);
        assetManager.loadTexture(LogicConstants.ID_TRASH_GLASS, LogicConstants.ID_TRASH_GLASS);

        sceneManager = new SceneManager(context);
        sceneManager.setScene(new MenuScene(context, sceneManager));
    }

    @Override
    public void render() {
        super.render();
        if (sceneManager != null) {
            sceneManager.render(Gdx.graphics.getDeltaTime());
            // Ensure any open batches from the final scene are flushed!
            context.getGraphics().end();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (sceneManager != null) {
            sceneManager.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (sceneManager != null) {
            sceneManager.dispose();
        }
    }
}
