package ru.job4j.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * The class is responsible for creating a connection to the database
 */
public class ConnectionCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionCreator.class);

    /**
     * Сreates a connection to the database
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
            LOGGER.info("Сonnection is established");
            return connection;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
