package com.example.bank.infrastructure.eventstore;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "event_store",
        uniqueConstraints = @UniqueConstraint(columnNames = {"aggregateId", "version"}))
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateId;
    private String eventType;
    private int version;

    @Column(columnDefinition = "TEXT")
    private String eventData;

    private Instant occurredAt;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAggregateId() { return aggregateId; }
    public void setAggregateId(String aggregateId) { this.aggregateId = aggregateId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
    public String getEventData() { return eventData; }
    public void setEventData(String eventData) { this.eventData = eventData; }
    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant occurredAt) { this.occurredAt = occurredAt; }
}