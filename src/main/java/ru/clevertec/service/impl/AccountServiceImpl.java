package ru.clevertec.service.impl;

import java.math.BigDecimal;
import ru.clevertec.model.Account;
import ru.clevertec.service.AccountService;

public class AccountServiceImpl implements AccountService {
    @Override
    public void replenish(Account account, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            account.setBalance(account.getBalance().add(amount));
        }
    }

    @Override
    public void withdraw(Account account, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0 && account.getBalance().compareTo(amount) >= 0) {
            account.setBalance(account.getBalance().subtract(amount));
        }
    }

    @Override
    public void transfer(Account sourceAccount, Account targetAccount, BigDecimal amount) {
        if (targetAccount != null && amount.compareTo(BigDecimal.ZERO) > 0
                && sourceAccount.getBalance().compareTo(amount) >= 0) {
            withdraw(sourceAccount, amount);
            replenish(targetAccount, amount);
        }
    }
}
