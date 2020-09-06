package ru.job4j.model;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * A model of post
 */
public class Post {
    private final String name;
    private final String text;
    private final String link;
    private final Timestamp created;

    public Post(String name, String text, String link, Timestamp created) {
        this.name = name;
        this.text = text;
        this.link = link;
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getLink() {
        return link;
    }

    public Timestamp getCreated() {
        return created;
    }

    @Override
    public String toString() {
        return "Post{" + "name=" + name + ", link='" + link + ", created=" + created + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(name, post.name)
                && Objects.equals(created, post.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, created);
    }
}
