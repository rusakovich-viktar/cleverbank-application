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
    private Currency currency; // Валюта
    private BigDecimal amount; // количество
    private Account sourceAccount; // Счет-источник
    private Account targetAccount; // Счет-получатель
    private Bank sourceBank; // банк-источник
    private Bank targetBank; // банк-получатель
    private LocalDateTime timestamp;
    private TransactionType type;
}
