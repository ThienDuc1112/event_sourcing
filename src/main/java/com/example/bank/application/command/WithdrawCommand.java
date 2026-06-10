package com.example.bank.application.command;

public class WithdrawCommand implements Command {
    private final String accountId;
    private final double amount;

    public WithdrawCommand(String accountId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    @Override
    public String getAggregateId() { return accountId; }
    public double getAmount() { return amount; }
}
