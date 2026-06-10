package com.example.bank.infrastructure.eventstore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaEventStoreRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findByAggregateIdOrderByVersionAsc(String aggregateId);
    Optional<EventEntity> findTopByAggregateIdOrderByVersionDesc(String aggregateId);
}
