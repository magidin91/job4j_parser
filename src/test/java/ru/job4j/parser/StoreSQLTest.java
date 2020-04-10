package ru.job4j.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

public class StoreSQLTest {
    private StoreSQL storeSQL;
    private Connection connection;

    @Before
    public void start() throws SQLException {
        connection = new ConnectionCreator().init();
        storeSQL = new StoreSQL(connection);
        storeSQL.getConnection().setAutoCommit(false);
    }

    @After
    public void finish() throws SQLException {
        connection.rollback();
        connection.close();
    }

    @Test
    public void checkConnection() {
        assertNotNull(connection);
    }

    @Test
    public void saveAndGet() {
        Vacancy exp = new Vacancy("developer12345", "text", "link", new Date(1000L));
        storeSQL.save(exp);
        Vacancy rsl = storeSQL.get(vacancy -> vacancy.getName().equals("developer12345")).get(0);
        assertEquals(rsl, exp);
    }
}