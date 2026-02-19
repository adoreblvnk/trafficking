package com.sit.trafficking.logic.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sit.trafficking.engine.interfaces.InputListener;
import com.sit.trafficking.engine.scenes.AbstractScene;
import com.sit.trafficking.engine.scenes.SceneManager;
import com.sit.trafficking.logic.LogicConstants;

public class PauseOverlay extends AbstractScene implements InputListener {

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
        if (keycode == Input.Keys.ESCAPE) {
            SceneManager.getInstance().popScene();
            return true;
        }
        return false;
    }

    // Implement required interface methods (can be empty/false)
    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) { return false; }
    @Override
    public boolean onDrag(int x, int y, int ptr) { return false; }
    @Override
    public boolean onTouchUp(int x, int y, int ptr, int btn) { return false; }

    @Override
    public void render() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, LogicConstants.OVERLAY_ALPHA);
        shapeRenderer.rect(0, 0, screenWidth, screenHeight);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
        font.draw(batch, "PAUSED", screenWidth / 2f - 50, screenHeight / 2f + 20);
        font.draw(batch, "Press ESC to Resume", screenWidth / 2f - 100, screenHeight / 2f - 30);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }
}
