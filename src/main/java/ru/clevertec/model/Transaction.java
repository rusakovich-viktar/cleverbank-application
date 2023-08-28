package ru.clevertec.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private Long id;
    private Currency currency;
    private BigDecimal amount;
    private Account sourceAccount;
    private Account targetAccount;
    private Bank sourceBank;
    private Bank targetBank;
    private LocalDateTime timestamp;
    private TransactionType type;
}
