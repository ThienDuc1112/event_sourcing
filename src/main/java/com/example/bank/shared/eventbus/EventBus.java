package com.example.bank.shared.eventbus;

import com.example.bank.domain.event.DomainEvent;

public interface EventBus {
    void publish(DomainEvent event);
}
