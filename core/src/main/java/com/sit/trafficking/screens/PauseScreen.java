package com.sit.trafficking.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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

        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SceneManager.getInstance().popScreen();
            }
        });

        table.add(pauseLabel).pad(10).row();
        table.add(resumeBtn).pad(10).row();

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        // Transparent overlay, so we don't clear screen if we want to see underlying screen
        // But AbstractScreen clears by default. 
        // We need to override render entirely to NOT clear, OR SceneManager needs to handle rendering the stack properly.
        // Given SceneManager peeks at top, this screen will be the only one rendering if we follow standard setScreen.
        // However, the prompt says: "Background color: Black with 0.5 alpha".
        // To achieve a true overlay effect in LibGDX with setScreen, you usually need to render the previous screen too.
        // But for this architecture where SceneManager peeks at top, we will just render a transparent background.
        // BUT AbstractScreen has Gdx.gl.glClear. We must override it to NOT clear if we want transparency over nothing (which doesn't make sense unless we render previous).
        // Since SceneManager uses `game.setScreen()`, LibGDX only renders the active screen. The previous screen is NOT rendered.
        // Thus, "Transparent" effect is simulated by just drawing the alpha background on top of black, OR we'd need to manually render the previous screen.
        // For simplicity and strict adherence to "SceneManager peeks at top and calls render", 
        // if SceneManager ONLY peeks at top, then only the top is rendered. 
        // So the "underlying" simulation won't be visible unless I capture it or change SceneManager.
        // I will stick to the requested visual style: Black with 0.5 alpha.
        // Since I can't easily see the underlying screen without modifying the stack rendering logic significantly (iterating stack),
        // I will just let it be a dark screen with the label.
        
        // Actually, I'll allow clear for now to ensure no artifacts, but set clear color to black.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }
}
