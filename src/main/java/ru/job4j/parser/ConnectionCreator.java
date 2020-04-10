package ru.job4j.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionCreator.class);
    private Properties config = new Properties();

    /**
     * Метод загружает пропертис и устанавливает соединение с БД, возвращает объект connection
     */
    public Connection init() {
        try (InputStream in = ConnectionCreator.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            Connection connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password"));
            LOGGER.info("Установлено соединение соединение");
            return connection;

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    /**
     * Метод возвращает проперти по ключу
     */
    public String get(String key) {
        return this.config.getProperty(key);
    }
}
