package ru.clevertec.repository.impl;

import static ru.clevertec.model.TransactionType.REPLENISHMENT;
import static ru.clevertec.model.TransactionType.TRANSFERRING;
import static ru.clevertec.model.TransactionType.WITHDRAWAL;
import static ru.clevertec.util.Constants.Messages.ERROR_FROM_CREATE_TRANSACTION;
import static ru.clevertec.util.Constants.Messages.ERROR_REPLENISHMENT;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.log4j.Log4j2;
import ru.clevertec.model.Account;
import ru.clevertec.model.Transaction;
import ru.clevertec.repository.AccountRepository;
import ru.clevertec.repository.TransactionRepository;
import ru.clevertec.repository.connection.ConnectionPool;

@Log4j2
public class TransactionRepositoryImpl implements TransactionRepository {
    public static final String CREATE_TRANSACTION = "INSERT INTO transactions (currency, amount, source_account_id, target_account_id, source_bank_id, target_bank_id, timestamp, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
    private final AccountRepository accountRepository;

    public TransactionRepositoryImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Long createTransaction(Transaction transaction, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TRANSACTION, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, transaction.getCurrency().getCurrencyCode());
            preparedStatement.setBigDecimal(2, transaction.getAmount());
            preparedStatement.setLong(3, transaction.getSourceAccount().getId());
            preparedStatement.setLong(4, transaction.getTargetAccount().getId());
            preparedStatement.setLong(5, transaction.getSourceBank().getId());
            preparedStatement.setLong(6, transaction.getTargetBank().getId());
            preparedStatement.setTimestamp(7, Timestamp.valueOf(transaction.getTimestamp()));
            preparedStatement.setString(8, transaction.getType().name());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.error(ERROR_FROM_CREATE_TRANSACTION, e);
            throw new RuntimeException();
        }
        return null;
    }

    @Override
    public void replenishAccountBalance(Account account, BigDecimal amount) {

        Connection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            connection.setAutoCommit(false);

            Transaction transaction = new Transaction();
            transaction.setCurrency(account.getCurrency());
            transaction.setAmount(amount);
            transaction.setSourceAccount(account);
            transaction.setTargetAccount(account);
            transaction.setSourceBank(account.getBank());
            transaction.setTargetBank(account.getBank());
            transaction.setTimestamp(LocalDateTime.now());
            transaction.setType(REPLENISHMENT);
            Long transactionId = createTransaction(transaction, connection);

            accountRepository.updateAccountBalance(account.getId(), account.getBalance().add(amount), connection);

            viewBankReceipt(transaction, transactionId);

            System.out.println("Replenishment successful. New balance: " + account.getBalance().add(transaction.getAmount()));

            connection.commit();
        } catch (Exception e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                log.error("Exception, connection.rollback()", ex);
            }
            log.error(ERROR_REPLENISHMENT, e);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("Exception from finallyBlock", e);
            }
        }
    }

    @Override
    public void doTransferFunds(Account sourceAccount, Account targetAccount, BigDecimal amount) {

        Connection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            connection.setAutoCommit(false);

            Transaction transaction = new Transaction();
            transaction.setCurrency(sourceAccount.getCurrency());
            transaction.setAmount(amount);
            transaction.setSourceAccount(sourceAccount);
            transaction.setTargetAccount(targetAccount);
            transaction.setSourceBank(sourceAccount.getBank());
            transaction.setTargetBank(targetAccount.getBank());
            transaction.setTimestamp(LocalDateTime.now());
            transaction.setType(TRANSFERRING);
            Long transactionId = createTransaction(transaction, connection);

            accountRepository.updateAccountBalance(sourceAccount.getId(), sourceAccount.getBalance().subtract(amount), connection);

            accountRepository.updateAccountBalance(targetAccount.getId(), targetAccount.getBalance().add(amount), connection);

            viewBankReceipt(transaction, transactionId);

            System.out.println("Перевод успешно выполнен");

            connection.commit();
        } catch (Exception e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                log.error("SQLException, connection.rollback()", ex);
            }
            log.error(ERROR_REPLENISHMENT, e);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("Exception from finallyBlock", e);
            }
        }
    }


    @Override
    public void withdrawFromAccount(Account account, BigDecimal amount) {
        Connection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            connection.setAutoCommit(false);

            Transaction transaction = new Transaction();
            transaction.setCurrency(account.getCurrency());
            transaction.setAmount(amount);
            transaction.setSourceAccount(account);
            transaction.setTargetAccount(account);
            transaction.setSourceBank(account.getBank());
            transaction.setTargetBank(account.getBank());
            transaction.setTimestamp(LocalDateTime.now());
            transaction.setType(WITHDRAWAL);
            Long transactionId = createTransaction(transaction, connection);
            accountRepository.updateAccountBalance(account.getId(), account.getBalance().subtract(amount), connection);

            viewBankReceipt(transaction, transactionId);

            System.out.println("withdrawal successful. New balance: " + account.getBalance().subtract(transaction.getAmount()));
            connection.commit();
        } catch (Exception e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                log.error("SQLException, connection.rollback()", ex);
            }
            log.error("Ошибка пополнения средств", e);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("SQLException from finallyBlock", e);
            }
        }
    }

    private void viewBankReceipt(Transaction transaction, Long transactionId) {
        //TODO TEST_METHOD
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
    }
}
