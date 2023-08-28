package ru.clevertec.repository;

import java.math.BigDecimal;
import ru.clevertec.model.Account;
import ru.clevertec.model.Transaction;

public interface TransactionRepository {

    Long createTransaction(Transaction transaction);

    void doTransferFunds(Account sourceAccount, Account targetAccount, BigDecimal amount);

}
