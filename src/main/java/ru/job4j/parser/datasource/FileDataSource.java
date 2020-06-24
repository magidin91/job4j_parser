package ru.job4j.parser.datasource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class FileDataSource implements DataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDataSource.class);

    /**
     * Returns Document from File
     */
    @Override
    public Document getDocument(String path) {
        Document rsl = null;
        try {
            rsl = Jsoup.parse(new File(path), "UTF-8");
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return rsl;
    }
}
