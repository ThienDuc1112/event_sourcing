package com.example.bank.application.commandhandler;
import com.example.bank.application.command.*;
import com.example.bank.application.port.EventStorePort;
import com.example.bank.domain.aggregate.AccountAggregate;
import com.example.bank.domain.decider.AccountDecider;
import com.example.bank.domain.event.DomainEvent;
import com.example.bank.shared.eventbus.EventBus;
import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandHandler {
    private static final Logger log = LoggerFactory.getLogger(CommandHandler.class);
    private static final int MAX_RETRIES = 3;

    private final EventStorePort eventStore;
    private final AccountDecider decider;
    private final EventBus eventBus;

    public CommandHandler(EventStorePort eventStore, AccountDecider decider, EventBus eventBus) {
        this.eventStore = eventStore;
        this.decider = decider;
        this.eventBus = eventBus;
    }

    public void handle(Command command) {
        executeWithRetry(() -> {
            String aggregateId = command.getAggregateId();
            List<DomainEvent> storedEvents = eventStore.loadEvents(aggregateId);

            // Rebuild state from events using decider
            AccountAggregate state = decider.replay(decider.initialState(), storedEvents);

            // Decide new events
            List<DomainEvent> newEvents = decider.decide(state, command);

            // Save events
            int expectedVersion = state.getVersion();
            eventStore.saveEvents(aggregateId, newEvents, expectedVersion);

            // Publish events to update read models
            newEvents.forEach(eventBus::publish);

            log.info("Command processed: {} for aggregate {}",
                    command.getClass().getSimpleName(), aggregateId);
        });
    }

    private void executeWithRetry(Runnable action) {
        OptimisticLockException lastException = null;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                action.run();
                return;
            } catch (OptimisticLockException e) {
                lastException = e;
                log.warn("Optimistic lock conflict on attempt {}/{}", attempt, MAX_RETRIES);
                if (attempt < MAX_RETRIES) {
                    try {
                        Thread.sleep(100L * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Retry interrupted", ie);
                    }
                }
            }
        }

        throw new RuntimeException("Failed after " + MAX_RETRIES + " retries", lastException);
    }
}
