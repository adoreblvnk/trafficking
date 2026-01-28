package com.sit.trafficking.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.sit.trafficking.engine.managers.SoundManager;
import com.sit.trafficking.engine.scenes.AbstractScene;
import com.sit.trafficking.engine.scenes.SceneManager;
import com.sit.trafficking.utils.Constants;

/**
 * Initial scene to load assets.
 */
public class LoadingScene extends AbstractScene {

    @Override
    public void create() {
        // Start loading assets
        SoundManager.getInstance().load();
    }

    @Override
    public void update(float dt) {
        // We need to check if AssetManager is done.
        // SoundManager wraps AssetManager but doesn't expose update() directly in my interface.
        // I should probably add an update() or isFinished() to SoundManager or access it.
        // For this assignment, I'll assume SoundManager handles the AssetManager calls internally or I added a method.
        // Let's modify SoundManager or just assume it finishes quickly for "demo" purposes, 
        // BUT strict requirements say "Checks AssetManager.update()".
        // I will access it via a getter or add a helper in SoundManager. 
        // Re-reading SoundManager code: I didn't add update(). I'll fix SoundManager or just do it here strictly?
        // Wait, SoundManager is a Singleton. I can't easily access its internal AssetManager if private.
        // I will update SoundManager.java to include update() method in a moment.
        // For now, I'll write this class assuming SoundManager has an update() method that returns boolean.
        
        if (SoundManager.getInstance().update()) {
             SceneManager.getInstance().setScene(new MenuScene());
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        // Simple progress bar
        float progress = SoundManager.getInstance().getProgress();
        shapeRenderer.rect(100, Constants.SCREEN_HEIGHT / 2f - 25, (Constants.SCREEN_WIDTH - 200) * progress, 50);
        shapeRenderer.end();
    }
}
