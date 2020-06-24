package ru.job4j.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.model.Post;
import ru.job4j.parser.dateconverter.SQLRuDateConverter;
import ru.job4j.parser.datasource.DataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Parses java vacancies on sql.ru
 */
public class SqlRuParser implements Parse {
    private final DataSource dataSource;

    public SqlRuParser(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Loads posts from SqlRu
     */
    public List<Post> list(String url, Predicate<String> predicate) {
        List<Post> postList = new ArrayList<>();
        Document doc = dataSource.getDocument(url);
        if (doc == null) {
            return postList;
        }
        Elements tr = doc.select("#content-wrapper-forum > table.forumTable > tbody > tr");
        for (int j = 4; j < tr.size(); j++) {
            Element tdA = tr.get(j).select("td.postslisttopic>a").first();
            String name = tdA.text();
            if (predicate.test(name)) {
                String link = tdA.attr("href");
                Post post = detail(link);
                if (post != null) {
                    postList.add(post);
                }
            }
        }
        return postList;
    }

    /**
     * loads a specific post from its link
     */
    @Override
    public Post detail(String link) {
        Post post = null;
        Document doc = dataSource.getDocument(link);
        if (doc == null) {
            return post;
        }
        String name = getName(doc);
        String text = getText(doc);
        String date = getDate(doc);
        post = new Post(name, text, link, SQLRuDateConverter.convertToTimeStamp(date));
        return post;
    }

    /**
     * Returns the name of the vacancy
     */
    private String getName(Document doc) {
        Element td = doc.select("#content-wrapper-forum > table.msgTable").first().
                select("td.messageHeader").first();
        return td.text().replace("[new]", "");
    }

    /**
     * Returns the text of the vacancy
     */
    private String getText(Document doc) {
        Elements td = doc.select("#content-wrapper-forum > table.msgTable").first().
                select("tbody> tr:nth-child(2) > td:nth-child(2)");
        return td.text();
    }

    /**
     * Returns the date when the post was created
     */
    private String getDate(Document doc) {
        Element td = doc.select("#content-wrapper-forum > table.msgTable").first().
                select("td.msgFooter").first();
        return td.text();
    }
}
