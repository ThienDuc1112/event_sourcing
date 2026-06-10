package com.example.bank.infrastructure.repository;

import com.example.bank.application.port.AccountViewRepositoryPort;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AccountViewRepositoryAdapter implements AccountViewRepositoryPort {

    private final JpaAccountViewRepository accountRepository;
    private final JpaTransactionViewRepository transactionRepository;

    public AccountViewRepositoryAdapter(JpaAccountViewRepository accountRepository,
                                        JpaTransactionViewRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<AccountViewDto> findById(String accountId) {
        return accountRepository.findById(accountId)
                .map(this::toAccountDto);
    }

    @Override
    public List<AccountViewDto> findAll() {
        return accountRepository.findAll().stream()
                .map(this::toAccountDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionViewDto> findTransactionsByAccountId(String accountId) {
        return transactionRepository.findByAccountIdOrderByTimestampDesc(accountId).stream()
                .map(this::toTransactionDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveAccount(AccountViewRepositoryPort.AccountViewDto account) {
        AccountViewEntity entity = new AccountViewEntity();
        entity.setAccountId(account.accountId());
        entity.setBalance(account.balance());
        entity.setLastUpdated(account.lastUpdated());
        accountRepository.save(entity);
    }

    @Override
    public void saveTransaction(TransactionViewDto transaction) {
        TransactionViewEntity entity = new TransactionViewEntity();
        entity.setAccountId(transaction.accountId());
        entity.setType(transaction.type());
        entity.setAmount(transaction.amount());
        entity.setTimestamp(transaction.timestamp());
        transactionRepository.save(entity);
    }

    private AccountViewDto toAccountDto(AccountViewEntity entity) {
        return new AccountViewDto(
                entity.getAccountId(),
                entity.getBalance(),
                entity.getLastUpdated()
        );
    }

    private TransactionViewDto toTransactionDto(TransactionViewEntity entity) {
        return new TransactionViewDto(
                entity.getId(),
                entity.getAccountId(),
                entity.getType(),
                entity.getAmount(),
                entity.getTimestamp()
        );
    }
}
