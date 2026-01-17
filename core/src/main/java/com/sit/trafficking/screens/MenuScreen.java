package com.sit.trafficking.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sit.trafficking.engine.managers.SceneManager;

public class MenuScreen extends AbstractScreen {

    public MenuScreen() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        TextButton startBtn = new TextButton("Start Simulation", textButtonStyle);
        TextButton settingsBtn = new TextButton("Settings", textButtonStyle);
        TextButton exitBtn = new TextButton("Exit", textButtonStyle);

        startBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SceneManager.getInstance().pushScreen(new SimulationScreen());
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
                Gdx.app.exit();
            }
        });

        table.add(startBtn).size(400, 80).pad(15).row();
        table.add(settingsBtn).size(400, 80).pad(15).row();
        table.add(exitBtn).size(400, 80).pad(15).row();

        stage.addActor(table);
    }
}
