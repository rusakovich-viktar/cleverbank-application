package ru.clevertec.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import ru.clevertec.model.Account;
import ru.clevertec.model.User;

public interface AccountRepository {
    /**
     * Retrieves a User object based on the provided login and password.
     *
     * @param login    The login of the user to be retrieved.
     * @param password The password associated with the user.
     * @return A User object if a matching user is found, or null if no user matches the provided credentials.
     */
    User getUserByLoginAndPassword(String login, String password);

    /**
     * Retrieves a list of Account objects associated with a specific user by their user ID.
     *
     * @param userId The ID of the user for whom to retrieve accounts.
     * @return A list of Account objects associated with the specified user, or an empty list if no accounts are found.
     */
    List<Account> getAccountsByUserId(Long userId);

    /**
     * Retrieves an Account object based on the provided account number.
     *
     * @param accountNumber The account number of the account to be retrieved.
     * @return An Account object if an account with the specified account number is found, or null if no match is found.
     */
    Account getAccountByAccountNumber(String accountNumber);

    /**
     * Retrieves a list of all available Account objects.
     *
     * @return A list of all Account objects, or an empty list if no accounts are available.
     */
    List<Account> getAllAccounts();

    /**
     * Updates the balance of an account with the specified account ID and sets it to the new balance.
     *
     * @param accountId  The ID of the account to update.
     * @param newBalance The new balance to set for the account.
     * @param connection The database connection for performing the update.
     * @return The updated balance of the account after the operation.
     */
    BigDecimal updateAccountBalance(Long accountId, BigDecimal newBalance, Connection connection);

}
