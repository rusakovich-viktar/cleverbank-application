package ru.clevertec.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import ru.clevertec.model.Transaction;
import ru.clevertec.service.ReceiptService;

@AllArgsConstructor
@Getter
@Setter
@Log4j2
public class ReceiptServiceImpl implements ReceiptService {

    @Override
    public void doGenerateTheReceipt(Transaction transaction) {
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + "_id" + transaction.getId() + ".txt";
        try {
            File checkFolder = new File("check");
            if (!checkFolder.exists()) {
                checkFolder.mkdir();
            }

            File checkFile = new File(checkFolder, fileName);
            FileWriter writer = new FileWriter(checkFile);

            int lineWidth = 40;

            String transactionIdStr = Long.toString(transaction.getId());
            String transactionType = transaction.getType().getTranslation();
            String sourceBank = transaction.getSourceBank().getName();
            String targetBank = transaction.getTargetBank().getName();
            String sourceAccount = transaction.getSourceAccount().getAccountNumber();
            String targetAccount = transaction.getTargetAccount().getAccountNumber();
            String amount = transaction.getAmount().toString();
            String currency = transaction.getCurrency().getCurrencyCode();

            String title = "Банковский чек";
            String secondLineText = "| Чек: ";
            String thirdLineText = "| Тип транзакции: ";
            String fourthLineText = "| Банк отправителя: ";
            String fifthLineText = "| Банк получателя: ";
            String sixthLineText = "| Счет отправителя:";
            String seventhLineText = "| Счет получателя: ";
            String eighthLineText = "| Сумма: ";
            String openedFormattedText = "  %-";
            String closedText = " |";

            String transactionDate = transaction.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String transactionTime = transaction.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            String lineTop = "__________________________________________";
            String lineBottom = "|________________________________________|";

            int paddingFirst = (lineWidth - title.length() - 2) / 2;
            int paddingSecond = lineWidth - secondLineText.length() - closedText.length() - transactionIdStr.length();
            int paddingThird = lineWidth - thirdLineText.length() - closedText.length() - transactionType.length();
            int paddingFourth = lineWidth - fourthLineText.length() - closedText.length() - sourceBank.length();
            int paddingFifth = lineWidth - fifthLineText.length() - closedText.length() - targetBank.length();
            int paddingSixth = lineWidth - sixthLineText.length() - closedText.length() - sourceAccount.length();
            int paddingSeventh = lineWidth - seventhLineText.length() - closedText.length() - targetAccount.length();
            int paddingEighth = lineWidth - eighthLineText.length() - closedText.length() - currency.length() - amount.length() + 1;

            String formatFirst = String.format("| %" + paddingFirst + "s%s%" + paddingFirst + "s |", "", title, "");
            String formatSecond = String.format(secondLineText + openedFormattedText + paddingSecond + "s%s |", "", transaction.getId());
            String formatThird = String.format(thirdLineText + openedFormattedText + paddingThird + "s%s |", "", transactionType);
            String formatDateTime = String.format("| " + transactionDate + " ".repeat(lineWidth - transactionTime.length() - transactionTime.length() - closedText.length() - closedText.length()) + transactionTime + " |");
            String formatFourth = String.format(fourthLineText + openedFormattedText + paddingFourth + "s%s |", "", sourceBank);
            String formatFifth = String.format(fifthLineText + openedFormattedText + paddingFifth + "s%s |", "", targetBank);
            String formatSixth = String.format(sixthLineText + openedFormattedText + paddingSixth + "s%s |", "", sourceAccount);
            String formatSeventh = String.format(seventhLineText + openedFormattedText + paddingSeventh + "s%s |", "", targetAccount);
            String formattedAmount = String.format("%s%" + paddingEighth + "s%s %s |", eighthLineText, "", amount, currency);

            writer.write(lineTop + "\n");
            writer.write(formatFirst + "\n");
            writer.write(formatSecond + "\n");
            writer.write(formatDateTime + "\n");
            writer.write(formatThird + "\n");
            writer.write(formatFourth + "\n");
            writer.write(formatFifth + "\n");
            writer.write(formatSixth + "\n");
            writer.write(formatSeventh + "\n");
            writer.write(formattedAmount + "\n");
            writer.write(lineBottom + "\n");
            writer.close();

            log.info("Чек успешно сохранен: " + checkFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Ошибка при сохранении чека: " + e.getMessage());
        }
    }
}
