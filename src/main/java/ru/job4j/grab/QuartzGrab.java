package ru.job4j.grab;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Post;
import ru.job4j.parser.Parse;
import ru.job4j.store.Store;

import java.util.List;
import java.util.Properties;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Using Quartz periodically collects data and stores it in storage
 */
public class QuartzGrab implements Grab {
    private final Properties config;
    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzGrab.class);

    public QuartzGrab(Properties config) {
        this.config = config;
    }

    /**
     * Starts Quartz
     */
    public void init(Parse parser, Store store) {
        try {
            Scheduler scheduler = scheduler();
            JobDataMap data = new JobDataMap();
            data.put("parser", parser);
            data.put("store", store);
            JobDetail job = newJob(ParseAndStoreJob.class)
                    .usingJobData(data)
                    .build();
            Trigger trigger = newTrigger()
                    .withSchedule(cronSchedule(config.getProperty("cron.time")))
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }

    /**
     * Returns a scheduler
     */
    private Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    /**
     * Collects data and stores it in storage
     */
    public static class ParseAndStoreJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            Parse parser = (Parse) context.getJobDetail().getJobDataMap().get("parser");
            Store store = (Store) context.getJobDetail().getJobDataMap().get("store");
            List<Post> vacancies = parser.list("https://www.sql.ru/forum/job-offers/",
                    name -> name.toLowerCase().matches("(.*)(?!java\\s*script)(java)(.*)"));
            LOGGER.info("Vacancies are received");
            vacancies.forEach(store::save);
            LOGGER.info("Vacancies are saved to the database");
        }
    }
}
