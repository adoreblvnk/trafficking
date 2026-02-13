package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.InputAdapter;
import com.sit.trafficking.engine.interfaces.InputListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InputManager extends InputAdapter {

    private final List<InputListener> listeners;

    public InputManager() {
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public void addListener(InputListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    public void removeListener(InputListener l) {
        listeners.remove(l);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        try {
            for (InputListener l : listeners) {
                if (l.onTouchDown(screenX, screenY, pointer, button)) return true;
            }
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("InputManager", "Input failure: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        try {
            for (InputListener l : listeners) {
                if (l.onDrag(screenX, screenY, pointer)) return true;
            }
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("InputManager", "Input failure: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        try {
            for (InputListener l : listeners) {
                if (l.onTouchUp(screenX, screenY, pointer, button)) return true;
            }
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("InputManager", "Input failure: " + e.getMessage());
        }
        return false;
    }
    
    // Implemented to satisfy UML even if Interface doesn't use it yet
    @Override
    public boolean keyDown(int keycode) {
        try {
            for (InputListener l : listeners) {
                if (l.onKeyDown(keycode)) return true;
            }
        } catch (Exception e) {
            com.badlogic.gdx.Gdx.app.error("InputManager", "Input failure: " + e.getMessage());
        }
        return false;
    }
}
