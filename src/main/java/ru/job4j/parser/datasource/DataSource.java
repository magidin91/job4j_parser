package ru.job4j.parser.datasource;

import org.jsoup.nodes.Document;

public interface DataSource {
    /**
     * Returns Document from source
     */
    Document getDocument(String source);
}
