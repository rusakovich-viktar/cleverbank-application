package ru.clevertec.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.clevertec.model.Account;
import ru.clevertec.repository.AccountRepository;
import ru.clevertec.service.AccountService;

@Getter
@Setter
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;


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

    @Override
    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    @Override
    public List<Account> findAccountsByUserLogin(String userLogin) {
        return accountRepository.findAccountsByUserLogin(userLogin);
    }

    @Override
    public Long findUserIdByLogin(String userLogin) {
        return accountRepository.findUserIdByLogin(userLogin);
    }

    @Override
    public List<Account> findAccountsByUserId(Long userId) {
        return accountRepository.findAccountsByUserId(userId);
    }

//    @Override
//    public BigDecimal getAccountBalanceByAccountNumber(String accountNumber) {
//        return accountRepository.getAccountBalanceByAccountNumber(accountNumber);
//    }
}
