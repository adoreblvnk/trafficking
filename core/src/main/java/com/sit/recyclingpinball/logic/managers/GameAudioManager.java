package com.sit.recyclingpinball.logic.managers;

import com.sit.recyclingpinball.engine.platform.libgdx.PlatformAudio;
import com.sit.recyclingpinball.logic.events.BallDrainedEvent;
import com.sit.recyclingpinball.logic.events.BallLaunchedEvent;
import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.events.PinballEventVisitor;
import com.sit.recyclingpinball.logic.events.TrashCollectedEvent;

public class GameAudioManager implements PinballEventVisitor {
    private final PlatformAudio audio;

    public GameAudioManager(PlatformAudio audio, PinballEventBus bus) {
        this.audio = audio;
        bus.register(this);
    }

    @Override
    public void visit(TrashCollectedEvent event) {
        audio.playSound(com.sit.recyclingpinball.logic.LogicConstants.SOUND_COLLECT,
                com.sit.recyclingpinball.logic.LogicConstants.VOLUME_DEFAULT);
    }

    @Override
    public void visit(BallDrainedEvent event) {
        audio.playSound(com.sit.recyclingpinball.logic.LogicConstants.SOUND_LOSE,
                com.sit.recyclingpinball.logic.LogicConstants.VOLUME_DEFAULT);
    }

    @Override
    public void visit(BallLaunchedEvent event) {
        audio.playSound(com.sit.recyclingpinball.logic.LogicConstants.SOUND_STRETCH,
                com.sit.recyclingpinball.logic.LogicConstants.VOLUME_DEFAULT);
    }
}
