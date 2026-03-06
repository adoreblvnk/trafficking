package com.sit.covid26.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.sit.covid26.engine.interfaces.providers.IGraphicsProvider;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 * libGDX implementation of IGraphicsProvider.
 * Encapsulates ShapeRenderer and GL20 operations for platform-independent rendering.
 * The ShapeRenderer is NOT exposed; all interactions happen through this interface.
 */
public class LibGdxGraphics implements IGraphicsProvider {

    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final Map<String, Texture> textures;
    private BitmapFont font;
    private boolean isShapeBatchOpen = false;

    public LibGdxGraphics() {
        this.shapeRenderer = new ShapeRenderer();
        this.spriteBatch = new SpriteBatch();
        this.textures = new HashMap<>();
    }

    @Override
    public void clearScreen(float r, float g, float b) {
        Gdx.gl.glClearColor(r, g, b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void setColor(float r, float g, float b, float a) {
        shapeRenderer.setColor(r, g, b, a);
    }

    @Override
    public void drawRect(float x, float y, float width, float height) {
        if (!isShapeBatchOpen) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            isShapeBatchOpen = true;
        }
        shapeRenderer.rect(x, y, width, height);
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2, float width) {
        if (!isShapeBatchOpen) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            isShapeBatchOpen = true;
        }
        shapeRenderer.rectLine(x1, y1, x2, y2, width);
    }

    @Override
    public void beginShapes() {
        if (!isShapeBatchOpen) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            isShapeBatchOpen = true;
        }
    }

    @Override
    public void endShapes() {
        if (isShapeBatchOpen) {
            shapeRenderer.end();
            isShapeBatchOpen = false;
        }
    }

    @Override
    public void enableBlend() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void disableBlend() {
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void setProjectionMatrix(float width, float height) {
        Matrix4 projection = new Matrix4().setToOrtho2D(0, 0, width, height);
        shapeRenderer.setProjectionMatrix(projection);
        spriteBatch.setProjectionMatrix(projection);
    }

    @Override
    public boolean loadFont(String fontPath, int size) {
        FreeTypeFontGenerator generator = null;
        try {
            generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = size;
            BitmapFont generatedFont = generator.generateFont(parameter);

            if (font != null) {
                font.dispose();
            }

            font = generatedFont;
            return true;
        } catch (Exception e) {
            Gdx.app.error("LibGdxGraphics", "Failed to load font: " + fontPath, e);
            return false;
        } finally {
            if (generator != null) {
                generator.dispose();
            }
        }
    }

    @Override
    public void drawText(String text, float x, float y) {
        if (font == null) {
            return;
        }

        spriteBatch.begin();
        font.draw(spriteBatch, text, x, y);
        spriteBatch.end();
    }

    @Override
    public void drawTexture(String textureId, float x, float y, float w, float h) {
        Texture texture = textures.get(textureId);
        if (texture == null) {
            texture = new Texture(Gdx.files.internal("textures/" + textureId + ".png"));
            textures.put(textureId, texture);
        }
        spriteBatch.draw(texture, x, y, w, h);
    }

    @Override
    public void begin() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        spriteBatch.begin();
    }

    @Override
    public void end() {
        spriteBatch.end();
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        try {
            endShapes();
            shapeRenderer.dispose();
        } catch (Exception e) {
            Gdx.app.error("LibGdxGraphics", "Failed to dispose ShapeRenderer", e);
        }

        try {
            if (font != null) {
                font.dispose();
            }
        } catch (Exception e) {
            Gdx.app.error("LibGdxGraphics", "Failed to dispose BitmapFont", e);
        }

        try {
            spriteBatch.dispose();
        } catch (Exception e) {
            Gdx.app.error("LibGdxGraphics", "Failed to dispose SpriteBatch", e);
        }
    }

}
