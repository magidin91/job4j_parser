package ru.job4j.connection;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConnectionCreatorTest {

    @Test
    public void init() {
        assertNotNull(new ConnectionCreator().init());
    }
}