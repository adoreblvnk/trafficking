package com.sit.trafficking;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.sit.trafficking.engine.managers.IOManager;
import com.sit.trafficking.engine.managers.SoundManager;
import com.sit.trafficking.engine.managers.TimeManager;
import com.sit.trafficking.engine.scenes.SceneManager;
import com.sit.trafficking.logic.factories.SceneFactory;

/**
 * GameMaster (Main)
 * Acts as the entry point and orchestrator.
 */
public class Main extends Game {

    private SceneManager sceneManager;

    @Override
    public void create() {
        SoundManager soundManager = new SoundManager();
        IOManager ioManager = new IOManager();
        TimeManager timeManager = new TimeManager();
        sceneManager = new SceneManager(soundManager, ioManager, timeManager);
        
        SceneFactory sceneFactory = new SceneFactory(sceneManager);

        // Start with Menu
        sceneManager.pushOverlay(sceneFactory.createMenuScene());
    }

    @Override
    public void render() {
        sceneManager.render(Gdx.graphics.getDeltaTime());
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
