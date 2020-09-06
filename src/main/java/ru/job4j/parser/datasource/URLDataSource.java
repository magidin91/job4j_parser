package ru.job4j.parser.datasource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class URLDataSource implements DataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(URLDataSource.class);

    /**
     * Returns Document from URL
     */
    @Override
    public Document getDocument(String url) {
        Document rsl = null;
        try {
            rsl = Jsoup.connect(url).get();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return rsl;
    }
}
