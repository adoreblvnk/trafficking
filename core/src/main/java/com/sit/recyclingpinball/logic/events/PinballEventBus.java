package com.sit.recyclingpinball.logic.events;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class PinballEventBus {
    private final Set<PinballEventVisitor> listeners;

    public PinballEventBus() {
        this.listeners = new CopyOnWriteArraySet<>();
    }

    public void register(PinballEventVisitor listener) {
        listeners.add(listener);
    }

    public void unregister(PinballEventVisitor listener) {
        listeners.remove(listener);
    }

    public void post(IPinballEvent event) {
        listeners.forEach(event::accept);
    }
}
