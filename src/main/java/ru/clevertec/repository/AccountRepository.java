package ru.clevertec.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import ru.clevertec.model.Account;
import ru.clevertec.model.User;

public interface AccountRepository {

    User getUserByLoginAndPassword(String login, String password);

    List<Account> getAccountsByUserId(Long userId);

    Account getAccountByAccountNumber(String accountNumber);

    List<Account> getAllAccounts();

    BigDecimal updateAccountBalance(Long accountId, BigDecimal newBalance, Connection connection);

}
