package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;

/**
 */
// Centralizes platform subsystems. Kept concrete to enforce the strict Logic ->
// Engine -> Platform rule.
public class PlatformContext {

    private final PlatformDisplay displayProvider;
    private final PlatformTime timeProvider;
    private final PlatformAudio audioProvider;
    private final PlatformAssetManager assetProvider;
    private final PlatformGraphics graphicsProvider;
    private final PlatformInput inputProvider;
    private final PlatformIO ioProvider;

    public PlatformContext() {
        this.displayProvider = new PlatformDisplay();
        this.timeProvider = new PlatformTime();
        this.audioProvider = new PlatformAudio();
        this.assetProvider = new PlatformAssetManager();
        this.graphicsProvider = new PlatformGraphics(assetProvider);
        this.inputProvider = new PlatformInput(displayProvider);
        this.ioProvider = new PlatformIO();
    }

    public PlatformDisplay getDisplay() {
        return displayProvider;
    }

    public PlatformTime getTime() {
        return timeProvider;
    }

    public PlatformAudio getAudio() {
        return audioProvider;
    }

    public PlatformGraphics getGraphics() {
        return graphicsProvider;
    }

    public PlatformAssetManager getAssets() {
        return assetProvider;
    }

    public PlatformInput getInput() {
        return inputProvider;
    }

    public PlatformIO getIO() {
        return ioProvider;
    }

    public void dispose() {
        graphicsProvider.dispose();
        assetProvider.dispose();
        audioProvider.dispose();
    }

    public void exit() {
        Gdx.app.exit();
    }
}
