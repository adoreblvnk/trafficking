package com.sit.recyclingpinball.engine.interfaces;

import com.sit.recyclingpinball.engine.interfaces.providers.EngineKey;

public interface IInputManager {
    void addListener(InputListener l);
    boolean touchDown(int screenX, int screenY, int pointer, int button);
    boolean touchDragged(int screenX, int screenY, int pointer);
    boolean touchUp(int screenX, int screenY, int pointer, int button);
    boolean keyDown(EngineKey keycode);
    boolean keyUp(EngineKey keycode);
}
