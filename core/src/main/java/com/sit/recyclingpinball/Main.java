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
        for (String sound : LogicConstants.SOUND_ASSETS) {
            assetManager.loadSound(sound);
        }

        // Load Fonts
        assetManager.loadFont(LogicConstants.FONT_GEIST_BOLD, LogicConstants.FONT_SIZE_SMALL);

        // Load Textures
        for (String texture : LogicConstants.TEXTURE_ASSETS) {
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
