package ru.job4j.parser;

import java.util.List;
import java.util.function.Predicate;

public interface Store {

    /**
     * Метод записывает вакансию в Базу данных
     */
    void save(Vacancy vacancy);

    /**
     * Метод записывает вакансии, собранные парсером, в Базу данных
     */
     void saveAll(Parse parser);

    /**
     * Метод возвращает список вакансий в зависимости от предиката из Базы данных
     */
    List<Vacancy> get(Predicate<Vacancy> filter);
}