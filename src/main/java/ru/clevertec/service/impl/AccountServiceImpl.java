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

@Getter
@Setter
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    @Override
    public User findUserByLoginAndPassword(String login, String password) {
        return accountRepository.findUserByLoginAndPassword(login, password);
    }

    @Override
    public List<Account> findAccountsByUserId(Long userId) {
        return accountRepository.findAccountsByUserId(userId);
    }


    @Override
    public Account findAccountByAccountNumber(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber);
    }

}
