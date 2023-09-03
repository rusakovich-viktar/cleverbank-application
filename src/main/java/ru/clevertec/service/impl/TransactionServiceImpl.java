package ru.clevertec.service.impl;

import java.math.BigDecimal;
import lombok.extern.log4j.Log4j2;
import ru.clevertec.model.Account;
import ru.clevertec.model.Transaction;
import ru.clevertec.repository.TransactionRepository;
import ru.clevertec.service.ReceiptService;
import ru.clevertec.service.TransactionService;

@Log4j2
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ReceiptService receiptService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, ReceiptService receiptService) {
        this.transactionRepository = transactionRepository;
        this.receiptService = receiptService;
    }

    @Override
    public void doTransferFunds(Account sourceAccount, Account targetAccount, BigDecimal amount) {
        Transaction transaction = transactionRepository.doTransferFunds(sourceAccount, targetAccount, amount);
        receiptService.doGenerateTheReceipt(transaction);
    }

    @Override
    public void replenishAccountBalance(Account sourceAccount, Account targetAccount, BigDecimal amount) {
        Transaction transaction = transactionRepository.replenishAccountBalance(sourceAccount, targetAccount, amount);
        receiptService.doGenerateTheReceipt(transaction);
    }

    @Override
    public void withdrawFromAccount(Account sourceAccount, Account targetAccount, BigDecimal amount) {
        Transaction transaction = transactionRepository.withdrawFromAccount(sourceAccount, targetAccount, amount);
        receiptService.doGenerateTheReceipt(transaction);
    }


}
