package ru.clevertec.repository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import ru.clevertec.model.Account;
import ru.clevertec.model.User;

public interface AccountRepository {

    Account findById(Long accountId);

    List<Account> findAccountsByUserLogin(String userLogin);

    Long findUserIdByLogin(String login);

    User findUserByLogin(String login);

    User findUserByLoginAndPassword(String login, String password);

    List<Account> findAccountsByUserId(Long userId);

    void updateAccount(Account account);

    Account findAccountByNumber(String accountNumber);

}
