package com.sit.recyclingpinball.engine.platform.libgdx;

import com.sit.recyclingpinball.engine.EngineConstants;

/**
 * Centralized virtual-viewport mapping for LibGDX desktop.
 * Keeps rendering and input coordinates consistent across window sizes/DPIs.
 */
public final class LibGdxViewport {
    private LibGdxViewport() {}

    private static volatile int viewportX = 0;
    private static volatile int viewportY = 0;
    private static volatile int viewportWidth = EngineConstants.VIRTUAL_WIDTH;
    private static volatile int viewportHeight = EngineConstants.VIRTUAL_HEIGHT;
    private static volatile float scale = 1f;

    public static void updateForWindow(int windowWidth, int windowHeight) {
        float sx = windowWidth / (float) EngineConstants.VIRTUAL_WIDTH;
        float sy = windowHeight / (float) EngineConstants.VIRTUAL_HEIGHT;
        float s = Math.min(sx, sy);
        if (s <= 0f || Float.isNaN(s) || Float.isInfinite(s)) s = 1f;

        int vpW = Math.max(1, Math.round(EngineConstants.VIRTUAL_WIDTH * s));
        int vpH = Math.max(1, Math.round(EngineConstants.VIRTUAL_HEIGHT * s));
        int vpX = (windowWidth - vpW) / 2;
        int vpY = (windowHeight - vpH) / 2;

        LibGdxViewport.scale = s;
        LibGdxViewport.viewportX = vpX;
        LibGdxViewport.viewportY = vpY;
        LibGdxViewport.viewportWidth = vpW;
        LibGdxViewport.viewportHeight = vpH;
    }

    public static int getViewportX() { return viewportX; }
    public static int getViewportY() { return viewportY; }
    public static int getViewportWidth() { return viewportWidth; }
    public static int getViewportHeight() { return viewportHeight; }
    public static float getScale() { return scale; }

    public static int screenToVirtualX(int screenX) {
        return Math.round((screenX - viewportX) / scale);
    }

    /**
     * LibGDX provides screenY with origin at top-left of the window.
     * We keep that convention for forwarding; scenes that need bottom-left can invert using VIRTUAL_HEIGHT.
     */
    public static int screenToVirtualY(int screenY) {
        return Math.round((screenY - viewportY) / scale);
    }
}

