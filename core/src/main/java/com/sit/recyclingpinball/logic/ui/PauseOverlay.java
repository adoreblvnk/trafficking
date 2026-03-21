package com.sit.recyclingpinball.logic.ui;

import com.sit.recyclingpinball.engine.interfaces.providers.EngineKey;

import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.managers.CollisionManager;
import com.sit.recyclingpinball.engine.managers.EntityManager;
import com.sit.recyclingpinball.engine.managers.InputManager;
import com.sit.recyclingpinball.engine.managers.MovementManager;
import com.sit.recyclingpinball.engine.scenes.AbstractScene;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.logic.scenes.MenuScene;

public class PauseOverlay extends AbstractScene implements InputListener {
    private final SceneManager sceneManager;

    public PauseOverlay(IEngineContext context, SceneManager sceneManager) {
        super(context, new EntityManager(), new CollisionManager(new com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle(0, 0, 1920, 1080)), new InputManager(), new MovementManager());
        this.sceneManager = sceneManager;
    }

    @Override
    public void create() {
        getInputManager().addListener(this);
    }

    @Override
    public void update(float dt) {
        // Pauses game time by not calling super.update(dt) or simply freezing physics
    }

    @Override
    public void render() {
        getContext().getGraphics().begin();
        // 60% opacity — dark enough to communicate "paused", light enough to see game state.
        // Button backgrounds behind text already ensure readability, so we don't need heavier dimming.
        getContext().getGraphics().fillRectangle(0, 0, 1900, 1000, 0, 0, 0, 0.6f);

        // Dark text on light buttons
        getContext().getGraphics().setTextColor(0.2f, 0.15f, 0.1f, 1f);

        // PAUSED title with button background
        float pauseBtnW = 384;
        float pauseBtnH = 80;
        float pauseBtnX = 950 - pauseBtnW / 2;
        float pauseBtnY = 580;
        getContext().getGraphics().drawTexture("button_rectangle_depth_flat", pauseBtnX, pauseBtnY, pauseBtnW, pauseBtnH);
        getContext().getGraphics().drawTextCentered("PAUSED", "Geist-Bold", pauseBtnX, pauseBtnY, pauseBtnW, pauseBtnH);

        // Resume button
        float btnW = 384;
        float btnH = 64;
        float btnX = 950 - btnW / 2;
        getContext().getGraphics().drawTexture("button_rectangle_depth_flat", btnX, 490, btnW, btnH);
        getContext().getGraphics().drawTextCentered("Resume", "Geist-Bold", btnX, 490, btnW, btnH);

        // Main Menu button
        getContext().getGraphics().drawTexture("button_rectangle_depth_flat", btnX, 410, btnW, btnH);
        getContext().getGraphics().drawTextCentered("Main Menu", "Geist-Bold", btnX, 410, btnW, btnH);

        // Reset text color to white
        getContext().getGraphics().setTextColor(1f, 1f, 1f, 1f);

        getContext().getGraphics().end();
    }

    private boolean isClicked(int screenX, int screenY, float btnX, float btnY, float btnW, float btnH) {
        float mappedY = getContext().getDisplay().getHeight() - screenY;
        return screenX >= btnX && screenX <= btnX + btnW && mappedY >= btnY && mappedY <= btnY + btnH;
    }

    @Override
    public boolean onKeyDown(EngineKey keycode) {
        if (keycode == EngineKey.ESCAPE) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.popScene();
            return true;
        } else if (keycode == EngineKey.M) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new MenuScene(getContext(), sceneManager));
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        float btnW = 384;
        float btnH = 64;
        float btnX = 950 - btnW / 2;

        if (isClicked(x, y, btnX, 490, btnW, btnH)) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.popScene();
            return true;
        }
        if (isClicked(x, y, btnX, 410, btnW, btnH)) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new MenuScene(getContext(), sceneManager));
            return true;
        }
        return false;
    }
    @Override public boolean onDrag(int x, int y, int ptr) { return false; }
    @Override public boolean onTouchUp(int x, int y, int ptr, int btn) { return false; }
}
