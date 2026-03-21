package com.sit.recyclingpinball.logic.events;

import java.util.ArrayList;
import java.util.List;

public class PinballEventBus {
    private final List<PinballEventVisitor> listeners;

    public PinballEventBus() {
        this.listeners = new ArrayList<>();
    }

    public void register(PinballEventVisitor listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void unregister(PinballEventVisitor listener) {
        listeners.remove(listener);
    }

    public void post(IPinballEvent event) {
        for (PinballEventVisitor l : listeners) {
            event.accept(l);
        }
    }
}
