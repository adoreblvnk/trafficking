package com.sit.trafficking.engine.scenes;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.sit.trafficking.engine.EngineConstants;
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
    protected BitmapFont font;

    public AbstractScene() {
        this.entityManager = new EntityManager();
        this.collisionManager = new CollisionManager();
        this.inputManager = new InputManager();
        this.movementManager = new MovementManager();
        this.shapeRenderer = new ShapeRenderer();
    }

    public abstract void create();

    protected void loadFont(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(EngineConstants.DEFAULT_FONT_PATH));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();
    }


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
        try {
            entityManager.render(shapeRenderer);
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("AbstractScene", "Render system failure: " + e.getMessage());
        }
    }

    public void dispose() {
        shapeRenderer.dispose();
        if (font != null) {
            font.dispose();
        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
    
    public InputManager getInputManager() {
        return inputManager;
    }
}
