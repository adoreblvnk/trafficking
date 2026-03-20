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
import com.sit.recyclingpinball.logic.scenes.SimulationScene;
import com.sit.recyclingpinball.logic.level.ILevelBlueprint;

public class SimulationResultOverlay extends AbstractScene implements InputListener {
    private final SceneManager sceneManager;
    private final boolean isWin;
    private final int score;
    private final int totalTrash;
    private final ILevelBlueprint blueprint;

    public SimulationResultOverlay(IEngineContext context, SceneManager sceneManager, boolean isWin, int score, int totalTrash, ILevelBlueprint blueprint) {
        super(context, new EntityManager(), new CollisionManager(), new InputManager(), new MovementManager());
        this.sceneManager = sceneManager;
        this.isWin = isWin;
        this.score = score;
        this.totalTrash = totalTrash;
        this.blueprint = blueprint;
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
        // Semi-transparent dark backdrop
        context.getGraphics().fillRectangle(0, 0, 1900, 1000, 0, 0, 0, 0.7f);

        // Dark text on light buttons
        context.getGraphics().setTextColor(0.2f, 0.15f, 0.1f, 1f);

        // Title text with button background
        String text = isWin ? "YOU WIN!" : "GAME OVER!";
        float titleBtnW = 384;
        float titleBtnH = 80;
        float titleBtnX = 950 - titleBtnW / 2;
        float titleBtnY = 600 - titleBtnH / 2;
        context.getGraphics().drawTexture("button_rectangle_depth_flat", titleBtnX, titleBtnY, titleBtnW, titleBtnH);
        context.getGraphics().drawText(text, "Geist-Bold", 950 - 80, 615);

        // Star icons showing collected trash
        float starsStartX = 950 - (totalTrash * 70) / 2.0f;
        float starsY = 480;
        for (int i = 0; i < totalTrash; i++) {
            float starX = starsStartX + i * 70;
            if (i < score) {
                context.getGraphics().drawTexture("star", starX, starsY, 64, 60);
            } else {
                context.getGraphics().fillRectangle(starX + 16, starsY + 14, 32, 32, 0.3f, 0.3f, 0.3f, 0.4f);
            }
        }

        // Score text (white on dark overlay — no button behind this)
        context.getGraphics().setTextColor(1f, 1f, 1f, 1f);
        context.getGraphics().drawText("Collected " + score + " / " + totalTrash + " trash", "Geist-Bold", 850, 470);

        // Return instruction with button background (dark text on button)
        context.getGraphics().setTextColor(0.2f, 0.15f, 0.1f, 1f);
        float retBtnW = 480;
        float retBtnH = 64;
        float retBtnX = 950 - retBtnW / 2;
        float retBtnY = 370;
        context.getGraphics().drawTexture("button_rectangle_depth_flat", retBtnX, retBtnY, retBtnW, retBtnH);
        context.getGraphics().drawText("Main Menu", "Geist-Bold", 890, 410);

        // Retry instruction with button background (dark text on button)
        float retryBtnY = 280;
        context.getGraphics().drawTexture("button_rectangle_depth_flat", retBtnX, retryBtnY, retBtnW, retBtnH);
        context.getGraphics().drawText("Retry", "Geist-Bold", 920, 320);

        // Reset text color to white
        context.getGraphics().setTextColor(1f, 1f, 1f, 1f);

        context.getGraphics().end();
    }

    private boolean isClicked(int screenX, int screenY, float btnX, float btnY, float btnW, float btnH) {
        float mappedY = context.getDisplay().getHeight() - screenY;
        return screenX >= btnX && screenX <= btnX + btnW && mappedY >= btnY && mappedY <= btnY + btnH;
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        float retBtnW = 480;
        float retBtnH = 64;
        float retBtnX = 950 - retBtnW / 2;
        float retBtnY = 370;
        float retryBtnY = 280;

        if (isClicked(x, y, retBtnX, retBtnY, retBtnW, retBtnH)) {
            context.getAudio().playSound("click", 0.6f);
            sceneManager.setScene(new MenuScene(context, sceneManager));
            return true;
        }
        if (isClicked(x, y, retBtnX, retryBtnY, retBtnW, retBtnH)) 
            context.getAudio().playSound("click", 0.6f);
            sceneManager.setScene(new SimulationScene(context, sceneManager, blueprint));
            return true;
        }
        return false;
    }

    @Override public boolean onDrag(int x, int y, int ptr) { return false; }
    @Override public boolean onTouchUp(int x, int y, int ptr, int btn) { return false; }
}
