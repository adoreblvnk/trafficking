package com.sit.recyclingpinball.logic.scenes;

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
        super(context, new EntityManager(), new CollisionManager(), new InputManager(), new MovementManager());
        this.sceneManager = sceneManager;
    }

    @Override
    public void create() {
        getInputManager().addListener(this);
    }

    @Override
    public void render() {
        context.getGraphics().clearScreen(0.1f, 0.1f, 0.1f);
        context.getGraphics().begin();

        // Full-screen dirty beach background
        context.getGraphics().drawTexture("dirty_beach", 0, 0, 1900, 1000);

        // Dark text for button labels
        context.getGraphics().setTextColor(0.2f, 0.15f, 0.1f, 1f);

        // Title with button background
        float titleBtnW = 384;
        float titleBtnH = 80;
        float titleBtnX = 950 - titleBtnW / 2;
        float titleBtnY = 670;
        context.getGraphics().drawTexture("button_rectangle_depth_flat", titleBtnX, titleBtnY, titleBtnW, titleBtnH);
        context.getGraphics().drawText("Level Select", "Geist-Bold", 870, 720);

        // Level 1 button
        float btnW = 480;
        float btnH = 64;
        float btnX = 950 - btnW / 2;
        context.getGraphics().drawTexture("button_rectangle_depth_flat", btnX, 570, btnW, btnH);
        context.getGraphics().drawText("Level 1", "Geist-Bold", 900, 610);

        // Level 2 button
        context.getGraphics().drawTexture("button_rectangle_depth_flat", btnX, 480, btnW, btnH);
        context.getGraphics().drawText("Level 2", "Geist-Bold", 900, 520);

        // Level 3 button
        context.getGraphics().drawTexture("button_rectangle_depth_flat", btnX, 390, btnW, btnH);
        context.getGraphics().drawText("Level 3", "Geist-Bold", 900, 430);

        // Level 4 button
        context.getGraphics().drawTexture("button_rectangle_depth_flat", btnX, 300, btnW, btnH);
        context.getGraphics().drawText("Level 4", "Geist-Bold", 900, 340);

        // Level 5 button
        context.getGraphics().drawTexture("button_rectangle_depth_flat", btnX, 210, btnW, btnH);
        context.getGraphics().drawText("Level 5", "Geist-Bold", 900, 250);

        // Back button
        context.getGraphics().drawTexture("button_rectangle_depth_flat", btnX, 120, btnW, btnH);
        context.getGraphics().drawText("Back", "Geist-Bold", 930, 160);

        // Reset text color to white
        context.getGraphics().setTextColor(1f, 1f, 1f, 1f);

        context.getGraphics().end();
        super.render();
    }

    private boolean isClicked(int screenX, int screenY, float btnX, float btnY, float btnW, float btnH) {
        float mappedY = context.getDisplay().getHeight() - screenY;
        return screenX >= btnX && screenX <= btnX + btnW && mappedY >= btnY && mappedY <= btnY + btnH;
    }

    @Override
    public boolean onKeyDown(int keycode) {
        if (keycode == com.badlogic.gdx.Input.Keys.NUM_1) {
            context.getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(context, sceneManager, new Level1Blueprint()));
            return true;
        } else if (keycode == com.badlogic.gdx.Input.Keys.NUM_2) {
            context.getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(context, sceneManager, new Level2Blueprint()));
            return true;
        } else if (keycode == com.badlogic.gdx.Input.Keys.NUM_3) {
            context.getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(context, sceneManager, new Level3Blueprint()));
            return true;
        } else if (keycode == com.badlogic.gdx.Input.Keys.NUM_4) {
            context.getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(context, sceneManager, new Level4Blueprint()));
            return true;
        } else if (keycode == com.badlogic.gdx.Input.Keys.NUM_5) {
            context.getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(context, sceneManager, new Level5Blueprint()));
            return true;
        } else if (keycode == com.badlogic.gdx.Input.Keys.ESCAPE) {
            context.getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new MenuScene(context, sceneManager));
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
            context.getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(context, sceneManager, new Level1Blueprint()));
            return true;
        }
        if (isClicked(x, y, btnX, 480, btnW, btnH)) {
            context.getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(context, sceneManager, new Level2Blueprint()));
            return true;
        }
        if (isClicked(x, y, btnX, 390, btnW, btnH)) {
            context.getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(context, sceneManager, new Level3Blueprint()));
            return true;
        }
        if (isClicked(x, y, btnX, 300, btnW, btnH)) {
            context.getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(context, sceneManager, new Level4Blueprint()));
            return true;
        }
        if (isClicked(x, y, btnX, 210, btnW, btnH)) {
            context.getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new SimulationScene(context, sceneManager, new Level5Blueprint()));
            return true;
        }
        if (isClicked(x, y, btnX, 120, btnW, btnH)) {
            context.getAudio().playSound("click", 1.0f);
            sceneManager.setScene(new MenuScene(context, sceneManager));
            return true;
        }
        return false;
    }
    @Override public boolean onDrag(int x, int y, int ptr) { return false; }
    @Override public boolean onTouchUp(int x, int y, int ptr, int btn) { return false; }
}
