package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;
import com.sit.recyclingpinball.engine.interfaces.providers.IInputProvider;
import com.sit.recyclingpinball.engine.interfaces.IInputManager;
import com.sit.recyclingpinball.engine.interfaces.providers.IDisplay;

/**
 * libGDX input provider adapter.
 */
public class LibGdxInputProvider implements IInputProvider {

    private final IDisplay display;

    public LibGdxInputProvider(IDisplay display) {
        this.display = display;
    }

    @Override
    public void setActiveProcessor(IInputManager inputManager) {
        Gdx.input.setInputProcessor(new LibGdxInputAdapter(inputManager, display));
    }

    @Override
    public void clearActiveProcessor() {
        Gdx.input.setInputProcessor(null);
    }
}
