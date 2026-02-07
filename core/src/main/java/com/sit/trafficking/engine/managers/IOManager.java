package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.Gdx;

public class IOManager {

    public IOManager() {
    }

    public String readTextFile(String internalPath) {
        return Gdx.files.internal(internalPath).readString();
    }

    public void writeSaveFile(String data) {
        Gdx.files.local("save.json").writeString(data, false);
    }
}
