package com.sit.recyclingpinball;

import com.badlogic.gdx.Game;
import com.sit.recyclingpinball.engine.platform.libgdx.PlatformContext;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.engine.managers.AssetManager;

import com.sit.recyclingpinball.logic.LogicConstants;
import com.sit.recyclingpinball.logic.factories.SceneFactory;
import com.sit.recyclingpinball.logic.factories.StateFactory;

public class Main extends Game {
    private SceneManager sceneManager;
    private PlatformContext context;
    private AssetManager assetManager;

    @Override
    public void create() {
        // Wire platform dependencies at the root so the core engine remains framework-agnostic.
        context = new PlatformContext();
        assetManager = new AssetManager(context.getAudio(), context.getAssets());
        // Preload all assets upfront to enforce the Flyweight pattern and prevent gameplay hitching.
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
        StateFactory stateFactory = new StateFactory();
        SceneFactory sceneFactory = new SceneFactory(sceneManager, stateFactory);
        sceneManager.setScene(sceneFactory.createMenuScene());
    }

    @Override
    public void render() {
        super.render();
        if (sceneManager != null) {
            sceneManager.render(context.getTime().getDeltaTime());
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
