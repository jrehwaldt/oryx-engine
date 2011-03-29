package de.hpi.oryxengine.correlation.timing;

import javax.annotation.Nonnull;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.adapter.AdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.InboundPullAdapter;
import de.hpi.oryxengine.correlation.adapter.PullAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.TimedAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.error.ErrorAdapter;
import de.hpi.oryxengine.exception.AdapterSchedulingException;

// TODO: Auto-generated Javadoc
/**
 * The Class TimingManagerImpl.
 */
public class TimingManagerImpl
implements TimingManager {
    
    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /** The scheduler. */
    private final Scheduler scheduler;
    
    /** The error adapter. */
    private final ErrorAdapter errorAdapter;
    
    /**
     * Default constructor.
     * 
     * @param errorAdapter the error handler as {@link ErrorAdapter}
     * @throws SchedulerException if creating a scheduler fails
     */
    public TimingManagerImpl(@Nonnull ErrorAdapter errorAdapter, CorrelationManager correlationManager)
    throws SchedulerException {
        this.errorAdapter = errorAdapter;
        
        final SchedulerFactory factory = new org.quartz.impl.StdSchedulerFactory();
        this.scheduler = factory.getScheduler();
        this.scheduler.start();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerPullAdapter(@Nonnull InboundPullAdapter adapter)
    throws AdapterSchedulingException {
        
        final TimedAdapterConfiguration configuration = adapter.getConfiguration();
        final long interval = configuration.getTimeInterval();
        
        final String jobName = jobName(configuration);
        final String jobGroupName = jobGroupName(configuration);
        final String triggerName = triggerName(configuration);
        
        JobDetail jobDetail = new JobDetail(jobName, jobGroupName, configuration.getScheduledClass());
        JobDataMap data = jobDetail.getJobDataMap();
        
        data.put(PullAdapterJob.ADAPTER_KEY, adapter);
        data.put(PullAdapterJob.ERROR_HANDLER_KEY, this.errorAdapter);
        
        registerJob(jobDetail,
                    data,
                    triggerName,
                    SimpleTrigger.REPEAT_INDEFINITELY,
                    interval);
    }
    
    /**
     * Creates a unique trigger name for an adapter's configuration.
     * 
     * @param configuration the adapter configuration
     * @return a unique trigger name
     */
    private static @Nonnull String triggerName(@Nonnull AdapterConfiguration configuration) {
        return String.format("trigger-%s", configuration.getUniqueName());
    }
    /**
     * Creates a unique job name for an adapter's configuration.
     * 
     * @param configuration the adapter configuration
     * @return a unique job name
     */
    private static @Nonnull String jobName(@Nonnull AdapterConfiguration configuration) {
        return String.format("job-%s", configuration.getUniqueName());
    }
    /**
     * Creates a unique group name for an adapter's configuration.
     * 
     * @param configuration the adapter configuration
     * @return a unique group name
     */
    private static @Nonnull String jobGroupName(@Nonnull AdapterConfiguration configuration) {
        return String.format("job-group-%s", configuration.getUniqueName());
    }

    /**
     * Registers the job for intermediate events and for adapters.
     *
     * @param detail the detail
     * @param map the map
     * @param triggerName the trigger name
     * @param repeat the repeat
     * @param interval the interval
     * @throws AdapterSchedulingException the adapter scheduling exception
     */
    private void registerJob(JobDetail detail, JobDataMap map, String triggerName , Integer repeat, long interval)
    throws AdapterSchedulingException {

        Trigger trigger = new SimpleTrigger(triggerName, repeat, interval);
        
        try {
            this.scheduler.scheduleJob(detail, trigger);
        } catch (SchedulerException se) {
            logger.error("Unable to register plugin due to scheduler failure.", se);
            throw new AdapterSchedulingException(se);
        }
        
    }

    public void registerNonRecurringJob(TimedAdapterConfiguration configuration)
    throws AdapterSchedulingException {
        
        JobDetail jobDetail = new JobDetail(jobName(configuration), jobGroupName(configuration), configuration.getScheduledClass());
        JobDataMap data = jobDetail.getJobDataMap();
        
        registerJob(jobDetail,
            data,
            triggerName(configuration),
            0,
            configuration.getTimeInterval());
        
    }

}
