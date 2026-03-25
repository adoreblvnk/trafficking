package com.sit.recyclingpinball.engine.interfaces.providers;

import com.sit.recyclingpinball.engine.interfaces.IInputManager;

/**
 * Platform-independent input provider interface. Owns binding and unbinding the
 * active input processor.
 */
public interface IInputProvider {

    /**
     * Sets the active input processor.
     *
     * @param inputManager
     *            input manager to activate
     */
    void setActiveProcessor(IInputManager inputManager);

    /**
     * Clears any active input processor.
     */
    void clearActiveProcessor();
}
