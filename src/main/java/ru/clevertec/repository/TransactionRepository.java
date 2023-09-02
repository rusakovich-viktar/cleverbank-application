package ru.clevertec.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import ru.clevertec.model.Account;
import ru.clevertec.model.Transaction;

public interface TransactionRepository {

    Transaction saveTransaction(Transaction transaction, Connection connection);

    Transaction doTransferFunds(Account sourceAccount, Account targetAccount, BigDecimal amount);

    Transaction replenishAccountBalance(Account sourceAccount, Account targetAccount, BigDecimal amount);

    Transaction withdrawFromAccount(Account sourceAccount, Account targetAccount, BigDecimal amount);

}
