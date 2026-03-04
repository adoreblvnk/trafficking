package com.sit.covid26.engine.platform.libgdx;

import com.badlogic.gdx.InputAdapter;
import com.sit.covid26.engine.managers.InputManager;

/**
 * libGDX input bridge that forwards framework callbacks to the pure Java InputManager.
 */
public class LibGdxInputAdapter extends InputAdapter {

    private final InputManager inputManager;

    public LibGdxInputAdapter(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return inputManager != null && inputManager.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return inputManager != null && inputManager.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return inputManager != null && inputManager.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown(int keycode) {
        return inputManager != null && inputManager.keyDown(keycode);
    }
}
