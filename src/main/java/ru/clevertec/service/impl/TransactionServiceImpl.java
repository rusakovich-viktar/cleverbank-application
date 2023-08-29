package ru.clevertec.service.impl;

import java.math.BigDecimal;
import ru.clevertec.model.Account;
import ru.clevertec.model.Transaction;
import ru.clevertec.repository.TransactionRepository;
import ru.clevertec.service.TransactionService;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Long createTransaction(Transaction transaction) {
        return transactionRepository.createTransaction(transaction);
    }

    @Override
    public void doTransferFunds(Account sourceAccount, Account targetAccount, BigDecimal amount) {
        transactionRepository.doTransferFunds(sourceAccount, targetAccount, amount);

    }

    @Override
    public void replenishAccountBalance(Account account, BigDecimal amount) {
        transactionRepository.replenishAccountBalance(account, amount);
    }

    @Override
    public void withdrawFromAccount(Account account, BigDecimal amount) {
        transactionRepository.withdrawFromAccount(account, amount);

    }

}
