package ru.clevertec.service;

import java.math.BigDecimal;
import ru.clevertec.model.Account;

public interface TransactionService {
    /**
     * Transfers funds from a source account to a target account.
     *
     * @param sourceAccount The source account from which the funds will be debited.
     * @param targetAccount The target account to which the funds will be credited.
     * @param amount        The amount of funds to transfer.
     */
    void doTransferFunds(Account sourceAccount, Account targetAccount, BigDecimal amount);

    /**
     * Replenishes the balance of an account by transferring funds from a source account to the target account.
     *
     * @param sourceAccount The source account from which the funds will be debited for replenishment.
     * @param targetAccount The target account to which the funds will be credited for replenishment.
     * @param amount        The amount of funds to transfer for replenishment.
     */
    void replenishAccountBalance(Account sourceAccount, Account targetAccount, BigDecimal amount);

    /**
     * Withdraws funds from a source account and transfers them to a target account.
     *
     * @param sourceAccount The source account from which the funds will be debited.
     * @param targetAccount The target account to which the funds will be credited.
     * @param amount        The amount of funds to withdraw and transfer.
     */
    void withdrawFromAccount(Account sourceAccount, Account targetAccount, BigDecimal amount);

}