package com.sit.trafficking.logic.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sit.trafficking.engine.entities.AbstractEntity;
import com.sit.trafficking.engine.entities.DynamicEntity;
import com.sit.trafficking.engine.interfaces.InputListener;
import com.sit.trafficking.engine.scenes.AbstractScene;
import com.sit.trafficking.engine.scenes.SceneManager;
import com.sit.trafficking.logic.LogicConstants;

public class SimulationScene extends AbstractScene implements InputListener {

    private AbstractEntity draggedEntity;
    private Vector2 dragStartPos = new Vector2();
    private Vector2 dragCurrentPos = new Vector2();
    private boolean isDragging = false;

    @Override
    public void create() {
        // Setup Input
        Gdx.input.setInputProcessor(inputManager);
        inputManager.addListener(this);

        // Load Sound
        SceneManager.getInstance().getSoundManager().loadSound("crash", "sounds/car_crash_1.wav");

        // Load Level
        new com.sit.trafficking.logic.factories.LevelFactory().loadLevel(entityManager, "levels/engine_demo.json");

        // Create Initial Dynamic Entity (extra)
        spawnDynamicEntity(LogicConstants.SCREEN_WIDTH / 2f, LogicConstants.SCREEN_HEIGHT / 2f);
    }

    private void spawnDynamicEntity(float x, float y) {
        String id = "car_" + MathUtils.random(10000);
        DynamicEntity car = new DynamicEntity(id, x, y, LogicConstants.VEHICLE_SIZE, LogicConstants.VEHICLE_SIZE);
        car.setColor(new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1));
        
        // Nudge if inside wall (simple check)
        if (x < LogicConstants.NUDGE_OFFSET) car.getPosition().x = LogicConstants.NUDGE_OFFSET;
        if (x > LogicConstants.SCREEN_WIDTH - LogicConstants.NUDGE_OFFSET) car.getPosition().x = LogicConstants.SCREEN_WIDTH - LogicConstants.NUDGE_OFFSET * 2;
        if (y < LogicConstants.NUDGE_OFFSET) car.getPosition().y = LogicConstants.NUDGE_OFFSET;
        if (y > LogicConstants.SCREEN_HEIGHT - LogicConstants.NUDGE_OFFSET) car.getPosition().y = LogicConstants.SCREEN_HEIGHT - LogicConstants.NUDGE_OFFSET * 2;

        entityManager.addEntity(car);
    }

    @Override
    public void update(float dt) {
        // Global Input check for Pause
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            SceneManager.getInstance().pushOverlay(new PauseOverlay());
        }
        
        super.update(dt);
    }

    @Override
    public void render() {
        // Clear screen
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        super.render();
        
        // Draw drag line
        if (isDragging && draggedEntity != null) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rectLine(dragStartPos, dragCurrentPos, 2);
        }
        shapeRenderer.end();
    }

    // Input Listener Implementation

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        // Convert screen Y to world Y
        float worldY = LogicConstants.SCREEN_HEIGHT - y;

        if (btn == Input.Buttons.RIGHT) {
            spawnDynamicEntity(x, worldY);
            return true;
        }

        if (btn == Input.Buttons.LEFT) {
            // Check intersection
            for (AbstractEntity e : entityManager.getEntities()) {
                if (e.getBounds().contains(x, worldY) && !e.isStatic()) {
                    draggedEntity = e;
                    isDragging = true;
                    dragStartPos.set(x, worldY);
                    dragCurrentPos.set(x, worldY);
                    // Stop it while dragging
                    if (e instanceof DynamicEntity) {
                        ((DynamicEntity) e).setVelocity(0, 0);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onDrag(int x, int y, int ptr) {
        if (isDragging && draggedEntity != null) {
            float worldY = LogicConstants.SCREEN_HEIGHT - y;
            dragCurrentPos.set(x, worldY);
            // Move entity with mouse
            draggedEntity.getPosition().set(x - draggedEntity.getWidth()/2, worldY - draggedEntity.getHeight()/2);
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchUp(int x, int y, int ptr, int btn) {
        if (isDragging && draggedEntity != null) {
            if (draggedEntity instanceof DynamicEntity) {
                Vector2 throwVel = new Vector2(dragCurrentPos).sub(dragStartPos).scl(LogicConstants.SLINGSHOT_MULTIPLIER);
                // Clamp max speed
                throwVel.clamp(0, LogicConstants.MAX_VELOCITY); 
                ((DynamicEntity) draggedEntity).setVelocity(throwVel.x, throwVel.y);
            }
            isDragging = false;
            draggedEntity = null;
            return true;
        }
        return false;
    }
}
