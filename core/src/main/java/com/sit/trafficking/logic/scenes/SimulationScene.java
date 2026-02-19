package com.sit.trafficking.logic.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sit.trafficking.engine.entities.AbstractEntity;
import com.sit.trafficking.engine.entities.DynamicEntity;
import com.sit.trafficking.engine.entities.StaticEntity;
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

        Gdx.input.setInputProcessor(inputManager);
        inputManager.addListener(this);

        SceneManager.getInstance().getSoundManager().loadSound(LogicConstants.SOUND_CRASH_ID, LogicConstants.SOUND_CRASH_PATH);

        boolean worldLoaded = world.loadWorld(entityManager, LogicConstants.DEFAULT_WORLD_PATH);
        if (!worldLoaded) {
            Gdx.app.log("SimulationScene", "Failed to load default world, starting with empty world");
        }

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        createBorderWalls(screenW, screenH);

        spawnDynamicEntity(screenW / 2f, screenH / 2f, true);
        spawnDynamicEntity(screenW / 3f, screenH / 3f, true);
    }

    private void createBorderWalls(float screenW, float screenH) {
        entityManager.removeEntity("border_top");
        entityManager.removeEntity("border_bottom");
        entityManager.removeEntity("border_left");
        entityManager.removeEntity("border_right");

        entityManager.addEntity(new StaticEntity("border_top", 0, screenH - 20, screenW, 20));
        entityManager.addEntity(new StaticEntity("border_bottom", 0, 0, screenW, 20));
        entityManager.addEntity(new StaticEntity("border_left", 0, 0, 20, screenH));
        entityManager.addEntity(new StaticEntity("border_right", screenW - 20, 0, 20, screenH));
    }

    private void spawnDynamicEntity(float x, float y, boolean giveInitialVelocity) {
        String id = "car_" + MathUtils.random(10000);
        DynamicEntity car = new DynamicEntity(id, x, y, LogicConstants.VEHICLE_SIZE, LogicConstants.VEHICLE_SIZE);
        car.setColor(new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1));

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        if (x < LogicConstants.NUDGE_OFFSET) car.getPosition().x = LogicConstants.NUDGE_OFFSET;
        if (x > screenW - LogicConstants.NUDGE_OFFSET) car.getPosition().x = screenW - LogicConstants.NUDGE_OFFSET * 2;
        if (y < LogicConstants.NUDGE_OFFSET) car.getPosition().y = LogicConstants.NUDGE_OFFSET;
        if (y > screenH - LogicConstants.NUDGE_OFFSET) car.getPosition().y = screenH - LogicConstants.NUDGE_OFFSET * 2;

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
        float worldY = Gdx.graphics.getHeight() - y;

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
            float worldY = Gdx.graphics.getHeight() - y;
            dragCurrentPos.set(x, worldY);
            draggedEntity.setPosition(x - draggedEntity.getWidth()/2, worldY - draggedEntity.getHeight()/2);
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchUp(int x, int y, int ptr, int btn) {
        if (isDragging && draggedEntity != null) {
            if (draggedEntity instanceof DynamicEntity) {
                Vector2 throwVel = new Vector2(dragStartPos).sub(dragCurrentPos).scl(LogicConstants.SLINGSHOT_MULTIPLIER);
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

    @Override
    public boolean onKeyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            SceneManager.getInstance().pushOverlay(new PauseOverlay());
            return true;
        }
        
        if (keycode == Input.Keys.F5) {
            boolean saved = world.saveCurrentState(entityManager);
            if (saved) {
                Gdx.app.log("SimulationScene", "Quick Save Complete!");
            } else {
                Gdx.app.error("SimulationScene", "Quick Save Failed!");
            }
            return true;
        }

        if (keycode == Input.Keys.F9) {
            boolean loaded = world.loadSaveState(entityManager);
            if (loaded) {
                Gdx.app.log("SimulationScene", "Quick Load Complete!");
            } else {
                Gdx.app.log("SimulationScene", "Quick Load Failed - No save file found");
            }
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        entityManager.removeEntity("border_top");
        entityManager.removeEntity("border_bottom");
        entityManager.removeEntity("border_left");
        entityManager.removeEntity("border_right");

        entityManager.addEntity(new StaticEntity("border_top", 0, height - 20, width, 20));
        entityManager.addEntity(new StaticEntity("border_bottom", 0, 0, width, 20));
        entityManager.addEntity(new StaticEntity("border_left", 0, 0, 20, height));
        entityManager.addEntity(new StaticEntity("border_right", width - 20, 0, 20, height));

        float margin = LogicConstants.NUDGE_OFFSET;
        float maxX = width - margin;
        float maxY = height - margin;

        for (AbstractEntity e : entityManager.getEntities()) {
            if (e.getId().startsWith("border_") || e.isStatic()) continue;

            Vector2 pos = e.getPosition();
            float clampedX = MathUtils.clamp(pos.x, margin, maxX - e.getWidth());
            float clampedY = MathUtils.clamp(pos.y, margin, maxY - e.getHeight());

            if (pos.x != clampedX || pos.y != clampedY) {
                e.setPosition(clampedX, clampedY);
            }
        }
    }
}
