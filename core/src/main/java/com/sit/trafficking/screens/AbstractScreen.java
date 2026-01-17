package com.sit.trafficking.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class AbstractScreen implements Screen {
    protected Stage stage;
    protected Viewport viewport;
    protected ShapeRenderer shapeRenderer;
    
    // UI Styles
    protected TextButton.TextButtonStyle textButtonStyle;
    protected Label.LabelStyle labelStyle;
    protected BitmapFont fontRegular;
    protected BitmapFont fontBold;

    public AbstractScreen() {
        this.viewport = new FitViewport(1280, 720);
        this.stage = new Stage(viewport);
        this.shapeRenderer = new ShapeRenderer();
        createUiStyles();
    }

    private void createUiStyles() {
        // Generate fonts using FreeType
        fontRegular = generateFont("fonts/Geist-Regular.ttf", 24);
        fontBold = generateFont("fonts/Geist-Bold.ttf", 24);
        
        // 1x1 White Pixel
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture whiteTexture = new Texture(pixmap);
        pixmap.dispose();
        
        TextureRegionDrawable whiteDrawable = new TextureRegionDrawable(new TextureRegion(whiteTexture));

        // Button Style (Using Regular Font)
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = fontRegular;
        textButtonStyle.up = whiteDrawable.tint(Color.GRAY);
        textButtonStyle.down = whiteDrawable.tint(Color.DARK_GRAY);
        textButtonStyle.over = whiteDrawable.tint(Color.LIGHT_GRAY);
        
        // Label Style (Using Regular Font by default)
        labelStyle = new Label.LabelStyle();
        labelStyle.font = fontRegular;
        labelStyle.fontColor = Color.WHITE;
    }

    private BitmapFont generateFont(String path, int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    @Override
    public void render(float delta) {
        // Clear screen black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void show() { Gdx.input.setInputProcessor(stage); }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
        if (fontRegular != null) fontRegular.dispose();
        if (fontBold != null) fontBold.dispose();
    }
}
