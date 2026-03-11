package com.sit.recyclingpinball.engine.interfaces.providers;

/**
 * Platform-independent audio provider interface.
 * Abstracts sound loading and playback from any audio framework.
 */
public interface IAudioProvider {
    
    /**
     * Loads a sound from the given file path and stores it under the given name.
     * 
     * @param path the file path to the sound resource
     * @param name the identifier for this sound
     * @return true if the sound was loaded successfully, false otherwise
     */
    boolean loadSound(String path, String name);
    
    /**
     * Plays a previously loaded sound by its name.
     * 
     * @param name the identifier of the sound to play
     * @param volume the volume to play the sound at (0.0 to 1.0)
     */
    void playSound(String name, float volume);
    
    /**
     * Disposes of all audio resources and clears the sound bank.
     */
    void dispose();
}
