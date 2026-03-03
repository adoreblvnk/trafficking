package com.sit.trafficking.logic.scenes;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.sit.trafficking.engine.entities.AbstractEntity;
import com.sit.trafficking.engine.entities.DynamicEntity;
import com.sit.trafficking.engine.entities.StaticEntity;
import com.sit.trafficking.engine.interfaces.InputListener;
import com.sit.trafficking.engine.interfaces.providers.IEngineContext;
import com.sit.trafficking.engine.managers.CollisionManager;
import com.sit.trafficking.engine.managers.EntityManager;
import com.sit.trafficking.engine.managers.InputManager;
import com.sit.trafficking.engine.managers.MovementManager;
import com.sit.trafficking.engine.scenes.AbstractScene;
import com.sit.trafficking.engine.scenes.SceneManager;
import com.sit.trafficking.logic.LogicConstants;
import com.sit.trafficking.logic.factories.SceneFactory;
import com.sit.trafficking.logic.factories.World;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main simulation scene for traffic management gameplay.
 * No longer directly uses libGDX - all graphics/display calls go through IEngineContext.
 */
public class SimulationScene extends AbstractScene implements InputListener {

    private static final Random RANDOM = new Random();
    private static final Logger LOGGER = Logger.getLogger(SimulationScene.class.getName());

    private AbstractEntity draggedEntity;
    private Vector2 dragStartPos = new Vector2();
    private Vector2 dragCurrentPos = new Vector2();
    private boolean isDragging = false;
    private World world;
    private final SceneManager sceneManager;
    private final SceneFactory sceneFactory;

    public SimulationScene(IEngineContext context, SceneManager sceneManager, SceneFactory sceneFactory, EntityManager entityManager, CollisionManager collisionManager, InputManager inputManager, MovementManager movementManager) {
        super(context, entityManager, collisionManager, inputManager, movementManager);
        this.sceneManager = sceneManager;
        this.sceneFactory = sceneFactory;
    }

    //initialises world data, load assets, and spawn initial entities
    @Override
    public void create() {
        world = new World(sceneManager.getIOManager(), sceneManager.getSoundManager());

        getInputManager().addListener(this);

        sceneManager.getSoundManager().loadSound(LogicConstants.SOUND_CRASH_ID, LogicConstants.SOUND_CRASH_PATH);

        boolean worldLoaded = world.loadWorld(getEntityManager(), LogicConstants.DEFAULT_WORLD_PATH);
        if (!worldLoaded) {
            LOGGER.log(Level.SEVERE, "Failed to load default world: " + LogicConstants.DEFAULT_WORLD_PATH);
        }

        float screenW = context.getDisplay().getWidth();
        float screenH = context.getDisplay().getHeight();
        createBorderWalls(screenW, screenH);

        spawnDynamicEntity(screenW / 2f, screenH / 2f, true);
        spawnDynamicEntity(screenW / 3f, screenH / 3f, true);
    }

    //defines static collision boundaries around the screen edges
    private void createBorderWalls(float screenW, float screenH) {
        getEntityManager().removeEntity("border_top");
        getEntityManager().removeEntity("border_bottom");
        getEntityManager().removeEntity("border_left");
        getEntityManager().removeEntity("border_right");

        float thickness = LogicConstants.BORDER_WALL_THICKNESS;
        getEntityManager().addEntity(new StaticEntity("border_top", 0, screenH - thickness, screenW, thickness, 0.5f, 0.5f, 0.5f));
        getEntityManager().addEntity(new StaticEntity("border_bottom", 0, 0, screenW, thickness, 0.5f, 0.5f, 0.5f));
        getEntityManager().addEntity(new StaticEntity("border_left", 0, 0, thickness, screenH, 0.5f, 0.5f, 0.5f));
        getEntityManager().addEntity(new StaticEntity("border_right", screenW - thickness, 0, thickness, screenH, 0.5f, 0.5f, 0.5f));
    }

    //spawn entity with optional motion and collision logic
    private void spawnDynamicEntity(float x, float y, boolean giveInitialVelocity) {
        String id = "car_" + RANDOM.nextInt(10001);
        DynamicEntity car = new DynamicEntity(id, x, y, LogicConstants.VEHICLE_SIZE, LogicConstants.VEHICLE_SIZE);
        car.setColor(RANDOM.nextFloat(), RANDOM.nextFloat(), RANDOM.nextFloat(), 1);

        float screenW = context.getDisplay().getWidth();
        float screenH = context.getDisplay().getHeight();

        if (x < LogicConstants.NUDGE_OFFSET) car.getPosition().x = LogicConstants.NUDGE_OFFSET;
        if (x > screenW - LogicConstants.NUDGE_OFFSET) car.getPosition().x = screenW - LogicConstants.NUDGE_OFFSET * 2;
        if (y < LogicConstants.NUDGE_OFFSET) car.getPosition().y = LogicConstants.NUDGE_OFFSET;
        if (y > screenH - LogicConstants.NUDGE_OFFSET) car.getPosition().y = screenH - LogicConstants.NUDGE_OFFSET * 2;

        // Autonomous Movement Logic
        if (giveInitialVelocity) {
            float speed = 100f + RANDOM.nextFloat() * 200f;
            float angleDeg = RANDOM.nextFloat() * 360f;
            double angleRad = Math.toRadians(angleDeg);
            car.setVelocity((float) Math.cos(angleRad) * speed, (float) Math.sin(angleRad) * speed);

            // Logic Injection: Listener
            car.setCollisionListener((source, target) -> {
                if (source instanceof DynamicEntity) {
                     DynamicEntity m = (DynamicEntity) source;
                     if (m.getVelocity().len2() > LogicConstants.CRASH_SOUND_THRESHOLD) {
                         sceneManager.getSoundManager().playSound(LogicConstants.SOUND_CRASH_ID, LogicConstants.DEFAULT_VOLUME);
                     }
                }
            });
        }

        getEntityManager().addEntity(car);
    }

    //updates world physics and ensure entities remain within play area
    @Override
    public void update(float dt) {
        super.update(dt);
        clampEntitiesToBounds();
    }

    //forces non-static entities to stay within the border wall margins
    private void clampEntitiesToBounds() {
        float screenW = context.getDisplay().getWidth();
        float screenH = context.getDisplay().getHeight();
        float margin = LogicConstants.BORDER_WALL_THICKNESS;
        float maxX = screenW - margin;
        float maxY = screenH - margin;

        for (AbstractEntity e : getEntityManager().getEntities()) {
            if (e.getId().startsWith("border_") || e.isStatic()) continue;

            Vector2 pos = e.getPosition();
            float clampedX = Math.max(margin, Math.min(maxX - e.getWidth(), pos.x));
            float clampedY = Math.max(margin, Math.min(maxY - e.getHeight(), pos.y));

            if (pos.x != clampedX || pos.y != clampedY) {
                e.setPosition(clampedX, clampedY);
            }
        }
    }
    
    //clears screen and draw entities plus active drag indicators
    @Override
    public void render() {
        // Clear screen
        context.getGraphics().clearScreen(0.2f, 0.2f, 0.2f);

        context.getGraphics().beginShapes();
        super.render();

        // Draw drag line
        if (isDragging && draggedEntity != null) {
            context.getGraphics().setColor(1.0f, 0.0f, 0.0f, 1.0f);
            context.getGraphics().drawLine(dragStartPos.x, dragStartPos.y, dragCurrentPos.x, dragCurrentPos.y, 2);
        }
        context.getGraphics().endShapes();
    }

    // Input Listener Implementation
    //select entities for dragging or spawn new ones on right click
    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        float worldY = context.getDisplay().getHeight() - y;

        if (btn == Input.Buttons.RIGHT) {
            spawnDynamicEntity(x, worldY, true);
            return true;
        }

        if (btn == Input.Buttons.LEFT) {
            // Check intersection
            for (AbstractEntity e : getEntityManager().getEntities()) {
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

    //updates the position of the dragged entity relative to cursor movement
    @Override
    public boolean onDrag(int x, int y, int ptr) {
        if (isDragging && draggedEntity != null) {
            float worldY = context.getDisplay().getHeight() - y;
            dragCurrentPos.set(x, worldY);
            draggedEntity.setPosition(x - draggedEntity.getWidth()/2, worldY - draggedEntity.getHeight()/2);
            return true;
        }
        return false;
    }

    //releases the dragged entity and apply slingshot velocity
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

    //processes global scene commands like pausing and save/load
    @Override
    public boolean onKeyDown(int keycode) {
        float screenW = context.getDisplay().getWidth();
        float screenH = context.getDisplay().getHeight();

        switch (keycode) {
            case Input.Keys.ESCAPE:
                sceneManager.pushOverlay(sceneFactory.createPauseOverlay());
                return true;
            case Input.Keys.F5:
                boolean saved = world.saveCurrentState(getEntityManager(), screenW, screenH);
                if (saved) {
                    LOGGER.info("World state saved");
                } else {
                    LOGGER.log(Level.SEVERE, "Failed to save world state");
                }
                return true;
            case Input.Keys.F9:
                boolean loaded = world.loadSaveState(getEntityManager(), screenW, screenH);
                if (loaded) {
                    createBorderWalls(screenW, screenH);
                } else {
                    LOGGER.log(Level.SEVERE, "Failed to load saved world state");
                }
                return true;
            default:
                return false;
        }
    }

    //recalculates borders and clamp entity positions when the window size changes
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        getEntityManager().removeEntity("border_top");
        getEntityManager().removeEntity("border_bottom");
        getEntityManager().removeEntity("border_left");
        getEntityManager().removeEntity("border_right");

        float thickness = LogicConstants.BORDER_WALL_THICKNESS;
        getEntityManager().addEntity(new StaticEntity("border_top", 0, height - thickness, width, thickness, 0.5f, 0.5f, 0.5f));
        getEntityManager().addEntity(new StaticEntity("border_bottom", 0, 0, width, thickness, 0.5f, 0.5f, 0.5f));
        getEntityManager().addEntity(new StaticEntity("border_left", 0, 0, thickness, height, 0.5f, 0.5f, 0.5f));
        getEntityManager().addEntity(new StaticEntity("border_right", width - thickness, 0, thickness, height, 0.5f, 0.5f, 0.5f));

        float margin = LogicConstants.BORDER_WALL_THICKNESS;
        float maxX = width - margin;
        float maxY = height - margin;

        for (AbstractEntity e : getEntityManager().getEntities()) {
            if (e.getId().startsWith("border_") || e.isStatic()) continue;

            Vector2 pos = e.getPosition();
            float clampedX = Math.max(margin, Math.min(maxX - e.getWidth(), pos.x));
            float clampedY = Math.max(margin, Math.min(maxY - e.getHeight(), pos.y));

            if (pos.x != clampedX || pos.y != clampedY) {
                e.setPosition(clampedX, clampedY);
            }
        }
    }
}
