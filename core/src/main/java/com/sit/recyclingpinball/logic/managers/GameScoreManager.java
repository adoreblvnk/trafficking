package com.sit.recyclingpinball.logic.managers;

import com.sit.recyclingpinball.logic.events.BallDrainedEvent;
import com.sit.recyclingpinball.logic.events.BallLaunchedEvent;
import com.sit.recyclingpinball.logic.events.IPinballEvent;
import com.sit.recyclingpinball.logic.events.PinballEventBus;
import com.sit.recyclingpinball.logic.events.PinballEventListener;
import com.sit.recyclingpinball.logic.events.TrashCollectedEvent;

public class GameScoreManager implements PinballEventListener {
    private int score = 0;
    private int ballsLeft = 3;
    private final int winScore;
    private boolean ballInPlay = false;

    public GameScoreManager(PinballEventBus bus, int winScore) {
        this.winScore = winScore;
        bus.register(this);
    }

    @Override
    public void onEvent(IPinballEvent event) {
        if (event instanceof TrashCollectedEvent) {
            score++;
        } else if (event instanceof BallLaunchedEvent) {
            ballsLeft--;
            ballInPlay = true;
        } else if (event instanceof BallDrainedEvent) {
            ballInPlay = false;
        }
    }

    public int getScore() { return score; }
    public int getBallsLeft() { return ballsLeft; }
    public boolean isWon() { return score >= winScore; }
    public boolean isLost() { return ballsLeft <= 0 && score < winScore && !ballInPlay; }
}
