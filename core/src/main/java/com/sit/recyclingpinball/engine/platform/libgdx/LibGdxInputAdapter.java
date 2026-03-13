package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.InputAdapter;
import com.sit.recyclingpinball.engine.managers.InputManager;

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
        int vx = LibGdxViewport.screenToVirtualX(screenX);
        int vy = LibGdxViewport.screenToVirtualY(screenY);
        return inputManager != null && inputManager.touchDown(vx, vy, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        int vx = LibGdxViewport.screenToVirtualX(screenX);
        int vy = LibGdxViewport.screenToVirtualY(screenY);
        return inputManager != null && inputManager.touchDragged(vx, vy, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        int vx = LibGdxViewport.screenToVirtualX(screenX);
        int vy = LibGdxViewport.screenToVirtualY(screenY);
        return inputManager != null && inputManager.touchUp(vx, vy, pointer, button);
    }

    @Override
    public boolean keyDown(int keycode) {
        return inputManager != null && inputManager.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return inputManager != null && inputManager.keyUp(keycode);
    }
}
