package ru.clevertec.repository.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import ru.clevertec.model.Account;
import ru.clevertec.model.Bank;
import ru.clevertec.model.User;
import ru.clevertec.repository.AccountRepository;
import ru.clevertec.util.DbUtilsYaml;


public class AccountRepositoryImpl implements AccountRepository {
    public static final String FIND_USER_BY_LOGIN_AND_PASSWORD = "SELECT * FROM users WHERE login = ? AND password = ?";
    public static final String FIND_ACCOUNTS_BY_USER_ID = "SELECT a.*, b.name FROM accounts a JOIN banks b ON a.bank_id = b.id WHERE user_id = ?";
    public static final String FIND_ACCOUNT_BY_ACCOUNT_NUMBER = "SELECT a.*, b.name, u.id, u.last_name  FROM accounts a  JOIN banks b ON a.bank_id = b.id  JOIN users u ON a.user_id = u.id  WHERE account_number = ?";

    private final Connection connection = DbUtilsYaml.connection();

    public AccountRepositoryImpl(Connection connection) {
    }

    @Override
    public User findUserByLoginAndPassword(String login, String password) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_LOGIN_AND_PASSWORD)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setIdentificationNumberOfPassport(resultSet.getString("identification_number_of_passport"));
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setPatronymic(resultSet.getString("patronymic"));
                    user.setLogin(resultSet.getString("login"));
                    user.setPassword(resultSet.getString("password"));

                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Account> findAccountsByUserId(Long userId) {
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ACCOUNTS_BY_USER_ID)) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Account account = new Account();
                    account.setId(resultSet.getLong("id"));
                    account.setAccountNumber(resultSet.getString("account_number"));
                    account.setBalance(resultSet.getBigDecimal("balance"));
                    account.setCurrency(Currency.getInstance(resultSet.getString("currency")));
                    account.setAccountOpeningDate(resultSet.getTimestamp("account_opening_date").toLocalDateTime());
                    Bank bank = new Bank();
                    bank.setId(resultSet.getLong("bank_id"));
                    bank.setName(resultSet.getString("name"));
                    account.setBank(bank);
                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    @Override
    public Account findAccountByAccountNumber(String accountNumber) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ACCOUNT_BY_ACCOUNT_NUMBER)) {
            preparedStatement.setString(1, accountNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Account account = new Account();
                    account.setId(resultSet.getLong("id"));
                    account.setAccountNumber(resultSet.getString("account_number"));
                    account.setBalance(resultSet.getBigDecimal("balance"));
                    account.setCurrency(Currency.getInstance(resultSet.getString("currency")));
                    account.setAccountOpeningDate(resultSet.getTimestamp("account_opening_date").toLocalDateTime());

                    Bank bank = new Bank();
                    bank.setId(resultSet.getLong("bank_id"));
                    bank.setName(resultSet.getString("name"));

                    User user = new User();
                    user.setId(resultSet.getLong("user_id"));
                    user.setLastName(resultSet.getString("last_name"));
                    account.setBank(bank);
                    account.setUser(user);

                    return account;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
        //TODO
    }

    @Override
    public void updateAccountBalance(Long accountId, BigDecimal newBalance) {
        String updateQuery = "UPDATE accounts SET balance = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setBigDecimal(1, newBalance);
            preparedStatement.setLong(2, accountId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating account balance", e);
        }

    }
}




