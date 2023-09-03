package ru.clevertec.config;

import static ru.clevertec.util.Constants.Attributes.JDBC_URL;
import static ru.clevertec.util.Constants.Attributes.PASSWORD;
import static ru.clevertec.util.Constants.Attributes.USERNAME;
import static ru.clevertec.util.Constants.Messages.ERROR_LOADING_CONFIGURATION;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

/**
 * This class reads configuration data from a YAML file.
 */
public class ReaderConfigFile {
    /**
     * Retrieves the interest rate from the configuration file.
     *
     * @return The interest rate as a BigDecimal.
     */
    public BigDecimal getInterestRate() {
        try (InputStream inputStream = ReaderConfigFile.class.getClassLoader().getResourceAsStream("property.yml")) {
            Yaml yaml = new Yaml();
            Map<String, Map<String, String>> configMap = yaml.load(inputStream);
            Map<String, String> mapValue = configMap.get("bankOperations");
            String interestRateRaw = mapValue.get("interestRate");
            return new BigDecimal(interestRateRaw);
        } catch (IOException e) {
            throw new RuntimeException(ERROR_LOADING_CONFIGURATION, e);
        }
    }

    /**
     * Retrieves Flyway configuration data from the configuration file.
     *
     * @return The FlywayConfig object containing database connection details.
     */
    public FlywayConfig getFlywayConfig() {
        try (InputStream inputStream = ReaderConfigFile.class.getClassLoader().getResourceAsStream("property.yml")) {
            Yaml yaml = new Yaml();
            Map<String, Map<String, String>> configMap = yaml.load(inputStream);
            Map<String, String> flywayMap = configMap.get("hikari");

            FlywayConfig flywayConfig = new FlywayConfig();
            flywayConfig.setUrl(flywayMap.get(JDBC_URL));
            flywayConfig.setUser(flywayMap.get(USERNAME));
            flywayConfig.setPassword(flywayMap.get(PASSWORD));

            return flywayConfig;
        } catch (IOException e) {
            throw new RuntimeException(ERROR_LOADING_CONFIGURATION, e);
        }
    }
}


