package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import com.sit.recyclingpinball.engine.managers.AssetManager;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.graphics.Texture;

/**
 * libGDX implementation of IGraphicsProvider. Now delegates asset management to
 * the AssetManager singleton.
 */
public class LibGdxGraphics implements IGraphicsProvider {

    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private boolean isShapeBatchOpen = false;
    private boolean isSpriteBatchOpen = false;
    private boolean isDisposed = false;
    private final AssetManager assetManager;

    public LibGdxGraphics(AssetManager assetManager) {
        this.shapeRenderer = new ShapeRenderer();
        this.spriteBatch = new SpriteBatch();
        this.assetManager = assetManager;
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

    private void ensureShapeBatch() {
        if (isSpriteBatchOpen) {
            spriteBatch.end();
            isSpriteBatchOpen = false;
        }
        if (!isShapeBatchOpen) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            isShapeBatchOpen = true;
        }
    }

    private void ensureSpriteBatch() {
        if (isShapeBatchOpen) {
            shapeRenderer.end();
            isShapeBatchOpen = false;
        }
        if (!isSpriteBatchOpen) {
            spriteBatch.begin();
            isSpriteBatchOpen = true;
        }
    }

    @Override
    public void drawRect(float x, float y, float width, float height) {
        ensureShapeBatch();
        shapeRenderer.rect(x, y, width, height);
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2, float r, float g, float b, float a) {
        if (isSpriteBatchOpen) {
            spriteBatch.end();
            isSpriteBatchOpen = false;
        }
        if (isShapeBatchOpen) {
            shapeRenderer.end();
            isShapeBatchOpen = false;
        }
        enableBlend();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(r, g, b, a);
        shapeRenderer.line(x1, y1, x2, y2);
        shapeRenderer.end();
        disableBlend();
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2, float width) {
        ensureShapeBatch();
        shapeRenderer.rectLine(x1, y1, x2, y2, width);
    }

    @Override
    public void beginShapes() {
        ensureShapeBatch();
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
    public Object loadTextureResource(String path) {
        try {
            return new Texture(Gdx.files.internal(path));
        } catch (Exception e) {
            Gdx.app.error("LibGdxGraphics", "Failed to load texture: " + path, e);
            return null;
        }
    }

    @Override
    public Object loadFontResource(String path, int size) {
        FreeTypeFontGenerator generator = null;
        try {
            generator = new FreeTypeFontGenerator(Gdx.files.internal(path));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = size;
            return generator.generateFont(parameter);
        } catch (Exception e) {
            Gdx.app.error("LibGdxGraphics", "Failed to load font: " + path, e);
            return null;
        } finally {
            if (generator != null) {
                generator.dispose();
            }
        }
    }

    private float textR = 1f, textG = 1f, textB = 1f, textA = 1f;

    @Override
    public void setTextColor(float r, float g, float b, float a) {
        this.textR = r;
        this.textG = g;
        this.textB = b;
        this.textA = a;
    }

    private void applyTextColor(BitmapFont f) {
        f.setColor(textR, textG, textB, textA);
    }

    @Override
    public void drawText(String text, float x, float y) {
        // Default text drawing with fallback
        drawText(text, null, x, y);
    }

    @Override
    public void drawText(String text, String fontId, float x, float y) {
        BitmapFont f = (BitmapFont) assetManager.getFont(fontId);
        if (f == null)
            return;
        ensureSpriteBatch();
        applyTextColor(f);
        f.draw(spriteBatch, text, x, y);
    }

    @Override
    public void drawText(String text, String fontId, float x, float y, float targetWidth) {
        BitmapFont f = (BitmapFont) assetManager.getFont(fontId);
        if (f == null)
            return;
        ensureSpriteBatch();
        applyTextColor(f);
        f.draw(spriteBatch, text, x, y, targetWidth, com.badlogic.gdx.utils.Align.center, true);
    }

    private final GlyphLayout glyphLayout = new GlyphLayout();

    @Override
    public void drawTextCentered(String text, String fontId, float x, float y, float width, float height) {
        BitmapFont f = (BitmapFont) assetManager.getFont(fontId);
        if (f == null)
            return;
        ensureSpriteBatch();
        applyTextColor(f);

        glyphLayout.setText(f, text);
        float textX = x + (width - glyphLayout.width) / 2f;
        float textY = y + (height + glyphLayout.height) / 2f;

        f.draw(spriteBatch, text, textX, textY);
    }

    @Override
    public void fillRectangle(float x, float y, float w, float h, float r, float g, float b, float alpha) {
        if (isShapeBatchOpen) {
            shapeRenderer.end();
            isShapeBatchOpen = false;
        }
        if (isSpriteBatchOpen) {
            spriteBatch.end();
            isSpriteBatchOpen = false;
        }
        enableBlend();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(r, g, b, alpha);
        shapeRenderer.rect(x, y, w, h);
        shapeRenderer.end();
        disableBlend();
    }

    @Override
    public void drawTexture(String textureId, float x, float y, float w, float h) {
        Texture texture = (Texture) assetManager.getTexture(textureId);
        if (texture == null)
            return;
        ensureSpriteBatch();
        spriteBatch.draw(texture, x, y, w, h);
    }

    @Override
    public void drawTexture(String textureId, float x, float y, float width, float height, float originX, float originY,
            float rotationDegrees) {
        Texture texture = (Texture) assetManager.getTexture(textureId);
        if (texture == null)
            return;
        ensureSpriteBatch();
        spriteBatch.draw(texture, x, y, originX, originY, width, height, 1.0f, 1.0f, rotationDegrees, 0, 0,
                texture.getWidth(), texture.getHeight(), false, false);
    }

    @Override
    public void begin() {
    }

    @Override
    public void end() {
        if (isShapeBatchOpen) {
            shapeRenderer.end();
            isShapeBatchOpen = false;
        }
        if (isSpriteBatchOpen) {
            spriteBatch.end();
            isSpriteBatchOpen = false;
        }
    }

    @Override
    public void disposeTextureResource(Object texture) {
        if (texture != null) {
            ((Texture) texture).dispose();
        }
    }

    @Override
    public void disposeFontResource(Object font) {
        if (font != null) {
            ((BitmapFont) font).dispose();
        }
    }

    @Override
    public void dispose() {
        if (isDisposed) {
            return;
        }

        try {
            end();
            shapeRenderer.dispose();
            spriteBatch.dispose();
            isDisposed = true;
        } catch (Exception e) {
            Gdx.app.error("LibGdxGraphics", "Failed to dispose resources", e);
        }
    }
}
