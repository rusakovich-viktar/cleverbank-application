package ru.clevertec;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import ru.clevertec.repository.connection.ConnectionPool;

public class Main {

    public static void main(String[] args) throws SQLException {

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM transactions");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                System.out.println("Record: id=" + id);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

    }
}
