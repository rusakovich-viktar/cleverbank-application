package ru.clevertec.service.impl;

import java.math.BigDecimal;
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
    public User findUserByLoginAndPassword(String login, String password) {
        return accountRepository.findUserByLoginAndPassword(login, password);
    }

    @Override
    public List<Account> findAccountsByUserId(Long userId) {
        return accountRepository.findAccountsByUserId(userId);
    }

    @Override
    public void replenishAccountBalance(Account account, BigDecimal amount) {

        Transaction transaction = new Transaction();
        transaction.setCurrency(account.getCurrency());
        transaction.setAmount(amount);
        transaction.setSourceAccount(account);
        transaction.setTargetAccount(account);
        transaction.setSourceBank(account.getBank());
        transaction.setTargetBank(account.getBank());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(TransactionType.REPLENISHMENT);
        Long transactionId = transactionService.createTransaction(transaction);

        accountRepository.updateAccountBalance(account.getId(), account.getBalance().add(amount));

        System.out.println("|______________________________________");
        System.out.println("|             Банковский чек");
        System.out.println("|Чек: " + transactionId);
        System.out.println("|" + transaction.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " " + transaction.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println("|Тип транзакции " + transaction.getType().getTranslation());
        System.out.println("|Банк отправителя: " + transaction.getSourceBank().getName());
        System.out.println("|Банк получателя: " + transaction.getTargetBank().getName());
        System.out.println("|Счет отправителя: " + transaction.getSourceAccount().getAccountNumber());
        System.out.println("|Счет получателя: " + transaction.getTargetAccount().getAccountNumber());
        System.out.println("|Сумма: " + transaction.getAmount() + " " + transaction.getCurrency().getCurrencyCode());
        System.out.println("|_____________________________________");

        System.out.println("Replenishment successful. New balance: " + account.getBalance().add(transaction.getAmount()));
    }

    @Override
    public void withdrawFromAccount(Account account, BigDecimal amount) {

        Transaction transaction = new Transaction();
        transaction.setCurrency(account.getCurrency());
        transaction.setAmount(amount);
        transaction.setSourceAccount(account);
        transaction.setTargetAccount(account);
        transaction.setSourceBank(account.getBank());
        transaction.setTargetBank(account.getBank());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(TransactionType.WITHDRAWAL);
        Long transactionId = transactionService.createTransaction(transaction);

        accountRepository.updateAccountBalance(account.getId(), account.getBalance().subtract(amount));

        System.out.println("|______________________________________");
        System.out.println("|             Банковский чек");
        System.out.println("|Чек: " + transactionId);
        System.out.println("|" + transaction.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " " + transaction.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println("|Тип транзакции " + transaction.getType().getTranslation());
        System.out.println("|Банк отправителя: " + transaction.getSourceBank().getName());
        System.out.println("|Банк получателя: " + transaction.getTargetBank().getName());
        System.out.println("|Счет отправителя: " + transaction.getSourceAccount().getAccountNumber());
        System.out.println("|Счет получателя: " + transaction.getTargetAccount().getAccountNumber());
        System.out.println("|Сумма: " + transaction.getAmount() + " " + transaction.getCurrency().getCurrencyCode());
        System.out.println("|_____________________________________");

        System.out.println("withdrawal successful. New balance: " + account.getBalance().subtract(transaction.getAmount()));
    }

    @Override
    public Account findAccountByAccountNumber(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber);
    }

}
