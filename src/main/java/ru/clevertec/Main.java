package ru.clevertec;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import ru.clevertec.model.Account;
import ru.clevertec.model.Bank;
import ru.clevertec.model.ApplicationStatus;
import ru.clevertec.model.TransactionType;
import ru.clevertec.model.User;
import ru.clevertec.service.AccountService;
import ru.clevertec.service.TransactionService;
import ru.clevertec.service.impl.AccountServiceImpl;
import ru.clevertec.service.impl.TransactionServiceImpl;

public class Main {

    private static TransactionService transactionService;
    private static AccountService accountService;

    public static void main(String[] args) {
        ApplicationStatus applicationStatus = new ApplicationStatus();
        // Создаем сервисы
        AccountService accountService = new AccountServiceImpl();
        transactionService = new TransactionServiceImpl(accountService);
        init();
//        while (cleverBankEnvironmentService.isWorking()) {
//            environmentUIService.openBankEnvironment();
//        }
        System.out.println("Clever Bank is closed");


//        // Выполняем операцию перевода средств
//        BigDecimal amount = BigDecimal.valueOf(200);
//        TransactionType transactionType = TransactionType.TRANSFERRING;
//
//        transactionService.performTransaction(account1, account2, amount, transactionType);
//
//        // Выводим информацию о счетах после операции
//        System.out.println("Account 1 balance: " + account1.getBalance());
//        System.out.println("Account 2 balance: " + account2.getBalance());
    }

    private static void init() {
        // Создаем банки
        Bank bank1 = new Bank(1L, "Bank1", new ArrayList<>());
        Bank bank2 = new Bank(2L, "Bank2", new ArrayList<>());

        // Создаем пользователей
        User user1 = new User(1L, "12345", "John", "Doe", "Smith", "john123", "password", new ArrayList<>());
        User user2 = new User(2L, "67890", "Alice", "Johnson", "Brown", "alice456", "password", new ArrayList<>());

        // Создаем счета
        Account account1 = new Account(1L, "123456789", BigDecimal.valueOf(1000), Currency.getInstance("USD"), LocalDateTime.now(), user1, bank1, new ArrayList<>());
        Account account2 = new Account(2L, "987654321", BigDecimal.valueOf(2000), Currency.getInstance("USD"), LocalDateTime.now(), user2, bank2, new ArrayList<>());

        // Создаем список счетов для каждого пользователя
        user1.setAccounts(List.of(account1));
        user2.setAccounts(List.of(account2));

        // Создаем список счетов для каждого банка
        bank1.setAccounts(List.of(account1));
        bank2.setAccounts(List.of(account2));
    }
}
