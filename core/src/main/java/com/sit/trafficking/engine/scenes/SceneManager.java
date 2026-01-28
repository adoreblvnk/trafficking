package com.sit.trafficking.engine.scenes;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * Singleton managing the scene stack.
 * Allows for overlays (like Pause Menu) on top of active scenes.
 */
public class SceneManager {

    private static SceneManager instance;
    private final Deque<AbstractScene> sceneStack;

    private SceneManager() {
        this.sceneStack = new ArrayDeque<>();
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    /**
     * Pushes a new scene onto the stack.
     * @param scene The scene to add.
     */
    public void pushOverlay(AbstractScene scene) {
        scene.create();
        sceneStack.push(scene);
    }

    /**
     * Removes the top scene from the stack.
     */
    public void popScene() {
        if (!sceneStack.isEmpty()) {
            AbstractScene s = sceneStack.pop();
            s.dispose();
        }
    }
    
    /**
     * Replaces the entire stack with a single scene.
     */
    public void setScene(AbstractScene scene) {
        while(!sceneStack.isEmpty()) {
            popScene();
        }
        pushOverlay(scene);
    }

    /**
     * Updates and renders the scene stack.
     * Logic: Update only the top scene. Render all scenes from bottom to top.
     * @param dt Delta time.
     */
    public void render(float dt) {
        if (sceneStack.isEmpty()) return;

        // Update ONLY the top scene
        sceneStack.peek().update(dt);

        // Render ALL scenes (from bottom to top for correct layering)
        // ArrayDeque iterator goes top-to-bottom usually, so we need reverse iteration for bottom-to-top rendering
        Iterator<AbstractScene> it = sceneStack.descendingIterator();
        while (it.hasNext()) {
            it.next().render();
        }
    }
    
    public AbstractScene getCurrentScene() {
        return sceneStack.peek();
    }
}
