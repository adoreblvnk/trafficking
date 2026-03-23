package com.sit.recyclingpinball;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.sit.recyclingpinball.engine.platform.libgdx.LibGdxContext;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.engine.managers.AssetManager;

import com.sit.recyclingpinball.logic.LogicConstants;

public class Main extends Game {
    private SceneManager sceneManager;
    private LibGdxContext context;
    private AssetManager assetManager;

    @Override
    public void create() {
        // ARCHITECTURE JUSTIFICATION: Composition Root Pattern
        // Main.java serves as the "Composition Root" for the entire application.
        // It is the only location where concrete implementations (LibGdxContext)
        // are instantiated and wired to their respective managers. This keeps
        // the core game engine and logic layers completely decoupled from
        // platform-specific bootstrapping.
        assetManager = new AssetManager();
        context = new LibGdxContext(assetManager);

        // PERFORMANCE OPTIMIZATION: Asset Preloading & Flyweight Pattern
        // We initialize the AssetManager and explicitly load all resources upfront.
        // This ensures the Flyweight pattern is strictly enforced—multiple game
        // objects (e.g., trash, pinball) share single memory references for their
        // textures and sounds. Preloading also eliminates disk I/O "hitching"
        // during the gameplay loop, ensuring a consistent 60FPS render cycle.
        assetManager.initialize(context.getAudio(), context.getGraphics());

        // Load Sounds
        String[] sounds = {LogicConstants.SOUND_CLICK, LogicConstants.SOUND_COLLECT, LogicConstants.SOUND_LOSE,
                LogicConstants.SOUND_WIN, LogicConstants.SOUND_BOUNCE, LogicConstants.SOUND_FLIP,
                LogicConstants.SOUND_STRETCH};
        for (String sound : sounds) {
            assetManager.loadSound(sound);
        }

        // Load Fonts
        assetManager.loadFont(LogicConstants.FONT_GEIST_BOLD, LogicConstants.FONT_SIZE_SMALL);

        // Load Textures
        String[] textures = {LogicConstants.TEX_DIRTY_BEACH, LogicConstants.TEX_BUTTON_RECT_DEPTH_FLAT,
                LogicConstants.TEX_BEACH_BACKGROUND, LogicConstants.TEX_UI_PANEL_BG, LogicConstants.TEX_STAR,
                LogicConstants.TEX_PINBALL_DEFAULT, LogicConstants.TEX_SLIDE_VERTICAL_GREY,
                LogicConstants.TEX_BALL_BLUE_LARGE, LogicConstants.TEX_FLIPPER, LogicConstants.TEX_TRASH_PLASTIC,
                LogicConstants.TEX_TRASH_PAPER, LogicConstants.TEX_TRASH_GLASS};
        for (String texture : textures) {
            assetManager.loadTexture(texture);
        }

        sceneManager = new SceneManager(context);
        com.sit.recyclingpinball.logic.factories.AssemblyFactory assemblyFactory = new com.sit.recyclingpinball.logic.factories.AssemblyFactory(
                context, sceneManager);
        sceneManager.setScene(assemblyFactory.createMenuScene());
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
        if (assetManager != null) {
            assetManager.dispose();
        }
        if (context != null) {
            context.dispose();
        }
    }
}
