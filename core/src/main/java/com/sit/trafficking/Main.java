package com.sit.trafficking;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.sit.trafficking.engine.scenes.SceneManager;
import com.sit.trafficking.logic.scenes.MenuScene;

/**
 * GameMaster (Main)
 * Acts as the entry point and orchestrator.
 */
public class Main extends Game {

    @Override
    public void create() {
        // Start with Menu
        SceneManager.getInstance().pushOverlay(new MenuScene());
    }

    @Override
    public void render() {
        SceneManager.getInstance().render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        SceneManager.getInstance().dispose();
    }
}
