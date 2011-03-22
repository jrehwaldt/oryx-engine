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

import de.hpi.oryxengine.correlation.CorrelationManagerImpl;
import de.hpi.oryxengine.correlation.adapter.InboundPullAdapter;
import de.hpi.oryxengine.correlation.adapter.PullAdapterConfiguration;
import de.hpi.oryxengine.exception.DalmatinaException;

/**
 * The Class TimingManagerImpl.
 */
public class TimingManagerImpl
implements TimingManager {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final Scheduler scheduler;
    private final CorrelationManagerImpl correlation;
    
    /**
     * Default constructor.
     * 
     * @param correlation the correlation manager.
     * @throws SchedulerException if creating a scheduler fails
     */
    public TimingManagerImpl(@Nonnull CorrelationManagerImpl correlation)
    throws SchedulerException {
        this.correlation = correlation;
        
        final SchedulerFactory factory = new org.quartz.impl.StdSchedulerFactory();
        this.scheduler = factory.getScheduler();
        this.scheduler.start();
    }
    
    /**
     * Registers a new pull adapter.
     * 
     * @param adapter the adapter
     * @throws OryxEngineException thrown if scheduling fails
     */
    public void registerPullAdapter(@Nonnull InboundPullAdapter adapter)
    throws DalmatinaException {
        
        PullAdapterConfiguration configuration = adapter.getConfiguration();
        
        final String jobName = jobName(configuration);
        final String jobGroupName = jobGroupName(configuration);
        final String triggerName = triggerName(configuration);
        final long interval = configuration.getPullInterval();
        
        JobDetail jobDetail = new JobDetail(jobName, jobGroupName, PullAdapterJob.class);
        JobDataMap data = jobDetail.getJobDataMap();
        
        data.put(PullAdapterJob.ADAPTER_KEY, adapter);
        
        Trigger trigger = new SimpleTrigger(triggerName, SimpleTrigger.REPEAT_INDEFINITELY, interval);
        
        try {
            this.scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException se) {
            logger.error("Unable to register plugin due to scheduler failure.", se);
            throw new DalmatinaException("Unable to register plugin due to scheduler failure.", se);
        }
    }
    
    /**
     * Creates a unique trigger name for an adapter's configuration.
     * 
     * @param configuration the adapter configuration
     * @return a unique trigger name
     */
    private static @Nonnull String triggerName(@Nonnull PullAdapterConfiguration configuration) {
        return String.format("trigger-%s", configuration.getUniqueName());
    }
    /**
     * Creates a unique job name for an adapter's configuration.
     * 
     * @param configuration the adapter configuration
     * @return a unique job name
     */
    private static @Nonnull String jobName(@Nonnull PullAdapterConfiguration configuration) {
        return String.format("job-%s", configuration.getUniqueName());
    }
    /**
     * Creates a unique group name for an adapter's configuration.
     * 
     * @param configuration the adapter configuration
     * @return a unique group name
     */
    private static @Nonnull String jobGroupName(@Nonnull PullAdapterConfiguration configuration) {
        return String.format("job-group-%s", configuration.getUniqueName());
    }
}
