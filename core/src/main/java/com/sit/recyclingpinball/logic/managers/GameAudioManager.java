package com.sit.recyclingpinball.logic.managers;

import com.sit.recyclingpinball.engine.interfaces.providers.IAudioProvider;
import com.sit.recyclingpinball.logic.events.BallDrainedEvent;
import com.sit.recyclingpinball.logic.events.BallLaunchedEvent;
import com.sit.recyclingpinball.logic.events.IPinballEvent;
import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.events.PinballEventListener;
import com.sit.recyclingpinball.logic.events.TrashCollectedEvent;

public class GameAudioManager implements PinballEventListener {
    private final IAudioProvider audio;

    public GameAudioManager(IAudioProvider audio, PinballEventBus bus) {
        this.audio = audio;
        audio.loadSound("sounds/collect.mp3", "collect");
        audio.loadSound("sounds/lose.mp3", "lose");
        audio.loadSound("sounds/win.mp3", "win");
        audio.loadSound("sounds/bounce.mp3", "bounce");
        audio.loadSound("sounds/flip.mp3", "flip");
        audio.loadSound("sounds/stretch.mp3", "stretch");
        audio.loadSound("sounds/click.mp3", "click");
        bus.register(this);
    }

    @Override
    public void onEvent(IPinballEvent event) {
        if (event instanceof TrashCollectedEvent) {
            audio.playSound("collect", 1.0f);
        } else if (event instanceof BallDrainedEvent) {
            audio.playSound("lose", 1.0f);
        } else if (event instanceof BallLaunchedEvent) {
            audio.playSound("stretch", 1.0f);
            // TODO: Load and play "sounds/launch.mp3" here in the future.
        }
    }
}
