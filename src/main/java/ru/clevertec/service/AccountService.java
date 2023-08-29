package ru.clevertec.service;

import java.math.BigDecimal;
import java.util.List;
import ru.clevertec.model.Account;
import ru.clevertec.model.User;

public interface AccountService {

    User findUserByLoginAndPassword(String login, String password);

    List<Account> findAccountsByUserId(Long userId);

    Account findAccountByAccountNumber(String accountNumber);

}
