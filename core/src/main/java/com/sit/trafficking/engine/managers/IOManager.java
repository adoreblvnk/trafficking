package com.sit.trafficking.engine.managers;

import com.sit.trafficking.engine.interfaces.providers.IIOProvider;
import java.util.Optional;

/**
 * Abstraction for file system operations supporting both internal assets and local storage.
 */
public class IOManager {

    private final IIOProvider ioProvider;

    public IOManager(IIOProvider ioProvider) {
        this.ioProvider = ioProvider;
    }

    public Optional<String> readTextFile(String internalPath) {
        if (internalPath == null || internalPath.isEmpty()) {
            return Optional.empty();
        }
        return ioProvider.readInternalText(internalPath);
    }

    public Optional<String> readSaveFile(String localPath) {
        if (localPath == null || localPath.isEmpty()) {
            return Optional.empty();
        }
        return ioProvider.readLocalText(localPath);
    }

    public boolean writeSaveFile(String localPath, String data) {
        if (localPath == null || localPath.isEmpty()) {
            return false;
        }
        if (data == null) {
            return false;
        }
        return ioProvider.writeLocalText(localPath, data);
    }
}
