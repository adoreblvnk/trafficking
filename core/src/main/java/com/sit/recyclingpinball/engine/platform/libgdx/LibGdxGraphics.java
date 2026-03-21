package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.sit.recyclingpinball.engine.EngineConstants;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 * libGDX implementation of IGraphicsProvider.
 * Encapsulates ShapeRenderer and GL20 operations for platform-independent
 * rendering.
 * The ShapeRenderer is NOT exposed; all interactions happen through this
 * interface.
 */
public class LibGdxGraphics implements IGraphicsProvider {

    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final Map<String, Texture> textures;
    private BitmapFont font;
    private boolean isShapeBatchOpen = false;

    private final Map<String, BitmapFont> fonts = new HashMap<>();

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

    private boolean isSpriteBatchOpen = false;

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

            // Register font by its simple name (e.g. "Geist-Bold")
            String simpleName = fontPath;
            int lastSlash = fontPath.lastIndexOf('/');
            if (lastSlash >= 0)
                simpleName = simpleName.substring(lastSlash + 1);
            int lastDot = simpleName.lastIndexOf('.');
            if (lastDot >= 0)
                simpleName = simpleName.substring(0, lastDot);
            fonts.put(simpleName, generatedFont);

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
        if (font == null) {
            return;
        }

        ensureSpriteBatch();
        applyTextColor(font);
        font.draw(spriteBatch, text, x, y);
    }

    @Override
    public void drawText(String text, String fontName, float x, float y) {
        BitmapFont targetFont = fonts.get(fontName);
        if (targetFont == null) {
            String path = EngineConstants.FONTS_DIR + fontName + EngineConstants.FONT_EXTENSION;
            if (Gdx.files.internal(path).exists()) {
                loadFont(path, 24);
                targetFont = fonts.get(fontName);
            }
            if (targetFont == null)
                targetFont = font;
        }

        if (targetFont == null)
            return;

        ensureSpriteBatch();
        applyTextColor(targetFont);
        targetFont.draw(spriteBatch, text, x, y);
    }

    @Override
    public void drawText(String text, String fontName, float x, float y, float targetWidth) {
        BitmapFont targetFont = fonts.get(fontName);
        if (targetFont == null) {
            String path = EngineConstants.FONTS_DIR + fontName + EngineConstants.FONT_EXTENSION;
            if (Gdx.files.internal(path).exists()) {
                loadFont(path, 24);
                targetFont = fonts.get(fontName);
            }
            if (targetFont == null)
                targetFont = font;
        }

        if (targetFont == null)
            return;

        ensureSpriteBatch();
        applyTextColor(targetFont);
        targetFont.draw(spriteBatch, text, x, y, targetWidth, com.badlogic.gdx.utils.Align.center, true);
    }

    private final GlyphLayout glyphLayout = new GlyphLayout();

    @Override
    public void drawTextCentered(String text, String fontName, float x, float y, float width, float height) {
        BitmapFont targetFont = fonts.get(fontName);
        if (targetFont == null) {
            String path = EngineConstants.FONTS_DIR + fontName + EngineConstants.FONT_EXTENSION;
            if (Gdx.files.internal(path).exists()) {
                loadFont(path, 24);
                targetFont = fonts.get(fontName);
            }
            if (targetFont == null)
                targetFont = font;
        }

        if (targetFont == null)
            return;

        ensureSpriteBatch();
        applyTextColor(targetFont);
        
        glyphLayout.setText(targetFont, text);
        float textX = x + (width - glyphLayout.width) / 2f;
        float textY = y + (height + glyphLayout.height) / 2f;
        
        targetFont.draw(spriteBatch, text, textX, textY);
    }

    @Override
    public void fillRectangle(float x, float y, float w, float h, float r, float g, float b, float alpha) {
        // Must end any open batches first
        if (isShapeBatchOpen) {
            shapeRenderer.end();
            isShapeBatchOpen = false;
        }
        if (isSpriteBatchOpen) {
            spriteBatch.end();
            isSpriteBatchOpen = false;
        }
        // Enable blending BEFORE begin, draw, then end() flushes vertices while
        // blending is still active
        enableBlend();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(r, g, b, alpha);
        shapeRenderer.rect(x, y, w, h);
        shapeRenderer.end();
        disableBlend();
    }

    @Override
    public void drawTexture(String textureId, float x, float y, float w, float h) {
        Texture texture = textures.get(textureId);
        if (texture == null) {
            texture = new Texture(Gdx.files.internal(EngineConstants.TEXTURES_DIR + textureId + EngineConstants.TEXTURE_EXTENSION));
            textures.put(textureId, texture);
        }
        ensureSpriteBatch();
        spriteBatch.draw(texture, x, y, w, h);
    }

    @Override
    public void drawTexture(String textureId, float x, float y, float width, float height, float originX, float originY,
            float rotationDegrees) {
        Texture texture = textures.get(textureId);
        if (texture == null) {
            texture = new Texture(Gdx.files.internal(EngineConstants.TEXTURES_DIR + textureId + EngineConstants.TEXTURE_EXTENSION));
            textures.put(textureId, texture);
        }
        ensureSpriteBatch();
        spriteBatch.draw(texture, x, y, originX, originY, width, height, 1.0f, 1.0f, rotationDegrees, 0, 0,
                texture.getWidth(), texture.getHeight(), false, false);
    }

    @Override
    public void begin() {
        // Now mostly a no-op marker to start frame
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
