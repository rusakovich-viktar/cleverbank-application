package ru.clevertec.repository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import ru.clevertec.model.Account;

public interface AccountRepository {

    Account findById(Long accountId);

    List<Account> findAccountsByUserLogin(String userLogin);

    Long findUserIdByLogin(String login);

    List<Account> findAccountsByUserId(Long userId);


}
