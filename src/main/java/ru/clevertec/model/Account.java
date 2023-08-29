package ru.clevertec.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private Currency currency;
    private LocalDateTime accountOpeningDate;
    private User user;
    private Bank bank;
    private List<Transaction> transactions;

}
