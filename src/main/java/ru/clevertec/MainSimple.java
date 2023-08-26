package ru.clevertec;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import ru.clevertec.model.Account;
import ru.clevertec.model.User;
import ru.clevertec.repository.AccountRepository;
import ru.clevertec.repository.impl.AccountRepositoryImpl;
import ru.clevertec.service.AccountService;
import ru.clevertec.service.impl.AccountServiceImpl;
import ru.clevertec.util.DbUtilsYaml;

public class MainSimple {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = DbUtilsYaml.connection()) {
            AccountRepository accountRepository = new AccountRepositoryImpl(connection);
            AccountService accountService = new AccountServiceImpl(accountRepository);

            Scanner scanner = new Scanner(System.in);


            int choice;
            do {
                System.out.println();
                System.out.println("Choose an action:");
                System.out.println("***********************");
                System.out.println("1. View account data");
                System.out.println("2. View all accounts");
                System.out.println("3. Операции со счетом");
                System.out.println("4. Exit");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter account ID: ");
                        Long accountId = scanner.nextLong();
//                        Account account = accountService.getAccountById(accountId);
//                        if (account != null) {
//                            System.out.println("Account found:");
//                            System.out.println("ID: " + account.getId());
//                            System.out.println("Account Number: " + account.getAccountNumber());
//                            System.out.println("Balance: " + account.getBalance());
//                            // Выведите остальные поля
//                        } else {
//                            System.out.println("Account not found.");
//                        }
                        break;
                    case 2:
                        System.out.print("Enter user login: ");
                        scanner.nextLine();
                        String userLogin = scanner.nextLine();
                        User userByLogin = accountService.findUserByLogin(userLogin);
                        if (userByLogin.getId() != null) {
                            List<Account> accounts = accountService.findAccountsByUserId(userByLogin.getId());
                            if (!accounts.isEmpty()) {
                                System.out.println("________________________________");
                                System.out.println("Hello " + userByLogin.getLastName() + " " + userByLogin.getFirstName() + " " + userByLogin.getPatronymic() + ", your accounts is:");
                                System.out.println();
                                for (Account account : accounts) {

                                    System.out.println("Account Number: " + account.getAccountNumber() + " || " + account.getBank().getName());
                                    System.out.println("Balance: " + account.getBalance());
                                    System.out.println("//");
                                }
                                System.out.println();
                                System.out.println("Do you want to see details of account? (y/n)");
                                System.out.println("***********************");
                                String seeMore = scanner.next();
                                if ("y".equalsIgnoreCase(seeMore)) {
                                    System.out.println();
                                    System.out.println("Choose an account:");
                                    System.out.println("***********************");
                                    // Выводим список доступных счетов
                                    for (int i = 0; i < accounts.size(); i++) {
                                        System.out.println((i + 1) + ". Account Number: " + accounts.get(i).getAccountNumber()) ;
                                    }
                                    // Получаем выбор пользователя
                                    int accountChoice = scanner.nextInt();
                                    if (accountChoice >= 1 && accountChoice <= accounts.size()) {
                                        Account selectedAccount = accounts.get(accountChoice - 1);
                                        // Отображаем подробную информацию о выбранном счете
                                        System.out.println("Account Number: " + selectedAccount.getAccountNumber());
                                        System.out.println("Bank Name: " + selectedAccount.getBank().getName());
                                        System.out.println("Balance: " + selectedAccount.getBalance());
                                        System.out.println("Currency: " + selectedAccount.getCurrency());
                                        System.out.println("Account Opening Date: " + selectedAccount.getAccountOpeningDate());
                                    } else {
                                        System.out.println("Exiting...");
                                    }
                                } else {
                                    System.out.println("Exit to main menu");
                                }
                            } else {
                                System.err.println("No accounts found for user " + userLogin);
                            }
                        } else {
                            System.err.println("User with login " + userLogin + " not found.");
                        }
                        break;
                    case 3:
                        System.out.println("Операции со счетом: ");
                        System.out.println("1. Пополнить счет");
                        System.out.println("2. Снять со счета");
                        System.out.println("3. Перевод на другой счет");
                        int innerChoice = scanner.nextInt();
                        switch (innerChoice) {
                            case 1:
                                System.out.println("1. Пополнить счет");
                                int accountChoiceOne = scanner.nextInt();
                                //TODO ПОПОЛНИТЬ СВОЙ ИЛИ ЧУЖОЙ, ЕСЛИ СВОЙ ТО ВЫБРАТЬ КАКОЙ ИЗ ПО НОМЕРУ
                                break;
                            case 2:
                                System.out.println("2. Снять со счета");
                                int accountChoiceTwo = scanner.nextInt();

                                break;
                            case 3:
                                System.out.println("3. Перевод на другой счет");
                                int accountChoiceThree = scanner.nextInt();

                                break;
                            default:
                                System.out.println("Invalid inner choice");
                                break;
                        }


                        break;
                    case 4:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.err.println("Invalid choice. Please try again.");
                        break;
                }
            } while (choice != 4);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


