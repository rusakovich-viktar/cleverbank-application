package ru.clevertec.service;

import java.math.BigDecimal;
import ru.clevertec.model.Account;

public interface TransactionService {

//    Transaction createTransaction(Transaction transaction, Connection connection);

    void doTransferFunds(Account sourceAccount, Account targetAccount, BigDecimal amount);

    void replenishAccountBalance(Account sourceAccount, Account targetAccount, BigDecimal amount);

    void withdrawFromAccount(Account sourceAccount, Account targetAccount, BigDecimal amount);

}