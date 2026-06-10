package com.example.bank.application.queryhandler;


import com.example.bank.application.port.AccountViewRepositoryPort;
import com.example.bank.application.query.*;
import com.example.bank.domain.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountQueryHandler {
    private final AccountViewRepositoryPort accountViewRepository;

    public AccountQueryHandler(AccountViewRepositoryPort accountViewRepository) {
        this.accountViewRepository = accountViewRepository;
    }

    public AccountViewRepositoryPort.AccountViewDto handle(GetAccountQuery query) {
        return accountViewRepository.findById(query.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(query.getAccountId()));
    }

    public List<AccountViewRepositoryPort.TransactionViewDto> handle(GetTransactionsQuery query) {
        return accountViewRepository.findTransactionsByAccountId(query.getAccountId());
    }
}
