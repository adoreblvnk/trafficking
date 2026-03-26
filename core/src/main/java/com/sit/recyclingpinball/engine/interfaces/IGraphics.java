package com.sit.recyclingpinball.engine.interfaces;

public interface IGraphics {
    void clearScreen(float r, float g, float b);
    void setColor(float r, float g, float b, float a);
    void drawRect(float x, float y, float width, float height);
    void drawLine(float x1, float y1, float x2, float y2, float r, float g, float b, float a);
    void drawLine(float x1, float y1, float x2, float y2, float width);
    void beginShapes();
    void endShapes();
    void enableBlend();
    void disableBlend();
    void setProjectionMatrix(float width, float height);
    boolean loadTextureResource(String path);
    boolean loadTextureResource(String path, String id);
    boolean loadFontResource(String path, int size);
    Object getTextureResource(String id);
    Object getFontResource(String id);
    void setTextColor(float r, float g, float b, float a);
    void drawText(String text, float x, float y);
    void drawText(String text, String fontId, float x, float y);
    void drawText(String text, String fontId, float x, float y, float targetWidth);
    void drawTextCentered(String text, String fontId, float x, float y, float width, float height);
    void fillRectangle(float x, float y, float w, float h, float r, float g, float b, float alpha);
    void drawTexture(String textureId, float x, float y, float w, float h);
    void drawTexture(String textureId, float x, float y, float width, float height, float originX, float originY, float rotationDegrees);
    void begin();
    void end();
    void dispose();
}
