package com.sit.trafficking.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sit.trafficking.engine.managers.SceneManager;

public class SettingsScreen extends AbstractScreen {

    public SettingsScreen() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label volLabel = new Label("Volume: 100%", labelStyle);
        TextButton backBtn = new TextButton("Back", textButtonStyle);

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SceneManager.getInstance().popScreen();
            }
        });

        table.add(volLabel).pad(10).row();
        table.add(backBtn).pad(10).row();

        stage.addActor(table);
    }
}
