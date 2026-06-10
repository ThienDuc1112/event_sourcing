package com.example.bank.domain.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountCreated extends DomainEvent {
    private final double initialBalance;

    @JsonCreator
    public AccountCreated(
            @JsonProperty("aggregateId") String aggregateId,
            @JsonProperty("version") int version,
            @JsonProperty("initialBalance") double initialBalance) {
        super(aggregateId, version);
        this.initialBalance = initialBalance;
    }

    public double getInitialBalance() {
        return initialBalance;
    }

    @Override
    public String getEventType() {
        return "ACCOUNT_CREATED";
    }
}