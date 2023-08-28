package ru.clevertec.repository.impl;

import static ru.clevertec.util.Constants.CASH;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Currency;
import ru.clevertec.model.Account;
import ru.clevertec.model.Bank;
import ru.clevertec.model.Transaction;
import ru.clevertec.model.TransactionType;
import ru.clevertec.repository.TransactionRepository;
import ru.clevertec.util.DbUtilsYaml;

public class TransactionRepositoryImpl implements TransactionRepository {
    private final Connection connection = DbUtilsYaml.connection();

    public TransactionRepositoryImpl(Connection connection) {
    }

    public void createTransaction(Transaction transaction) {
        String query = "INSERT INTO transactions (currency, amount, source_account_id, target_account_id, source_bank_id, target_bank_id, timestamp, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, transaction.getCurrency().getCurrencyCode());
            preparedStatement.setBigDecimal(2, transaction.getAmount());
            if (transaction.getSourceAccount() != null) {
                preparedStatement.setLong(3, transaction.getSourceAccount().getId());
            }
            if (transaction.getTargetAccount() != null) {
                preparedStatement.setLong(4, transaction.getTargetAccount().getId());
            }
            if (transaction.getSourceBank() != null) {
                preparedStatement.setLong(5, transaction.getSourceBank().getId());
            } else {
                transaction.setSourceBank(transaction.getTargetBank());
                preparedStatement.setLong(5, CASH);
            }
            if (transaction.getTargetBank() != null) {
                preparedStatement.setLong(6, transaction.getTargetBank().getId());
            } else {
                preparedStatement.setLong(6, CASH);
            }
            preparedStatement.setTimestamp(7, Timestamp.valueOf(transaction.getTimestamp()));
            preparedStatement.setString(8, transaction.getType().name());

            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void transferFunds(Account sourceAccount, Account targetAccount, BigDecimal amount) {
        String transferQuery = "INSERT INTO transactions (currency, amount, source_account_id, target_account_id, source_bank_id, target_bank_id, timestamp, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(transferQuery)) {
            connection.setAutoCommit(false); // Отключаем автоматическую фиксацию

            Currency currency = sourceAccount.getCurrency();
            LocalDateTime timestamp = LocalDateTime.now();
            Bank sourceBank = sourceAccount.getBank();
            Bank targetBank = targetAccount.getBank();

            // Создаем транзакцию списания
            preparedStatement.setString(1, currency.getCurrencyCode());
            preparedStatement.setBigDecimal(2, amount.negate());
            preparedStatement.setLong(3, sourceAccount.getId());
            preparedStatement.setLong(4, targetAccount.getId());
            preparedStatement.setLong(5, sourceBank.getId());
            preparedStatement.setLong(6, targetBank.getId());
            preparedStatement.setTimestamp(7, Timestamp.valueOf(timestamp));
            preparedStatement.setString(8, TransactionType.TRANSFERRING.name());
            preparedStatement.executeUpdate();

            // Создаем транзакцию зачисления
            preparedStatement.setBigDecimal(2, amount);
            preparedStatement.setLong(3, targetAccount.getId());
            preparedStatement.setLong(4, sourceAccount.getId());
            preparedStatement.setLong(5, targetBank.getId());
            preparedStatement.setLong(6, sourceBank.getId());
            preparedStatement.setString(8, TransactionType.REPLENISHMENT.name());
            preparedStatement.executeUpdate();

            connection.commit(); // Фиксируем транзакцию
            connection.setAutoCommit(true); // Включаем автоматическую фиксацию
        } catch (SQLException e) {
            throw new RuntimeException("Error transferring funds", e);
        }

    }


//    public void saveTransaction(Transaction transaction) throws SQLException {
//        String sql = "INSERT INTO transactions (source_account_id, target_account_id, amount, type, timestamp) VALUES (?, ?, ?, ?, ?)";
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//            preparedStatement.setLong(1, transaction.getSourceAccount().getId());
//            preparedStatement.setLong(2, transaction.getTargetAccount().getId());
//            preparedStatement.setBigDecimal(3, transaction.getAmount());
//            preparedStatement.setString(4, transaction.getType().toString());
//            preparedStatement.setTimestamp(5, Timestamp.valueOf(transaction.getTimestamp()));
//
//            preparedStatement.executeUpdate();
//        }}


}

