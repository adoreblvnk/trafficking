package com.sit.recyclingpinball.engine.interfaces.providers;

import java.util.Optional;

/**
 * Platform-independent IO provider interface. Abstracts reading and writing
 * text files from assets and local storage.
 */
public interface IIOProvider {

    java.util.List<String> listInternalFiles(String directory, String extension);

    /**
     * Reads a text file from internal assets/resources.
     *
     * @param internalPath
     *            internal resource path
     * @return file content if found/readable
     */
    Optional<String> readInternalText(String internalPath);

    /**
     * Reads a text file from local writable storage.
     *
     * @param localPath
     *            local file path
     * @return file content if found/readable
     */
    Optional<String> readLocalText(String localPath);

    /**
     * Writes text to a local writable storage path.
     *
     * @param localPath
     *            local file path
     * @param data
     *            text content to write
     * @return true when write succeeds
     */
    boolean writeLocalText(String localPath, String data);

    /**
     * Deserializes a JSON string into an object of the specified class.
     *
     * @param json
     *            the JSON string
     * @param type
     *            the class type
     * @param <T>
     *            the type parameter
     * @return the deserialized object
     */
    <T> T fromJson(String json, Class<T> type);
}
