-- Добавление пользователей
INSERT INTO users (identification_number_of_passport, first_name, last_name, patronymic, login, password)
VALUES ('12345', 'John', 'Doe', 'Smith', 'qqq', 'www');

INSERT INTO users (identification_number_of_passport, first_name, last_name, patronymic, login, password)
VALUES ('67890', 'Alice', 'Johnson', 'Brown', 'alice456', 'password');

-- Добавление банков
INSERT INTO banks (name) VALUES ('Bank1');
INSERT INTO banks (name) VALUES ('Bank2');

-- Добавление счетов
INSERT INTO accounts (account_number, balance, currency, account_opening_date, user_id, bank_id)
VALUES ('123456789', 1000.00, 'USD', NOW(), 1, 1);

INSERT INTO accounts (account_number, balance, currency, account_opening_date, user_id, bank_id)
VALUES ('987654321', 2000.00, 'USD', NOW(), 2, 2);

-- Добавление транзакций
INSERT INTO transactions (currency, amount, source_account_id, target_account_id, source_bank_id, target_bank_id, timestamp, type)
VALUES ('USD', 200.00, 1, 2, 1, 2, NOW(), 'TRANSFERRING');