package org.jodaengine.eventmanagement.timing;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.configuration.PullAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapter;
import org.jodaengine.eventmanagement.adapter.incoming.IncomingPullAdapter;
import org.jodaengine.eventmanagement.timing.job.PullAdapterJob;
import org.jodaengine.exception.AdapterSchedulingException;
import org.jodaengine.exception.EngineInitializationFailedException;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
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

/**
 * The Class TimingManagerImpl.
 */
public class QuartzJobManager implements TimingManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Scheduler scheduler;
    private final ErrorAdapter errorAdapter;
    public static final String TOKEN_KEY = "token";

    private Map<IncomingPullAdapter, JobKey> runningJobKeyTable;

    /**
     * Default constructor.
     * 
     * @param errorAdapter
     *            the error handler as {@link ErrorAdapter}
     */
    public QuartzJobManager(@Nonnull ErrorAdapter errorAdapter) {

        this.errorAdapter = errorAdapter;
        final SchedulerFactory factory = new StdSchedulerFactory();
        try {

            this.scheduler = factory.getScheduler();

        } catch (SchedulerException se) {
            logger.error("Initializing the scheduler failed.", se);
            throw new EngineInitializationFailedException("Creating a timer manager failed.", se);
        }
    }

    /**
     * Starting the {@link QuartzJobManager}.
     */
    public void start() {

        logger.info("Starting the QuartzJobManage.");
        try {

            scheduler.start();

        } catch (SchedulerException se) {
            logger.error("Initializing the scheduler failed.", se);
            throw new EngineInitializationFailedException("Creating a timer manager failed.", se);
        }
    }

    /**
     * stops the {@link QuartzJobManager}..
     */
    public void stop() {

        logger.info("Stoping the QuartzJobManage.");

        try {
            scheduler.shutdown(false);
        } catch (SchedulerException se) {
            logger.error("Shutting down the scheduler failed.", se);
            throw new EngineInitializationFailedException("Stopping the QuartzJobManager failed.", se);
        }
    }

    @Override
    public void registerJobForInboundPullAdapter(@Nonnull IncomingPullAdapter inboundPulladapter)
    throws AdapterSchedulingException {

        final QuartzPullAdapterConfiguration configuration = (QuartzPullAdapterConfiguration) inboundPulladapter
        .getConfiguration();

        JobDetail jobDetail = buildJobDetailFrom(configuration);
        prepareJobDataMap(inboundPulladapter, jobDetail);

        SimpleTrigger trigger = buildSimpleTrigger(configuration);

        registerQuartzJob(inboundPulladapter, jobDetail, trigger);

        getRunningJobKeyTable().put(inboundPulladapter, jobDetail.getKey());

    }

    @Override
    public void unregisterJobForInboundPullAdapter(IncomingPullAdapter inboundPulladapter)
    throws AdapterSchedulingException {

        JobKey jobKeyOfAdapter = getRunningJobKeyTable().get(inboundPulladapter);
        if (jobKeyOfAdapter == null) {
            return;
        }

        unregisterQuartzJob(inboundPulladapter, jobKeyOfAdapter);
        
        getRunningJobKeyTable().remove(inboundPulladapter);
    }

    /**
     * Builds the {@link JobDetail} from the configuration.
     * 
     * @param configuration
     *            - the {@link QuartzPullAdapterConfiguration} of the adapter for which a job should be registered
     * @return a {@link JobDetail}, one part of a QuartzJob
     */
    @Nonnull
    private JobDetail buildJobDetailFrom(QuartzPullAdapterConfiguration configuration) {

        final String jobName = jobName(configuration);
        final String jobGroupName = jobGroupName(configuration);

        JobDetail resultJobDetail = JobBuilder.newJob(configuration.getScheduledClass())
        .withIdentity(jobName, jobGroupName).build();
        return resultJobDetail;
    }

    /**
     * Prepares the jobDataMap which is past to a job. This map contains the {@link ErrorAdapter} and the
     * {@link IncomingPullAdapter} for which the QuartzJob is registered.
     * 
     * @param adapter
     *            - the {@link IncomingPullAdapter} for which the QuartzJob is registered
     * @param jobDetail
     *            - a jobDetail in order to modify his {@link JobDataMap dataMap}
     */
    private void prepareJobDataMap(IncomingPullAdapter adapter, JobDetail jobDetail) {

        JobDataMap data = jobDetail.getJobDataMap();

        data.put(PullAdapterJob.ADAPTER_KEY, adapter);
        data.put(PullAdapterJob.ERROR_HANDLER_KEY, this.errorAdapter);
    }

    /**
     * Builds the {@link SimpleTrigger} from the configuration.
     * 
     * @param configuration
     *            - the {@link QuartzPullAdapterConfiguration} of the adapter for which a job should be registered
     * @return a {@link SimpleTrigger}, one part of a QuartzJob
     */
    @Nonnull
    private SimpleTrigger buildSimpleTrigger(QuartzPullAdapterConfiguration configuration) {

        final long interval = configuration.getTimeInterval();
        final String triggerName = triggerName(configuration);
        final String triggerGroupName = triggerGroup(configuration);

        SimpleTrigger resultTrigger;
        if (!configuration.pullingOnce()) {

            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().repeatForever()
            .withIntervalInMilliseconds(interval);

            resultTrigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName).startNow()
            .withSchedule(scheduleBuilder).build();

        } else {

            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0);

            GregorianCalendar date = new GregorianCalendar();
            date.setTimeInMillis(System.currentTimeMillis() + configuration.getTimeInterval());
            resultTrigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName)
            .startAt(date.getTime()).withSchedule(scheduleBuilder).build();
        }

        return resultTrigger;
    }

    /**
     * Registers a job for QUARTZ Scheduler.
     * 
     * @param adapter
     *            - the {@link IncomingPullAdapter}for which a job is registered
     * @param jobDetail
     *            - the job detail which includes the data for the job.
     * @param trigger
     *            - the trigger which defines when (repeated or just for one time) and how to execute the job.
     * @throws AdapterSchedulingException
     *             - the adapter scheduling exception
     */
    private void registerQuartzJob(IncomingPullAdapter adapter, JobDetail jobDetail, Trigger trigger)
    throws AdapterSchedulingException {

        try {

            this.scheduler.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException quartzException) {

            String errorMessage = "While registering a job for the eventAdapter '" + adapter.toString()
                + "' the following exception occurred: " + quartzException.getMessage();
            logger.error(errorMessage, quartzException);
            throw new AdapterSchedulingException(errorMessage, quartzException);
        }
    }

    /**
     * Deleting the QuartzJob assigned to an {@link IncomingPullAdapter}.
     * 
     * @param adapter
     *            - the {@link IncomingPullAdapter}for which a job is registered
     * @param jobKeyOfAdapter
     *            - the key of the job that is assigned the {@link IncomingPullAdapter}
     * @throws AdapterSchedulingException
     *             - the adapter scheduling exception
     */
    private void unregisterQuartzJob(IncomingPullAdapter adapter, JobKey jobKeyOfAdapter)
    throws AdapterSchedulingException {

        try {

            this.scheduler.deleteJob(jobKeyOfAdapter);

        } catch (SchedulerException quartzException) {
            String errorMessage = "While deleting a job for the eventAdapter '" + adapter.toString()
                + "' the following exception occurred: " + quartzException.getMessage();
            logger.error(errorMessage, quartzException);
            throw new AdapterSchedulingException(errorMessage, quartzException);
        }
    }

    /**
     * Creates a unique trigger name for an adapter's configuration.
     * 
     * @param configuration
     *            the adapter configuration
     * @return a unique trigger name
     */
    private static @Nonnull
    String triggerName(@Nonnull PullAdapterConfiguration configuration) {

        return String.format("trigger-%s", configuration.getUniqueName());
    }

    /**
     * Creates a unique trigger group name for an adapter's configuration.
     * 
     * @param configuration
     *            - the adapter configuration
     * @return a unique trigger group name
     */
    private static @Nonnull
    String triggerGroup(@Nonnull PullAdapterConfiguration configuration) {

        return String.format("trigger-group-%s", configuration.getUniqueName());
    }

    /**
     * Creates a unique job name for an adapter's configuration.
     * 
     * @param configuration
     *            the adapter configuration
     * @return a unique job name
     */
    private static @Nonnull
    String jobName(@Nonnull PullAdapterConfiguration configuration) {

        return String.format("job-%s", configuration.getUniqueName());
    }

    /**
     * Creates a unique group name for an adapter's configuration.
     * 
     * @param configuration
     *            the adapter configuration
     * @return a unique group name
     */
    private static @Nonnull
    String jobGroupName(@Nonnull PullAdapterConfiguration configuration) {

        return String.format("job-group-%s", configuration.getUniqueName());
    }

    // @Override
    // public String registerNonRecurringJob(TimerConfiguration configuration, Token token)
    // throws AdapterSchedulingException {
    //
    // JobDetail jobDetail = new JobDetail(jobName(configuration), jobGroupName(configuration),
    // configuration.getScheduledClass());
    // JobDataMap data = jobDetail.getJobDataMap();
    // data.put(TimingManagerImpl.TOKEN_KEY, token);
    //
    // Calendar date = new GregorianCalendar();
    // date.setTimeInMillis(System.currentTimeMillis() + configuration.getTimeInterval());
    //
    // Trigger trigger = new SimpleTrigger(triggerName(configuration), date.getTime());
    //
    // registerJob(jobDetail, trigger);
    //
    // return jobDetail.getFullName();
    //
    // }

    // @Override
    // public void unregisterJob(String jobCompleteName) {

    // String[] tmp = jobCompleteName.split("\\.");
    // try {
    // this.scheduler.deleteJob(tmp[1], tmp[0]);
    // } catch (SchedulerException e) {
    // e.printStackTrace();
    // }
    // }

    /**
     * Getter for {@link QuartzJobManager#runningJobKeyTable}.
     * 
     * @return the {@link Map} that links an {@link IncomingPullAdapter} to it corresponding {@link JobKey QuartzJobKey}
     */
    private Map<IncomingPullAdapter, JobKey> getRunningJobKeyTable() {

        if (runningJobKeyTable == null) {
            this.runningJobKeyTable = new HashMap<IncomingPullAdapter, JobKey>();
        }
        return runningJobKeyTable;
    }

    // @Override
    // public String registerNonRecurringJob(TimerConfiguration configuration, Token token)
    // throws AdapterSchedulingException {
    //
    // JobDetail jobDetail = new JobDetail(jobName(configuration), jobGroupName(configuration),
    // configuration.getScheduledClass());
    // JobDataMap data = jobDetail.getJobDataMap();
    // data.put(TimingManagerImpl.TOKEN_KEY, token);
    //
    // Calendar date = new GregorianCalendar();
    // date.setTimeInMillis(System.currentTimeMillis() + configuration.getTimeInterval());
    //
    // Trigger trigger = new SimpleTrigger(triggerName(configuration), date.getTime());
    //
    // registerJob(jobDetail, trigger);
    //
    // return jobDetail.getFullName();
    //
    // }
    
    // @Override
    // public void unregisterJob(String jobCompleteName) {
    
    // String[] tmp = jobCompleteName.split("\\.");
    // try {
    // this.scheduler.deleteJob(tmp[1], tmp[0]);
    // } catch (SchedulerException e) {
    // e.printStackTrace();
    // }
    // }
    
    // === testing method ===
    /**
     * Only for test methods.
     * 
     * @return the number of currently running jobs
     */
    public int numberOfCurrentRunningJobs() {
    
        return getRunningJobKeyTable().size();
    }

    // public void emptyScheduler()
    // throws SchedulerException {

    // String[] groups = scheduler.getJobGroupNames();
    // for (String group : groups) {
    // String[] jobs = scheduler.getJobNames(group);
    // for (String job : jobs) {
    // unregisterJob(group + "." + job);
    // }
    //
    // }

    // }
}
