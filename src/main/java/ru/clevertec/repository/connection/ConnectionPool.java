package ru.clevertec.repository.connection;

import static ru.clevertec.util.Constants.Attributes.JDBC_URL;
import static ru.clevertec.util.Constants.Attributes.MAXIMUM_POOL_SIZE;
import static ru.clevertec.util.Constants.Attributes.MINIMUM_IDLE;
import static ru.clevertec.util.Constants.Attributes.PASSWORD;
import static ru.clevertec.util.Constants.Attributes.USERNAME;
import static ru.clevertec.util.Constants.Messages.ERROR_LOADING_CONFIGURATION;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.yaml.snakeyaml.Yaml;

/**
 * This class provides a connection pool to the database using HikariCP.
 */
@Log4j2
public class ConnectionPool {

    private static HikariDataSource dataSource = createDataSource();

    /**
     * Creates a new HikariCP data source based on the configuration.
     *
     * @return The HikariDataSource instance.
     */
    private static HikariDataSource createDataSource() {
        HikariConfig config = loadConfig("property.yml");
        return new HikariDataSource(config);
    }

    /**
     * Loads the HikariCP configuration from a YAML file.
     *
     * @param configFile The name of the configuration file.
     * @return The HikariConfig instance.
     */
    private static HikariConfig loadConfig(String configFile) {
        try (InputStream inputStream = ConnectionPool.class.getClassLoader().getResourceAsStream(configFile)) {
            Yaml yaml = new Yaml();
            Map<String, Map<String, Object>> configMap = yaml.load(inputStream);
            Map<String, Object> property = configMap.get("hikari");
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl((String) property.get(JDBC_URL));
            config.setUsername((String) property.get(USERNAME));
            config.setPassword((String) property.get(PASSWORD));
            config.setMinimumIdle((int) property.get(MINIMUM_IDLE));
            config.setMaximumPoolSize((int) property.get(MAXIMUM_POOL_SIZE));
            return config;
        } catch (IOException e) {
            log.error(ERROR_LOADING_CONFIGURATION + " from {}: {}", configFile, e.getMessage());
            throw new RuntimeException(ERROR_LOADING_CONFIGURATION, e);
        }
    }

    /**
     * Retrieves a database connection from the connection pool.
     *
     * @return A Connection object.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
