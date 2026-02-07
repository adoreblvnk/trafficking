package com.sit.trafficking.logic.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sit.trafficking.engine.scenes.AbstractScene;
import com.sit.trafficking.engine.scenes.SceneManager;
import com.sit.trafficking.logic.LogicConstants;

public class PauseOverlay extends AbstractScene {

    private SpriteBatch batch;
    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // Default font for simplicity in overlay
        font.setColor(Color.WHITE);
        font.getData().setScale(2.0f);
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            SceneManager.getInstance().popScene();
        }
    }

    @Override
    public void render() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, LogicConstants.OVERLAY_ALPHA);
        shapeRenderer.rect(0, 0, LogicConstants.SCREEN_WIDTH, LogicConstants.SCREEN_HEIGHT);
        shapeRenderer.end();
        
        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
        font.draw(batch, "PAUSED", LogicConstants.SCREEN_WIDTH / 2f - 50, LogicConstants.SCREEN_HEIGHT / 2f);
        font.draw(batch, "Press ESC to Resume", LogicConstants.SCREEN_WIDTH / 2f - 100, LogicConstants.SCREEN_HEIGHT / 2f - 40);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();
    }
}
