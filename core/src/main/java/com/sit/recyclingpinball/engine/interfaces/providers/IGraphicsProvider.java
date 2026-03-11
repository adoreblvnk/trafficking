package com.sit.recyclingpinball.engine.interfaces.providers;

/**
 * Platform-independent graphics provider interface.
 * Abstracts shape rendering and screen clearing from any graphics framework.
 */
public interface IGraphicsProvider {
    
    /**
     * Clears the screen with the specified color.
     * 
     * @param r red component (0.0 to 1.0)
     * @param g green component (0.0 to 1.0)
     * @param b blue component (0.0 to 1.0)
     */
    void clearScreen(float r, float g, float b);
    
    /**
     * Sets the active drawing color.
     * 
     * @param r red component (0.0 to 1.0)
     * @param g green component (0.0 to 1.0)
     * @param b blue component (0.0 to 1.0)
     * @param a alpha component (0.0 to 1.0)
     */
    void setColor(float r, float g, float b, float a);
    
    /**
     * Draws a filled rectangle.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     */
    void drawRect(float x, float y, float width, float height);
    
    /**
     * Draws a line between two points.
     * 
     * @param x1 the starting x coordinate
     * @param y1 the starting y coordinate
     * @param x2 the ending x coordinate
     * @param y2 the ending y coordinate
     * @param width the width of the line
     */
    void drawLine(float x1, float y1, float x2, float y2, float width);
    void drawLine(float x1, float y1, float x2, float y2, float r, float g, float b, float a);
    
    /**
     * Begins a batch of shape drawing operations (e.g., for optimized rendering).
     */
    void beginShapes();
    
    /**
     * Ends the current shape drawing batch.
     */
    void endShapes();
    
    /**
     * Enables alpha blending for transparent rendering.
     */
    void enableBlend();
    
    /**
     * Disables alpha blending.
     */
    void disableBlend();

    /**
     * Updates the internal projection for rendering after a window resize.
     * Implementations handle framework-specific matrix logic internally.
     *
     * @param width current screen width
     * @param height current screen height
     */
    void setProjectionMatrix(float width, float height);

    /**
     * Loads a font from the given path and applies the requested size.
     *
     * @param fontPath relative asset path to the font file
     * @param size target font size
     * @return true if the font was loaded
     */
    boolean loadFont(String fontPath, int size);

    /**
     * Sets the color used for subsequent text drawing calls.
     *
     * @param r red component (0.0 to 1.0)
     * @param g green component (0.0 to 1.0)
     * @param b blue component (0.0 to 1.0)
     * @param a alpha component (0.0 to 1.0)
     */
    void setTextColor(float r, float g, float b, float a);

    /**
     * Draws text using the currently loaded font.
     *
     * @param text text to draw
     * @param x x position
     * @param y y position
     */
    void drawText(String text, float x, float y);

    void drawText(String text, String fontName, float x, float y);

    void drawText(String text, String fontName, float x, float y, float targetWidth);

    void fillRectangle(float x, float y, float w, float h, float r, float g, float b, float alpha);
    
    void drawTexture(String textureId, float x, float y, float w, float h);
    
    void drawTexture(String textureId, float x, float y, float width, float height, float originX, float originY, float rotationDegrees);
    
    void begin();
    
    void end();
    
    /**
     * Disposes of native graphics resources.
     */
    void dispose();
}
