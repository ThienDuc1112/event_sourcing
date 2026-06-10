package com.example.bank.domain.decider;

import com.example.bank.domain.event.DomainEvent;

import java.util.Collections;
import java.util.List;

public class Decision<E extends DomainEvent> {
    private final List<E> events;
    private final String rejectionReason;
    private final boolean isAccepted;

    private Decision(List<E> events, String rejectionReason, boolean isAccepted) {
        this.events = events;
        this.rejectionReason = rejectionReason;
        this.isAccepted = isAccepted;
    }

    public static <E extends DomainEvent> Decision<E> accept(List<E> events) {
        return new Decision<>(events, null, true);
    }

    public static <E extends DomainEvent> Decision<E> reject(String reason) {
        return new Decision<>(Collections.emptyList(), reason, false);
    }

    public boolean isAccepted() { return isAccepted; }
    public List<E> getEvents() { return events; }
    public String getRejectionReason() { return rejectionReason; }
}
