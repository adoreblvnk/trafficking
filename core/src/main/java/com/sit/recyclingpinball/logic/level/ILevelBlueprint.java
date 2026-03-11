package com.sit.recyclingpinball.logic.level;

public interface ILevelBlueprint {
    BoardLayout construct(BoardBuilder builder);
    String getLevelName();
    String getEducationalText();
}
