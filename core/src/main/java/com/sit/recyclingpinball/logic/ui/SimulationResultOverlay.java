package com.sit.recyclingpinball.logic.ui;

import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.managers.CollisionManager;
import com.sit.recyclingpinball.engine.managers.EntityManager;
import com.sit.recyclingpinball.engine.managers.InputManager;
import com.sit.recyclingpinball.engine.managers.MovementManager;
import com.sit.recyclingpinball.engine.scenes.AbstractScene;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.logic.scenes.MenuScene;

public class SimulationResultOverlay extends AbstractScene implements InputListener {
    private final SceneManager sceneManager;
    private final boolean isWin;

    public SimulationResultOverlay(IEngineContext context, SceneManager sceneManager, boolean isWin) {
        super(context, new EntityManager(), new CollisionManager(), new InputManager(), new MovementManager());
        this.sceneManager = sceneManager;
        this.isWin = isWin;
    }

    @Override
    public void create() {
        getInputManager().addListener(this);
        context.getAudio().loadSound("sounds/win.mp3", "win");
        context.getAudio().loadSound("sounds/lose.mp3", "lose");
        if (isWin) {
            context.getAudio().playSound("win", 1.0f);
        } else {
            context.getAudio().playSound("lose", 1.0f);
        }
    }

    @Override
    public void update(float dt) {}

    @Override
    public void render() {
        context.getGraphics().begin();
        context.getGraphics().fillRectangle(0, 0, 1900, 1000, 0, 0, 0, 0.7f);
        String text = isWin ? "YOU WIN!" : "GAME OVER!";
        context.getGraphics().drawText(text, "Geist-Bold", 800, 600);
        context.getGraphics().drawText("Click anywhere to return to Menu", "Geist-Bold", 800, 500);
        context.getGraphics().end();
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        sceneManager.setScene(new MenuScene(context, sceneManager));
        return true;
    }

    @Override public boolean onDrag(int x, int y, int ptr) { return false; }
    @Override public boolean onTouchUp(int x, int y, int ptr, int btn) { return false; }
}
