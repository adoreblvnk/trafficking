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
    private final int totalTrash;
    private boolean ballInPlay = false;

    public GameScoreManager(PinballEventBus bus, int totalTrash) {
        this.totalTrash = totalTrash;
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
    public int getTotalTrash() { return totalTrash; }

    /**
     * Game is over when all trash is collected, or all balls have drained.
     */
    public boolean isGameOver() {
        if (score >= totalTrash) return true;
        return ballsLeft <= 0 && !ballInPlay;
    }

    /**
     * Win: game is over AND at least 1 trash was collected.
     */
    public boolean isWon() { return isGameOver() && score >= 1; }

    /**
     * Loss: game is over AND zero trash was collected.
     */
    public boolean isLost() { return isGameOver() && score < 1; }
}
