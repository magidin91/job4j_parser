package ru.job4j;

import ru.job4j.config.ConfigLoader;
import ru.job4j.connection.ConnectionCreator;
import ru.job4j.grab.QuartzGrab;
import ru.job4j.parser.SqlRuParser;
import ru.job4j.parser.datasource.URLDataSource;
import ru.job4j.store.Store;
import ru.job4j.store.SQLStore;
import ru.job4j.web.Web;

import java.util.Properties;

/**
 * Starts the entire application
 */
public class GrabberApplication {
    public static void main(String[] args) {
        Store store = new SQLStore(new ConnectionCreator().init());
        Properties config = ConfigLoader.config();
        new QuartzGrab(config).init(new SqlRuParser(new URLDataSource()), store);
        new Web(config).web(store);
    }
}
