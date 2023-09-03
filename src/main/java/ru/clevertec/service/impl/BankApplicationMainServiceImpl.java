package ru.clevertec.service.impl;

import static ru.clevertec.util.Constants.Messages.ENTER_A_NUMBER_BETWEEN_ONE_AND;
import static ru.clevertec.util.Constants.Messages.ENTER_THE_AMOUNT;
import static ru.clevertec.util.Constants.Messages.INSUFFICIENT_FUNDS_ON_THE_ACCOUNT;
import static ru.clevertec.util.Constants.Messages.INVALID_CHOICE_PLEASE_TRY_AGAIN;
import static ru.clevertec.util.Constants.Messages.LOGIN_REFUSE_MESSAGE;
import static ru.clevertec.util.Constants.Messages.LOGIN_SUCCESSFUL_MESSAGE;
import static ru.clevertec.util.Constants.Messages.NO_ACCOUNTS_FOUND_FOR_THE_USER;
import static ru.clevertec.util.DrawUI.drawStartMenuQ;
import static ru.clevertec.util.DrawUI.drawTransactionMenu;
import static ru.clevertec.util.DrawUI.drawTransactionTargetMenu;
import static ru.clevertec.util.DrawUI.drawTransferOperationHeader;
import static ru.clevertec.util.DrawUI.drawTransferSourceHeader;
import static ru.clevertec.util.DrawUI.drawViewAllAccountsOperationHeader;
import static ru.clevertec.util.DrawUI.drawWithdrawOperationHeader;
import static ru.clevertec.util.InputUtils.readIBigDecimalFromConsoleWithoutBounds;
import static ru.clevertec.util.InputUtils.readIntFromConsole;
import static ru.clevertec.util.InputUtils.readIntFromConsoleWithoutBounds;
import static ru.clevertec.util.InputUtils.readStringFromConsole;
import static ru.clevertec.util.InputUtils.waitEnterKeyPressed;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import ru.clevertec.model.Account;
import ru.clevertec.model.User;
import ru.clevertec.repository.AccountRepository;
import ru.clevertec.repository.TransactionRepository;
import ru.clevertec.repository.impl.AccountRepositoryImpl;
import ru.clevertec.repository.impl.TransactionRepositoryImpl;
import ru.clevertec.service.AccountService;
import ru.clevertec.service.BankApplicationMainService;
import ru.clevertec.service.DatabaseMigrationService;
import ru.clevertec.service.ReceiptService;
import ru.clevertec.service.SchedulerService;
import ru.clevertec.service.TransactionService;
import ru.clevertec.util.DrawUI;

@Log4j2
public class BankApplicationMainServiceImpl implements BankApplicationMainService {

    DatabaseMigrationService migrationService = new DatabaseMigrationServiceImpl();
    AccountRepository accountRepository = new AccountRepositoryImpl();
    TransactionRepository transactionRepository = new TransactionRepositoryImpl(accountRepository);
    ReceiptService receiptService = new ReceiptServiceImpl();
    TransactionService transactionService = new TransactionServiceImpl(transactionRepository, receiptService);
    AccountService accountService = new AccountServiceImpl(accountRepository, transactionService);
    SchedulerService schedulerService = new SchedulerServiceImpl(accountService, transactionService);

    public BankApplicationMainServiceImpl() {

    }

    public void doCleverBankApplicationRun() {
        migrationService.migrateDatabase();
        System.setProperty("log4j.configurationFile", "log4j2.yml");
        schedulerService.startPeriodicallyCalculateInterest();

        boolean exit = false;
        boolean userLoggedIn = false;
        User userByLoginAndPassword = null;

        do {
            if (!userLoggedIn) {
                DrawUI.drawLogo();
                userByLoginAndPassword = loginUser();

                if (userByLoginAndPassword != null) {
                    userLoggedIn = true;
                }
            }

            userLoggedIn = showMainBankMenu(transactionService, accountService, userLoggedIn, userByLoginAndPassword);
        } while (shouldContinue());
    }

    private User loginUser() {
        DrawUI.drawEnterLoginOperationHeader();
        String userLogin = readStringFromConsole();

        DrawUI.drawEnterPasswordOperationHeader();
        String userPassword = readStringFromConsole();

        User user = accountService.getUserByLoginAndPassword(userLogin, userPassword);

        if (user != null) {
            log.info(LOGIN_SUCCESSFUL_MESSAGE);
        } else {
            log.info(LOGIN_REFUSE_MESSAGE);
        }

        return user;
    }

    private boolean shouldContinue() {

        return true;
    }

    private boolean showMainBankMenu(TransactionService transactionService, AccountService accountService, boolean userLoggedIn, User userByLoginAndPassword) {
        if (userLoggedIn) {

            drawStartMenuQ();
            int bound = 5;
            int choice = readIntFromConsole(ENTER_A_NUMBER_BETWEEN_ONE_AND + bound, bound);

            switch (choice) {
                case 1 -> { //TODO rename
                }
                case 2 -> viewAccountsInformation(accountService, userByLoginAndPassword);
                case 3 -> doMainOperationsWithAccount(transactionService, accountService, userByLoginAndPassword);
                case 4 -> userLoggedIn = doLogout(userByLoginAndPassword);
                case 5 -> {
                    String confirmation = readStringFromConsole("Вы уверены, что хотите завершить работу программы? Действие необратимо (y/n)");
                    if (confirmation.equalsIgnoreCase("y")) {
                        log.warn("Программа остановлена в штатном режиме");
                        schedulerService.stopPeriodicallyInterestCalculation();
                        System.exit(0);
                    }
                }
                default -> log.info(INVALID_CHOICE_PLEASE_TRY_AGAIN);
            }
        }
        return userLoggedIn;
    }

    private static boolean doLogout(User userByLoginAndPassword) {
        boolean userLoggedIn;
        log.warn("User" + userByLoginAndPassword.getLogin() + "is logout\n Exiting...");
        userLoggedIn = false;
        return userLoggedIn;
    }


    private void doMainOperationsWithAccount(TransactionService transactionService, AccountService accountService, User userByLoginAndPassword) {
        drawTransactionMenu();
        int boundTransactionChoice = 3;
        int innerChoice = readIntFromConsole(ENTER_A_NUMBER_BETWEEN_ONE_AND + boundTransactionChoice, boundTransactionChoice);

        switch (innerChoice) {
            case 1 -> replenishInnerMenu(accountService, userByLoginAndPassword);
            case 2 -> withdrawInnerMenu(accountService, userByLoginAndPassword);
            case 3 -> transferInnerMenu(transactionService, accountService, userByLoginAndPassword);
            default -> log.info(INVALID_CHOICE_PLEASE_TRY_AGAIN);
        }
    }

    private void transferInnerMenu(TransactionService transactionService,
                                   AccountService accountService,
                                   User userByLoginAndPassword) {
        drawTransferOperationHeader();
        drawTransferSourceHeader();
        List<Account> accounts;
        Account sourceAccount;
        Account targetAccount = null;

        accounts = accountService.getAccountsByUserId(userByLoginAndPassword.getId());
        if (!accounts.isEmpty()) {
            for (int i = 0; i < accounts.size(); i++) {
                System.out.println((i + 1) + ". Account Number: " + accounts.get(i).getAccountNumber() + "  || Current balance: " + accounts.get(i).getBalance());
            }

            int accountChoice = readIntFromConsoleWithoutBounds();
            if (accountChoice >= 1 && accountChoice <= accounts.size()) {
                sourceAccount = accounts.get(accountChoice - 1);
                drawTransactionTargetMenu();

                int boundTransactionChoice = 2;
                int innerChoice = readIntFromConsole(ENTER_A_NUMBER_BETWEEN_ONE_AND + boundTransactionChoice, boundTransactionChoice);

                switch (innerChoice) {
                    case 1 -> {

                        for (int i = 0; i < accounts.size(); i++) {
                            System.out.println((i + 1) + ". Account Number: " + accounts.get(i).getAccountNumber() + "  || Current balance: " + accounts.get(i).getBalance());
                        }

                        int innerAccountChoice;
                        do {
                            innerAccountChoice = readIntFromConsoleWithoutBounds();
                            if (innerAccountChoice >= 1 && innerAccountChoice <= accounts.size()) {
                                targetAccount = accounts.get(innerAccountChoice - 1);
                            } else {
                                log.info(INVALID_CHOICE_PLEASE_TRY_AGAIN);
                            }
                        } while (innerAccountChoice < 1 || innerAccountChoice > accounts.size());
                    }

                    case 2 -> {
                        boolean exit = false;
                        do {
                            String targetAccountNumber = readStringFromConsole("Введите номер счета получателя (или 'exit' для выхода):");
                            if ("exit".equalsIgnoreCase(targetAccountNumber)) {
                                log.info("Выход из цикла");
                                exit = true;
                            } else {
                                targetAccount = accountService.getAccountByAccountNumber(targetAccountNumber);
                                if (targetAccount == null) {
                                    log.info("Счет получателя не найден. Пожалуйста, убедитесь, что вы ввели правильный номер счета.");
                                } else {
                                    log.info("Счет найден. Фамилия получателя: " + targetAccount.getUser().getLastName());
                                    exit = true;
                                }
                            }
                        } while (!exit);
                    }
                    default -> log.info(INVALID_CHOICE_PLEASE_TRY_AGAIN);
                }
                log.info(ENTER_THE_AMOUNT);
                BigDecimal amountToTargetTransfer = readIBigDecimalFromConsoleWithoutBounds();
                if (amountToTargetTransfer.compareTo(sourceAccount.getBalance()) > 0) {
                    log.info(INSUFFICIENT_FUNDS_ON_THE_ACCOUNT);
                } else {
                    transactionService.doTransferFunds(sourceAccount, targetAccount, amountToTargetTransfer);
                    waitEnterKeyPressed();
                }
            } else {
                log.info(INVALID_CHOICE_PLEASE_TRY_AGAIN);
            }
        } else {
            log.info(NO_ACCOUNTS_FOUND_FOR_THE_USER);
        }

    }

    private void withdrawInnerMenu(AccountService accountService, User
            userByLoginAndPassword) {
        List<Account> accounts;
        Account sourceAccount;
        Account targetAccount = null;
        drawWithdrawOperationHeader();
        accounts = accountService.getAccountsByUserId(userByLoginAndPassword.getId());
        if (!accounts.isEmpty()) {
            for (int i = 0; i < accounts.size(); i++) {
                log.info((i + 1) + ". Account Number: " + accounts.get(i).getAccountNumber() + "  || Current balance: " + accounts.get(i).getBalance());
            }
            int accountChoice = readIntFromConsoleWithoutBounds();
            if (accountChoice >= 1 && accountChoice <= accounts.size()) {
                sourceAccount = accounts.get(accountChoice - 1);
                log.info(ENTER_THE_AMOUNT);
                BigDecimal amountToTargetWithdraw = readIBigDecimalFromConsoleWithoutBounds();
                if (amountToTargetWithdraw.compareTo(sourceAccount.getBalance()) > 0) {
                    log.info(INSUFFICIENT_FUNDS_ON_THE_ACCOUNT);
                } else {
                    transactionService.withdrawFromAccount(sourceAccount, targetAccount, amountToTargetWithdraw);
                }
            } else {
                log.info(INVALID_CHOICE_PLEASE_TRY_AGAIN);
            }
        } else {
            log.info(NO_ACCOUNTS_FOUND_FOR_THE_USER);
        }
    }

    private void replenishInnerMenu(AccountService accountService, User
            userByLoginAndPassword) {
        List<Account> accounts;
        Account sourceAccount = null;
        Account targetAccount;
        DrawUI.drawReplenishOperationHeader();
        accounts = accountService.getAccountsByUserId(userByLoginAndPassword.getId());
        if (!accounts.isEmpty()) {
            for (int i = 0; i < accounts.size(); i++) {
                System.out.println((i + 1) + ". Account Number: " + accounts.get(i).getAccountNumber() + "  || Current balance: " + accounts.get(i).getBalance());
            }
            int accountChoice = readIntFromConsoleWithoutBounds();
            if (accountChoice >= 1 && accountChoice <= accounts.size()) {
                targetAccount = accounts.get(accountChoice - 1);
                log.info(ENTER_THE_AMOUNT);
                BigDecimal amount = readIBigDecimalFromConsoleWithoutBounds();

                transactionService.replenishAccountBalance(sourceAccount, targetAccount, amount);

            } else {
                log.info(INVALID_CHOICE_PLEASE_TRY_AGAIN);
            }
        } else {
            log.info(NO_ACCOUNTS_FOUND_FOR_THE_USER);
        }
    }

    private void viewAccountsInformation(AccountService accountService, User
            userByLoginAndPassword) {

        drawViewAllAccountsOperationHeader();
        List<Account> accounts = accountService.getAccountsByUserId(userByLoginAndPassword.getId());

        if (!accounts.isEmpty()) {

            System.out.println("Hello " + userByLoginAndPassword.getLastName() + " " + userByLoginAndPassword.getFirstName() + " " + userByLoginAndPassword.getPatronymic() + ", your accounts is:");
            for (Account account : accounts) {

                System.out.println("Account Number: " + account.getAccountNumber() + " || " + account.getBank().getName());
                System.out.println("Balance: " + account.getBalance() + " " + account.getCurrency());
            }
            System.out.println();
            System.out.println("Do you want to see details of accounts? (y/n)");
            System.out.println("************************************************************");
            String seeMore = readStringFromConsole();

            if ("y".equalsIgnoreCase(seeMore)) {
                DrawUI.drawDetailsAccountInfoOperationHeader();
                for (int i = 0; i < accounts.size(); i++) {
                    System.out.println((i + 1) + ". Account Number: " + accounts.get(i).getAccountNumber() + " " + accounts.get(i).getBalance());
                }
                int accountChoice = readIntFromConsoleWithoutBounds();
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
            log.warn("No accounts found for user " + userByLoginAndPassword.getLogin());
        }
    }

}
