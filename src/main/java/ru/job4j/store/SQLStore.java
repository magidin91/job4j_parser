package ru.job4j.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Interacts with the database
 */
public class SQLStore implements Store, AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLStore.class);
    private final Connection connection;

    public SQLStore(Connection connection) {
        this.connection = connection;
        createTable();
    }

    /**
     * Saves a post to the database
     */
    public void save(Post post) {
        try (PreparedStatement st = connection.prepareStatement("insert into posts (name, text, link, created)"
                + " values (?,?,?,?) ON CONFLICT (link) DO NOTHING")) {
            st.setString(1, post.getName());
            st.setString(2, post.getText());
            st.setString(3, post.getLink());
            st.setTimestamp(4, post.getCreated());
            st.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Returns all posts from the database
     */
    public List<Post> getAll() {
        List<Post> vacancies = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement("select* from posts")) {
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Post post = new Post(
                            rs.getString("name"),
                            rs.getString("text"),
                            rs.getString("link"),
                            rs.getTimestamp("created"));
                    vacancies.add(post);
                    LOGGER.info("The data was obtained from a database");
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return vacancies;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
            LOGGER.info("Connection is closed");
        }
    }

    /**
     * Creates a table "posts" if it hasn't been created yet
     */
    private void createTable() {
        try (PreparedStatement st = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS posts (id serial primary key, " +
                        "name varchar(2000), text text, link varchar(2000), created timestamp(0), UNIQUE(link))")) {
            st.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}