package ru.clevertec.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a financial transaction in the system.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    /**
     * The unique identifier of the transaction.
     */
    private Long id;

    /**
     * The currency associated with the transaction amount.
     */
    private Currency currency;

    /**
     * The amount involved in the transaction.
     */
    private BigDecimal amount;

    /**
     * The source account from which the transaction originated.
     */
    private Account sourceAccount;

    /**
     * The target account to which the transaction is directed.
     */
    private Account targetAccount;

    /**
     * The source bank associated with the source account.
     */
    private Bank sourceBank;

    /**
     * The target bank associated with the target account.
     */
    private Bank targetBank;

    /**
     * The timestamp representing when the transaction occurred.
     */
    private LocalDateTime timestamp;

    /**
     * The type of the transaction (e.g., Replenishment, Withdrawal, Transferring).
     */
    private TransactionType type;
}
