package com.sit.trafficking.engine.interfaces.providers;

import com.sit.trafficking.engine.managers.InputManager;

/**
 * Platform-independent input provider interface.
 * Owns binding and unbinding the active input processor.
 */
public interface IInputProvider {

    /**
     * Sets the active input processor.
     *
     * @param inputManager input manager to activate
     */
    void setActiveProcessor(InputManager inputManager);

    /**
     * Clears any active input processor.
     */
    void clearActiveProcessor();
}
