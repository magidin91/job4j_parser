package ru.job4j.parser;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class Quartz implements Grab {
    private static final Logger LOGGER = LoggerFactory.getLogger(Quartz.class);
    private static Parse parser;
    private static Store store;

    public void init(Parse parse, Store storage) {
        parser = parse;
        store = storage;
        try (InputStream in = JSoupParser.class.getClassLoader().getResourceAsStream("app.properties")) {
            final Properties config = new Properties();
            config.load(in);
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(ParserJob.class).build();
            Trigger trigger = newTrigger()
                    .withSchedule(cronSchedule(config.getProperty("cron.time")))
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }

    public static class ParserJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            try {
                parser.parse("https://www.sql.ru/forum/job-offers/",
                        name -> name.toLowerCase().matches("(.*)(?!java\\s*script)(java)(.*)"));
                store.saveAll(parser);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public static void main(String[] args) {
        new Quartz().init(new JSoupParser(), new StoreSQL(new ConnectionCreator().init()));
    }
}
