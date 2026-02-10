package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.Gdx;

public class IOManager {

    public IOManager() {
    }

    public String readTextFile(String internalPath) {
        return Gdx.files.internal(internalPath).readString();
    }
    
    public String readSaveFile(String localPath) {
        com.badlogic.gdx.files.FileHandle file = Gdx.files.local(localPath);
        if (file.exists()) {
            return file.readString();
        }
        return null;
    }

    public void writeSaveFile(String localPath, String data) {
        Gdx.files.local(localPath).writeString(data, false);
    }
}
