package com.sit.recyclingpinball.logic.factories;

import com.sit.recyclingpinball.logic.entities.PinballEntity;
import com.sit.recyclingpinball.logic.states.DrainedState;
import com.sit.recyclingpinball.logic.states.IPinballState;
import com.sit.recyclingpinball.logic.states.IdleState;
import com.sit.recyclingpinball.logic.states.InPlayState;

public class StateFactory {

    public IPinballState createInPlayState() {
        return new InPlayState();
    }

    public IPinballState createIdleState(PinballEntity entity) {
        return new IdleState(entity);
    }

    public IPinballState createDrainedState() {
        return new DrainedState();
    }
}
