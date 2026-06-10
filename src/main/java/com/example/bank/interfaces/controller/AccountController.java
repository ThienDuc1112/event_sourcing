package com.example.bank.interfaces.controller;

import com.example.bank.application.command.*;
import com.example.bank.application.commandhandler.CommandHandler;
import com.example.bank.application.port.AccountViewRepositoryPort;
import com.example.bank.application.query.*;
import com.example.bank.application.queryhandler.AccountQueryHandler;
import com.example.bank.interfaces.dto.AccountResponse;
import com.example.bank.interfaces.dto.DepositRequest;
import com.example.bank.interfaces.dto.TransactionResponse;
import com.example.bank.interfaces.dto.WithdrawRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final CommandHandler commandHandler;
    private final AccountQueryHandler queryHandler;

    public AccountController(CommandHandler commandHandler, AccountQueryHandler queryHandler) {
        this.commandHandler = commandHandler;
        this.queryHandler = queryHandler;
    }

    @PostMapping("/accounts/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositRequest request) {
        commandHandler.handle(new DepositCommand(request.getAccountId(), request.getAmount()));
        return ResponseEntity.ok("Deposited " + request.getAmount());
    }

    @PostMapping("/accounts/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawRequest request) {
        commandHandler.handle(new WithdrawCommand(request.getAccountId(), request.getAmount()));
        return ResponseEntity.ok("Withdrawn " + request.getAmount());
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String accountId) {
        AccountViewRepositoryPort.AccountViewDto account = queryHandler.handle(new GetAccountQuery(accountId));
        return ResponseEntity.ok(new AccountResponse(account.accountId(), account.balance(), account.lastUpdated()));
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable String accountId) {
        List<AccountViewRepositoryPort.TransactionViewDto> transactions =
                queryHandler.handle(new GetTransactionsQuery(accountId));
        List<TransactionResponse> responses = transactions.stream()
                .map(t -> new TransactionResponse(t.id(), t.accountId(), t.type(), t.amount(), t.timestamp()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
