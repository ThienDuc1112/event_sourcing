package com.example.bank.interfaces.projector;

import com.example.bank.application.port.AccountViewRepositoryPort;
import com.example.bank.domain.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class AccountProjector {

    private final AccountViewRepositoryPort repository;

    public AccountProjector(AccountViewRepositoryPort repository) {
        this.repository = repository;
    }

    @EventListener
    @Transactional
    public void on(MoneyDeposited event) {
        AccountViewRepositoryPort.AccountViewDto account = repository.findById(event.getAggregateId()).orElseThrow();
        repository.saveAccount(new AccountViewRepositoryPort.AccountViewDto(
                account.accountId(),
                event.getNewBalance(),
                Instant.now()
        ));

        repository.saveTransaction(new AccountViewRepositoryPort.TransactionViewDto(
                null,
                event.getAggregateId(),
                "DEPOSIT",
                event.getAmount(),
                event.getOccurredAt()
        ));
    }

    @EventListener
    @Transactional
    public void on(MoneyWithdrawn event) {
        AccountViewRepositoryPort.AccountViewDto account = repository.findById(event.getAggregateId()).orElseThrow();
        repository.saveAccount(new AccountViewRepositoryPort.AccountViewDto(
                account.accountId(),
                event.getNewBalance(),
                Instant.now()
        ));

        repository.saveTransaction(new AccountViewRepositoryPort.TransactionViewDto(
                null,
                event.getAggregateId(),
                "WITHDRAW",
                event.getAmount(),
                event.getOccurredAt()
        ));
    }
}