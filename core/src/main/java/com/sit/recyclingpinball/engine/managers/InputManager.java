package com.sit.recyclingpinball.engine.managers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sit.recyclingpinball.engine.interfaces.InputListener;
import com.sit.recyclingpinball.engine.platform.libgdx.PlatformKey;

/**
 * Distributes input events to registered listeners in subscription order.
 * Swallows exceptions to prevent input pipeline crashes.
 */
public class InputManager implements com.sit.recyclingpinball.engine.interfaces.IInputManager {

    private static final Logger LOGGER = Logger.getLogger(InputManager.class.getName());

    private final List<InputListener> listeners;

    public InputManager() {
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public void addListener(InputListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for (InputListener l : listeners) {
            try {
                if (l.onTouchDown(screenX, screenY, pointer, button))
                    return true;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Input failure in touchDown", e);
            }
        }
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        for (InputListener l : listeners) {
            try {
                if (l.onDrag(screenX, screenY, pointer))
                    return true;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Input failure in touchDragged", e);
            }
        }
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for (InputListener l : listeners) {
            try {
                if (l.onTouchUp(screenX, screenY, pointer, button))
                    return true;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Input failure in touchUp", e);
            }
        }
        return false;
    }

    // Implemented to satisfy UML even if Interface doesn't use it yet
    public boolean keyDown(PlatformKey keycode) {
        for (InputListener l : listeners) {
            try {
                if (l.onKeyDown(keycode))
                    return true;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Input failure in keyDown", e);
            }
        }
        return false;
    }

    public boolean keyUp(PlatformKey keycode) {
        for (InputListener l : listeners) {
            try {
                if (l.onKeyUp(keycode))
                    return true;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Input failure in keyUp", e);
            }
        }
        return false;
    }
}
