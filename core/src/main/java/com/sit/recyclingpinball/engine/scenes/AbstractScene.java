package com.sit.recyclingpinball.engine.scenes;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.managers.CollisionManager;
import com.sit.recyclingpinball.engine.managers.EntityManager;
import com.sit.recyclingpinball.engine.managers.InputManager;
import com.sit.recyclingpinball.engine.managers.MovementManager;

/**
 * Base for all game screens; provides shared managers and a consistent
 * lifecycle. Now depends on IEngineContext for platform-independent access to
 * display, graphics, and audio.
 */
public abstract class AbstractScene {

    private static final Logger LOGGER = Logger.getLogger(AbstractScene.class.getName());

    private EntityManager entityManager;
    private CollisionManager collisionManager;
    private InputManager inputManager;
    private MovementManager movementManager;
    private final IEngineContext context;

    // Gives every scene its own manager instances for isolation and predictable
    // teardown.
    public AbstractScene(IEngineContext context, EntityManager entityManager, CollisionManager collisionManager,
            InputManager inputManager, MovementManager movementManager) {
        this.context = context;
        this.entityManager = entityManager;
        this.collisionManager = collisionManager;
        this.inputManager = inputManager;
        this.movementManager = movementManager;
    }

    public IEngineContext getContext() {
        return context;
    }

    public abstract void create();

    // Runs entity updates, then movement, then collision so physics and callbacks
    // stay consistent.
    public void update(float dt) {
        try {
            entityManager.update(dt);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Entity update failed", e);
        }

        try {
            movementManager.processMovement(entityManager.getEntitiesByType(com.sit.recyclingpinball.engine.interfaces.Movable.class), dt);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Movement processing failed", e);
        }

        try {
            collisionManager.processCollisions(entityManager.getEntities());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Collision processing failed", e);
        }
    }

    public void render() {
        try {
            entityManager.render(context.getGraphics());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Entity rendering failed", e);
        }
    }

    // Releases GPU and font resources so scenes can be swapped without leaks.
    public void dispose() {
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    // Keeps rendering in screen coordinates when the window is resized.
    public void resize(int width, int height) {
        context.getGraphics().setProjectionMatrix(width, height);
    }
}
