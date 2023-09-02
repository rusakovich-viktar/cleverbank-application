package ru.clevertec.repository.impl;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static ru.clevertec.model.TransactionType.REPLENISHMENT;
import static ru.clevertec.model.TransactionType.TRANSFERRING;
import static ru.clevertec.model.TransactionType.WITHDRAWAL;
import static ru.clevertec.util.Constants.Messages.ERROR_FROM_CREATE_TRANSACTION;
import static ru.clevertec.util.Constants.Messages.ERROR_REPLENISHMENT;
import static ru.clevertec.util.Constants.Messages.ERROR_WITHDRAWAL;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import ru.clevertec.model.Account;
import ru.clevertec.model.Transaction;
import ru.clevertec.model.TransactionType;
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

    public Transaction saveTransaction(Transaction transaction, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TRANSACTION, RETURN_GENERATED_KEYS)) {
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
                    long generatedId = generatedKeys.getLong(1);
                    transaction.setId(generatedId);

                    return transaction;
                }
            }
        } catch (SQLException e) {
            log.error(ERROR_FROM_CREATE_TRANSACTION, e);
            throw new RuntimeException();
        }
        return null;
    }

    @Override
    public Transaction replenishAccountBalance(Account sourceAccount, Account targetAccount, BigDecimal amount) {
        Connection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            connection.setAutoCommit(false);

            Transaction transaction = createAndSaveTransaction(sourceAccount, targetAccount, amount, REPLENISHMENT, connection);
            BigDecimal newBalance = updateBalance(targetAccount, amount, REPLENISHMENT, connection);
            log.info("Пополнение успешно выполнено. New balance: {}. Транзакция: {}", newBalance, transaction.getId());
            connection.commit();
            return transaction;
        } catch (Exception e) {
            handleTransactionError(connection, ERROR_REPLENISHMENT, e);
        } finally {
            closeConnection(connection);
        }
        return null;
    }

    @Override
    public Transaction withdrawFromAccount(Account sourceAccount, Account targetAccount, BigDecimal amount) {
        Connection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            connection.setAutoCommit(false);
            Transaction transaction = createAndSaveTransaction(sourceAccount, targetAccount, amount, WITHDRAWAL, connection);
            BigDecimal newBalance = updateBalance(sourceAccount, amount, WITHDRAWAL, connection);
            log.info("Списание успешно выполнено. New balance: {}. Транзакция: {}", newBalance, transaction.getId());
            connection.commit();
            return transaction;
        } catch (Exception e) {
            handleTransactionError(connection, ERROR_WITHDRAWAL, e);
        } finally {
            closeConnection(connection);
        }
        return null;
    }

    @Override
    public Transaction doTransferFunds(Account sourceAccount, Account targetAccount, BigDecimal amount) {

        Connection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            connection.setAutoCommit(false);
            Transaction transactionWithoutId = createTransaction(sourceAccount, targetAccount, amount, TRANSFERRING);
            Transaction transaction = saveTransaction(transactionWithoutId, connection);
            accountRepository.updateAccountBalance(sourceAccount.getId(), sourceAccount.getBalance().subtract(amount), connection);
            accountRepository.updateAccountBalance(targetAccount.getId(), targetAccount.getBalance().add(amount), connection);
            log.info("Перевод успешно выполнен. Транзакция: {}", transaction.getId());
            connection.commit();
            return transaction;
        } catch (Exception e) {
            handleTransactionError(connection, ERROR_REPLENISHMENT, e);
        } finally {
            closeConnection(connection);
        }
        return null;
    }

    private Transaction createTransaction(Account sourceAccount, Account targetAccount, BigDecimal amount, TransactionType transactionType) {
        Transaction transactionWithoutId = new Transaction();
        if (sourceAccount == null) {
            sourceAccount = targetAccount;
        } else if (targetAccount == null) {
            targetAccount = sourceAccount;
        }
        transactionWithoutId.setCurrency(sourceAccount.getCurrency());
        transactionWithoutId.setAmount(amount);
        transactionWithoutId.setSourceAccount(sourceAccount);
        transactionWithoutId.setTargetAccount(targetAccount);
        transactionWithoutId.setSourceBank(sourceAccount.getBank());
        transactionWithoutId.setTargetBank(targetAccount.getBank());
        transactionWithoutId.setTimestamp(LocalDateTime.now());
        transactionWithoutId.setType(transactionType); //
        return transactionWithoutId;
    }

    private BigDecimal calculateNewBalance(Account account, BigDecimal amount, TransactionType transactionType) {
        if (transactionType == REPLENISHMENT) {
            return account.getBalance().add(amount);
        } else if (transactionType == WITHDRAWAL) {
            return account.getBalance().subtract(amount);
        }
        return account.getBalance();
    }

    private Transaction createAndSaveTransaction(Account sourceAccount, Account targetAccount, BigDecimal amount, TransactionType transactionType, Connection connection) {
        Transaction transactionWithoutId = createTransaction(sourceAccount, targetAccount, amount, transactionType);
        return saveTransaction(transactionWithoutId, connection);
    }

    private BigDecimal updateBalance(Account account, BigDecimal amount, TransactionType transactionType, Connection connection) {
        return accountRepository.updateAccountBalance(account.getId(), calculateNewBalance(account, amount, transactionType), connection);
    }

    private void handleTransactionError(Connection connection, String errorMessage, Exception e) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException ex) {
            log.error("Ошибка, connection.rollback()", ex);
        }
        log.error(errorMessage, e);
    }

    private void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        } catch (SQLException e) {
            log.error("Ошибка при закрытии соединения", e);
        }
    }
}
