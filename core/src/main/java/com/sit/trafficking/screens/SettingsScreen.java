package com.sit.trafficking.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sit.trafficking.engine.managers.SceneManager;
import com.sit.trafficking.utils.Constants;

public class SettingsScreen extends AbstractScreen {

    private Label volLabel;

    public SettingsScreen() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        volLabel = new Label("Volume: " + (int)Constants.VOLUME + "%", labelStyle);
        // Center alignment for text in label
        volLabel.setAlignment(1); 
        
        TextButton decreaseBtn = new TextButton("-", textButtonStyle);
        TextButton increaseBtn = new TextButton("+", textButtonStyle);
        TextButton backBtn = new TextButton("Back", textButtonStyle);

        decreaseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.VOLUME = Math.max(0, Constants.VOLUME - 10);
                updateLabel();
            }
        });

        increaseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Constants.VOLUME = Math.min(100, Constants.VOLUME + 10);
                updateLabel();
            }
        });

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SceneManager.getInstance().popScreen();
            }
        });

        // Volume Row
        Table volTable = new Table();
        volTable.add(decreaseBtn).size(50, 50).pad(10);
        volTable.add(volLabel).pad(10).width(250); 
        volTable.add(increaseBtn).size(50, 50).pad(10);

        table.add(volTable).row();
        table.add(backBtn).size(200, 60).pad(30).row();

        stage.addActor(table);
    }

    private void updateLabel() {
        volLabel.setText("Volume: " + (int)Constants.VOLUME + "%");
    }
}
