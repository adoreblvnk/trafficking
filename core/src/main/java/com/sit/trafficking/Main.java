package com.sit.trafficking;

import com.badlogic.gdx.Game;
import com.sit.trafficking.engine.managers.SoundManager;
import com.sit.trafficking.engine.scenes.SceneManager;
import com.sit.trafficking.scenes.LoadingScene;
import com.sit.trafficking.utils.TimeManager;

/**
 * Main entry point for the LibGDX application.
 */
public class Main extends Game {

    @Override
    public void create() {
        // Initialize Singletons if needed (lazy loaded mostly)
        // Push initial scene
        SceneManager.getInstance().pushOverlay(new LoadingScene());
    }

    @Override
    public void render() {
        // Update TimeManager if it needed explicit updates, but it pulls from Gdx.
        // Update and Render Scene Stack
        SceneManager.getInstance().render(TimeManager.getInstance().getDeltaTime());
    }

    @Override
    public void dispose() {
        // Clear stack
        while (SceneManager.getInstance().getCurrentScene() != null) {
             SceneManager.getInstance().popScene();
        }
        SoundManager.getInstance().dispose();
    }
}
