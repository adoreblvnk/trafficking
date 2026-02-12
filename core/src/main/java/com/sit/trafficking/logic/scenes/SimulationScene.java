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

import com.sit.trafficking.logic.factories.World;

public class SimulationScene extends AbstractScene implements InputListener {

    private AbstractEntity draggedEntity;
    private Vector2 dragStartPos = new Vector2();
    private Vector2 dragCurrentPos = new Vector2();
    private boolean isDragging = false;
    private World world;

    @Override
    public void create() {
        world = new World();
        
        // Setup Input
        Gdx.input.setInputProcessor(inputManager);
        inputManager.addListener(this);

        // Load Sound
        SceneManager.getInstance().getSoundManager().loadSound(LogicConstants.SOUND_CRASH_ID, LogicConstants.SOUND_CRASH_PATH);

        // Load World
        boolean worldLoaded = world.loadWorld(entityManager, LogicConstants.DEFAULT_WORLD_PATH);
        if (!worldLoaded) {
            Gdx.app.log("SimulationScene", "Failed to load default world, starting with empty world");
        }

        // Create Initial Traffic (Autonomous Movement)
        spawnDynamicEntity(LogicConstants.SCREEN_WIDTH / 2f, LogicConstants.SCREEN_HEIGHT / 2f, true);
        spawnDynamicEntity(LogicConstants.SCREEN_WIDTH / 3f, LogicConstants.SCREEN_HEIGHT / 3f, true);
    }

    private void spawnDynamicEntity(float x, float y, boolean giveInitialVelocity) {
        String id = "car_" + MathUtils.random(10000);
        DynamicEntity car = new DynamicEntity(id, x, y, LogicConstants.VEHICLE_SIZE, LogicConstants.VEHICLE_SIZE);
        car.setColor(new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1));
        
        // Nudge if inside wall (simple check)
        if (x < LogicConstants.NUDGE_OFFSET) car.getPosition().x = LogicConstants.NUDGE_OFFSET;
        if (x > LogicConstants.SCREEN_WIDTH - LogicConstants.NUDGE_OFFSET) car.getPosition().x = LogicConstants.SCREEN_WIDTH - LogicConstants.NUDGE_OFFSET * 2;
        if (y < LogicConstants.NUDGE_OFFSET) car.getPosition().y = LogicConstants.NUDGE_OFFSET;
        if (y > LogicConstants.SCREEN_HEIGHT - LogicConstants.NUDGE_OFFSET) car.getPosition().y = LogicConstants.SCREEN_HEIGHT - LogicConstants.NUDGE_OFFSET * 2;

        // Autonomous Movement Logic
        if (giveInitialVelocity) {
            float speed = MathUtils.random(100f, 300f);
            float angle = MathUtils.random(0, 360);
            car.setVelocity(MathUtils.cosDeg(angle) * speed, MathUtils.sinDeg(angle) * speed);
            
            // Logic Injection: Listener
            car.setCollisionListener((source, target) -> {
                if (source instanceof DynamicEntity) {
                     DynamicEntity m = (DynamicEntity) source;
                     if (m.getVelocity().len2() > LogicConstants.CRASH_SOUND_THRESHOLD) {
                         SceneManager.getInstance().getSoundManager().playSound(LogicConstants.SOUND_CRASH_ID, LogicConstants.DEFAULT_VOLUME);
                     }
                }
            });
        }

        entityManager.addEntity(car);
    }

    @Override
    public void update(float dt) {
        // Global Input check for Pause
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            SceneManager.getInstance().pushOverlay(new PauseOverlay());
        }
        
        // SAVE (F5)
        if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
            boolean saved = world.saveCurrentState(entityManager);
            if (saved) {
                Gdx.app.log("SimulationScene", "Quick Save Complete!");
            } else {
                Gdx.app.error("SimulationScene", "Quick Save Failed!");
            }
        }

        // LOAD (F9)
        if (Gdx.input.isKeyJustPressed(Input.Keys.F9)) {
            boolean loaded = world.loadSaveState(entityManager);
            if (loaded) {
                Gdx.app.log("SimulationScene", "Quick Load Complete!");
            } else {
                Gdx.app.log("SimulationScene", "Quick Load Failed - No save file found");
            }
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
            spawnDynamicEntity(x, worldY, true);
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
