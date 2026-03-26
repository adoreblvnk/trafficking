package com.sit.recyclingpinball.engine.platform.libgdx;

public interface PlatformInputProcessor {
    boolean touchDown(int screenX, int screenY, int pointer, int button);

    boolean touchDragged(int screenX, int screenY, int pointer);

    boolean touchUp(int screenX, int screenY, int pointer, int button);

    boolean keyDown(PlatformKey keycode);

    boolean keyUp(PlatformKey keycode);
}
