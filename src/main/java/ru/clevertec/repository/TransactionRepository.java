package ru.clevertec.repository;

import java.math.BigDecimal;
import ru.clevertec.model.Account;
import ru.clevertec.model.Transaction;

public interface TransactionRepository {

    void createTransaction(Transaction transaction);

    void transferFunds(Account sourceAccount, Account targetAccount, BigDecimal amount);

}
