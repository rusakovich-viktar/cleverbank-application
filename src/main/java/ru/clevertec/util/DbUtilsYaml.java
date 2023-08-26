package ru.clevertec.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class DbUtilsYaml {
    public static Connection connection() {
        Connection connection = null;
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream("src/main/resources/database.yml");
            Map<String, Map<String, String>> configMap = yaml.load(inputStream);
            Map<String, String> dbConfig = configMap.get("database");
            String url = dbConfig.get("url");
            String username = dbConfig.get("username");
            String password = dbConfig.get("password");
            connection = DriverManager.getConnection(url, username, password);
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQLException");
            e.printStackTrace();
        }
        return connection;
    }
}
