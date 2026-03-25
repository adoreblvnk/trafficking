package com.sit.recyclingpinball.logic.entities;

import com.sit.recyclingpinball.engine.platform.libgdx.math.PlatformVector2;
import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.factories.StateFactory;
import com.sit.recyclingpinball.logic.states.IPinballState;

public interface IPinballEntityContext {
    PlatformVector2 getPosition();
    void setPosition(float x, float y);
    PlatformVector2 getVelocity();
    void setVelocity(float vx, float vy);
    void setState(IPinballState state);
    StateFactory getStateFactory();
    PinballEventBus getEventBus();
}
