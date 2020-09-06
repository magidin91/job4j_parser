package ru.job4j.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * The class is responsible for loading configuration properties
 */
public class ConfigLoader {

    /**
     * Returns a properties
     */
    public static Properties config() {
        Properties config = new Properties();
        try (InputStream in = ConfigLoader.class.getClassLoader().getResourceAsStream("app.properties")) {
            config.load(in);
            return config;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
