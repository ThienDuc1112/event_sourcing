package com.example.bank.application.port;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AccountViewRepositoryPort {

    record AccountViewDto(String accountId, double balance, Instant lastUpdated) {}
    record TransactionViewDto(Long id, String accountId, String type, double amount, Instant timestamp) {}

    Optional<AccountViewDto> findById(String accountId);
    List<AccountViewDto> findAll();
    List<TransactionViewDto> findTransactionsByAccountId(String accountId);
    void saveAccount(AccountViewDto account);
    void saveTransaction(TransactionViewDto transaction);
}