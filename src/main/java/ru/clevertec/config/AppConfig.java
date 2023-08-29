package ru.clevertec.config;

import java.io.InputStream;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

//@Log4j2
public class AppConfig {

    private static final String CONFIG_FILE = "database.yaml";

    private Map<String, Map<String, Object>> config;

    public AppConfig() {
        loadConfig();
    }

    private void loadConfig() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            Yaml yaml = new Yaml();
            config = yaml.load(inputStream);
        } catch (Exception e) {
//            log.error("Exception (loadConfig())", e);
        }
    }

    public Map<String, Object> getProperty(String key) {
        return config.get(key);
    }

//    public Map<String, Object> getConfig() {
//        return config;
//    }

}
