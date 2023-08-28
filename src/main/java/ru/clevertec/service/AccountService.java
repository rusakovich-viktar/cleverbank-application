package ru.clevertec.service;

import java.math.BigDecimal;
import java.util.List;
import ru.clevertec.model.Account;
import ru.clevertec.model.User;

public interface AccountService {

    User findUserByLoginAndPassword(String login, String password);

    List<Account> findAccountsByUserId(Long userId);

    void replenishAccountBalance(Account account, BigDecimal amount);

    void withdrawFromAccount(Account account, BigDecimal amount);

    Account findAccountByAccountNumber(String accountNumber);

}
