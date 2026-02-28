package com.sit.trafficking.engine.scenes;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.sit.trafficking.engine.EngineConstants;
import com.sit.trafficking.engine.managers.CollisionManager;
import com.sit.trafficking.engine.managers.EntityManager;
import com.sit.trafficking.engine.managers.InputManager;
import com.sit.trafficking.engine.managers.MovementManager;

// Base for all game screens; provides shared managers and a consistent lifecycle.
public abstract class AbstractScene {

    private EntityManager entityManager;
    private CollisionManager collisionManager;
    private InputManager inputManager;
    private MovementManager movementManager;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    // Gives every scene its own manager instances for isolation and predictable teardown.
    public AbstractScene() {
        this.entityManager = new EntityManager();
        this.collisionManager = new CollisionManager();
        this.inputManager = new InputManager();
        this.movementManager = new MovementManager();
        this.shapeRenderer = new ShapeRenderer();
    }

    public abstract void create();

    // Centralizes font loading so scenes avoid duplicating TTF setup and paths.
    protected void loadFont(int size) {
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(EngineConstants.DEFAULT_FONT_PATH));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = size;
            parameter.color = Color.WHITE;
            font = generator.generateFont(parameter);
            generator.dispose();
        } catch (Exception e) {
            Gdx.app.error("AbstractScene", "Failed to load font with size " + size, e);
        }
    }


    // Runs entity updates, then movement, then collision so physics and callbacks stay consistent.
    public void update(float dt) {
        try {
            entityManager.update(dt);
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("AbstractScene", "System failure", e);
        }

        try {
            movementManager.processMovement(entityManager.getEntities(), dt);
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("AbstractScene", "System failure", e);
        }

        try {
            collisionManager.processCollisions(entityManager.getEntities());
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("AbstractScene", "System failure", e);
        }
    }

    public void render() {
        try {
            entityManager.render(shapeRenderer);
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("AbstractScene", "Render system failure", e);
        }
    }

    // Releases GPU and font resources so scenes can be swapped without leaks.
    public void dispose() {
        try {
            shapeRenderer.dispose();
        } catch (Exception e) {
            Gdx.app.error("AbstractScene", "Failed to dispose ShapeRenderer", e);
        }

        if (font != null) {
            try {
                font.dispose();
            } catch (Exception e) {
                Gdx.app.error("AbstractScene", "Failed to dispose font", e);
            }
        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    public MovementManager getMovementManager() {
        return movementManager;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public BitmapFont getFont() {
        return font;
    }

    // Keeps rendering in screen coordinates when the window is resized.
    public void resize(int width, int height) {
        shapeRenderer.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, width, height));
    }
}
