package com.sit.trafficking.engine.scenes;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sit.trafficking.engine.managers.CollisionManager;
import com.sit.trafficking.engine.managers.EntityManager;
import com.sit.trafficking.engine.managers.InputManager;
import com.sit.trafficking.engine.managers.MovementManager;

public abstract class AbstractScene {
    
    protected EntityManager entityManager;
    protected CollisionManager collisionManager;
    protected InputManager inputManager;
    protected MovementManager movementManager;
    protected ShapeRenderer shapeRenderer;

    public AbstractScene() {
        this.entityManager = new EntityManager();
        this.collisionManager = new CollisionManager();
        this.inputManager = new InputManager();
        this.movementManager = new MovementManager();
        this.shapeRenderer = new ShapeRenderer();
    }

    public abstract void create();

    public void update(float dt) {
        entityManager.update(dt);
        movementManager.processMovement(entityManager.getEntities(), dt);
        collisionManager.processCollisions(entityManager.getEntities());
    }

    public void render() {
        entityManager.render(shapeRenderer);
    }

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
