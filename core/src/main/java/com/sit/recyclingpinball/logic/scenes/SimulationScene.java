package com.sit.recyclingpinball.logic.scenes;

import com.badlogic.gdx.Input;
import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.managers.CollisionManager;
import com.sit.recyclingpinball.engine.managers.EntityManager;
import com.sit.recyclingpinball.engine.managers.InputManager;
import com.sit.recyclingpinball.engine.managers.MovementManager;
import com.sit.recyclingpinball.engine.scenes.AbstractScene;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.logic.entities.FlipperEntity;
import com.sit.recyclingpinball.logic.entities.PinballEntity;
import com.sit.recyclingpinball.logic.entities.TrashEntity;
import com.sit.recyclingpinball.logic.entities.WallEntity;
import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.events.TrashCollectedEvent;
import com.sit.recyclingpinball.logic.level.BoardBuilder;
import com.sit.recyclingpinball.logic.level.BoardLayout;
import com.sit.recyclingpinball.logic.level.ILevelBlueprint;
import com.sit.recyclingpinball.logic.managers.GameAudioManager;
import com.sit.recyclingpinball.logic.managers.GameScoreManager;
import com.sit.recyclingpinball.logic.ui.PauseOverlay;
import com.sit.recyclingpinball.logic.ui.SimulationResultOverlay;

public class SimulationScene extends AbstractScene implements InputListener {
    private final SceneManager sceneManager;
    private final ILevelBlueprint blueprint;
    private PinballEventBus eventBus;
    private GameScoreManager scoreManager;
    private GameAudioManager audioManager;
    private PinballEntity pinball;
    private int totalTrash;

    public SimulationScene(IEngineContext context, SceneManager sceneManager, ILevelBlueprint blueprint) {
        super(context, new EntityManager(), new CollisionManager(), new InputManager(), new MovementManager());
        this.sceneManager = sceneManager;
        this.blueprint = blueprint;
    }

    @Override
    public void create() {
        getInputManager().addListener(this);

        eventBus = new PinballEventBus();

        BoardBuilder builder = new BoardBuilder();
        BoardLayout layout = blueprint.construct(builder);

        totalTrash = layout.getTrashes().size();
        scoreManager = new GameScoreManager(eventBus, totalTrash);
        audioManager = new GameAudioManager(context.getAudio(), eventBus);

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

        pinball = new PinballEntity("pinball", 1801, 400, eventBus);
        getEntityManager().addEntity(pinball);
        getInputManager().addListener(pinball);

        pinball.setCollisionListener((a, b) -> {
            if (b instanceof TrashEntity) {
                TrashEntity t = (TrashEntity) b;
                eventBus.post(new TrashCollectedEvent(t.getType()));
                getEntityManager().removeEntity(t.getId());
            }
        });
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (scoreManager.isWon()) {
            sceneManager.pushOverlay(new SimulationResultOverlay(context, sceneManager, true, scoreManager.getScore(), totalTrash));
        } else if (scoreManager.isLost()) {
            sceneManager.pushOverlay(new SimulationResultOverlay(context, sceneManager, false, scoreManager.getScore(), totalTrash));
        } else if (pinball.getPosition().y < -50 && scoreManager.getBallsLeft() > 0) {
            // Respawn logic
            pinball.setPosition(1801, 400);
            pinball.setVelocity(0, 0);
            pinball.setState(new com.sit.recyclingpinball.logic.states.IdleState());
        }
    }

    @Override
    public void render() {
        context.getGraphics().clearScreen(0.8f, 0.9f, 1.0f);
        
        // 1. Draw Background
        context.getGraphics().drawTexture("beach_background", 0, 0, 1900, 1000);
        context.getGraphics().end();

        // 2. Draw Game Entities (Walls, Flippers, Trash, Pinball)
        super.render();
        context.getGraphics().end();

        // 3. Draw UI Overlay
        context.getGraphics().drawTexture("ui_panel_bg", 0, 0, 400, 1000);
        context.getGraphics().drawText("Score: " + scoreManager.getScore(), "Geist-Bold", 50, 900);
        context.getGraphics().drawText("Balls: " + scoreManager.getBallsLeft(), "Geist-Bold", 50, 850);
        context.getGraphics().drawText(blueprint.getEducationalText(), "Geist-Bold", 50, 800, 300);

        // 4. Draw star icons for collected trash
        int collected = scoreManager.getScore();
        for (int i = 0; i < totalTrash; i++) {
            float starX = 50 + i * 70;
            float starY = 100;
            if (i < collected) {
                context.getGraphics().drawTexture("star", starX, starY, 64, 60);
            } else {
                // Draw dimmed placeholder — dark rectangle behind unfilled star position
                context.getGraphics().fillRectangle(starX + 16, starY + 14, 32, 32, 0.3f, 0.3f, 0.3f, 0.4f);
            }
        }

        context.getGraphics().end();
    }

    @Override
    public boolean onKeyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            sceneManager.pushOverlay(new PauseOverlay(context, sceneManager));
            return true;
        }
        return false;
    }

    @Override public boolean onTouchDown(int x, int y, int ptr, int btn) { return false; }
    @Override public boolean onDrag(int x, int y, int ptr) { return false; }
    @Override public boolean onTouchUp(int x, int y, int ptr, int btn) { return false; }
}
