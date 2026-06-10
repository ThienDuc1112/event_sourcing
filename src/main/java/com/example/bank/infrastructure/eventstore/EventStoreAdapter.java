package com.example.bank.infrastructure.eventstore;

import com.example.bank.application.port.EventStorePort;
import com.example.bank.domain.event.AccountCreated;
import com.example.bank.domain.event.DomainEvent;
import com.example.bank.domain.event.MoneyDeposited;
import com.example.bank.domain.event.MoneyWithdrawn;
import com.example.bank.shared.exception.OptimisticLockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventStoreAdapter implements EventStorePort {
    private static final Logger log = LoggerFactory.getLogger(EventStoreAdapter.class);

    private final JpaEventStoreRepository repository;
    private final ObjectMapper objectMapper;

    public EventStoreAdapter(JpaEventStoreRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void saveEvents(String aggregateId, List<DomainEvent> events, int expectedVersion) {
        int currentVersion = getCurrentVersion(aggregateId);

        if (currentVersion != expectedVersion) {
            throw new OptimisticLockException(
                    String.format("Version mismatch: expected %d but was %d", expectedVersion, currentVersion)
            );
        }

        List<EventEntity> entities = events.stream()
                .map(event -> toEntity(aggregateId, event))
                .collect(Collectors.toList());

        try {
            repository.saveAll(entities);
            log.debug("Saved {} events for aggregate {}", events.size(), aggregateId);
        } catch (DataIntegrityViolationException e) {
            throw new OptimisticLockException("Version conflict when saving", e);
        }
    }

    @Override
    public List<DomainEvent> loadEvents(String aggregateId) {
        return repository.findByAggregateIdOrderByVersionAsc(aggregateId)
                .stream()
                .map(this::toDomainEvent)
                .collect(Collectors.toList());
    }

    @Override
    public int getCurrentVersion(String aggregateId) {
        return repository.findTopByAggregateIdOrderByVersionDesc(aggregateId)
                .map(EventEntity::getVersion)
                .orElse(0);
    }

    private EventEntity toEntity(String aggregateId, DomainEvent event) {
        EventEntity entity = new EventEntity();
        entity.setAggregateId(aggregateId);
        entity.setEventType(event.getClass().getSimpleName());
        entity.setVersion(event.getVersion());
        entity.setOccurredAt(event.getOccurredAt());
        try {
            entity.setEventData(objectMapper.writeValueAsString(event));
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
        return entity;
    }

    private DomainEvent toDomainEvent(EventEntity entity) {
        try {
            String eventType = entity.getEventType();
            String eventData = entity.getEventData();

            log.info("Deserializing event: type={}, data={}", eventType, eventData);

            // Map event_type to actual class
            DomainEvent event;
            switch (eventType) {
                case "AccountCreated":
                    event = objectMapper.readValue(eventData, AccountCreated.class);
                    break;
                case "MoneyDeposited":
                    event = objectMapper.readValue(eventData, MoneyDeposited.class);
                    break;
                case "MoneyWithdrawn":
                    event = objectMapper.readValue(eventData, MoneyWithdrawn.class);
                    break;
                default:
                    throw new RuntimeException("Unknown event type: " + eventType);
            }

            return event;
        } catch (Exception e) {
            log.error("Failed to deserialize event: {}", entity.getEventType(), e);
            throw new RuntimeException("Failed to deserialize event: " + entity.getEventType(), e);
        }
    }
}
