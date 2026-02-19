package com.sit.trafficking.logic.scenes;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.sit.trafficking.engine.interfaces.InputListener;
import com.sit.trafficking.engine.scenes.AbstractScene;
import com.sit.trafficking.engine.scenes.SceneManager;
import com.sit.trafficking.logic.LogicConstants;

public class MenuScene extends AbstractScene implements InputListener {

    private SpriteBatch batch;
    
    @Override
    public void create() {
        batch = new SpriteBatch();
        loadFont(LogicConstants.FONT_SIZE_MENU);
        inputManager.addListener(this);
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public boolean onKeyDown(int keycode) {
        if (keycode == Input.Keys.ENTER) {
            SceneManager.getInstance().setScene(new SimulationScene());
            return true;
        }
        return false;
    }

    // Required Interface Methods
    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) { return false; }

    @Override
    public boolean onDrag(int x, int y, int ptr) { return false; }

    @Override
    public boolean onTouchUp(int x, int y, int ptr, int btn) { return false; }

    @Override
    public void render() {
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);
        
        batch.begin();
        font.draw(batch, "TRAFFICKING SIMULATION", LogicConstants.MENU_TITLE_X, LogicConstants.MENU_TITLE_Y);
        font.draw(batch, "Press ENTER to Start", LogicConstants.MENU_TITLE_X, LogicConstants.MENU_SUBTITLE_Y);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }
}
