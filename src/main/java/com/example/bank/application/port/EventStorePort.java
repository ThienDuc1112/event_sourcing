package com.example.bank.application.port;

import com.example.bank.domain.event.DomainEvent;
import java.util.List;

public interface EventStorePort {
    void saveEvents(String aggregateId, List<DomainEvent> events, int expectedVersion);
    List<DomainEvent> loadEvents(String aggregateId);
    int getCurrentVersion(String aggregateId);
}
