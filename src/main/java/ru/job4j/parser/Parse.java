package ru.job4j.parser;

import ru.job4j.model.Post;

import java.util.List;
import java.util.function.Predicate;

public interface Parse {

    /**
     * Loads posts from a link
     * @param predicate the condition for determining the desired posts
     */
    List<Post> list(String link, Predicate<String> predicate);

    /**
     * loads a specific post from its link
     */
    Post detail(String link);
}