package de.hpi.oryxengine.correlation.timing;

import javax.annotation.Nonnull;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.correlation.adapter.InboundPullAdapter;
import de.hpi.oryxengine.exception.DalmatinaException;

/**
 * This is a quartz-scheduler job implementation for
 * pulling adapter.
 * 
 * @author Jan Rehwaldt
 */
public class PullAdapterJob
implements Job {
    
    public static final String ADAPTER_KEY = "adapter";
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void execute(@Nonnull JobExecutionContext context)
    throws JobExecutionException {
        
        JobDataMap data = context.getJobDetail().getJobDataMap();
        
        InboundPullAdapter adapter = (InboundPullAdapter) data.get(ADAPTER_KEY);
        try {
            adapter.pull();
        } catch (DalmatinaException oee) {
            logger.error("Adapter failed while pulling.", oee);
            // TODO exception handling
        }
    }
}
