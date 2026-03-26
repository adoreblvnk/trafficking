package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

/**
 * Rendering adapter for libGDX draw operations.
 *
 * Asset lookup is delegated to PlatformAssetManager so this class focuses on
 * draw state and render commands instead of cache ownership.
 */

// Acts as a concrete facade to prevent circular dependencies between Platform and Engine.
public class PlatformGraphics {

    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final PlatformAssetManager assetManager;
    private final GlyphLayout glyphLayout;

    private boolean isShapeBatchOpen = false;
    private boolean isSpriteBatchOpen = false;
    private boolean isDisposed = false;

    private float textR = 1f;
    private float textG = 1f;
    private float textB = 1f;
    private float textA = 1f;

    public PlatformGraphics(PlatformAssetManager assetManager) {
        this.shapeRenderer = new ShapeRenderer();
        this.spriteBatch = new SpriteBatch();
        this.assetManager = assetManager;
        this.glyphLayout = new GlyphLayout();
        enableBlend();
    }

    public void clearScreen(float r, float g, float b) {
        Gdx.gl.glClearColor(r, g, b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void setColor(float r, float g, float b, float a) {
        shapeRenderer.setColor(r, g, b, a);
    }

    private void ensureShapeBatch(ShapeRenderer.ShapeType type) {
        if (isSpriteBatchOpen) {
            spriteBatch.end();
            isSpriteBatchOpen = false;
        }
        if (isShapeBatchOpen && shapeRenderer.getCurrentType() != type) {
            shapeRenderer.end();
            isShapeBatchOpen = false;
        }
        if (!isShapeBatchOpen) {
            enableBlend();
            shapeRenderer.begin(type);
            isShapeBatchOpen = true;
        }
    }

    private void ensureShapeBatch() {
        ensureShapeBatch(ShapeRenderer.ShapeType.Filled);
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

    public void drawRect(float x, float y, float width, float height) {
        ensureShapeBatch();
        shapeRenderer.rect(x, y, width, height);
    }

    public void drawLine(float x1, float y1, float x2, float y2, float r, float g, float b, float a) {
        ensureShapeBatch(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(r, g, b, a);
        shapeRenderer.line(x1, y1, x2, y2);
    }

    public void drawLine(float x1, float y1, float x2, float y2, float width) {
        ensureShapeBatch();
        shapeRenderer.rectLine(x1, y1, x2, y2, width);
    }

    public void beginShapes() {
        ensureShapeBatch();
    }

    public void endShapes() {
        if (isShapeBatchOpen) {
            shapeRenderer.end();
            isShapeBatchOpen = false;
        }
    }

    public void enableBlend() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void disableBlend() {
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void setProjectionMatrix(float width, float height) {
        Matrix4 projection = new Matrix4().setToOrtho2D(0, 0, width, height);
        shapeRenderer.setProjectionMatrix(projection);
        spriteBatch.setProjectionMatrix(projection);
    }

    public boolean loadTextureResource(String path) {
        return assetManager.loadTextureResource(path);
    }

    public boolean loadTextureResource(String path, String id) {
        return assetManager.loadTextureResource(path, id);
    }

    public boolean loadFontResource(String path, int size) {
        return assetManager.loadFontResource(path, size);
    }

    public Object getTextureResource(String id) {
        return assetManager.getTextureResource(id);
    }

    public Object getFontResource(String id) {
        return assetManager.getFontResource(id);
    }

    public void setTextColor(float r, float g, float b, float a) {
        this.textR = r;
        this.textG = g;
        this.textB = b;
        this.textA = a;
    }

    private void applyTextColor(BitmapFont font) {
        font.setColor(textR, textG, textB, textA);
    }

    public void drawText(String text, float x, float y) {
        drawText(text, null, x, y);
    }

    public void drawText(String text, String fontId, float x, float y) {
        BitmapFont font = assetManager.getFontResource(fontId);
        if (font == null) {
            return;
        }
        ensureSpriteBatch();
        applyTextColor(font);
        font.draw(spriteBatch, text, x, y);
    }

    public void drawText(String text, String fontId, float x, float y, float targetWidth) {
        BitmapFont font = assetManager.getFontResource(fontId);
        if (font == null) {
            return;
        }
        ensureSpriteBatch();
        applyTextColor(font);
        font.draw(spriteBatch, text, x, y, targetWidth, com.badlogic.gdx.utils.Align.center, true);
    }

    public void drawTextCentered(String text, String fontId, float x, float y, float width, float height) {
        BitmapFont font = assetManager.getFontResource(fontId);
        if (font == null) {
            return;
        }
        ensureSpriteBatch();
        applyTextColor(font);

        glyphLayout.setText(font, text);
        float textX = x + (width - glyphLayout.width) / 2f;
        float textY = y + (height + glyphLayout.height) / 2f;

        font.draw(spriteBatch, text, textX, textY);
    }

    public void fillRectangle(float x, float y, float w, float h, float r, float g, float b, float alpha) {
        ensureShapeBatch();
        shapeRenderer.setColor(r, g, b, alpha);
        shapeRenderer.rect(x, y, w, h);
    }

    public void drawTexture(String textureId, float x, float y, float w, float h) {
        Texture texture = assetManager.getTextureResource(textureId);
        if (texture == null) {
            return;
        }
        ensureSpriteBatch();
        spriteBatch.draw(texture, x, y, w, h);
    }

    public void drawTexture(String textureId, float x, float y, float width, float height, float originX, float originY,
            float rotationDegrees) {
        Texture texture = assetManager.getTextureResource(textureId);
        if (texture == null) {
            return;
        }
        ensureSpriteBatch();
        spriteBatch.draw(texture, x, y, originX, originY, width, height, 1.0f, 1.0f, rotationDegrees, 0, 0,
                texture.getWidth(), texture.getHeight(), false, false);
    }

    public void begin() {
    }

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
            Gdx.app.error("PlatformGraphics", "Failed to dispose resources", e);
        }
    }
}
