package com.example.bank.application.query;

public class GetAccountQuery {
    private final String accountId;

    public GetAccountQuery(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() { return accountId; }
}
