package com.sit.covid26;

import com.badlogic.gdx.Game;
import com.sit.covid26.engine.managers.IOManager;
import com.sit.covid26.engine.managers.SoundManager;
import com.sit.covid26.engine.managers.TimeManager;
import com.sit.covid26.engine.platform.libgdx.LibGdxContext;
import com.sit.covid26.engine.scenes.SceneManager;
import com.sit.covid26.logic.factories.SceneFactory;

/**
 * GameMaster (Main)
 * Acts as the entry point and orchestrator.
 * Instantiates LibGdxContext as the ONLY place where libGDX is directly used for system-level setup.
 */
public class Main extends Game {

    private SceneManager sceneManager;
    private LibGdxContext engineContext;

    @Override
    public void create() {
        // Create the platform context - this is the ONLY place LibGdxContext is instantiated
        engineContext = new LibGdxContext();

        // Create game-level managers that wrap the platform providers
        SoundManager soundManager = new SoundManager(engineContext.getAudio());
        IOManager ioManager = new IOManager(engineContext.getIO());
        TimeManager timeManager = new TimeManager(engineContext.getTime());

        // Create the scene manager with the context
        sceneManager = new SceneManager(engineContext, soundManager, ioManager, timeManager);

        // Create the scene factory with the context for scene dependency injection
        SceneFactory sceneFactory = new SceneFactory(sceneManager, engineContext);

        // Start with Menu
        sceneManager.pushOverlay(sceneFactory.createMenuScene());
    }

    @Override
    public void render() {
        sceneManager.render(engineContext.getTime().getDeltaTime());
    }

    @Override
    public void dispose() {
        sceneManager.dispose();
    }

    @Override
    public void resize(int width, int height) {
        sceneManager.resize(width, height);
    }
}
