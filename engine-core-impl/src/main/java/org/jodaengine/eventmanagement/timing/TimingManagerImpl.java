package org.jodaengine.eventmanagement.timing;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.annotation.Nonnull;


import org.jodaengine.eventmanagement.EventConfiguration;
import org.jodaengine.eventmanagement.adapter.InboundPullAdapter;
import org.jodaengine.eventmanagement.adapter.TimedConfiguration;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapter;
import org.jodaengine.exception.AdapterSchedulingException;
import org.jodaengine.process.token.Token;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class TimingManagerImpl.
 */
public class TimingManagerImpl
implements TimingManager {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Scheduler scheduler;
    private final ErrorAdapter errorAdapter;
    public static final String TOKEN_KEY = "token";
    /**
     * Default constructor.
     * 
     * @param errorAdapter the error handler as {@link ErrorAdapter}
     * @throws SchedulerException if creating a scheduler fails
     */
    public TimingManagerImpl(@Nonnull ErrorAdapter errorAdapter)
    throws SchedulerException {
        this.errorAdapter = errorAdapter;
        
        final SchedulerFactory factory = new org.quartz.impl.StdSchedulerFactory();
        this.scheduler = factory.getScheduler();
        this.scheduler.start();
    }
    
    @Override
    public void registerPullAdapter(@Nonnull InboundPullAdapter adapter)
    throws AdapterSchedulingException {
        
        final TimedConfiguration configuration = adapter.getConfiguration();
        final long interval = configuration.getTimeInterval();
        
        final String jobName = jobName(configuration);
        final String jobGroupName = jobGroupName(configuration);
        final String triggerName = triggerName(configuration);
        
        JobDetail jobDetail = new JobDetail(jobName, jobGroupName, configuration.getScheduledClass());
        JobDataMap data = jobDetail.getJobDataMap();
        
        data.put(PullAdapterJob.ADAPTER_KEY, adapter);
        data.put(PullAdapterJob.ERROR_HANDLER_KEY, this.errorAdapter);
        
        Trigger trigger = new SimpleTrigger(triggerName, SimpleTrigger.REPEAT_INDEFINITELY, interval);
        
        registerJob(jobDetail,
                    trigger);
    }
    
    /**
     * Creates a unique trigger name for an adapter's configuration.
     * 
     * @param configuration the adapter configuration
     * @return a unique trigger name
     */
    private static @Nonnull String triggerName(@Nonnull EventConfiguration configuration) {
        return String.format("trigger-%s", configuration.getUniqueName());
    }
    /**
     * Creates a unique job name for an adapter's configuration.
     * 
     * @param configuration the adapter configuration
     * @return a unique job name
     */
    private static @Nonnull String jobName(@Nonnull EventConfiguration configuration) {
        return String.format("job-%s", configuration.getUniqueName());
    }
    /**
     * Creates a unique group name for an adapter's configuration.
     * 
     * @param configuration the adapter configuration
     * @return a unique group name
     */
    private static @Nonnull String jobGroupName(@Nonnull EventConfiguration configuration) {
        return String.format("job-group-%s", configuration.getUniqueName());
    }

    /**
     * Registers a job for QUARTZ Scheduler.
     *
     * @param detail the job detail which includes the data for the job.
     * @param trigger the trigger which defines when (repeated or just for one time) and how to execute the job.
     * @throws AdapterSchedulingException the adapter scheduling exception
     */
    private void registerJob(JobDetail detail, Trigger trigger)
    throws AdapterSchedulingException {
 
        try {
            this.scheduler.scheduleJob(detail, trigger);
        } catch (SchedulerException se) {
            logger.error("Unable to register plugin due to scheduler failure.", se);
            throw new AdapterSchedulingException(se);
        }
        
    }

    @Override
    public String registerNonRecurringJob(TimedConfiguration configuration, Token token)
    throws AdapterSchedulingException {
        
        JobDetail jobDetail = new JobDetail(
            jobName(configuration),
            jobGroupName(configuration),
            configuration.getScheduledClass());
        JobDataMap data = jobDetail.getJobDataMap();
        data.put(TimingManagerImpl.TOKEN_KEY, token);
        
        Calendar date = new GregorianCalendar();
        date.setTimeInMillis(System.currentTimeMillis() + configuration.getTimeInterval());

        Trigger trigger = new SimpleTrigger(triggerName(configuration), date.getTime());
        
        registerJob(jobDetail,
           trigger);
        
        return jobDetail.getFullName();
        
    }

    @Override
    public void unregisterJob(String jobCompleteName) {
        String[] tmp = jobCompleteName.split("\\.");
        try {
            this.scheduler.deleteJob(tmp[1], tmp[0]);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public int countScheduledJobGroups() {
        int jobs = 0;
        try {
            jobs = this.scheduler.getJobGroupNames().length;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    @Override
    public void shutdownScheduler() {

        try {
            this.scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        
    }
    
    @Override
    public void emptyScheduler() throws SchedulerException {

        String[] groups = scheduler.getJobGroupNames();
        for (String group : groups) {
            String[] jobs = scheduler.getJobNames(group);
            for (String job : jobs) {
                unregisterJob(group + "." + job);  
            }

        }
        
    }
    
}
