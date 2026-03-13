package com.sit.recyclingpinball.engine.platform.libgdx;

import com.sit.recyclingpinball.engine.interfaces.providers.IAudioProvider;
import com.sit.recyclingpinball.engine.interfaces.providers.IDisplay;
import com.sit.recyclingpinball.engine.interfaces.providers.IEngineContext;
import com.sit.recyclingpinball.engine.interfaces.providers.IGraphicsProvider;
import com.sit.recyclingpinball.engine.interfaces.providers.IIOProvider;
import com.sit.recyclingpinball.engine.interfaces.providers.IInputProvider;
import com.sit.recyclingpinball.engine.interfaces.providers.ITimeProvider;

/**
 * libGDX implementation of IEngineContext.
 * Factory and holder for all libGDX platform providers.
 * This is the ONLY place where libGDX-specific implementations are instantiated for the engine.
 */
public class LibGdxContext implements IEngineContext {

    private final IDisplay displayProvider;
    private final ITimeProvider timeProvider;
    private final IAudioProvider audioProvider;
    private final IGraphicsProvider graphicsProvider;
    private final IInputProvider inputProvider;
    private final IIOProvider ioProvider;

    /**
     * Constructs the engine context by instantiating all libGDX providers.
     */
    public LibGdxContext() {
        this.displayProvider = new LibGdxDisplay();
        this.timeProvider = new LibGdxTime();
        this.audioProvider = new LibGdxAudio();
        this.graphicsProvider = new LibGdxGraphics();
        this.inputProvider = new LibGdxInputProvider();
        this.ioProvider = new LibGdxIOProvider();
    }

    @Override
    public IDisplay getDisplay() {
        return displayProvider;
    }

    @Override
    public ITimeProvider getTime() {
        return timeProvider;
    }

    @Override
    public IAudioProvider getAudio() {
        return audioProvider;
    }

    @Override
    public IGraphicsProvider getGraphics() {
        return graphicsProvider;
    }

    @Override
    public IInputProvider getInput() {
        return inputProvider;
    }

    @Override
    public IIOProvider getIO() {
        return ioProvider;
    }

    /**
     * Casts the time provider to LibGdxTime to access time scaling.
     * This is safe because LibGdxContext always creates LibGdxTime internally.
     */
    public LibGdxTime getTimeManager() {
        return (LibGdxTime) timeProvider;
    }

    /**
     * Casts the graphics provider to LibGdxGraphics to access advanced operations.
     * This is safe because LibGdxContext always creates LibGdxGraphics internally.
     */
    public LibGdxGraphics getGraphicsManager() {
        return (LibGdxGraphics) graphicsProvider;
    }

    /**
     * Disposes all native resources held by providers.
     */
    @Override
    public void dispose() {
        graphicsProvider.dispose();
        audioProvider.dispose();
    }

    @Override
    public void exit() {
        com.badlogic.gdx.Gdx.app.exit();
    }
}
