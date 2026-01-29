package com.sit.trafficking.engine.scenes;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sit.trafficking.engine.managers.CollisionManager;
import com.sit.trafficking.engine.managers.EntityManager;
import com.sit.trafficking.engine.managers.InputManager;

/**
 * Base class for all game scenes.
 * Encapsulates the core managers required for a simulation scene.
 */
public abstract class AbstractScene {
    
    protected final EntityManager entityManager;
    protected final CollisionManager collisionManager;
    protected final InputManager inputManager;
    protected final ShapeRenderer shapeRenderer;

    public AbstractScene() {
        this.entityManager = new EntityManager();
        this.collisionManager = new CollisionManager();
        this.inputManager = new InputManager();
        this.shapeRenderer = new ShapeRenderer();
    }

    /**
     * Called when the scene is first pushed to the stack.
     */
    public abstract void create();

    /**
     * Updates scene logic.
     * @param dt Delta time.
     */
    public abstract void update(float dt);

    /**
     * Renders the scene.
     */
    public abstract void render();

    /**
     * Cleans up resources.
     */
    public void dispose() {
        shapeRenderer.dispose();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }
}
