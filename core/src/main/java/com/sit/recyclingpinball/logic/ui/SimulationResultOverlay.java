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

    public SimulationResultOverlay(IEngineContext context, SceneManager sceneManager, boolean isWin, int score,
            int totalTrash, ILevelBlueprint blueprint) {
        super(context, new EntityManager(), new CollisionManager(new com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle(0, 0, 1920, 1080)), new InputManager(), new MovementManager());
        this.sceneManager = sceneManager;
        this.isWin = isWin;
        this.score = score;
        this.totalTrash = totalTrash;
        this.blueprint = blueprint;
    }

    @Override
    public void create() {
        getInputManager().addListener(this);
        if (isWin) {
            getContext().getAudio().playSound("win", 1.0f);
        } else {
            getContext().getAudio().playSound("lose", 1.0f);
        }
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render() {
        getContext().getGraphics().begin();
        // Semi-transparent dark backdrop
        getContext().getGraphics().fillRectangle(0, 0, 1900, 1000, 0, 0, 0, 0.7f);

        // Dark text on light buttons
        getContext().getGraphics().setTextColor(0.2f, 0.15f, 0.1f, 1f);

        // Title text with button background
        String text = isWin ? "YOU WIN!" : "GAME OVER!";
        float titleBtnW = 384;
        float titleBtnH = 80;
        float titleBtnX = 950 - titleBtnW / 2;
        float titleBtnY = 600 - titleBtnH / 2;
        getContext().getGraphics().drawTexture("button_rectangle_depth_flat", titleBtnX, titleBtnY, titleBtnW, titleBtnH);
        getContext().getGraphics().drawTextCentered(text, "Geist-Bold", titleBtnX, titleBtnY, titleBtnW, titleBtnH);

        // Star icons showing collected trash
        float starsStartX = 950 - (totalTrash * 70) / 2.0f;
        float starsY = 480;
        for (int i = 0; i < totalTrash; i++) {
            float starX = starsStartX + i * 70;
            if (i < score) {
                getContext().getGraphics().drawTexture("star", starX, starsY, 64, 60);
            } else {
                getContext().getGraphics().fillRectangle(starX + 16, starsY + 14, 32, 32, 0.3f, 0.3f, 0.3f, 0.4f);
            }
        }

        // Score text (white on dark overlay — no button behind this)
        getContext().getGraphics().setTextColor(1f, 1f, 1f, 1f);
        // We'll leave the score text non-centered or we could center it if we calculate width. Let's keep it as is.
        getContext().getGraphics().drawText("Collected " + score + " / " + totalTrash + " trash", "Geist-Bold", 850, 470);

        // Return instruction with button background (dark text on button)
        getContext().getGraphics().setTextColor(0.2f, 0.15f, 0.1f, 1f);
        float retBtnW = 480;
        float retBtnH = 64;
        float retBtnX = 950 - retBtnW / 2;
        float retBtnY = 370;
        getContext().getGraphics().drawTexture("button_rectangle_depth_flat", retBtnX, retBtnY, retBtnW, retBtnH);
        getContext().getGraphics().drawTextCentered("Main Menu", "Geist-Bold", retBtnX, retBtnY, retBtnW, retBtnH);

        // Retry instruction with button background (dark text on button)
        float retryBtnY = 280;
        getContext().getGraphics().drawTexture("button_rectangle_depth_flat", retBtnX, retryBtnY, retBtnW, retBtnH);
        getContext().getGraphics().drawTextCentered("Retry", "Geist-Bold", retBtnX, retryBtnY, retBtnW, retBtnH);

        // Reset text color to white
        getContext().getGraphics().setTextColor(1f, 1f, 1f, 1f);

        getContext().getGraphics().end();
    }

    private boolean isClicked(int screenX, int screenY, float btnX, float btnY, float btnW, float btnH) {
        float mappedY = getContext().getDisplay().getHeight() - screenY;
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
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new MenuScene(getContext(), sceneManager));
            return true;
        }
        if (isClicked(x, y, retBtnX, retryBtnY, retBtnW, retBtnH)) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, blueprint));
            return true;
        }
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
