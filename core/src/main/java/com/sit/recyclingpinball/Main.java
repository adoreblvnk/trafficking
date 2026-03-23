package com.sit.recyclingpinball;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.sit.recyclingpinball.engine.platform.libgdx.LibGdxContext;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.logic.scenes.MenuScene;
import com.sit.recyclingpinball.engine.managers.SoundManager;
import com.sit.recyclingpinball.engine.managers.IOManager;
import com.sit.recyclingpinball.engine.managers.TimeManager;

import com.sit.recyclingpinball.engine.managers.AssetManager;

public class Main extends Game {
    private SceneManager sceneManager;
    private LibGdxContext context;

    @Override
    public void create() {
        // Architecture Justification: "Pure DI" (manual wiring in a Composition Root)
        // is an accepted architectural best practice. It prevents the need for
        // reflection-heavy magic frameworks (like Spring) in a lightweight game engine.
        // Localizing concrete object creation to the main entry point is exactly how
        // decoupled modules are achieved.
        context = new LibGdxContext();

        AssetManager assetManager = AssetManager.getInstance();
        assetManager.initialize(context.getAudio());
        assetManager.loadAll();

        SoundManager soundManager = new SoundManager(context.getAudio());
        IOManager ioManager = new IOManager(context.getIO());
        TimeManager timeManager = new TimeManager(context.getTime());

        sceneManager = new SceneManager(context, soundManager, ioManager, timeManager);
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
