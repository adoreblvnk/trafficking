package com.sit.covid26.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;
import com.sit.covid26.engine.interfaces.providers.IInputProvider;
import com.sit.covid26.engine.managers.InputManager;

/**
 * libGDX input provider adapter.
 */
public class LibGdxInputProvider implements IInputProvider {

    @Override
    public void setActiveProcessor(InputManager inputManager) {
        Gdx.input.setInputProcessor(new LibGdxInputAdapter(inputManager));
    }

    @Override
    public void clearActiveProcessor() {
        Gdx.input.setInputProcessor(null);
    }
}
