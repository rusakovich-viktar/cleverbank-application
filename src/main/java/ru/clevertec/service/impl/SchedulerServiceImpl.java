package ru.clevertec.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;
import ru.clevertec.config.ReaderConfigFile;
import ru.clevertec.model.Account;
import ru.clevertec.service.AccountService;
import ru.clevertec.service.SchedulerService;
import ru.clevertec.service.TransactionService;

@Log4j2
public class SchedulerServiceImpl implements SchedulerService {
    private ScheduledExecutorService scheduledExecutorService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public SchedulerServiceImpl(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void startPeriodicallyCalculateInterest() {
        ReaderConfigFile readerConfigFile = new ReaderConfigFile();
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        log.warn("startPeriodicallyCalculateInterest вызван.");
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                log.info("Планировщик срабатывание (проверка даты)");
                LocalDateTime currentTime = LocalDateTime.now();
                int currentDayOfMonth = currentTime.getDayOfMonth();
                if (currentDayOfMonth == currentTime.getMonth().length(currentTime.toLocalDate().isLeapYear())) {
                    CompletableFuture<Void> interestCalculation = CompletableFuture.runAsync(() -> {
                        try {
                            BigDecimal interestRate = readerConfigFile.getInterestRate();
                            List<Account> accounts = accountService.getAllAccounts();
                            for (Account account : accounts) {
                                BigDecimal currentBalance = account.getBalance();
                                BigDecimal interestAmount = currentBalance.multiply(interestRate);
                                transactionService.replenishAccountBalance(account, account, interestAmount);
                            }
                        } catch (Exception e) {
                            log.error("Error getInterestRate", e);
                        }
                    });
                    interestCalculation.thenAccept(result -> {
//                        log.info("Проценты успешно начислены на счета клиентов.");
                    });
                }
            } catch (Exception e) {
                log.error("Error scheduleAtFixedRate", e);
                throw new RuntimeException();
            }
        }, 0, 30, TimeUnit.SECONDS);
    }


    public void stopPeriodicallyInterestCalculation() {
        scheduledExecutorService.shutdown();
    }
}
