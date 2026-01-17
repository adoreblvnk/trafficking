package com.sit.trafficking.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.sit.trafficking.engine.managers.SceneManager;
import com.sit.trafficking.engine.managers.SoundManager;

public class LoadingScreen extends AbstractScreen {

    private Label loadingLabel;

    public LoadingScreen() {
        Table table = new Table();
        table.setFillParent(true);
        
        loadingLabel = new Label("LOADING ASSETS...", labelStyle);
        table.add(loadingLabel);
        
        stage.addActor(table);
        
        // Queue assets
        SoundManager.getInstance().queueAssets();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        
        if (SoundManager.getInstance().updateLoading()) {
            // Assets loaded, wait a tiny bit or proceed
            SceneManager.getInstance().pushScreen(new MenuScreen());
        } else {
             // Optional: Show progress
             float progress = SoundManager.getInstance().getProgress() * 100;
             loadingLabel.setText("LOADING... " + (int)progress + "%");
        }
    }
}
