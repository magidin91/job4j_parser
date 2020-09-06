package ru.job4j.store;

import ru.job4j.model.Post;

import java.util.List;

/**
 * Interacts with the data storage
 */
public interface Store {
    /**
     * Saves a post in the data storage
     */
    void save(Post post);

    /**
     * Returns all posts from the storage
     */
    List<Post> getAll();
}