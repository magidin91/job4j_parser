package ru.job4j.parser;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public interface Parse {
    /**
     * @return Метод собирает вакансии по ссылке в зависимости от предиката
     */
    List<Vacancy> parse(String link, Predicate<String> predicate) throws IOException;

    /**
     * @return Метод возвращает вакансию по ссылке
     */
    Vacancy detail(String link);

    /**
     * @return Метод возвращает список вакансий, собранных парсером
     */
    List<Vacancy> getVacancyList();

    /**
     * @return Метод возвращает индекс, с которого в текущем парсинге происходит вставка элементов
     */
    int getStartIndex();
}