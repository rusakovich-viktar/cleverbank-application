package ru.clevertec.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import ru.clevertec.model.Account;
import ru.clevertec.model.User;

public interface AccountRepository {

    User findUserByLoginAndPassword(String login, String password);

    List<Account> findAccountsByUserId(Long userId);

    Account findAccountByAccountNumber(String accountNumber);

    BigDecimal updateAccountBalance(Long accountId, BigDecimal newBalance, Connection connection);

}
