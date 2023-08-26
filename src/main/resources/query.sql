SELECT
    u.id AS user_id,
    u.identification_number_of_passport,
    u.first_name,
    u.last_name,
    u.patronymic,
    u.login,
    a.account_number,
    a.balance,
    a.currency,
    a.account_opening_date,
    b.name AS bank_name
FROM users u
         INNER JOIN accounts a ON u.id = a.user_id
         INNER JOIN banks b ON a.bank_id = b.id
WHERE u.id = ?;

SELECT a.*, b.name FROM accounts a JOIN banks b ON a.bank_id = b.id WHERE user_id = ?
