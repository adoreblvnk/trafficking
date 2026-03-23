package com.sit.recyclingpinball.logic.scenes;

import com.sit.recyclingpinball.engine.interfaces.providers.EngineKey;

import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.managers.CollisionManager;
import com.sit.recyclingpinball.engine.managers.EntityManager;
import com.sit.recyclingpinball.engine.managers.InputManager;
import com.sit.recyclingpinball.engine.managers.MovementManager;
import com.sit.recyclingpinball.engine.scenes.AbstractScene;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.logic.LogicConstants;
import com.sit.recyclingpinball.logic.entities.FlipperEntity;
import com.sit.recyclingpinball.logic.entities.PinballEntity;
import com.sit.recyclingpinball.logic.entities.TrashEntity;
import com.sit.recyclingpinball.logic.entities.WallEntity;
import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.events.TrashCollectedEvent;
import com.sit.recyclingpinball.logic.events.PinballEventVisitor;
import com.sit.recyclingpinball.logic.events.BallDrainedEvent;
import com.sit.recyclingpinball.logic.level.BoardBuilder;
import com.sit.recyclingpinball.logic.level.BoardLayout;
import com.sit.recyclingpinball.logic.level.ILevelBlueprint;
import com.sit.recyclingpinball.logic.managers.GameAudioManager;
import com.sit.recyclingpinball.logic.managers.GameScoreManager;

public class SimulationScene extends AbstractScene implements InputListener, PinballEventVisitor {
    private final SceneManager sceneManager;
    private final ILevelBlueprint blueprint;
    private PinballEventBus eventBus;
    private GameScoreManager scoreManager;
    private PinballEntity pinball;
    private int totalTrash;

    private final com.sit.recyclingpinball.logic.factories.AssemblyFactory assemblyFactory;

    public SimulationScene(IEngineContext context, SceneManager sceneManager,
            com.sit.recyclingpinball.logic.factories.AssemblyFactory assemblyFactory, ILevelBlueprint blueprint,
            EntityManager entityManager, CollisionManager collisionManager, InputManager inputManager,
            MovementManager movementManager) {
        super(context, entityManager, collisionManager, inputManager, movementManager);
        this.sceneManager = sceneManager;
        this.assemblyFactory = assemblyFactory;
        this.blueprint = blueprint;
    }

    @Override
    public void create() {
        getInputManager().addListener(this);

        eventBus = new PinballEventBus();
        eventBus.register(this);

        BoardBuilder builder = new BoardBuilder();
        BoardLayout layout = blueprint.construct(builder, eventBus);

        totalTrash = layout.getTrashes().size();
        scoreManager = new GameScoreManager(eventBus, totalTrash);
        new GameAudioManager(getContext().getAudio(), eventBus);

        for (WallEntity w : layout.getWalls()) {
            getEntityManager().addEntity(w);
        }
        for (FlipperEntity f : layout.getFlippers()) {
            getEntityManager().addEntity(f);
            getInputManager().addListener(f);
        }
        for (TrashEntity t : layout.getTrashes()) {
            getEntityManager().addEntity(t);
        }

        com.sit.recyclingpinball.logic.entities.ShooterRodEntity shooterRod = layout.getShooterRod();
        if (shooterRod != null) {
            getEntityManager().addEntity(shooterRod);
            getInputManager().addListener(shooterRod);
            // It automatically registers with MovementManager and CollisionManager via
            // EntityManager
        }

        pinball = new PinballEntity(LogicConstants.TAG_PINBALL, LogicConstants.PINBALL_START[0],
                LogicConstants.PINBALL_START[1], eventBus, assemblyFactory);
        getEntityManager().addEntity(pinball);
        getInputManager().addListener(pinball);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (scoreManager.isWon()) {
            sceneManager.pushOverlay(assemblyFactory.createSimulationResultOverlay(true, scoreManager.getScore(),
                    totalTrash, blueprint));
        } else if (scoreManager.isLost()) {
            sceneManager.pushOverlay(assemblyFactory.createSimulationResultOverlay(false, scoreManager.getScore(),
                    totalTrash, blueprint));
        }
    }

    @Override
    public void visit(BallDrainedEvent event) {
        if (scoreManager.getBallsLeft() > 0) {
            // Respawn logic
            pinball.setPosition(LogicConstants.PINBALL_START[0], LogicConstants.PINBALL_START[1]);
            pinball.setVelocity(0, 0);
            pinball.setState(assemblyFactory.createInPlayState());
        }
    }

    @Override
    public void visit(TrashCollectedEvent event) {
        getEntityManager().removeEntity(event.getEntityId());
    }

    @Override
    public void render() {
        getContext().getGraphics().clearScreen(LogicConstants.COLOR_SIM_BG[0], LogicConstants.COLOR_SIM_BG[1],
                LogicConstants.COLOR_SIM_BG[2]);

        // 1. Draw Background
        getContext().getGraphics().drawTexture(LogicConstants.TEX_BEACH_BACKGROUND, 0, 0, LogicConstants.SCENE_SIZE[0],
                LogicConstants.SCENE_SIZE[1]);
        getContext().getGraphics().end();

        // 2. Draw Game Entities (Walls, Flippers, Trash, Pinball)
        super.render();
        getContext().getGraphics().end();

        // 3. Draw UI Overlay
        getContext().getGraphics().drawTexture(LogicConstants.TEX_UI_PANEL_BG, 0, 0, 400, LogicConstants.SCENE_SIZE[1]);
        getContext().getGraphics().drawText(LogicConstants.TEXT_SCORE_PREFIX + scoreManager.getScore(),
                LogicConstants.FONT_GEIST_BOLD, LogicConstants.UI_SCORE_POS[0], LogicConstants.UI_SCORE_POS[1]);
        getContext().getGraphics().drawText(LogicConstants.TEXT_BALLS_PREFIX + scoreManager.getBallsLeft(),
                LogicConstants.FONT_GEIST_BOLD, LogicConstants.UI_BALLS_POS[0], LogicConstants.UI_BALLS_POS[1]);
        getContext().getGraphics().drawText(blueprint.getText(), LogicConstants.FONT_GEIST_BOLD,
                LogicConstants.UI_DESC_POS[0], LogicConstants.UI_DESC_POS[1], LogicConstants.UI_DESC_WIDTH);

        // 4. Draw star icons for collected trash
        int collected = scoreManager.getScore();
        for (int i = 0; i < totalTrash; i++) {
            int row = i / 4;
            int col = i % 4;
            float starX = LogicConstants.UI_STAR_START[0] + col * LogicConstants.UI_STAR_SPACING;
            float starY = LogicConstants.UI_STAR_START[1] - row * LogicConstants.UI_STAR_SPACING;
            if (i < collected) {
                getContext().getGraphics().drawTexture(LogicConstants.TEX_STAR, starX, starY, 64, 60);
            } else {
                // Draw dimmed placeholder — dark rectangle behind unfilled star position
                getContext().getGraphics().fillRectangle(starX + 16, starY + 14, 32, 32, 0.3f, 0.3f, 0.3f, 0.4f);
            }
        }

        getContext().getGraphics().end();
    }

    @Override
    public boolean onKeyDown(EngineKey keycode) {
        if (keycode == EngineKey.ESCAPE) {
            sceneManager.pushOverlay(assemblyFactory.createPauseOverlay());
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        return false;
    }

    @Override
    public boolean onDrag(int x, int y, int ptr) {
        return false;
    }

    @Override
    public boolean onTouchUp(int x, int y, int ptr, int btn) {
        return false;
    }
}
