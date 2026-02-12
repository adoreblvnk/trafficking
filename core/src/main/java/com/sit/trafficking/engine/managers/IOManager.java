package com.sit.trafficking.engine.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.util.Optional;

public class IOManager {

    private static final String TAG = "IOManager";

    public IOManager() {
    }

    public Optional<String> readTextFile(String internalPath) {
        try {
            com.badlogic.gdx.files.FileHandle file = Gdx.files.internal(internalPath);
            if (!file.exists()) {
                Gdx.app.error(TAG, "File not found: " + internalPath);
                return Optional.empty();
            }
            String content = file.readString();
            Gdx.app.log(TAG, "Successfully read file: " + internalPath);
            return Optional.of(content);
        } catch (GdxRuntimeException e) {
            Gdx.app.error(TAG, "Error reading file: " + internalPath, e);
            return Optional.empty();
        }
    }

    public Optional<String> readSaveFile(String localPath) {
        try {
            com.badlogic.gdx.files.FileHandle file = Gdx.files.local(localPath);
            if (!file.exists()) {
                Gdx.app.log(TAG, "Save file not found (this is normal for first run): " + localPath);
                return Optional.empty();
            }
            String content = file.readString();
            Gdx.app.log(TAG, "Successfully read save file: " + localPath);
            return Optional.of(content);
        } catch (GdxRuntimeException e) {
            Gdx.app.error(TAG, "Error reading save file: " + localPath, e);
            return Optional.empty();
        }
    }

    public boolean writeSaveFile(String localPath, String data) {
        try {
            Gdx.files.local(localPath).writeString(data, false);
            Gdx.app.log(TAG, "Successfully wrote save file: " + localPath);
            return true;
        } catch (GdxRuntimeException e) {
            Gdx.app.error(TAG, "Error writing save file: " + localPath, e);
            return false;
        }
    }
}
