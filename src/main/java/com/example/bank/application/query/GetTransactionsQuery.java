package com.example.bank.application.query;

public class GetTransactionsQuery {
    private final String accountId;

    public GetTransactionsQuery(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() { return accountId; }
}
