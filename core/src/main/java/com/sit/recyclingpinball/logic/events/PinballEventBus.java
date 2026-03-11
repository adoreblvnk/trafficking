package com.sit.recyclingpinball.logic.events;

import java.util.ArrayList;
import java.util.List;

public class PinballEventBus {
    private final List<PinballEventListener> listeners;

    public PinballEventBus() {
        this.listeners = new ArrayList<>();
    }

    public void register(PinballEventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void unregister(PinballEventListener listener) {
        listeners.remove(listener);
    }

    public void post(IPinballEvent event) {
        for (PinballEventListener l : listeners) {
            l.onEvent(event);
        }
    }
}
