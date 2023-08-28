package ru.clevertec.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import ru.clevertec.model.Account;
import ru.clevertec.model.Bank;
import ru.clevertec.model.Transaction;
import ru.clevertec.model.TransactionType;
import ru.clevertec.repository.TransactionRepository;
import ru.clevertec.service.TransactionService;

public class TransactionServiceImpl implements TransactionService {

    //    private final AccountService accountService;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void createTransaction(Transaction transaction) {
        transactionRepository.createTransaction(transaction);
    }

    @Override
    public void transferFunds(Account sourceAccount, Account targetAccount, BigDecimal amount) {
        transactionRepository.transferFunds(sourceAccount, targetAccount, amount);
    }


//    @Override
//    public void performTransaction(Account sourceAccount, Account targetAccount, BigDecimal amount, TransactionType type) {
//        if (type == TransactionType.REPLENISHMENT) {
//            accountService.withdraw(sourceAccount, amount);
//            accountService.replenish(targetAccount, amount);
//        } else if (type == TransactionType.WITHDRAWAL) {
//            accountService.replenish(sourceAccount, amount);
//            accountService.withdraw(targetAccount, amount);
//        } else if (type == TransactionType.TRANSFERRING) {
//            accountService.withdraw(sourceAccount, amount);
//            accountService.replenish(targetAccount, amount);
//        }
}

