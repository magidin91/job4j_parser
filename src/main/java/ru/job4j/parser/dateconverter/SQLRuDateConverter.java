package ru.job4j.parser.dateconverter;

import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * Converts SQLRu date to TimeStamp
 */
public class SQLRuDateConverter {
    /**
     * Converts a string representation of a date to TimeStamp
     */
    public static Timestamp convertToTimeStamp(String date) {
        date = date.replace(",", "");
        String[] timeAndDate = date.split(" ");
        String stringDay = timeAndDate[0];
        String timeStamp;
        if ("сегодня".equals(stringDay)) {
            String localDate = LocalDate.now().toString();
            timeStamp = String.format("%s %s:00", localDate, timeAndDate[1]);
        } else if ("вчера".equals(stringDay)) {
            String localDate = LocalDate.now().minusDays(1).toString();
            timeStamp = String.format("%s %s:00", localDate, timeAndDate[1]);
        } else {
            timeStamp = String.format("20%s-%d-%s %s:00",
                    timeAndDate[2], getMonthByName(timeAndDate[1]), stringDay, timeAndDate[3]);
        }
        return Timestamp.valueOf(timeStamp);
    }

    /**
     * Converts the string representation of the month to the standard numeric representation - from 1 to 12
     */
    private static int getMonthByName(String monthName) {
        int month;
        switch (monthName) {
            case ("янв"):
                month = 1;
                break;
            case ("фев"):
                month = 2;
                break;
            case ("мар"):
                month = 3;
                break;
            case ("апр"):
                month = 4;
                break;
            case ("май"):
                month = 5;
                break;
            case ("июн"):
                month = 6;
                break;
            case ("июл"):
                month = 7;
                break;
            case ("авг"):
                month = 8;
                break;
            case ("сен"):
                month = 9;
                break;
            case ("окт"):
                month = 10;
                break;
            case ("ноя"):
                month = 11;
                break;
            case ("дек"):
                month = 12;
                break;
            default:
                month = 0;
        }
        return month;
    }
}
