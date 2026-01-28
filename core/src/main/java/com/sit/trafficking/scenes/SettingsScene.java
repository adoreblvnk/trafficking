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
import com.sit.trafficking.engine.managers.SoundManager;
import com.sit.trafficking.engine.scenes.AbstractScene;
import com.sit.trafficking.engine.scenes.SceneManager;
import com.sit.trafficking.engine.ui.EngineUIFactory;

public class SettingsScene extends AbstractScene {

    private Stage stage;
    private Label volumeLabel;

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label title = new Label("Settings", EngineUIFactory.getLabelStyle());
        table.add(title).padBottom(50).colspan(3).row();

        // Volume Control
        TextButton btnMinus = new TextButton("-", EngineUIFactory.getButtonStyle());
        btnMinus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeVolume(-0.1f);
            }
        });

        volumeLabel = new Label(getVolumeText(), EngineUIFactory.getLabelStyle());

        TextButton btnPlus = new TextButton("+", EngineUIFactory.getButtonStyle());
        btnPlus.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeVolume(0.1f);
            }
        });

        table.add(btnMinus).width(50).height(50).padRight(10);
        table.add(volumeLabel).width(200).padRight(10); // Adjust width as needed
        table.add(btnPlus).width(50).height(50).row();

        // Back Button
        TextButton btnBack = new TextButton("Back", EngineUIFactory.getButtonStyle());
        btnBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SceneManager.getInstance().setScene(new MenuScene());
            }
        });
        table.add(btnBack).padTop(50).colspan(3).width(200).height(50).row();
    }

    private void changeVolume(float delta) {
        float currentVolume = SoundManager.getInstance().getVolume();
        SoundManager.getInstance().setVolume(currentVolume + delta);
        volumeLabel.setText(getVolumeText());
    }

    private String getVolumeText() {
        int volumePercent = Math.round(SoundManager.getInstance().getVolume() * 100);
        return "Volume: " + volumePercent + "%";
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
