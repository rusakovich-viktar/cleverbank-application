-- Добавление транзакций
INSERT INTO transactions (currency, amount, source_account_id, target_account_id, source_bank_id, target_bank_id, timestamp, type)
VALUES ('USD', 200.00, 1, 2, 1, 2, NOW(), 'TRANSFERRING');

INSERT INTO transactions (currency, amount, source_account_id, target_account_id, source_bank_id, target_bank_id, timestamp, type)
VALUES ('USD', 30.00, 1, 2, 1, 2, NOW(), 'TRANSFERRING');

INSERT INTO transactions (currency, amount, source_account_id, target_account_id, source_bank_id, target_bank_id, timestamp, type)
VALUES ('USD', 30.00, 1, 1, 1, 1, NOW(), 'REPLENISHMENT');

INSERT INTO transactions (currency, amount, source_account_id, target_account_id, source_bank_id, target_bank_id, timestamp, type)
VALUES ('USD', 30.00, 4, 4, 2, 2, NOW(), 'WITHDRAWAL');