package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlatformIO {
    private final Json json = new Json();

    public List<String> listInternalFiles(String directory, String extension) {
        List<String> list = new ArrayList<>();
        try {
            com.badlogic.gdx.files.FileHandle dir = Gdx.files.internal(directory);
            if (dir.exists() && dir.isDirectory()) {
                for (com.badlogic.gdx.files.FileHandle file : dir.list()) {
                    if (file.name().endsWith(extension)) {
                        list.add(file.path().replace("\\\\", "/"));
                    }
                }
            }
        } catch (GdxRuntimeException e) {
        }
        return list;
    }

    public Optional<String> readInternalText(String internalPath) {
        try {
            com.badlogic.gdx.files.FileHandle file = Gdx.files.internal(internalPath);
            if (!file.exists()) {
                return Optional.empty();
            }
            return Optional.of(file.readString());
        } catch (GdxRuntimeException e) {
            return Optional.empty();
        }
    }

    public <T> T fromJson(String jsonStr, Class<T> type) {
        return json.fromJson(type, jsonStr);
    }
}
