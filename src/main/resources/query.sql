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

    public Account findById(Long accountId) {
        String query = "SELECT a.id AS account_id, a.account_number, a.balance, a.currency, a.account_opening_date, " +
                "u.id AS user_id, u.identification_number_of_passport, u.first_name, u.last_name, " +
                "b.id AS bank_id, b.name " +
                "FROM accounts a " +
                "JOIN users u ON a.user_id = u.id " +
                "JOIN banks b ON a.bank_id = b.id " +
                "WHERE a.id = ?";