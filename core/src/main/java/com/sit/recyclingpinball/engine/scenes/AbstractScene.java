package com.sit.recyclingpinball.engine.scenes;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.sit.recyclingpinball.engine.interfaces.ICollisionManager;
import com.sit.recyclingpinball.engine.interfaces.IEntityManager;
import com.sit.recyclingpinball.engine.interfaces.IInputManager;
import com.sit.recyclingpinball.engine.interfaces.IMovementManager;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;

/**
 * Base for all game screens; provides shared managers and a consistent
 * lifecycle. Depends on manager interfaces for DIP compliance.
 */
public abstract class AbstractScene {

    private static final Logger LOGGER = Logger.getLogger(AbstractScene.class.getName());

    private final IEntityManager entityManager;
    private final ICollisionManager collisionManager;
    private final IInputManager inputManager;
    private final IMovementManager movementManager;
    private final IEngineContext context;

    // Gives every scene its own manager instances for isolation and predictable
    // teardown.
    public AbstractScene(IEngineContext context, IEntityManager entityManager, ICollisionManager collisionManager,
            IInputManager inputManager, IMovementManager movementManager) {
        this.context = context;
        this.entityManager = entityManager;
        this.collisionManager = collisionManager;
        this.inputManager = inputManager;
        this.movementManager = movementManager;
    }

    // Protected access keeps scene internals available to subclasses only.
    // External systems must interact through scene behavior, not manager state.
    protected IEngineContext getContext() {
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
            movementManager.processMovement(
                    entityManager.getEntitiesByType(com.sit.recyclingpinball.engine.interfaces.Movable.class), dt);
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

    // Exposed to inheriting scenes for composition, intentionally hidden from
    // non-scene classes to reduce coupling to internal engine subsystems.
    protected IEntityManager getEntityManager() {
        return entityManager;
    }

    protected IInputManager getInputManager() {
        return inputManager;
    }

    protected ICollisionManager getCollisionManager() {
        return collisionManager;
    }

    protected IMovementManager getMovementManager() {
        return movementManager;
    }

    // Keeps rendering in screen coordinates when the window is resized.
    public void resize(int width, int height) {
        context.getGraphics().setProjectionMatrix(width, height);
    }
}
