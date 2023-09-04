package ru.clevertec.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a user's bank account in the system.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    /**
     * The unique identifier of the account.
     */
    private Long id;

    /**
     * The account number associated with this account.
     */
    private String accountNumber;

    /**
     * The current balance of the account.
     */
    private BigDecimal balance;

    /**
     * The currency associated with the account balance.
     */
    private Currency currency;

    /**
     * The date and time when the account was opened.
     */
    private LocalDateTime accountOpeningDate;

    /**
     * The user to whom this account belongs.
     */
    private User user;

    /**
     * The bank where this account is held.
     */
    private Bank bank;

    /**
     * The list of transactions associated with this account.
     */
    private List<Transaction> transactions;
}
