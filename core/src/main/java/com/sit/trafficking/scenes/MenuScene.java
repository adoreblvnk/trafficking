package com.sit.trafficking.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sit.trafficking.engine.scenes.AbstractScene;
import com.sit.trafficking.engine.scenes.SceneManager;
import com.sit.trafficking.engine.ui.EngineUIFactory;

/**
 * Main Menu Scene.
 */
public class MenuScene extends AbstractScene {

    private Stage stage;

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label title = new Label("Trafficking Engine Demo", EngineUIFactory.getLabelStyle());
        table.add(title).padBottom(50).row();

        TextButton btnRun = new TextButton("Run Demo", EngineUIFactory.getButtonStyle());
        btnRun.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SceneManager.getInstance().setScene(new SimulationScene());
            }
        });
        table.add(btnRun).padBottom(20).width(200).height(50).row();

        TextButton btnSettings = new TextButton("Settings", EngineUIFactory.getButtonStyle());
        btnSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SceneManager.getInstance().setScene(new SettingsScene());
            }
        });
        table.add(btnSettings).padBottom(20).width(200).height(50).row();

        TextButton btnExit = new TextButton("Exit", EngineUIFactory.getButtonStyle());
        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        table.add(btnExit).width(200).height(50).row();
    }

    @Override
    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }
}
