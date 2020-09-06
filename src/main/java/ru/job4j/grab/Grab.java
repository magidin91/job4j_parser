package ru.job4j.grab;

import ru.job4j.parser.Parse;
import ru.job4j.store.Store;

/**
 * Collects data using Parser and saves it to storage using Store
 */
public interface Grab {
    void init(Parse parse, Store store);
}