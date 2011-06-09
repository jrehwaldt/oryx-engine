package org.jodaengine.eventmanagement.timing;

import java.util.Date;
import java.util.GregorianCalendar;

import org.jodaengine.eventmanagement.timing.job.SayHelloJob;
import org.jodaengine.eventmanagement.timing.job.SynchronizedJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Testing the configuration of the jobs passed to the quartz scheduler.
 * 
 * This test is only a manual test. Should not be considered as real test for the implementation.
 */
public class QuartzSpikeTest {

    private static final String WINNER_JOB_NAME = "winner";

    private static final String LOSER_JOB_NAME = "loser";

    private static final int TIME_TO_SLEEP = 500;

    private final static int MS_INTERVAL = 300;

    private final static int WINNER_MS_INTERVAL = 300;
    private final static int LOSER_MS_INTERVAL = 320;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Scheduler sched;

    /**
     * Setting up all necessary objects and mocks.
     * 
     * @throws SchedulerException
     *             if it fails
     */
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

    /**
     * Cleaning up the scheduler and other things.
     * 
     * @throws SchedulerException
     *             - if it fails
     */
    @AfterMethod
    public void tearDown()
    throws SchedulerException {

        // shut down the scheduler
        logger.info("------- Shutting Down ---------------------");
        sched.shutdown(true);
        logger.info("------- Shutdown Complete -----------------");

        SayHelloJob.reset();
        SynchronizedJob.reset();
    }

    /**
     * This method tests the Quartz implementation and some configurations. This method tests that a job is constantly
     * called.
     * 
     * @throws SchedulerException
     *             - if it fails
     * @throws InterruptedException
     *             - if it fails
     */
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

    /**
     * This method tests the configuration of the quartz scheduler in order to execute a job only once.
     * 
     * @throws SchedulerException
     *             - if it fails
     * @throws InterruptedException
     *             - if it fails
     */
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

        Assert.assertEquals(SayHelloJob.getTimesIsaidHello(), 1);
    }

    /**
     * Tests if the quartz ensures that the jobs are executed in the right time.
     * 
     * @throws SchedulerException
     *             - if it fails
     * @throws InterruptedException
     *             - if it fails
     */
    @Test(invocationCount = 1)
    public void testCriticalJobRace()
    throws SchedulerException, InterruptedException {

        JobDetail winnerJob = JobBuilder.newJob(SynchronizedJob.class).withIdentity(WINNER_JOB_NAME, "job-race")
        .build();
        SimpleTrigger winnerTrigger = buildTriggerFor(WINNER_JOB_NAME, WINNER_MS_INTERVAL);
        scheduleJob(winnerJob, winnerTrigger);

        JobDetail loserJob = JobBuilder.newJob(SynchronizedJob.class).withIdentity(LOSER_JOB_NAME, "job-race").build();
        SimpleTrigger loserTrigger = buildTriggerFor(LOSER_JOB_NAME, LOSER_MS_INTERVAL);
        scheduleJob(loserJob, loserTrigger);

        Thread.sleep(TIME_TO_SLEEP);

        Assert.assertEquals(SynchronizedJob.getNumberOfInvocation(), 2);
        Assert.assertTrue(SynchronizedJob.getInvocationTimeFor(WINNER_JOB_NAME) < SynchronizedJob
        .getInvocationTimeFor(LOSER_JOB_NAME));
        Assert.assertEquals(SynchronizedJob.getNumberOfInternalInvocation(), 1);
        Assert.assertEquals(SynchronizedJob.getNameOfInternalInvocer(), WINNER_JOB_NAME);
    }

    /**
     * Schedules the given {@link JobDetail} with the {@link Trigger}.
     * 
     * @param jobDetail
     *            - information about the {@link JobDetail job} that should be executed
     * @param trigger
     *            - the Trigger specifying when the Job should be executed
     * @throws SchedulerException
     *             - if it fails
     */
    private void scheduleJob(JobDetail jobDetail, SimpleTrigger trigger)
    throws SchedulerException {

        Date ft = sched.scheduleJob(jobDetail, trigger);
        logger.info("Job registered at " + System.currentTimeMillis());
        logger.info(jobDetail.getKey() + " will run at: " + ft + " and repeat: " + trigger.getRepeatCount()
            + " times, every " + trigger.getRepeatInterval() + " seconds");
    }

    private SimpleTrigger buildTriggerFor(String name, int msInterval) {

        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0);

        GregorianCalendar winnerStartDate = new GregorianCalendar();
        winnerStartDate.setTimeInMillis(System.currentTimeMillis() + msInterval);
        return TriggerBuilder.newTrigger().withIdentity(name + "-trigger", "test-group")
        .startAt(winnerStartDate.getTime()).withSchedule(scheduleBuilder).build();
    }

}
