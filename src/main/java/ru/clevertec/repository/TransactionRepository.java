package ru.clevertec.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import ru.clevertec.model.Account;
import ru.clevertec.model.Transaction;

public interface TransactionRepository {
    /**
     * Saves a Transaction object to the database using the provided database connection.
     *
     * @param transaction The Transaction object to be saved.
     * @param connection  The database connection for performing the save operation.
     * @return The saved Transaction object with an assigned ID (if applicable).
     */
    Transaction saveTransaction(Transaction transaction, Connection connection);

    /**
     * Initiates a funds transfer between two accounts and records the transaction.
     *
     * @param sourceAccount The source account from which the funds will be debited.
     * @param targetAccount The target account to which the funds will be credited.
     * @param amount        The amount of funds to transfer.
     * @return A Transaction object representing the completed funds transfer.
     */
    Transaction doTransferFunds(Account sourceAccount, Account targetAccount, BigDecimal amount);

    /**
     * Replenishes the balance of an account by transferring funds from a source account to the target account.
     *
     * @param sourceAccount The source account from which the funds will be debited for replenishment.
     * @param targetAccount The target account to which the funds will be credited for replenishment.
     * @param amount        The amount of funds to transfer for replenishment.
     * @return A Transaction object representing the completed replenishment transaction.
     */
    Transaction replenishAccountBalance(Account sourceAccount, Account targetAccount, BigDecimal amount);

    /**
     * Withdraws funds from a source account and transfers them to a target account.
     *
     * @param sourceAccount The source account from which the funds will be debited.
     * @param targetAccount The target account to which the funds will be credited.
     * @param amount        The amount of funds to withdraw and transfer.
     * @return A Transaction object representing the completed withdrawal transaction.
     */
    Transaction withdrawFromAccount(Account sourceAccount, Account targetAccount, BigDecimal amount);

}
