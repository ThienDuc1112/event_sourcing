package com.example.bank.interfaces.dto;

import java.time.Instant;

public record TransactionResponse(Long id, String accountId, String type, double amount, Instant timestamp) {}
