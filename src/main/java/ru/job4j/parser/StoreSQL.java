package ru.job4j.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class StoreSQL implements Store, AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreSQL.class);
    private Connection connection;

    public StoreSQL(Connection connection) {
        this.connection = connection;
        createTable();
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Метод создает таблицу, если она еще не создана
     */
    private void createTable() {
        try (Statement st = connection.createStatement()) {
            LOGGER.info("Создаю таблицу вакансий");
            st.execute("CREATE TABLE IF NOT EXISTS vacancies (id serial primary key, name varchar(2000), "
                    + "text text, link varchar(2000), date date, UNIQUE(name))");
            LOGGER.info("Таблица создана");
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void saveAll(Parse parser) {
        try (PreparedStatement st = connection.prepareStatement("insert into vacancies (name, text, link, date)"
                + " values (?,?,?,?) ON CONFLICT (name) DO NOTHING")) {
            connection.setAutoCommit(false);
            for (int i = parser.getStartIndex(); i < parser.getVacancyList().size(); i++) {
                Vacancy vacancy = parser.getVacancyList().get(i);
                st.setString(1, vacancy.getName());
                st.setString(2, vacancy.getText());
                st.setString(3, vacancy.getLink());
                st.setDate(4, vacancy.getDate());
                st.addBatch();
            }
            st.executeBatch();
            connection.commit();
            LOGGER.info("Данные внесены в Базу данных" + System.lineSeparator());
        } catch (SQLException e) {
            System.out.println("SQLException. Executing rollback");
            try {
                connection.rollback(); // в случае SQLException, возвращаем БД к исходному состоянию
            } catch (SQLException ex) {
                LOGGER.error(e.getMessage(), e);
            }
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                connection.setAutoCommit(true); //возвращаем режим автоматического коммита
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public void save(Vacancy vacancy) {
        try (PreparedStatement st = connection.prepareStatement("insert into vacancies (name, text, link, date)"
                + " values (?,?,?,?) ON CONFLICT (name) DO NOTHING")) {
            st.setString(1, vacancy.getName());
            st.setString(2, vacancy.getText());
            st.setString(3, vacancy.getLink());
            st.setDate(4, vacancy.getDate());
            st.executeUpdate();
            LOGGER.info("Вакансия внесена в Базу данных");
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

   public  List<Vacancy> get(Predicate<Vacancy> filter) {
        List<Vacancy> vacancies = new ArrayList<>();
        Vacancy vacancy;
        try (Statement st = connection.createStatement()) {
            try (ResultSet rs = st.executeQuery("select* from vacancies")) {
                while (rs.next()) {
                    vacancy = new Vacancy(rs.getString("name"), rs.getString("text"),
                            rs.getString("link"), rs.getDate("date"));
                    if (filter.test(vacancy)) {
                        vacancies.add(vacancy);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return vacancies;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) {
        //new StoreSQL(new ConnectionCreator().init()).save(new Vacancy("a", "url", "vacancyText", new Date(1111L)));
        new StoreSQL(new ConnectionCreator().init()).get(vacancy -> vacancy.getName().toLowerCase().
                contains("разработчик")).forEach(System.out::println);

    }
}