package com.example.bank.interfaces.dto;

import java.time.Instant;

public record AccountResponse(String accountId, double balance, Instant lastUpdated) {}
