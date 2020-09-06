package ru.job4j.parser.dateconverter;

import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDate;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SQLRuDateConverterTest {
    @Test
    public void convertTodayToTimeStamp() {
        assertThat(SQLRuDateConverter.convertToTimeStamp("сегодня, 23:14"),
                is(Timestamp.valueOf(String.format("%s 23:14:00", LocalDate.now().toString()))));
    }

    @Test
    public void convertYesterdayToTimeStamp() {
        assertThat(SQLRuDateConverter.convertToTimeStamp("вчера, 21:53"),
                is(Timestamp.valueOf(String.format("%s 21:53:00", LocalDate.now().minusDays(1).toString()))));
    }

    @Test
    public void convertDateToTimeStamp() {
        assertThat(SQLRuDateConverter.convertToTimeStamp("10 июн 20, 21:53"),
                is(Timestamp.valueOf("2020-06-10 21:53:00")));
    }
}