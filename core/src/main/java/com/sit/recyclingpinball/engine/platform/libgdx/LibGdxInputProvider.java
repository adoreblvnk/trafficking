package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;
import com.sit.recyclingpinball.engine.interfaces.providers.IInputProvider;
import com.sit.recyclingpinball.engine.managers.InputManager;

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
