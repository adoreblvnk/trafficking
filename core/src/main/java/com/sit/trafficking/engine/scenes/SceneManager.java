package com.sit.trafficking.engine.scenes;

import com.badlogic.gdx.Gdx;
import com.sit.trafficking.engine.managers.IOManager;
import com.sit.trafficking.engine.managers.SoundManager;
import com.sit.trafficking.engine.managers.TimeManager;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

// Owns the scene stack and global services, single entry point for scene transitions and overlays.
public class SceneManager {

    private static SceneManager instance;
    private final Deque<AbstractScene> sceneStack;
    private final SoundManager soundManager;
    private final IOManager ioManager;
    private final TimeManager timeManager;

    // Singleton so one manager drives all scene state and avoids duplicate services.
    private SceneManager() {
        this.sceneStack = new ArrayDeque<>();
        this.soundManager = new SoundManager();
        this.ioManager = new IOManager();
        this.timeManager = new TimeManager();
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    // Layers a scene on top of the current one and routes input to it (e.g. pause menu).
    public void pushOverlay(AbstractScene scene) {
        if (scene == null) {
            Gdx.app.error("SceneManager", "Cannot push null scene");
            return;
        }

        try {
            scene.create();
            sceneStack.push(scene);
            Gdx.input.setInputProcessor(scene.getInputManager());
        } catch (Exception e) {
            Gdx.app.error("SceneManager", "Failed to create scene", e);
        }
    }

    // Removes the top scene and restores input to the one below (or clears if none).
    public void popScene() {
        if (!sceneStack.isEmpty()) {
            AbstractScene s = sceneStack.pop();
            s.dispose();
        }
        if (!sceneStack.isEmpty()) {
            Gdx.input.setInputProcessor(sceneStack.peek().getInputManager());
        } else {
            Gdx.input.setInputProcessor(null);
        }
    }

    // Replaces the entire stack with one scene for full transitions (e.g. from menu to game).
    public void setScene(AbstractScene scene) {
        if (scene == null) {
            Gdx.app.error("SceneManager", "Cannot set null scene");
            return;
        }

        while(!sceneStack.isEmpty()) {
            popScene();
        }
        pushOverlay(scene);
    }

    public void render(float dt) {
        if (sceneStack.isEmpty()) return;

        try {
            sceneStack.peek().update(dt);
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("SceneManager", "Scene update failure", e);
        }

        Iterator<AbstractScene> it = sceneStack.descendingIterator();
        while (it.hasNext()) {
            try {
                it.next().render();
            } catch (Exception e) {
                com.badlogic.gdx.Gdx.app.error("SceneManager", "Scene render failure", e);
            }
        }
    }

    // Cleans up all scenes and shared services on app exit.
    public void dispose() {
        while (!sceneStack.isEmpty()) {
            popScene();
        }
        soundManager.dispose();
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public IOManager getIOManager() {
        return ioManager;
    }

    public TimeManager getTimeManager() {
        return timeManager;
    }

    // Forwards window resize to the active scene so projection stays correct.
    public void resize(int width, int height) {
        if (sceneStack.isEmpty()) return;

        try {
            sceneStack.peek().resize(width, height);
        } catch (Exception e) {
            Gdx.app.error("SceneManager", "Scene resize failure", e);
        }
    }
}
