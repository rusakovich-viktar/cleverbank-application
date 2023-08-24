package ru.clevertec.service;

import java.math.BigDecimal;
import ru.clevertec.model.Account;

public interface AccountService {
    void replenish(Account account, BigDecimal amount);
    void withdraw(Account account, BigDecimal amount);
    void transfer(Account sourceAccount, Account targetAccount, BigDecimal amount);
}
