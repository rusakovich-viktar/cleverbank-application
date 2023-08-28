package ru.clevertec.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import ru.clevertec.model.Account;
import ru.clevertec.model.User;

public interface AccountService {
    void replenish(Account account, BigDecimal amount);

    void withdraw(Account account, BigDecimal amount);

    void transfer(Account sourceAccount, Account targetAccount, BigDecimal amount);

//    BigDecimal getAccountBalanceByAccountNumber(String accountNumber);

    Account getAccountById(Long accountId);

    List<Account> findAccountsByUserLogin(String userLogin) throws SQLException;

    Long findUserIdByLogin(String userLogin);

    User findUserByLogin(String login);

    User findUserByLoginAndPassword(String login, String password);

    List<Account> findAccountsByUserId(Long userId);


    void replenishAccountBalance(Long accountId, BigDecimal amount);

    void withdrawFromAccount(Long accountId, BigDecimal amount);

    void transferFunds(Long senderAccountId, Long receiverAccountId, BigDecimal amount);

    Account findAccountByNumber(String accountNumber);

}
