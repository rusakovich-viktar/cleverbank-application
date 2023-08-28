package ru.clevertec.model;

public enum TransactionType {
    REPLENISHMENT("Пополнение"),
    WITHDRAWAL("Снятие"),
    TRANSFERRING("Перевод");

    private final String translation;

    private TransactionType(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }
}
