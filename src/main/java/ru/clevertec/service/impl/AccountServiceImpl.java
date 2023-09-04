package ru.clevertec.service.impl;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.clevertec.model.Account;
import ru.clevertec.model.User;
import ru.clevertec.repository.AccountRepository;
import ru.clevertec.service.AccountService;
import ru.clevertec.service.TransactionService;

/**
 * Implementation of the {@link AccountService} interface that provides account-related services.
 */
@Getter
@Setter
@AllArgsConstructor

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    @Override
    public User getUserByLoginAndPassword(String login, String password) {
        return accountRepository.getUserByLoginAndPassword(login, password);
    }

    @Override
    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.getAccountsByUserId(userId);
    }


    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.getAccountByAccountNumber(accountNumber);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.getAllAccounts();
    }


}
