package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.InputAdapter;

import java.util.ArrayList;
import java.util.List;

public class InputManager extends InputAdapter {

    public interface InputListener {
        boolean onTouchDown(int screenX, int screenY, int pointer, int button);

        boolean onDrag(int screenX, int screenY, int pointer);

        boolean onTouchUp(int screenX, int screenY, int pointer, int button);

        boolean onKeyDown(int keycode);
    }

    private final List<InputListener> listeners = new ArrayList<>();

    public void addListener(InputListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(InputListener listener) {
        listeners.remove(listener);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for (InputListener listener : listeners) {
            if (listener.onTouchDown(screenX, screenY, pointer, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        for (InputListener listener : listeners) {
            if (listener.onDrag(screenX, screenY, pointer)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for (InputListener listener : listeners) {
            if (listener.onTouchUp(screenX, screenY, pointer, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        for (InputListener listener : listeners) {
            if (listener.onKeyDown(keycode)) {
                return true;
            }
        }
        return false;
    }
}
