package com.example.bank.domain.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MoneyWithdrawn extends DomainEvent {
    private final double amount;
    private final double newBalance;

    @JsonCreator
    public MoneyWithdrawn(
            @JsonProperty("aggregateId") String aggregateId,
            @JsonProperty("version") int version,
            @JsonProperty("amount") double amount,
            @JsonProperty("newBalance") double newBalance) {
        super(aggregateId, version);
        this.amount = amount;
        this.newBalance = newBalance;
    }

    public double getAmount() {
        return amount;
    }

    public double getNewBalance() {
        return newBalance;
    }

    @Override
    public String getEventType() {
        return "MONEY_WITHDRAWN";
    }
}