package ru.clevertec.model;

/**
 * An enumeration representing different transaction types in the system.
 * Each transaction type has a corresponding translation that can be used for localization.
 */
public enum TransactionType {
    /**
     * Represents a transaction of type "Replenishment."
     */
    REPLENISHMENT("Пополнение"),
    /**
     * Represents a transaction of type "Withdrawal."
     */
    WITHDRAWAL("Снятие"),
    /**
     * Represents a transaction of type "Transferring."
     */
    TRANSFERRING("Перевод");

    private final String translation;

    /**
     * Constructs a TransactionType enum value with its corresponding translation.
     *
     * @param translation The translation of the transaction type.
     */
    TransactionType(String translation) {
        this.translation = translation;
    }

    /**
     * Get the translation of the transaction type.
     *
     * @return The translation as a string.
     */
    public String getTranslation() {
        return translation;
    }
}
