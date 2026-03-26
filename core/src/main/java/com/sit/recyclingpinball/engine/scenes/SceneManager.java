package com.sit.recyclingpinball.engine.scenes;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sit.recyclingpinball.engine.platform.libgdx.PlatformContext;
import com.sit.recyclingpinball.engine.platform.libgdx.PlatformInputProcessor;

/**
 * Owns the scene stack and global services, single entry point for scene
 * transitions and overlays. Now depends on PlatformContext for platform
 * independence.
 */
public class SceneManager {

    private static final Logger LOGGER = Logger.getLogger(SceneManager.class.getName());

    private final Deque<AbstractScene> sceneStack;
    private final PlatformContext context;

    public SceneManager(PlatformContext context) {
        if (context == null) {
            throw new IllegalArgumentException("EngineContext cannot be null");
        }
        this.context = context;
        this.sceneStack = new ArrayDeque<>();
    }

    // Layers a scene on top of the current one and routes input to it (e.g. pause
    // menu).
    public void pushOverlay(AbstractScene scene) {
        if (scene == null) {
            return;
        }

        try {
            scene.create();
            sceneStack.push(scene);
            bindActiveInput(scene);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to push overlay scene", e);
        }
    }

    // Removes the top scene and restores input to the one below (or clears if
    // none).
    public void popScene() {
        if (!sceneStack.isEmpty()) {
            AbstractScene s = sceneStack.pop();
            s.dispose();
        }
        if (!sceneStack.isEmpty()) {
            bindActiveInput(sceneStack.peek());
        } else {
            context.getInput().clearActiveProcessor();
        }
    }

    private void bindActiveInput(AbstractScene scene) {
        if (scene.getInputManager() instanceof PlatformInputProcessor processor) {
            context.getInput().setActiveProcessor(processor);
        } else {
            context.getInput().clearActiveProcessor();
            LOGGER.log(Level.WARNING, "Scene input manager does not implement PlatformInputProcessor");
        }
    }

    // Replaces the entire stack with one scene for full transitions (e.g. from menu
    // to game).
    public void setScene(AbstractScene scene) {
        if (scene == null) {
            return;
        }

        while (!sceneStack.isEmpty()) {
            popScene();
        }
        pushOverlay(scene);
    }

    public void render(float dt) {
        if (sceneStack.isEmpty())
            return;

        try {
            sceneStack.peek().update(dt);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Active scene update failed", e);
        }

        sceneStack.descendingIterator().forEachRemaining(scene -> {
            try {
                scene.render();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Scene render failed", e);
            }
        });
    }

    // Cleans up all scenes and shared services on app exit.
    public void dispose() {
        while (!sceneStack.isEmpty()) {
            popScene();
        }
    }

    public PlatformContext getContext() {
        return context;
    }

    // Forwards window resize to the active scene so projection stays correct.
    public void resize(int width, int height) {
        if (sceneStack.isEmpty())
            return;

        try {
            sceneStack.peek().resize(width, height);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Scene resize failed", e);
        }
    }
}
