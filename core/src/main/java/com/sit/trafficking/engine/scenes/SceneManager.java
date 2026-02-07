package com.sit.trafficking.engine.scenes;

import com.sit.trafficking.engine.managers.IOManager;
import com.sit.trafficking.engine.managers.SoundManager;
import com.sit.trafficking.engine.managers.TimeManager;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class SceneManager {

    private static SceneManager instance;
    private final Deque<AbstractScene> sceneStack;
    
    private final SoundManager soundManager;
    private final IOManager ioManager;
    private final TimeManager timeManager;

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

    public void pushOverlay(AbstractScene scene) {
        scene.create();
        sceneStack.push(scene);
    }

    public void popScene() {
        if (!sceneStack.isEmpty()) {
            AbstractScene s = sceneStack.pop();
            s.dispose();
        }
    }

    public void setScene(AbstractScene scene) {
        while(!sceneStack.isEmpty()) {
            popScene();
        }
        pushOverlay(scene);
    }

    public void render(float dt) {
        if (sceneStack.isEmpty()) return;

        // Update top scene
        sceneStack.peek().update(dt);

        // Render from bottom to top
        Iterator<AbstractScene> it = sceneStack.descendingIterator();
        while (it.hasNext()) {
            it.next().render();
        }
    }

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
}
