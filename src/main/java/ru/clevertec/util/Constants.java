package ru.clevertec.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    @UtilityClass
    public class Messages {
        public static final String ERROR_LOADING_CONFIGURATION = "Error loading configuration";
        public static final String ERROR_FROM_UPDATING_BALANCE = "Произошла ошибка при обновлении баланса";
        public static final String ERROR_FROM_CREATE_TRANSACTION = "Произошла ошибка при создании транзакции";
        public static final String ERROR_REPLENISHMENT = "Ошибка пополнения средств";
        public static final String LOGIN_SUCCESSFUL_MESSAGE = "Вход успешно выполнен";
        public static final String LOGIN_REFUSE_MESSAGE = "Во входе отказано. Проверьте введенные логин и пароль";
        public static final String INVALID_CHOICE_PLEASE_TRY_AGAIN = "Некорректный выбор. Попробуйте еще раз";
        public static final String NO_ACCOUNTS_FOUND_FOR_THE_USER = "У пользователя нет открытых счетов или они не найдены";
        public static final String ENTER_A_NUMBER_BETWEEN_ONE_AND = "Введите число от 1 до ";
    }

    @UtilityClass
    public class Attributes {
        public static final String JDBC_URL = "jdbcUrl";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String MINIMUM_IDLE = "minimumIdle";
        public static final String MAXIMUM_POOL_SIZE = "maximumPoolSize";


        public static final String ID = "id";
        public static final String DB_IDENTIFICATION_NUMBER_OF_PASSPORT = "identification_number_of_passport";
        public static final String DB_FIRST_NAME = "first_name";
        public static final String DB_LAST_NAME = "last_name";
        public static final String PATRONYMIC = "patronymic";
        public static final String LOGIN = "login";
        public static final String DB_USER_ID = "user_id";
        public static final String BANK_ID = "bank_id";
        public static final String NAME = "name";


        public static final String DB_ACCOUNT_NUMBER = "account_number";
        public static final String BALANCE = "balance";
        public static final String CURRENCY = "currency";
        public static final String DB_ACCOUNT_OPENING_DATE = "account_opening_date";


    }
}
