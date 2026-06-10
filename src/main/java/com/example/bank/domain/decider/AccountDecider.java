package com.example.bank.domain.decider;

import com.example.bank.application.command.Command;
import com.example.bank.application.command.DepositCommand;
import com.example.bank.application.command.WithdrawCommand;
import com.example.bank.domain.aggregate.AccountAggregate;
import com.example.bank.domain.event.DomainEvent;
import com.example.bank.domain.event.MoneyDeposited;
import com.example.bank.domain.event.MoneyWithdrawn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountDecider implements Decider<AccountAggregate, Command, DomainEvent> {

    @Override
    public AccountAggregate initialState() {
        return new AccountAggregate();
    }

    @Override
    public List<DomainEvent> decide(AccountAggregate state, Command command) {
        if (command instanceof DepositCommand) {
            DepositCommand cmd = (DepositCommand) command;
            if (state.getAccountId() == null) {
                throw new IllegalStateException("Account not found");
            }
            if (cmd.getAmount() <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
            return List.of(new MoneyDeposited(
                    state.getAccountId(),
                    state.getVersion() + 1,
                    cmd.getAmount(),
                    state.getBalance() + cmd.getAmount()
            ));
        }

        if (command instanceof WithdrawCommand) {
            WithdrawCommand cmd = (WithdrawCommand) command;
            if (state.getAccountId() == null) {
                throw new IllegalStateException("Account not found");
            }
            if (cmd.getAmount() <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
            if (cmd.getAmount() > state.getBalance()) {
                throw new IllegalStateException("Insufficient balance");
            }
            return List.of(new MoneyWithdrawn(
                    state.getAccountId(),
                    state.getVersion() + 1,
                    cmd.getAmount(),
                    state.getBalance() - cmd.getAmount()
            ));
        }

        throw new IllegalArgumentException("Unknown command: " + command.getClass());
    }

    @Override
    public AccountAggregate evolve(AccountAggregate state, DomainEvent event) {
        AccountAggregate newState = state.copy();

        if (event instanceof MoneyDeposited) {
            MoneyDeposited e = (MoneyDeposited) event;
            newState.setBalance(e.getNewBalance());
            newState.setVersion(e.getVersion());
        } else if (event instanceof MoneyWithdrawn) {
            MoneyWithdrawn e = (MoneyWithdrawn) event;
            newState.setBalance(e.getNewBalance());
            newState.setVersion(e.getVersion());
        }

        return newState;
    }
}
