package com.sit.recyclingpinball.logic.factories;

import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.managers.CollisionManager;
import com.sit.recyclingpinball.engine.managers.EntityManager;
import com.sit.recyclingpinball.engine.managers.InputManager;
import com.sit.recyclingpinball.engine.managers.MovementManager;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.logic.LogicConstants;
import com.sit.recyclingpinball.logic.level.ILevelBlueprint;
import com.sit.recyclingpinball.logic.scenes.LevelSelectScene;
import com.sit.recyclingpinball.logic.scenes.MenuScene;
import com.sit.recyclingpinball.logic.scenes.SimulationScene;
import com.sit.recyclingpinball.logic.ui.PauseOverlay;
import com.sit.recyclingpinball.logic.ui.SimulationResultOverlay;

public class SceneFactory {

    private final IEngineContext context;
    private final SceneManager sceneManager;
    private final StateFactory stateFactory;

    public SceneFactory(IEngineContext context, SceneManager sceneManager, StateFactory stateFactory) {
        this.context = context;
        this.sceneManager = sceneManager;
        this.stateFactory = stateFactory;
    }

    public MenuScene createMenuScene() {
        return new MenuScene(context, sceneManager, this, new EntityManager(),
                new CollisionManager(LogicConstants.SCENE_SIZE[0], LogicConstants.SCENE_SIZE[1]), new InputManager(),
                new MovementManager());
    }

    public LevelSelectScene createLevelSelectScene() {
        return new LevelSelectScene(context, sceneManager, this, new EntityManager(),
                new CollisionManager(LogicConstants.SCENE_SIZE[0], LogicConstants.SCENE_SIZE[1]), new InputManager(),
                new MovementManager());
    }

    public SimulationScene createSimulationScene(ILevelBlueprint blueprint) {
        return new SimulationScene(context, sceneManager, this, stateFactory, blueprint, new EntityManager(),
                new CollisionManager(LogicConstants.SCENE_SIZE[0], LogicConstants.SCENE_SIZE[1]), new InputManager(),
                new MovementManager());
    }

    public PauseOverlay createPauseOverlay() {
        return new PauseOverlay(context, sceneManager, this, new EntityManager(),
                new CollisionManager(LogicConstants.SCENE_SIZE[0], LogicConstants.SCENE_SIZE[1]), new InputManager(),
                new MovementManager());
    }

    public SimulationResultOverlay createSimulationResultOverlay(boolean won, int score, int totalTrash,
            ILevelBlueprint blueprint) {
        return new SimulationResultOverlay(context, sceneManager, this, won, score, totalTrash, blueprint,
                new EntityManager(), new CollisionManager(LogicConstants.SCENE_SIZE[0], LogicConstants.SCENE_SIZE[1]),
                new InputManager(), new MovementManager());
    }
}
