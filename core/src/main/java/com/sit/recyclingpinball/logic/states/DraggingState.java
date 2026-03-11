package com.sit.recyclingpinball.logic.states;

import com.sit.recyclingpinball.logic.entities.PinballEntity;
import com.sit.recyclingpinball.logic.events.BallLaunchedEvent;
import com.badlogic.gdx.math.Vector2;

public class DraggingState implements IPinballState {
    private int startX;
    private int startY;
    private int currentX;
    private int currentY;

    public DraggingState(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
        this.currentX = startX;
        this.currentY = startY;
    }

    public int getStartX() { return startX; }
    public int getStartY() { return startY; }
    public int getCurrentX() { return currentX; }
    public int getCurrentY() { return currentY; }

    @Override
    public void update(float dt, PinballEntity ctx) {
    }

    @Override
    public boolean onTouchDown(PinballEntity ctx, int x, int y, int ptr, int btn) { return true; }

    @Override
    public boolean onDrag(PinballEntity ctx, int x, int y, int ptr) {
        currentX = x;
        currentY = y;
        return true;
    }

    @Override
    public boolean onTouchUp(PinballEntity ctx, int x, int y, int ptr, int btn) {
        float dx = startX - currentX;
        float dy = currentY - startY;
        
        Vector2 force = new Vector2(dx, dy).scl(10f);
        ctx.setVelocity(force.x, force.y);
        ctx.getEventBus().post(new BallLaunchedEvent());
        ctx.setState(new InPlayState());
        return true;
    }
}
