package ru.clevertec.service;

import ru.clevertec.model.Transaction;

public interface ReceiptService {
    /**
     * Generates a receipt for the provided transaction.
     *
     * @param transaction The transaction for which to generate the receipt.
     */

    void doGenerateTheReceipt(Transaction transaction);

}
