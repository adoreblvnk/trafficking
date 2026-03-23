package com.sit.recyclingpinball.logic.factories;

import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.managers.CollisionManager;
import com.sit.recyclingpinball.engine.managers.EntityManager;
import com.sit.recyclingpinball.engine.managers.InputManager;
import com.sit.recyclingpinball.engine.managers.MovementManager;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.logic.level.ILevelBlueprint;
import com.sit.recyclingpinball.logic.scenes.LevelSelectScene;
import com.sit.recyclingpinball.logic.scenes.MenuScene;
import com.sit.recyclingpinball.logic.scenes.SimulationScene;
import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle;
import com.sit.recyclingpinball.logic.states.IPinballState;
import com.sit.recyclingpinball.logic.states.IdleState;
import com.sit.recyclingpinball.logic.states.InPlayState;
import com.sit.recyclingpinball.logic.states.DrainedState;
import com.sit.recyclingpinball.logic.entities.PinballEntity;

import com.sit.recyclingpinball.logic.ui.PauseOverlay;
import com.sit.recyclingpinball.logic.ui.SimulationResultOverlay;

public class AssemblyFactory {

    private final IEngineContext context;
    private final SceneManager sceneManager;

    public AssemblyFactory(IEngineContext context, SceneManager sceneManager) {
        this.context = context;
        this.sceneManager = sceneManager;
    }

    public MenuScene createMenuScene() {
        return new MenuScene(context, sceneManager, this, new EntityManager(),
                new CollisionManager(new PlatformRectangle(0, 0, 1920, 1080)), new InputManager(),
                new MovementManager());
    }

    public LevelSelectScene createLevelSelectScene() {
        return new LevelSelectScene(context, sceneManager, this, new EntityManager(),
                new CollisionManager(new PlatformRectangle(0, 0, 1920, 1080)), new InputManager(),
                new MovementManager());
    }

    public SimulationScene createSimulationScene(ILevelBlueprint blueprint) {
        return new SimulationScene(context, sceneManager, this, blueprint, new EntityManager(),
                new CollisionManager(new PlatformRectangle(0, 0, 1920, 1080)), new InputManager(),
                new MovementManager());
    }

    public PauseOverlay createPauseOverlay() {
        return new PauseOverlay(context, sceneManager, this, new EntityManager(),
                new CollisionManager(new PlatformRectangle(0, 0, 1920, 1080)), new InputManager(),
                new MovementManager());
    }

    public SimulationResultOverlay createSimulationResultOverlay(boolean won, int score, int totalTrash,
            ILevelBlueprint blueprint) {
        return new SimulationResultOverlay(context, sceneManager, this, won, score, totalTrash, blueprint,
                new EntityManager(), new CollisionManager(new PlatformRectangle(0, 0, 1920, 1080)), new InputManager(),
                new MovementManager());
    }

    public IPinballState createInPlayState() {
        return new InPlayState();
    }

    public IPinballState createIdleState(PinballEntity entity) {
        return new IdleState(entity);
    }

    public IPinballState createDrainedState() {
        return new DrainedState();
    }
}
