package ru.job4j.parser;

import org.junit.Test;
import ru.job4j.model.Post;
import ru.job4j.parser.datasource.FileDataSource;

import java.sql.Timestamp;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SqlRuParserTest {
    @Test
    public void detailTest() {
        Post exp = new Post(
                "КА/ разработчик Java/Scala, Проект BigData, Москва, от 150 000 нетт",
                "",
                "",
                Timestamp.valueOf("2019-08-01 14:30:00"));
        assertThat(
                new SqlRuParser(new FileDataSource()).detail(
                        getClass().getClassLoader().getResource("detail_test_page_1.html").getFile()), is(exp));
    }

    @Test
    public void listTest() {
        List<Post> exp = List.of(
                new Post(
                        "КА/ разработчик Java/Scala, Проект BigData, Москва, от 150 000 нетт",
                        "",
                        "",
                        Timestamp.valueOf("2019-08-01 14:30:00")),
                new Post("Java Developer, Краснодар, 135 000 gross",
                        "",
                        "",
                        Timestamp.valueOf("2019-07-25 11:34:00")),
                new Post("JAVA программист, крупный банк, м. Белорусская",
                        "",
                        "",
                        Timestamp.valueOf("2019-08-01 10:21:00"))
        );
        assertThat(
                new SqlRuParser(new FileDataSource()).list(
                        getClass().getClassLoader().getResource("list_test_page.html").getFile(),
                        name -> name.toLowerCase().matches("(.*)(?!java\\s*script)(java)(.*)")
                ), is(exp));
    }
}