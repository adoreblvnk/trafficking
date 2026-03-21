package com.sit.recyclingpinball.engine.interfaces.providers;

/**
 * Platform-independent engine context interface. Provides unified access to all
 * platform-specific providers.
 */
public interface IEngineContext {

    /**
     * Returns the display provider for querying screen dimensions.
     */
    IDisplay getDisplay();

    /**
     * Returns the time provider for querying delta time.
     */
    ITimeProvider getTime();

    /**
     * Returns the audio provider for loading and playing sounds.
     */
    IAudioProvider getAudio();

    /**
     * Returns the graphics provider for rendering shapes.
     */
    IGraphicsProvider getGraphics();

    /**
     * Returns the input provider for binding the active input processor.
     */
    IInputProvider getInput();

    /**
     * Returns the IO provider for reading and writing files.
     */
    IIOProvider getIO();

    /**
     * Disposes of all native resources held by the context.
     */
    void dispose();

    /**
     * Exits the application.
     */
    void exit();
}
