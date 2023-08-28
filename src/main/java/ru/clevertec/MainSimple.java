package ru.clevertec;

import static ru.clevertec.util.DrawUI.drawStartMenuQ;
import static ru.clevertec.util.DrawUI.drawTransactionMenu;
import static ru.clevertec.util.DrawUI.drawTransactionTargetMenu;
import static ru.clevertec.util.DrawUI.drawTransferOperationHeader;
import static ru.clevertec.util.DrawUI.drawTransferSourceHeader;
import static ru.clevertec.util.DrawUI.drawViewAllAccountsOperationHeader;
import static ru.clevertec.util.DrawUI.drawWithdrawOperationHeader;
import static ru.clevertec.util.InputUtils.readIntFromConsole;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import ru.clevertec.model.Account;
import ru.clevertec.model.User;
import ru.clevertec.repository.AccountRepository;
import ru.clevertec.repository.TransactionRepository;
import ru.clevertec.repository.impl.AccountRepositoryImpl;
import ru.clevertec.repository.impl.TransactionRepositoryImpl;
import ru.clevertec.service.AccountService;
import ru.clevertec.service.TransactionService;
import ru.clevertec.service.impl.AccountServiceImpl;
import ru.clevertec.service.impl.BankUserInterfaceServiceImpl;
import ru.clevertec.service.impl.ReceiptServiceImpl;
import ru.clevertec.service.impl.TransactionServiceImpl;
import ru.clevertec.util.DbUtilsYaml;
import ru.clevertec.util.DrawUI;

public class MainSimple {
    public static void main(String[] args) {
        doCleverBankApplicationRun();
    }

    private static void doCleverBankApplicationRun() {
        try (Connection connection = DbUtilsYaml.connection()) {
            AccountRepository accountRepository = new AccountRepositoryImpl(connection);
            TransactionRepository transactionRepository = new TransactionRepositoryImpl(connection);
            ReceiptServiceImpl receiptService = new ReceiptServiceImpl();
            TransactionService transactionService = new TransactionServiceImpl(transactionRepository, receiptService);
            AccountService accountService = new AccountServiceImpl(accountRepository, transactionService);
            BankUserInterfaceServiceImpl bankUserInterfaceService = new BankUserInterfaceServiceImpl(connection);


            Scanner scanner = new Scanner(System.in);

            boolean userLoggedIn = false;
            User userByLoginAndPassword = null;
            do {
                if (!userLoggedIn) {
                    DrawUI.drawLogo();
                    DrawUI.drawEnterLoginOperationHeader();
                    String userLogin = scanner.nextLine();

                    DrawUI.drawEnterPasswordOperationHeader();
                    String userPassword = scanner.nextLine();

                    userByLoginAndPassword = accountService.findUserByLoginAndPassword(userLogin, userPassword);

                    if (userByLoginAndPassword != null && userLogin.equals(userByLoginAndPassword.getLogin()) && userPassword.equals(userByLoginAndPassword.getPassword())) {
                        userLoggedIn = true;
                    } else {
                        System.err.println("Incorrect login or password. Please try again.");
                    }
                    //TODO Убрать энтити в ДТО
                }
                userLoggedIn = showMainBankMenu(transactionService, accountService, scanner, userLoggedIn, userByLoginAndPassword);
            }
            while (true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean showMainBankMenu(TransactionService transactionService, AccountService accountService, Scanner scanner, boolean userLoggedIn, User userByLoginAndPassword) {
        if (userLoggedIn) {

            drawStartMenuQ();
            int bound = 4;
            int choice = readIntFromConsole("Введите число от 1 до " + bound, bound);

            switch (choice) {
                case 1 -> { //TODO rename
                }
                case 2 -> viewAccountsInformation(accountService, scanner, userByLoginAndPassword);
                case 3 ->
                        doMainOperationsWithAccount(transactionService, accountService, scanner, userByLoginAndPassword);
                case 4 -> userLoggedIn = doLogoutAndExit();
                default -> System.err.println("Invalid choice. Please try again.");
            }
        }
        return userLoggedIn;
    }
    
    private static boolean doLogoutAndExit() {
        boolean userLoggedIn;
        System.out.println("Exiting...");
        userLoggedIn = false;
        return userLoggedIn;
    }

    private static void doMainOperationsWithAccount(TransactionService transactionService, AccountService accountService, Scanner scanner, User userByLoginAndPassword) {
        drawTransactionMenu();
        int boundTransactionChoice = 3;
        int innerChoice = readIntFromConsole("Введите число от 1 до " + boundTransactionChoice, boundTransactionChoice);

        switch (innerChoice) {
            case 1 -> replenishInnerMenu(accountService, scanner, userByLoginAndPassword);
            case 2 -> withdrawInnerMenu(accountService, scanner, userByLoginAndPassword);
            case 3 -> transferInnerMenu(transactionService, accountService, scanner, userByLoginAndPassword);
            default -> System.err.println("Invalid inner choice");
        }
    }

    private static void transferInnerMenu(TransactionService transactionService,
                                          AccountService accountService,
                                          Scanner scanner, User userByLoginAndPassword) {
        drawTransferOperationHeader();
        drawTransferSourceHeader();
        List<Account> accounts;
        Account sourceAccount;
        Account targetAccount = null;

        accounts = accountService.findAccountsByUserId(userByLoginAndPassword.getId());
        if (!accounts.isEmpty()) {
            for (int i = 0; i < accounts.size(); i++) {
                System.out.println((i + 1) + ". Account Number: " + accounts.get(i).getAccountNumber() + "  || Current balance: " + accounts.get(i).getBalance());
            }

            int accountChoice = scanner.nextInt();
            if (accountChoice >= 1 && accountChoice <= accounts.size()) {
                sourceAccount = accounts.get(accountChoice - 1);
                drawTransactionTargetMenu();

                int boundTransactionChoice = 2;
                int innerChoice = readIntFromConsole("Введите число от 1 до " + boundTransactionChoice, boundTransactionChoice);

                switch (innerChoice) {
                    case 1 -> {

                        for (int i = 0; i < accounts.size(); i++) {
                            System.out.println((i + 1) + ". Account Number: " + accounts.get(i).getAccountNumber() + "  || Current balance: " + accounts.get(i).getBalance());
                        }
                        int innerAccountChoice = scanner.nextInt();
                        if (innerAccountChoice >= 1 && innerAccountChoice <= accounts.size()) {
                            targetAccount = accounts.get(innerAccountChoice - 1);
                        } else {
                            System.err.println("Invalid account choice.");
                        }
                    }
                    case 2 -> {
                        System.out.println("Введите номер счета получателя:");
                        String targetAccountNumber = scanner.next();
                        targetAccount = accountService.findAccountByAccountNumber(targetAccountNumber);
                        if (targetAccount == null) {
                            System.out.println("Счет получателя не найден. Пожалуйста, убедитесь, что вы ввели правильный номер счета.");
                        } else {
                            System.out.println("Счет найден. Фамилия получателя: " + targetAccount.getUser().getLastName());
                        }
                    }
                    default -> System.err.println("Invalid inner choice");
                }
                System.out.println("Enter the amount to transfer:");
                //TODO crash from string
                BigDecimal amountToTargetTransfer = scanner.nextBigDecimal();
                if (amountToTargetTransfer.compareTo(sourceAccount.getBalance()) > 0) {
                    System.out.println("Недостаточно средств на счете");
                } else {
                    transactionService.doTransferFunds(sourceAccount, targetAccount, amountToTargetTransfer);
                }
            } else {
                System.err.println("Invalid account choice.");
            }
        } else {
            System.err.println("No accounts found for the user.");
        }

    }

    private static void withdrawInnerMenu(AccountService accountService, Scanner scanner, User
            userByLoginAndPassword) {
        List<Account> accounts;
        drawWithdrawOperationHeader();
        accounts = accountService.findAccountsByUserId(userByLoginAndPassword.getId());
        if (!accounts.isEmpty()) {
            for (int i = 0; i < accounts.size(); i++) {
                System.out.println((i + 1) + ". Account Number: " + accounts.get(i).getAccountNumber() + "  || Current balance: " + accounts.get(i).getBalance());
            }
            int accountChoice = scanner.nextInt();
            if (accountChoice >= 1 && accountChoice <= accounts.size()) {
                Account selectedAccount = accounts.get(accountChoice - 1);
                System.out.println("Enter the amount to withdraw:");
                //TODO crash from string
                BigDecimal amountToTargetWithdraw = scanner.nextBigDecimal();
                if (amountToTargetWithdraw.compareTo(selectedAccount.getBalance()) > 0) {
                    System.out.println("Недостаточно средств на счете");
                } else {
                    accountService.withdrawFromAccount(selectedAccount, amountToTargetWithdraw);
                }
            } else {
                System.err.println("Invalid account choice.");
            }
        } else {
            System.err.println("No accounts found for the user.");
        }
    }

    private static void replenishInnerMenu(AccountService accountService, Scanner scanner, User
            userByLoginAndPassword) {
        List<Account> accounts;
        DrawUI.drawReplenishOperationHeader();
        accounts = accountService.findAccountsByUserId(userByLoginAndPassword.getId());
        if (!accounts.isEmpty()) {
            for (int i = 0; i < accounts.size(); i++) {
                System.out.println((i + 1) + ". Account Number: " + accounts.get(i).getAccountNumber() + "  || Current balance: " + accounts.get(i).getBalance());
            }
            int accountChoice = scanner.nextInt();
            if (accountChoice >= 1 && accountChoice <= accounts.size()) {
                Account selectedAccount = accounts.get(accountChoice - 1);

                System.out.println("Enter the amount to replenish:");
                BigDecimal amount = scanner.nextBigDecimal();
//TODO

                accountService.replenishAccountBalance(selectedAccount, amount);
            } else {
                System.err.println("Invalid account choice.");
            }
        } else {
            System.err.println("No accounts found for the user.");
        }
    }

    private static void viewAccountsInformation(AccountService accountService, Scanner scanner, User
            userByLoginAndPassword) {

        drawViewAllAccountsOperationHeader();
        List<Account> accounts = accountService.findAccountsByUserId(userByLoginAndPassword.getId());

        if (!accounts.isEmpty()) {

            System.out.println("Hello " + userByLoginAndPassword.getLastName() + " " + userByLoginAndPassword.getFirstName() + " " + userByLoginAndPassword.getPatronymic() + ", your accounts is:");
            System.out.println();
            for (Account account : accounts) {

                System.out.println("Account Number: " + account.getAccountNumber() + " || " + account.getBank().getName());
                System.out.println("Balance: " + account.getBalance() + " " + account.getCurrency());
                System.out.println();
            }
            System.out.println();
            System.out.println("Do you want to see details of accounts? (y/n)");
            System.out.println("************************************************************");
            String seeMore = scanner.next();
            if ("y".equalsIgnoreCase(seeMore)) {
                DrawUI.drawDetailsAccountInfoOperationHeader();
                for (int i = 0; i < accounts.size(); i++) {
                    System.out.println((i + 1) + ". Account Number: " + accounts.get(i).getAccountNumber() + " " + accounts.get(i).getBalance());
                }
                int accountChoice = scanner.nextInt();
                if (accountChoice >= 1 && accountChoice <= accounts.size()) {
                    Account selectedAccount = accounts.get(accountChoice - 1);
                    System.out.println("Account Number: " + selectedAccount.getAccountNumber());
                    System.out.println("Bank Name: " + selectedAccount.getBank().getName());
                    System.out.println("Balance: " + selectedAccount.getBalance() + " " + selectedAccount.getCurrency());
                    System.out.println("Account Opening Date: " + selectedAccount.getAccountOpeningDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " " + selectedAccount.getAccountOpeningDate().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                } else {
                    System.out.println("Exiting...");
                }
            } else {
                System.out.println("Exit to main menu");
            }
        } else {
            System.err.println("No accounts found for user " + userByLoginAndPassword.getLogin());
        }
    }
}
