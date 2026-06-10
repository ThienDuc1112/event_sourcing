package com.example.bank.domain.aggregate;

public class AccountAggregate {
    private String accountId;
    private double balance;
    private int version;

    public AccountAggregate() {}

    public AccountAggregate(String accountId, double balance, int version) {
        this.accountId = accountId;
        this.balance = balance;
        this.version = version;
    }

    public AccountAggregate copy() {
        return new AccountAggregate(this.accountId, this.balance, this.version);
    }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }

    public boolean exists() { return accountId != null; }
}