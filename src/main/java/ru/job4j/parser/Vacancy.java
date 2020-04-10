package ru.job4j.parser;

import java.sql.Date;
import java.util.Objects;

public class Vacancy {
    private String name;
    private String text;
    private String link;
    private Date date;

    public Vacancy(String name, String text, String link, Date date) {
        this.name = name;
        this.text = text;
        this.link = link;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Vacancy{" + "name='" + name + '\''
                + ", link='" + link + '\''
                + ", date=" + date + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vacancy vacancy = (Vacancy) o;
        return Objects.equals(name, vacancy.name)
                && Objects.equals(text, vacancy.text)
                && Objects.equals(link, vacancy.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, text, link);
    }
}
