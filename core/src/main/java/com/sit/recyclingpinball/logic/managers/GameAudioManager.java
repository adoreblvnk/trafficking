package com.sit.recyclingpinball.logic.managers;

import com.sit.recyclingpinball.engine.interfaces.providers.IAudioProvider;
import com.sit.recyclingpinball.logic.events.BallDrainedEvent;
import com.sit.recyclingpinball.logic.events.BallLaunchedEvent;
import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.events.PinballEventVisitor;
import com.sit.recyclingpinball.logic.events.TrashCollectedEvent;

public class GameAudioManager implements PinballEventVisitor {
    private final IAudioProvider audio;

    public GameAudioManager(IAudioProvider audio, PinballEventBus bus) {
        this.audio = audio;
        bus.register(this);
    }

    @Override
    public void visit(TrashCollectedEvent event) {
        audio.playSound("collect", 1.0f);
    }

    @Override
    public void visit(BallDrainedEvent event) {
        audio.playSound("lose", 1.0f);
    }

    @Override
    public void visit(BallLaunchedEvent event) {
        audio.playSound("stretch", 1.0f);
        // TODO: Load and play "sounds/launch.mp3" here in the future.
    }
}
