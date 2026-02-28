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

    //sets up rendering tools and register for input events
    @Override
    public void create() {
        batch = new SpriteBatch();
        loadFont(LogicConstants.FONT_SIZE_MENU);
        getInputManager().addListener(this);
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

    //unused touch interactions for the pause screen
    @Override
    public boolean onTouchDown(int x, int y, int ptr, int btn) { return false; }
    @Override
    public boolean onDrag(int x, int y, int ptr) { return false; }
    @Override
    public boolean onTouchUp(int x, int y, int ptr, int btn) { return false; }

    //renders a semi-transparent background and pause text
    @Override
    public void render() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
        getShapeRenderer().setColor(0, 0, 0, LogicConstants.OVERLAY_ALPHA);
        getShapeRenderer().rect(0, 0, screenWidth, screenHeight);
        getShapeRenderer().end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
        getFont().draw(batch, "PAUSED", screenWidth / 2f - 50, screenHeight / 2f + 20);
        getFont().draw(batch, "Press ESC to Resume", screenWidth / 2f - 100, screenHeight / 2f - 30);
        batch.end();
    }

    //disposes of batch resources and call super disposal
    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }
}
