package com.sit.trafficking.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sit.trafficking.engine.entities.AbstractEntity;
import com.sit.trafficking.engine.entities.DynamicEntity;
import com.sit.trafficking.engine.factory.LevelFactory;
import com.sit.trafficking.engine.managers.InputManager;
import com.sit.trafficking.engine.scenes.AbstractScene;
import com.sit.trafficking.engine.scenes.SceneManager;
import com.sit.trafficking.utils.Constants;
import com.sit.trafficking.utils.TimeManager;

/**
 * The main simulation playground.
 * Implements "God Hand" input controls.
 */
public class SimulationScene extends AbstractScene implements InputManager.InputListener {

    private boolean isPaused = false;
    private AbstractEntity draggedEntity = null;
    private Vector2 dragOffset = new Vector2();
    private Vector2 lastMousePos = new Vector2();
    private Vector2 throwVelocity = new Vector2();

    @Override
    public void create() {
        // Load Level
        LevelFactory factory = new LevelFactory();
        factory.loadLevel(entityManager, "levels/engine_demo.json");

        // Input Handling
        resetInputProcessor();
        inputManager.addListener(this);
    }

    public void resetInputProcessor() {
        Gdx.input.setInputProcessor(inputManager);
    }

    @Override
    public void update(float dt) {
        // Toggle Pause
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            isPaused = !isPaused;
        }

        // Open Pause Overlay
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            SceneManager.getInstance().pushOverlay(new PauseOverlay());
        }
        
        // Handle Dragging logic (Updating dragged entity position)
        if (draggedEntity != null) {
            Vector3 mousePos3 = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            // No camera unproject needed if 1:1 and no camera, but usually coordinate system needs flipping Y
            // Mouse Y is from top, World Y is from bottom usually.
            float worldX = mousePos3.x;
            float worldY = Constants.SCREEN_HEIGHT - mousePos3.y;

            // Update velocity for throwing
            float vx = (worldX - lastMousePos.x) / dt; // Instant velocity
            float vy = (worldY - lastMousePos.y) / dt;
            // Smooth it a bit or just take it raw.
            throwVelocity.set(vx, vy);

            lastMousePos.set(worldX, worldY);
            
            // Move entity
            draggedEntity.getPosition().set(worldX - dragOffset.x, worldY - dragOffset.y);
            draggedEntity.getVelocity().set(0, 0); // Zero velocity while dragging
        }

        // Remove entity (Delete Key)
        if (Gdx.input.isKeyJustPressed(Input.Keys.DEL)) {
            AbstractEntity underCursor = getEntityUnderMouse();
            if (underCursor != null) {
                entityManager.removeEntity(underCursor.getId());
            }
        }
        
        // Replace entity ('R' Key)
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            AbstractEntity underCursor = getEntityUnderMouse();
            if (underCursor != null) {
                DynamicEntity bigger = new DynamicEntity(underCursor.getId(), 
                        underCursor.getPosition().x, underCursor.getPosition().y, 
                        underCursor.getWidth() * 1.5f, underCursor.getHeight() * 1.5f);
                bigger.setColor(com.badlogic.gdx.graphics.Color.GOLD);
                entityManager.replaceEntity(underCursor.getId(), bigger);
            }
        }

        float timeScale = TimeManager.getInstance().getTimeScale();
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            timeScale = Math.min(2.0f, timeScale + 0.1f);
            TimeManager.getInstance().setTimeScale(timeScale);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            timeScale = Math.max(0.1f, timeScale - 0.1f);
            TimeManager.getInstance().setTimeScale(timeScale);
        }

        if (!isPaused) {
            float scaledDt = dt * TimeManager.getInstance().getTimeScale();
            entityManager.update(scaledDt);

            // Pass entities to CollisionManager
            collisionManager.processCollisions(entityManager.getEntities());
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeType.Filled);
        entityManager.render(shapeRenderer);
        shapeRenderer.end();
    }

    private AbstractEntity getEntityUnderMouse() {
        float worldX = Gdx.input.getX();
        float worldY = Constants.SCREEN_HEIGHT - Gdx.input.getY();

        // Search backwards (topmost first)
        for (int i = entityManager.getEntities().size() - 1; i >= 0; i--) {
            AbstractEntity e = entityManager.getEntities().get(i);
            if (e.getBounds().contains(worldX, worldY)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public boolean onTouchDown(int screenX, int screenY, int pointer, int button) {
        float worldX = screenX;
        float worldY = Constants.SCREEN_HEIGHT - screenY;

        if (button == Input.Buttons.RIGHT) {
            // Spawn Entity
            String newId = "spawned_" + System.currentTimeMillis();
            DynamicEntity e = new DynamicEntity(newId, worldX - 15, worldY - 15, 30, 30);
            e.setColor(com.badlogic.gdx.graphics.Color.CYAN);
            entityManager.addEntity(e);
            return true;
        } else if (button == Input.Buttons.LEFT) {
            // Start Drag
            AbstractEntity e = getEntityUnderMouse();
            if (e != null && e instanceof DynamicEntity) {
                draggedEntity = e;
                dragOffset.set(worldX - e.getPosition().x, worldY - e.getPosition().y);
                lastMousePos.set(worldX, worldY);
                throwVelocity.set(0, 0);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onDrag(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean onTouchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT && draggedEntity != null) {
            // Throw
            draggedEntity.setVelocity(throwVelocity.x, throwVelocity.y);
            draggedEntity = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keycode) {
        return false;
    }
}
