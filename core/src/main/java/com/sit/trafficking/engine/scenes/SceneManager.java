package com.sit.trafficking.engine.scenes;

import com.sit.trafficking.engine.interfaces.providers.IEngineContext;
import com.sit.trafficking.engine.managers.IOManager;
import com.sit.trafficking.engine.managers.SoundManager;
import com.sit.trafficking.engine.managers.TimeManager;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Owns the scene stack and global services, single entry point for scene transitions and overlays.
 * Now depends on IEngineContext for platform independence.
 */
public class SceneManager {

    private static final Logger LOGGER = Logger.getLogger(SceneManager.class.getName());

    private final Deque<AbstractScene> sceneStack;
    private final SoundManager soundManager;
    private final IOManager ioManager;
    private final TimeManager timeManager;
    private final IEngineContext context;

    public SceneManager(IEngineContext context, SoundManager soundManager, IOManager ioManager, TimeManager timeManager) {
        if (context == null) {
            throw new IllegalArgumentException("EngineContext cannot be null");
        }
        this.context = context;
        this.sceneStack = new ArrayDeque<>();
        this.soundManager = soundManager;
        this.ioManager = ioManager;
        this.timeManager = timeManager;
    }

    // Layers a scene on top of the current one and routes input to it (e.g. pause menu).
    public void pushOverlay(AbstractScene scene) {
        if (scene == null) {
            return;
        }

        try {
            scene.create();
            sceneStack.push(scene);
            context.getInput().setActiveProcessor(scene.getInputManager());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to push overlay scene", e);
        }
    }

    // Removes the top scene and restores input to the one below (or clears if none).
    public void popScene() {
        if (!sceneStack.isEmpty()) {
            AbstractScene s = sceneStack.pop();
            s.dispose();
        }
        if (!sceneStack.isEmpty()) {
            context.getInput().setActiveProcessor(sceneStack.peek().getInputManager());
        } else {
            context.getInput().clearActiveProcessor();
        }
    }

    // Replaces the entire stack with one scene for full transitions (e.g. from menu to game).
    public void setScene(AbstractScene scene) {
        if (scene == null) {
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
            LOGGER.log(Level.SEVERE, "Active scene update failed", e);
        }

        Iterator<AbstractScene> it = sceneStack.descendingIterator();
        while (it.hasNext()) {
            try {
                it.next().render();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Scene render failed", e);
            }
        }
    }

    // Cleans up all scenes and shared services on app exit.
    public void dispose() {
        while (!sceneStack.isEmpty()) {
            popScene();
        }
        soundManager.dispose();
        context.dispose();
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

    public IEngineContext getContext() {
        return context;
    }

    // Forwards window resize to the active scene so projection stays correct.
    public void resize(int width, int height) {
        if (sceneStack.isEmpty()) return;

        try {
            sceneStack.peek().resize(width, height);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Scene resize failed", e);
        }
    }
}
