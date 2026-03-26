package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;

public class PlatformInput {

    private final PlatformDisplay display;

    public PlatformInput(PlatformDisplay display) {
        this.display = display;
    }

    public void setActiveProcessor(PlatformInputProcessor inputProcessor) {
        Gdx.input.setInputProcessor(new PlatformInputAdapter(inputProcessor, display));
    }

    public void clearActiveProcessor() {
        Gdx.input.setInputProcessor(null);
    }
}
