package com.example.bank.infrastructure.repository;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class AccountViewEntity {
    @Id
    private String accountId;
    private double balance;
    private Instant lastUpdated;

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public Instant getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Instant lastUpdated) { this.lastUpdated = lastUpdated; }
}