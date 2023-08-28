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
import ru.clevertec.repository.utils.ConnectionPool;
import ru.clevertec.util.DbUtilsYaml;


public class AccountRepositoryImpl implements AccountRepository {
    public static final String CHECK_BALANCE = "SELECT balance FROM accounts WHERE account_number = ?";

    private final Connection connection = DbUtilsYaml.connection();

    public AccountRepositoryImpl(Connection connection) {
    }

    @Override
    public Account findById(Long accountId) {
        String query = "SELECT a.id AS account_id, a.account_number, a.balance, a.currency, a.account_opening_date, " +
                "u.id AS user_id, u.identification_number_of_passport, u.first_name, u.last_name, " +
                "b.id AS bank_id, b.name " +
                "FROM accounts a " +
                "JOIN users u ON a.user_id = u.id " +
                "JOIN banks b ON a.bank_id = b.id " +
                "WHERE a.id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, accountId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Account account = new Account();
                    account.setId(resultSet.getLong("account_id"));
                    account.setAccountNumber(resultSet.getString("account_number"));
                    account.setBalance(resultSet.getBigDecimal("balance"));
                    account.setCurrency(Currency.getInstance(resultSet.getString("currency")));
                    account.setAccountOpeningDate(resultSet.getTimestamp("account_opening_date").toLocalDateTime());

                    User user = new User();
                    user.setId(resultSet.getLong("user_id"));
                    user.setIdentificationNumberOfPassport(resultSet.getString("identification_number_of_passport"));
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));


                    Bank bank = new Bank();
                    bank.setId(resultSet.getLong("bank_id"));
                    bank.setName(resultSet.getString("name"));


                    account.setUser(user);
                    account.setBank(bank);

                    return account;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    @Override
    public List<Account> findAccountsByUserLogin(String userLogin) {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT id, account_number, balance, currency, account_opening_date FROM accounts WHERE user_login = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userLogin);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Account account = new Account();
                    account.setId(resultSet.getLong("id"));
                    account.setAccountNumber(resultSet.getString("account_number"));
                    account.setBalance(resultSet.getBigDecimal("balance"));
                    account.setCurrency(Currency.getInstance(resultSet.getString("currency"))); // Пример поля "currency"
                    account.setAccountOpeningDate(resultSet.getTimestamp("account_opening_date").toLocalDateTime());
                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    @Override
    public Long findUserIdByLogin(String login) {
        String query = "SELECT id FROM users WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public User findUserByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);

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

                    // Здесь можно добавить код для загрузки связанных счетов пользователя

                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User findUserByLoginAndPassword(String login, String password) {
        String sql = "SELECT * FROM users WHERE login = ? AND password = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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

                    // Здесь можно добавить код для загрузки связанных счетов пользователя

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
//        String query = "SELECT id, account_number, balance, currency, account_opening_date FROM accounts WHERE user_id = ?";
        String queryAll = "SELECT a.*, b.name FROM accounts a JOIN banks b ON a.bank_id = b.id WHERE user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryAll)) {
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
    public Account findAccountByNumber(String accountNumber) {
        String queryAll = "SELECT a.*, b.name, u.id, u.last_name  FROM accounts a  JOIN banks b ON a.bank_id = b.id  JOIN users u ON a.user_id = u.id  WHERE account_number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryAll)) {
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
    }


    @Override
    public void updateAccount(Account account) {
        try (Connection connection = DbUtilsYaml.connection()) {
            String query = "UPDATE accounts SET balance = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setBigDecimal(1, account.getBalance());
                preparedStatement.setLong(2, account.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}





