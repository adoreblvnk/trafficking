package com.sit.recyclingpinball.logic.events;

public class BallLaunchedEvent implements IPinballEvent {
    private final float launchVelocity;

    public BallLaunchedEvent(float launchVelocity) {
        this.launchVelocity = launchVelocity;
    }

    public float getLaunchVelocity() {
        return launchVelocity;
    }
}
