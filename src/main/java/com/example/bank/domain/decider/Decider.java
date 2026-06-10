package com.example.bank.domain.decider;

import com.example.bank.domain.event.DomainEvent;

import java.util.List;

/**
 * Decider Pattern - Core of Event Sourcing
 * Decides what events to produce based on command and current state
 */
public interface Decider<S, C, E extends DomainEvent> {

    /**
     * Initial state of aggregate
     */
    S initialState();

    /**
     * Decide what events to produce from command
     */
    List<E> decide(S state, C command);

    /**
     * Apply event to state
     */
    S evolve(S state, E event);

    /**
     * Replay events to rebuild state
     */
    default S replay(S initialState, List<E> events) {
        S state = initialState;
        for (E event : events) {
            state = evolve(state, event);
        }
        return state;
    }
}