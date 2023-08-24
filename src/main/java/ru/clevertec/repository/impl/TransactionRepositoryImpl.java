//package ru.clevertec.repository.impl;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import ru.clevertec.model.Transaction;
//import ru.clevertec.repository.TransactionRepository;
//
//public class TransactionRepositoryImpl implements TransactionRepository {
//    private Connection connection;
//
//    // Конструктор, принимающий подключение к базе данных
//    public TransactionRepositoryImpl(Connection connection) {
//        this.connection = connection;
//    }
//
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
//        }
//    }
//}
