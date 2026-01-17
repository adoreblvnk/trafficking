package com.sit.trafficking.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sit.trafficking.engine.managers.SceneManager;

public class PauseScreen extends AbstractScreen {

    public PauseScreen() {
        // Semi-transparent background
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.5f);
        pixmap.fill();
        Image bg = new Image(new Texture(pixmap));
        bg.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        pixmap.dispose();
        
        stage.addActor(bg);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label pauseLabel = new Label("PAUSED", labelStyle);
        TextButton resumeBtn = new TextButton("Resume", textButtonStyle);
        TextButton settingsBtn = new TextButton("Settings", textButtonStyle);
        TextButton exitBtn = new TextButton("Exit to Menu", textButtonStyle);

        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SceneManager.getInstance().popScreen();
            }
        });

        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SceneManager.getInstance().pushScreen(new SettingsScreen());
            }
        });
        
        exitBtn.addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent event, float x, float y) {
                 SceneManager.getInstance().pushScreen(new MenuScreen());
             }
        });

        table.add(pauseLabel).pad(20).row();
        table.add(resumeBtn).size(300, 60).pad(10).row();
        table.add(settingsBtn).size(300, 60).pad(10).row();
        table.add(exitBtn).size(300, 60).pad(10).row();

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        // Keep the black background clear to prevent artifacts, 
        // though strictly we'd want to render the underlying screen for "transparency".
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }
}
