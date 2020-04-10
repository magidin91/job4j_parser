package ru.job4j.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class JSoupParser implements Parse {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSoupParser.class);
    private Date lastDate = Date.valueOf(LocalDate.of(LocalDate.now().getYear(), 1, 1));
    private List<Vacancy> vacancyList = new ArrayList<>();
    private int startIndex;

    public List<Vacancy> parse(String url, Predicate<String> predicate) throws IOException {
        LOGGER.info("Текущий парсинг начат");
        startIndex = vacancyList.size();
        boolean checkLastDate = true; //проверяем, что считали еще не все данные
        int i = 1; //переключение страницы
        while (checkLastDate) {
            Document doc = Jsoup.connect(String.format("%s%d", url, i))
                    .get();
            Elements tr = doc.select("#content-wrapper-forum > table.forumTable > tbody > tr");
            for (int j = 4; j < tr.size(); j++) {
                Element tdA = tr.get(j).select("td.postslisttopic>a").first();
                String name = tdA.text();
                String stringDate = tr.get(j).select("td.altCol").get(1).text();
                Date date = stringToDate(stringDate);
                if (predicate.test(name)) {
                    String link = tdA.attr("href");
                    String text = getText(link);
                    if (date.after(lastDate)) {
                        vacancyList.add(new Vacancy(name, text, link, date));

                    } else {
                        lastDate = Date.valueOf(LocalDate.now().minusDays(1)); //выставляем дату на день раньше,
                        // чтобы считать вакансии, добавленные в этот же день, но после парсинга
                        checkLastDate = false;
                        LOGGER.info("Текущий парсинг закончен");
                        break;
                    }
                }
            }
            i++;
        }
        System.out.println(String.format("======Получено %s новых вакансий======", (vacancyList.size() - startIndex)));
        return vacancyList;
    }

    @Override
    public Vacancy detail(String link) {
        return vacancyList.stream().filter(vacancy -> vacancy.getLink().equals(link)).findFirst().orElse(null);
    }

    /**
     * @return Метод приводит строковое представление даты к Date
     */
    private Date stringToDate(String date) {
        String[] timeAndDate = date.split(",");
        String[] onlyDate = timeAndDate[0].split(" ");
        String stringDay = onlyDate[0];
        LocalDate localDate;
        if ("сегодня".equals(stringDay)) {
            localDate = LocalDate.now();
        } else if ("вчера".equals(stringDay)) {
            localDate = LocalDate.now().minusDays(1);
        } else {
            int day = Integer.parseInt(stringDay);
            int month = getMonthByName(onlyDate[1]);
            int year = Integer.parseInt("20" + onlyDate[2]);
            localDate = LocalDate.of(year, month, day);
        }
        return Date.valueOf(localDate);
    }

    /**
     *  Метод приводит строковое представление месяца к стандартному числовому - от 1 до 12
     */
    private int getMonthByName(String monthName) {
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
            case ("отк"):
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

    /**
     * @return Метод возвращает текст вакансии по ее ссылке
     */
    private String getText(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        Elements td = doc.select("#content-wrapper-forum > table.msgTable").first().
                select("tbody> tr:nth-child(2) > td:nth-child(2)");
        return td.text();
    }

    public int getStartIndex() {
        return startIndex;
    }

    public List<Vacancy> getVacancyList() {
        return vacancyList;
    }

    public static void main(String[] args) throws IOException {
//        new JSoupParser().stringToDate("17 мар 20, 14:32");
//        System.out.println(new JSoupParser().getText("https://www.sql.ru/forum/1323994/pochta-banka-glavnyy-specialist-sql-ot-120-000-rub"));
        JSoupParser parser = new JSoupParser();
        parser.parse("https://www.sql.ru/forum/job-offers/", name -> name.toLowerCase().matches("(.*)(?!java\\s*script)(java)(.*)"));
        System.out.println(parser.detail("https://www.sql.ru/forum/1324128/java-razrabotchik"));
        //name.toLowerCase().matches("(.*)(?!java\\s*script)(java)(.*)")
    }
}
