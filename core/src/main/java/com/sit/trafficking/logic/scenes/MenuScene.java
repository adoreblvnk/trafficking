package com.sit.trafficking.logic.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.sit.trafficking.engine.interfaces.InputListener;
import com.sit.trafficking.engine.scenes.AbstractScene;
import com.sit.trafficking.engine.scenes.SceneManager;
import com.sit.trafficking.logic.LogicConstants;

public class MenuScene extends AbstractScene implements InputListener {

    private SpriteBatch batch;

    //initializes resources, load menu-specific fonts, and register for input events
    @Override
    public void create() {
        batch = new SpriteBatch();
        loadFont(LogicConstants.FONT_SIZE_MENU);
        inputManager.addListener(this);
    }

    @Override
    public void update(float dt) {
    }

    //handles scene transitions based on keyboard input
    @Override
    public boolean onKeyDown(int keycode) {
        if (keycode == Input.Keys.ENTER) {
            SceneManager.getInstance().setScene(new SimulationScene());
            return true;
        }
        return false;
    }

    //required interface methods - unused for specific scene
    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) { return false; }

    @Override
    public boolean onDrag(int x, int y, int ptr) { return false; }

    @Override
    public boolean onTouchUp(int x, int y, int ptr, int btn) { return false; }

    //draws the menu title and navigation instructions to the screen
    @Override
    public void render() {
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        batch.begin();
        font.draw(batch, "TRAFFICKING SIMULATION", centerX - 200, centerY + 50);
        font.draw(batch, "Press ENTER to Start", centerX - 150, centerY - 20);
        batch.end();
    }

    //cleans up native resources and call superclass disposal logic
    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }
}
