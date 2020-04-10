package ru.job4j.parser;

public interface Grab {
    /**
     * Метод создает планировщик Quartz и запускает программу
     */
    void init(Parse parse, Store store);
}