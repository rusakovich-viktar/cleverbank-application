package ru.clevertec.service;

import ru.clevertec.model.Transaction;

public interface ReceiptService {
    void doGenerateTheReceipt(Transaction transaction);

}
