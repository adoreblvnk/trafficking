package com.sit.recyclingpinball.engine.platform.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.sit.recyclingpinball.engine.interfaces.providers.IIOProvider;

import java.util.Optional;

/**
 * libGDX implementation of IIOProvider.
 */
public class LibGdxIOProvider implements IIOProvider {

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
    public java.util.List<String> listInternalFiles(String directory, String extension) {
        java.util.List<String> list = new java.util.ArrayList<>();
        try {
            com.badlogic.gdx.files.FileHandle dir = Gdx.files.internal(directory);
            if (dir.exists() && dir.isDirectory()) {
                for (com.badlogic.gdx.files.FileHandle file : dir.list()) {
                    if (file.name().endsWith(extension)) {
                        list.add(file.path());
                    }
                }
            }
        } catch (GdxRuntimeException e) {
        }
        return list;
    }

}
