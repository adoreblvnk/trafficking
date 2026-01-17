package com.sit.trafficking.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.sit.trafficking.engine.managers.SceneManager;

public class LoadingScreen extends AbstractScreen {

    private float timeElapsed;

    public LoadingScreen() {
        Table table = new Table();
        table.setFillParent(true);
        
        Label loadingLabel = new Label("LOADING...", labelStyle);
        table.add(loadingLabel);
        
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        
        timeElapsed += delta;
        if (timeElapsed > 1f) {
            SceneManager.getInstance().pushScreen(new MenuScreen());
        }
    }
}
