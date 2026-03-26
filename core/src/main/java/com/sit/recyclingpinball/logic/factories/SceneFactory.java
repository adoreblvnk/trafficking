package com.sit.recyclingpinball.logic.factories;

import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.interfaces.ICollisionManager;
import com.sit.recyclingpinball.engine.interfaces.IEntityManager;
import com.sit.recyclingpinball.engine.interfaces.IInputManager;
import com.sit.recyclingpinball.engine.interfaces.IMovementManager;
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
        IEntityManager entityManager = new EntityManager();
        ICollisionManager collisionManager = new CollisionManager(LogicConstants.SCENE_SIZE[0],
                LogicConstants.SCENE_SIZE[1]);
        IInputManager inputManager = new InputManager();
        IMovementManager movementManager = new MovementManager();

        return new MenuScene(context, sceneManager, this, entityManager, collisionManager, inputManager,
                movementManager);
    }

    public LevelSelectScene createLevelSelectScene() {
        IEntityManager entityManager = new EntityManager();
        ICollisionManager collisionManager = new CollisionManager(LogicConstants.SCENE_SIZE[0],
                LogicConstants.SCENE_SIZE[1]);
        IInputManager inputManager = new InputManager();
        IMovementManager movementManager = new MovementManager();

        return new LevelSelectScene(context, sceneManager, this, entityManager, collisionManager, inputManager,
                movementManager);
    }

    public SimulationScene createSimulationScene(ILevelBlueprint blueprint) {
        IEntityManager entityManager = new EntityManager();
        ICollisionManager collisionManager = new CollisionManager(LogicConstants.SCENE_SIZE[0],
                LogicConstants.SCENE_SIZE[1]);
        IInputManager inputManager = new InputManager();
        IMovementManager movementManager = new MovementManager();

        return new SimulationScene(context, sceneManager, this, stateFactory, blueprint, entityManager,
                collisionManager, inputManager, movementManager);
    }

    public PauseOverlay createPauseOverlay() {
        IEntityManager entityManager = new EntityManager();
        ICollisionManager collisionManager = new CollisionManager(LogicConstants.SCENE_SIZE[0],
                LogicConstants.SCENE_SIZE[1]);
        IInputManager inputManager = new InputManager();
        IMovementManager movementManager = new MovementManager();

        return new PauseOverlay(context, sceneManager, this, entityManager, collisionManager, inputManager,
                movementManager);
    }

    public SimulationResultOverlay createSimulationResultOverlay(boolean won, int score, int totalTrash,
            ILevelBlueprint blueprint) {
        IEntityManager entityManager = new EntityManager();
        ICollisionManager collisionManager = new CollisionManager(LogicConstants.SCENE_SIZE[0],
                LogicConstants.SCENE_SIZE[1]);
        IInputManager inputManager = new InputManager();
        IMovementManager movementManager = new MovementManager();

        return new SimulationResultOverlay(context, sceneManager, this, won, score, totalTrash, blueprint,
                entityManager, collisionManager, inputManager, movementManager);
    }
}
