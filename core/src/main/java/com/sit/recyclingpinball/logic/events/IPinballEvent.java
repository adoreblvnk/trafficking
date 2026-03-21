package com.sit.recyclingpinball.logic.events;

public interface IPinballEvent {
    void accept(PinballEventVisitor visitor);
}
