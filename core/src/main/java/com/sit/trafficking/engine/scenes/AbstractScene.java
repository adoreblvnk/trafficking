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
        try {
            entityManager.update(dt);
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("AbstractScene", "System failure: " + e.getMessage());
        }

        try {
            movementManager.processMovement(entityManager.getEntities(), dt);
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("AbstractScene", "System failure: " + e.getMessage());
        }

        try {
            collisionManager.processCollisions(entityManager.getEntities());
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("AbstractScene", "System failure: " + e.getMessage());
        }
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
