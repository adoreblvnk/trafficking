package com.sit.recyclingpinball.engine.interfaces;

import com.sit.recyclingpinball.engine.platform.libgdx.IPlatformInputHandler;

public interface IInputManager extends IPlatformInputHandler {
    void addListener(InputListener l);
}
