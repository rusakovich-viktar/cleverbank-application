package ru.clevertec.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The `Bank` class represents a bank entity within the system.
 * It contains information about the bank's unique identifier, name, and a list of accounts associated with the bank.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Bank {
    private Long id;
    private String name;
    private List<Account> accounts;
}
