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

/**
 * Implementation of the {@link TransactionRepository} interface that provides transaction data access services.
 */
@Log4j2
public class TransactionRepositoryImpl implements TransactionRepository {
    public static final String CREATE_TRANSACTION = "INSERT INTO transactions (currency, amount, source_account_id, target_account_id, source_bank_id, target_bank_id, timestamp, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
    private final AccountRepository accountRepository;

    public TransactionRepositoryImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
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

    /**
     * Creates a Transaction object based on source and target accounts, amount, and transaction type.
     *
     * @param sourceAccount   The source account from which the transaction originates.
     * @param targetAccount   The target account to which the transaction is directed.
     * @param amount          The amount involved in the transaction.
     * @param transactionType The type of transaction (e.g., deposit, withdrawal, transfer).
     * @return A Transaction object initialized with the provided information.
     */
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

    /**
     * Calculates the new balance of an account based on the transaction type and amount.
     *
     * @param account         The account for which the new balance is calculated.
     * @param amount          The amount involved in the transaction.
     * @param transactionType The type of transaction (e.g., replenishment, withdrawal).
     * @return The new balance of the account after the transaction.
     */
    private BigDecimal calculateNewBalance(Account account, BigDecimal amount, TransactionType transactionType) {
        if (transactionType == REPLENISHMENT) {
            return account.getBalance().add(amount);
        } else if (transactionType == WITHDRAWAL) {
            return account.getBalance().subtract(amount);
        }
        return account.getBalance();
    }

    /**
     * Creates a Transaction object, saves it, and returns the saved Transaction.
     *
     * @param sourceAccount   The source account from which the transaction originates.
     * @param targetAccount   The target account to which the transaction is directed.
     * @param amount          The amount involved in the transaction.
     * @param transactionType The type of transaction (e.g., deposit, withdrawal, transfer).
     * @param connection      The database connection for saving the transaction.
     * @return The saved Transaction object.
     */
    private Transaction createAndSaveTransaction(Account sourceAccount, Account targetAccount, BigDecimal amount, TransactionType transactionType, Connection connection) {
        Transaction transactionWithoutId = createTransaction(sourceAccount, targetAccount, amount, transactionType);
        return saveTransaction(transactionWithoutId, connection);
    }

    /**
     * Updates the balance of an account based on the transaction type and amount and saves the updated balance.
     *
     * @param account         The account for which the balance is updated.
     * @param amount          The amount involved in the transaction.
     * @param transactionType The type of transaction (e.g., replenishment, withdrawal).
     * @param connection      The database connection for saving the updated balance.
     * @return The updated balance of the account after the transaction.
     */
    private BigDecimal updateBalance(Account account, BigDecimal amount, TransactionType transactionType, Connection connection) {
        return accountRepository.updateAccountBalance(account.getId(), calculateNewBalance(account, amount, transactionType), connection);
    }

    /**
     * Handles a transaction error by rolling back the database connection (if available) and logging the error details.
     *
     * @param connection   The database connection to be rolled back (if not null).
     * @param errorMessage A descriptive error message.
     * @param e            The exception that triggered the error.
     */
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

    /**
     * Closes a database connection and sets auto-commit mode to true (if the connection is not null).
     *
     * @param connection The database connection to be closed (if not null).
     */
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
