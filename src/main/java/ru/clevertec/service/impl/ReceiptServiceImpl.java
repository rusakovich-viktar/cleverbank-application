package ru.clevertec.service.impl;

import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.clevertec.model.Transaction;
import ru.clevertec.service.ReceiptService;

@AllArgsConstructor
@Getter
@Setter

public class ReceiptServiceImpl implements ReceiptService {

    public void printReceipt(Transaction transaction, Long transactionId) {
        System.out.println("|______________________________________");
        System.out.println("|             Банковский чек");
        System.out.println("|Чек: " + transactionId);
        System.out.println("|" + transaction.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " " + transaction.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println("|Тип транзакции " + transaction.getType().getTranslation());
        System.out.println("|Банк отправителя: " + transaction.getSourceBank().getName());
        System.out.println("|Банк получателя: " + transaction.getTargetBank().getName());
        System.out.println("|Счет отправителя: " + transaction.getSourceAccount().getAccountNumber());
        System.out.println("|Счет получателя: " + transaction.getTargetAccount().getAccountNumber());
        System.out.println("|Сумма: " + transaction.getAmount() + " " + transaction.getCurrency().getCurrencyCode());
        System.out.println("|_____________________________________");
    }
}
