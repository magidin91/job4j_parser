package ru.job4j.store;

import org.junit.Test;
import ru.job4j.connection.ConnectionCreator;
import ru.job4j.model.Post;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SQLStoreTest {

    @Test
    public void save2andGet2() throws Exception {
        Connection connection = new ConnectionCreator().init();
        connection.setAutoCommit(false);
        Post post1 = new Post("checker1", "", "link1", Timestamp.valueOf("1111-1-11 11:11:11"));
        Post post2 = new Post("checker2", "", "link2", Timestamp.valueOf("2222-2-22 22:22:22"));
        try (SQLStore SQLStore = new SQLStore(connection)) {
            SQLStore.save(post1);
            SQLStore.save(post2);
            assertTrue(SQLStore.getAll().containsAll(List.of(post1, post2)));
            connection.rollback();
        }
    }

    @Test
    public void save2DuplicatesAndGet1() throws Exception {
        Connection connection = new ConnectionCreator().init();
        connection.setAutoCommit(false);
        Post post1 = new Post("checker1", "", "duplicate link", Timestamp.valueOf("1111-1-11 11:11:11"));
        Post post2 = new Post("checker2", "", "duplicate link", Timestamp.valueOf("2222-2-22 22:22:22"));
        try (SQLStore SQLStore = new SQLStore(connection)) {
            SQLStore.save(post1);
            SQLStore.save(post2);
            assertFalse(SQLStore.getAll().containsAll(List.of(post1, post2)));
            assertTrue(SQLStore.getAll().contains(post1));
            connection.rollback();
        }
    }
}