package ru.clevertec.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import ru.clevertec.model.Account;
import ru.clevertec.model.Transaction;
import ru.clevertec.model.TransactionType;
import ru.clevertec.service.AccountService;
import ru.clevertec.service.TransactionService;

public class TransactionServiceImpl implements TransactionService {

    private final AccountService accountService;

    public TransactionServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void performTransaction(Account sourceAccount, Account targetAccount, BigDecimal amount, TransactionType type) {
        if (type == TransactionType.REPLENISHMENT) {
            accountService.withdraw(sourceAccount, amount);
            accountService.replenish(targetAccount, amount);
        } else if (type == TransactionType.WITHDRAWAL) {
            accountService.replenish(sourceAccount, amount);
            accountService.withdraw(targetAccount, amount);
        } else if (type == TransactionType.TRANSFERRING) {
            accountService.withdraw(sourceAccount, amount);
            accountService.replenish(targetAccount, amount);
        }
    }
}
