package ru.clevertec.repository.impl;

import static ru.clevertec.util.Constants.Attributes.BALANCE;
import static ru.clevertec.util.Constants.Attributes.BANK_ID;
import static ru.clevertec.util.Constants.Attributes.CURRENCY;
import static ru.clevertec.util.Constants.Attributes.DB_ACCOUNT_NUMBER;
import static ru.clevertec.util.Constants.Attributes.DB_ACCOUNT_OPENING_DATE;
import static ru.clevertec.util.Constants.Attributes.DB_FIRST_NAME;
import static ru.clevertec.util.Constants.Attributes.DB_IDENTIFICATION_NUMBER_OF_PASSPORT;
import static ru.clevertec.util.Constants.Attributes.DB_LAST_NAME;
import static ru.clevertec.util.Constants.Attributes.DB_USER_ID;
import static ru.clevertec.util.Constants.Attributes.ID;
import static ru.clevertec.util.Constants.Attributes.LOGIN;
import static ru.clevertec.util.Constants.Attributes.NAME;
import static ru.clevertec.util.Constants.Attributes.PASSWORD;
import static ru.clevertec.util.Constants.Attributes.PATRONYMIC;
import static ru.clevertec.util.Constants.Messages.ERROR_FROM_UPDATING_BALANCE;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import ru.clevertec.model.Account;
import ru.clevertec.model.Bank;
import ru.clevertec.model.User;
import ru.clevertec.repository.AccountRepository;
import ru.clevertec.repository.connection.ConnectionPool;

@Log4j2
public class AccountRepositoryImpl implements AccountRepository {
    public static final String FIND_USER_BY_LOGIN_AND_PASSWORD = "SELECT * FROM users WHERE login = ? AND password = ?";
    public static final String FIND_ACCOUNTS_BY_USER_ID = "SELECT a.*, b.name FROM accounts a JOIN banks b ON a.bank_id = b.id WHERE user_id = ?";
    public static final String FIND_ACCOUNT_BY_ACCOUNT_NUMBER = "SELECT a.*, b.name, u.id, u.last_name  FROM accounts a  JOIN banks b ON a.bank_id = b.id  JOIN users u ON a.user_id = u.id  WHERE account_number = ?";
    public static final String UPDATE_ACCOUNT_BALANCE = "UPDATE accounts SET balance = ? WHERE id = ?";


    @Override
    public User findUserByLoginAndPassword(String login, String password) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_LOGIN_AND_PASSWORD)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return createUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            log.error("SQLException from findUserByLoginAndPassword", e);
        }
        return null;
    }

    private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong(ID));
        user.setIdentificationNumberOfPassport(resultSet.getString(DB_IDENTIFICATION_NUMBER_OF_PASSPORT));
        user.setFirstName(resultSet.getString(DB_FIRST_NAME));
        user.setLastName(resultSet.getString(DB_LAST_NAME));
        user.setPatronymic(resultSet.getString(PATRONYMIC));
        user.setLogin(resultSet.getString(LOGIN));
        user.setPassword(resultSet.getString(PASSWORD));
        return user;
    }

    @Override
    public List<Account> findAccountsByUserId(Long userId) {
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ACCOUNTS_BY_USER_ID)) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {

                    Account account = createAccountFromResultSet(resultSet);
                    Bank bank = createBankFromResultSet(resultSet);

                    account.setBank(bank);
                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            log.error("Error fetching accounts for user with ID:" + userId, e);
        }
        return accounts;
    }

    @Override
    public Account findAccountByAccountNumber(String accountNumber) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ACCOUNT_BY_ACCOUNT_NUMBER)) {
            preparedStatement.setString(1, accountNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Account account = createAccountFromResultSet(resultSet);

                    Bank bank = createBankFromResultSet(resultSet);
                    account.setBank(bank);

                    User user = new User();
                    user.setId(resultSet.getLong(DB_USER_ID));
                    user.setLastName(resultSet.getString(DB_LAST_NAME));
                    account.setUser(user);

                    return account;
                }
            }
        } catch (SQLException e) {
            log.error("SQLException from findAccountByAccountNumber", e);
        }
        return null;
    }

    private static Account createAccountFromResultSet(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setId(resultSet.getLong(ID));
        account.setAccountNumber(resultSet.getString(DB_ACCOUNT_NUMBER));
        account.setBalance(resultSet.getBigDecimal(BALANCE));
        account.setCurrency(Currency.getInstance(resultSet.getString(CURRENCY)));
        account.setAccountOpeningDate(resultSet.getTimestamp(DB_ACCOUNT_OPENING_DATE).toLocalDateTime());
        return account;
    }

    private static Bank createBankFromResultSet(ResultSet resultSet) throws SQLException {
        Bank bank = new Bank();
        bank.setId(resultSet.getLong(BANK_ID));
        bank.setName(resultSet.getString(NAME));
        return bank;
    }

    @Override
    public BigDecimal updateAccountBalance(Long accountId, BigDecimal newBalance, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT_BALANCE)) {
            preparedStatement.setBigDecimal(1, newBalance);
            preparedStatement.setLong(2, accountId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(ERROR_FROM_UPDATING_BALANCE, e);
            throw new RuntimeException();
        }
        return newBalance;
    }
}
