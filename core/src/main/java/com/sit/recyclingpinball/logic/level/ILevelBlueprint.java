package com.sit.recyclingpinball.logic.level;

import com.sit.recyclingpinball.logic.events.PinballEventBus;

public interface ILevelBlueprint {
    BoardLayout construct(BoardBuilder builder, PinballEventBus eventBus);

    String getText();

    String getLevelName();
}
