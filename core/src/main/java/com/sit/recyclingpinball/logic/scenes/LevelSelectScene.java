package com.sit.recyclingpinball.logic.scenes;

import com.sit.recyclingpinball.engine.interfaces.providers.EngineKey;

import com.sit.recyclingpinball.engine.scenes.AbstractScene;
import com.sit.recyclingpinball.engine.scenes.SceneManager;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.managers.*;
import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.logic.level.Level1Blueprint;
import com.sit.recyclingpinball.logic.level.Level2Blueprint;
import com.sit.recyclingpinball.logic.level.Level3Blueprint;
import com.sit.recyclingpinball.logic.level.Level4Blueprint;
import com.sit.recyclingpinball.logic.level.Level5Blueprint;

public class LevelSelectScene extends AbstractScene implements InputListener {
    private final SceneManager sceneManager;

    public LevelSelectScene(IEngineContext context, SceneManager sceneManager) {
        super(context, new EntityManager(), new CollisionManager(new com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformRectangle(0, 0, 1920, 1080)), new InputManager(), new MovementManager());
        this.sceneManager = sceneManager;
    }

    @Override
    public void create() {
        getInputManager().addListener(this);
    }

    @Override
    public void render() {
        getContext().getGraphics().clearScreen(0.1f, 0.1f, 0.1f);
        getContext().getGraphics().begin();

        // Full-screen dirty beach background
        getContext().getGraphics().drawTexture("dirty_beach", 0, 0, 1900, 1000);

        // Dark text for button labels
        getContext().getGraphics().setTextColor(0.2f, 0.15f, 0.1f, 1f);

        // Title with button background
        float titleBtnW = 384;
        float titleBtnH = 80;
        float titleBtnX = 950 - titleBtnW / 2;
        float titleBtnY = 670;
        getContext().getGraphics().drawTexture("button_rectangle_depth_flat", titleBtnX, titleBtnY, titleBtnW, titleBtnH);
        getContext().getGraphics().drawTextCentered("Level Select", "Geist-Bold", titleBtnX, titleBtnY, titleBtnW, titleBtnH);

        // Level 1 button
        float btnW = 480;
        float btnH = 64;
        float btnX = 950 - btnW / 2;
        getContext().getGraphics().drawTexture("button_rectangle_depth_flat", btnX, 570, btnW, btnH);
        getContext().getGraphics().drawTextCentered("Level 1", "Geist-Bold", btnX, 570, btnW, btnH);

        // Level 2 button
        getContext().getGraphics().drawTexture("button_rectangle_depth_flat", btnX, 480, btnW, btnH);
        getContext().getGraphics().drawTextCentered("Level 2", "Geist-Bold", btnX, 480, btnW, btnH);

        // Level 3 button
        getContext().getGraphics().drawTexture("button_rectangle_depth_flat", btnX, 390, btnW, btnH);
        getContext().getGraphics().drawTextCentered("Level 3", "Geist-Bold", btnX, 390, btnW, btnH);

        // Level 4 button
        getContext().getGraphics().drawTexture("button_rectangle_depth_flat", btnX, 300, btnW, btnH);
        getContext().getGraphics().drawTextCentered("Level 4", "Geist-Bold", btnX, 300, btnW, btnH);

        // Level 5 button
        getContext().getGraphics().drawTexture("button_rectangle_depth_flat", btnX, 210, btnW, btnH);
        getContext().getGraphics().drawTextCentered("Level 5", "Geist-Bold", btnX, 210, btnW, btnH);

        // Back button
        getContext().getGraphics().drawTexture("button_rectangle_depth_flat", btnX, 120, btnW, btnH);
        getContext().getGraphics().drawTextCentered("Back", "Geist-Bold", btnX, 120, btnW, btnH);

        // Reset text color to white
        getContext().getGraphics().setTextColor(1f, 1f, 1f, 1f);

        getContext().getGraphics().end();
        super.render();
    }

    private boolean isClicked(int screenX, int screenY, float btnX, float btnY, float btnW, float btnH) {
        float mappedY = getContext().getDisplay().getHeight() - screenY;
        return screenX >= btnX && screenX <= btnX + btnW && mappedY >= btnY && mappedY <= btnY + btnH;
    }

    @Override
    public boolean onKeyDown(EngineKey keycode) {
        if (keycode == EngineKey.NUM_1) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level1Blueprint()));
            return true;
        } else if (keycode == EngineKey.NUM_2) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level2Blueprint()));
            return true;
        } else if (keycode == EngineKey.NUM_3) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level3Blueprint()));
            return true;
        } else if (keycode == EngineKey.NUM_4) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level4Blueprint()));
            return true;
        } else if (keycode == EngineKey.NUM_5) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level5Blueprint()));
            return true;
        } else if (keycode == EngineKey.ESCAPE) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new MenuScene(getContext(), sceneManager));
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) {
        float btnW = 480;
        float btnH = 64;
        float btnX = 950 - btnW / 2;

        if (isClicked(x, y, btnX, 570, btnW, btnH)) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level1Blueprint()));
            return true;
        }
        if (isClicked(x, y, btnX, 480, btnW, btnH)) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level2Blueprint()));
            return true;
        }
        if (isClicked(x, y, btnX, 390, btnW, btnH)) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level3Blueprint()));
            return true;
        }
        if (isClicked(x, y, btnX, 300, btnW, btnH)) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level4Blueprint()));
            return true;
        }
        if (isClicked(x, y, btnX, 210, btnW, btnH)) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(getContext(), sceneManager, new Level5Blueprint()));
            return true;
        }
        if (isClicked(x, y, btnX, 120, btnW, btnH)) {
            getContext().getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new MenuScene(getContext(), sceneManager));
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
