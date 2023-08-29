package ru.clevertec.service;

import java.math.BigDecimal;
import java.sql.Connection;
import ru.clevertec.model.Account;
import ru.clevertec.model.Transaction;

public interface TransactionService {

    Long createTransaction(Transaction transaction, Connection connection);

    void doTransferFunds(Account sourceAccount, Account targetAccount, BigDecimal amount);

    void replenishAccountBalance(Account account, BigDecimal amount);

    void withdrawFromAccount(Account account, BigDecimal amount);

}