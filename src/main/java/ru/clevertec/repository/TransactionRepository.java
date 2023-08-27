package ru.clevertec.repository;

import java.math.BigDecimal;
import ru.clevertec.model.Transaction;

public interface TransactionRepository {

    void createTransaction(Transaction transaction);

}
