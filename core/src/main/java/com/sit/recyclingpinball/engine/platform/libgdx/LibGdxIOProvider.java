package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.sit.recyclingpinball.engine.interfaces.providers.IIOProvider;

import java.util.Optional;

/**
 * libGDX implementation of IIOProvider.
 */
public class LibGdxIOProvider implements IIOProvider {
    private final Json json = new Json();

    @Override
    public java.util.List<String> listInternalFiles(String directory, String extension) {
        java.util.List<String> list = new java.util.ArrayList<>();
        try {
            com.badlogic.gdx.files.FileHandle dir = com.badlogic.gdx.Gdx.files.internal(directory);
            if (dir.exists() && dir.isDirectory()) {
                for (com.badlogic.gdx.files.FileHandle file : dir.list()) {
                    if (file.name().endsWith(extension)) {
                        list.add(file.path().replace("\\\\", "/"));
                    }
                }
            }
        } catch (com.badlogic.gdx.utils.GdxRuntimeException e) {
        }
        return list;
    }

    @Override
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

    @Override
    public Optional<String> readLocalText(String localPath) {
        try {
            com.badlogic.gdx.files.FileHandle file = Gdx.files.local(localPath);
            if (!file.exists()) {
                return Optional.empty();
            }
            return Optional.of(file.readString());
        } catch (GdxRuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean writeLocalText(String localPath, String data) {
        try {
            Gdx.files.local(localPath).writeString(data, false);
            return true;
        } catch (GdxRuntimeException e) {
            return false;
        }
    }

    @Override
    public <T> T fromJson(String jsonStr, Class<T> type) {
        return this.json.fromJson(type, jsonStr);
    }

}
