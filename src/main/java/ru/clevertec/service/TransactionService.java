package ru.clevertec.service;

import java.math.BigDecimal;
import ru.clevertec.model.Account;
import ru.clevertec.model.Transaction;
import ru.clevertec.model.TransactionType;

public interface TransactionService {
//    void performTransaction(Account sourceAccount, Account targetAccount, BigDecimal amount, TransactionType type);

    void createTransaction(Transaction transaction);

    void transferFunds(Account sourceAccount, Account targetAccount, BigDecimal amount);


}