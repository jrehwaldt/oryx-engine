package org.jodaengine.eventmanagement.timing;

import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.jodaengine.eventmanagement.timing.job.SayHelloJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Testing the configuration of the jobs passed to the quartz scheduler.
 * 
 * This test is only a manual test. Should not be considered as real test for the implementation.
 */
public class QuartzSpikeTest {

    private static final int TIME_TO_SLEEP = 3000;

    private final static int MS_INTERVAL = 500;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Scheduler sched;

    @BeforeMethod
    public void setUp()
    throws SchedulerException {

        logger.info("------- Initializing ----------------------");

        // First we must get a reference to a scheduler
        SchedulerFactory sf = new StdSchedulerFactory();
        sched = sf.getScheduler();
        sched.start();

        logger.info("------- Initialization Complete -----------");

    }

    @Test
    public void testUnlimitedJopRepitition()
    throws SchedulerException, InterruptedException {

        JobDetail jobDetail = JobBuilder.newJob(SayHelloJob.class).withIdentity("say-hello", "say-hello-group").build();

        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().repeatForever()
        .withIntervalInMilliseconds(MS_INTERVAL);

        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("say-hallo-trigger", "say-hallo-group")
        .startNow().withSchedule(scheduleBuilder).build();

        scheduleJob(jobDetail, trigger);

        Thread.sleep(TIME_TO_SLEEP);
    }

    @Test
    public void testSingleJobCall()
    throws SchedulerException, InterruptedException {

        JobDetail jobDetail = JobBuilder.newJob(SayHelloJob.class).withIdentity("say-hello", "say-hello-group").build();

        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0);

        GregorianCalendar date = new GregorianCalendar();
        date.setTimeInMillis(System.currentTimeMillis() + MS_INTERVAL);
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("say-hallo-trigger", "say-hallo-group")
        .startAt(date.getTime()).withSchedule(scheduleBuilder).build();

        scheduleJob(jobDetail, trigger);

        Thread.sleep(TIME_TO_SLEEP);
        
        Assert.assertEquals(SayHelloJob.timesIsaidHello, 1);
    }

    private void scheduleJob(JobDetail jobDetail, SimpleTrigger trigger)
    throws SchedulerException {

        Date ft = sched.scheduleJob(jobDetail, trigger);
        logger.info("Job registered at " + System.currentTimeMillis());
        logger.info(jobDetail.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount()
            + " times, every " + trigger.getRepeatInterval() + " seconds");
    }

    @AfterMethod
    public void tearDown()
    throws SchedulerException {

        // shut down the scheduler
        logger.info("------- Shutting Down ---------------------");
        sched.shutdown(true);
        logger.info("------- Shutdown Complete -----------------");
        
        SayHelloJob.reset();
    }
}
