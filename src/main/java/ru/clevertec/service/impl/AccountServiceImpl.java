package ru.clevertec.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.clevertec.model.Account;
import ru.clevertec.model.Transaction;
import ru.clevertec.model.TransactionType;
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
    public User findUserByLogin(String login) {
        return accountRepository.findUserByLogin(login);
    }

    @Override
    public User findUserByLoginAndPassword(String login, String password) {
        return accountRepository.findUserByLoginAndPassword(login, password);
    }


    @Override
    public List<Account> findAccountsByUserId(Long userId) {
        return accountRepository.findAccountsByUserId(userId);
    }

    @Override
    public void replenishAccountBalance(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        // Выполняем операцию пополнения счета
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        accountRepository.updateAccount(account);

        // Создаем и сохраняем транзакцию
        Transaction transaction = new Transaction();
        transaction.setCurrency(account.getCurrency());
        transaction.setAmount(amount);
        transaction.setSourceAccount(account);
        transaction.setTargetAccount(account);
        transaction.setSourceBank(null);
        transaction.setTargetBank(account.getBank());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(TransactionType.REPLENISHMENT);
        transactionService.createTransaction(transaction);

        System.out.println("Replenishment successful. New balance: " + account.getBalance());
    }

    @Override
    public void withdrawFromAccount(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId);


        if (account == null) {
            System.err.println("Account not found.");
            throw new IllegalArgumentException("Account not found");
        }

        // Выполняем операцию пополнения счета
        BigDecimal newBalance = account.getBalance().subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        account.setBalance(newBalance);
        accountRepository.updateAccount(account);

        // Создаем и сохраняем транзакцию
        Transaction transaction = new Transaction();
        transaction.setCurrency(account.getCurrency());
        transaction.setAmount(amount);
        transaction.setSourceAccount(account);
        transaction.setTargetAccount(account);
        transaction.setSourceBank(account.getBank());
        transaction.setTargetBank(null);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(TransactionType.WITHDRAWAL);
        transactionService.createTransaction(transaction);

        System.out.println("|______________________________________");
        System.out.println("|             Банковский чек");
        System.out.println("|Чек: " + transaction.getId());
        System.out.println("|" + transaction.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " " + transaction.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println("|Тип транзакции " + transaction.getType().getTranslation());
        System.out.println("|Банк отправителя>: " + transaction.getSourceBank().getName());
//        System.out.println("|Банк получателя: " + transaction.getSourceBank().getName());
        System.out.println("|Счет отправителя: " + transaction.getSourceAccount().getAccountNumber());
        System.out.println("|Счет получателя: " + transaction.getTargetAccount().getAccountNumber());
        System.out.println("|Сумма: " + transaction.getAmount() + " " + transaction.getCurrency().getCurrencyCode());
        System.out.println("|_____________________________________");


        System.out.println("WITHDRAWAL successful. New balance: " + account.getBalance());
    }

    @Override
    public void transferFunds(Long senderAccountId, Long receiverAccountId, BigDecimal amount) {

    }

    @Override
    public Account findAccountByNumber(String accountNumber) {
        return accountRepository.findAccountByNumber(accountNumber);
    }


}


//    @Override
//    public BigDecimal getAccountBalanceByAccountNumber(String accountNumber) {
//        return accountRepository.getAccountBalanceByAccountNumber(accountNumber);
//    }


