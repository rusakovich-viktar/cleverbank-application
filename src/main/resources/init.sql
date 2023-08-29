-- Таблица пользователей
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       identification_number_of_passport VARCHAR(255) NOT NULL,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       patronymic VARCHAR(255),
                       login VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL
);

-- Таблица банков
CREATE TABLE banks (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL
);

-- Таблица счетов
CREATE TABLE accounts (
                          id SERIAL PRIMARY KEY,
                          account_number VARCHAR(255) NOT NULL,
                          balance DECIMAL(19, 2) NOT NULL,
                          currency VARCHAR(3) NOT NULL,
                          account_opening_date TIMESTAMP NOT NULL,
                          user_id INTEGER REFERENCES users(id),
                          bank_id INTEGER REFERENCES banks(id)
);

-- Таблица транзакций
CREATE TABLE transactions (
                              id SERIAL PRIMARY KEY,
                              currency VARCHAR(3) NOT NULL,
                              amount DECIMAL(19, 2) NOT NULL,
                              source_account_id INTEGER REFERENCES accounts(id),
                              target_account_id INTEGER REFERENCES accounts(id),
                              source_bank_id INTEGER REFERENCES banks(id),
                              target_bank_id INTEGER REFERENCES banks(id),
                              timestamp TIMESTAMP NOT NULL,
                              type VARCHAR(15) NOT NULL
);

ALTER TABLE users ADD COLUMN account_id INTEGER REFERENCES accounts(id);

