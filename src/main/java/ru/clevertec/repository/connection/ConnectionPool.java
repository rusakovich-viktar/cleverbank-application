package ru.clevertec.repository.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.yaml.snakeyaml.Yaml;

@Log4j2
public class ConnectionPool {

    private static HikariDataSource dataSource = createDataSource();

    private static HikariDataSource createDataSource() {
        HikariConfig config = loadConfig("property.yml");
        return new HikariDataSource(config);
    }

    private static HikariConfig loadConfig(String configFile) {
        try (InputStream inputStream = ConnectionPool.class.getClassLoader().getResourceAsStream(configFile)) {
            Yaml yaml = new Yaml();
            Map<String, Map<String, Object>> configMap = yaml.load(inputStream);
            Map<String, Object> property = configMap.get("hikari");
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl((String) property.get("jdbcUrl"));
            config.setUsername((String) property.get("username"));
            config.setPassword((String) property.get("password"));
            config.setMinimumIdle((int) property.get("minimumIdle"));
            config.setMaximumPoolSize((int) property.get("maximumPoolSize"));
            return config;
        } catch (Exception e) {
            log.error("Error loading configuration", e);
            throw new RuntimeException("Error loading configuration", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
