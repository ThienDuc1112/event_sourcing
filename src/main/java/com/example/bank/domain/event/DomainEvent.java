package com.example.bank.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.UUID;

public abstract class DomainEvent {
    private final String eventId;
    private final String aggregateId;
    private final Instant occurredAt;
    private final int version;

    protected DomainEvent(
            @JsonProperty("aggregateId") String aggregateId,
            @JsonProperty("version") int version) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = aggregateId;
        this.occurredAt = Instant.now();
        this.version = version;
    }

    @JsonProperty("eventId")
    public String getEventId() { return eventId; }

    @JsonProperty("aggregateId")
    public String getAggregateId() { return aggregateId; }

    @JsonProperty("occurredAt")
    public Instant getOccurredAt() { return occurredAt; }

    @JsonProperty("version")
    public int getVersion() { return version; }

    public abstract String getEventType();
}