package de.hpi.oryxengine.correlation.timing;

import javax.annotation.Nonnull;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import de.hpi.oryxengine.correlation.adapter.InboundPullAdapter;
import de.hpi.oryxengine.correlation.adapter.error.ErrorAdapter;
import de.hpi.oryxengine.exception.JodaEngineException;

/**
 * This is a quartz-scheduler job implementation for
 * pulling adapter.
 * 
 * @author Jan Rehwaldt
 */
public class PullAdapterJob
implements Job {
    
    public static final String ADAPTER_KEY = "adapter";
    public static final String ERROR_HANDLER_KEY = "error-handler";
    
    @Override
    public void execute(@Nonnull JobExecutionContext context)
    throws JobExecutionException {
        JobDataMap data = context.getJobDetail().getJobDataMap();
        
        InboundPullAdapter adapter = (InboundPullAdapter) data.get(ADAPTER_KEY);
        try {
            
            adapter.pull();
        } catch (JodaEngineException e) {
            ErrorAdapter error = (ErrorAdapter) data.get(ERROR_HANDLER_KEY);
            error.exceptionOccured("Adapter failed while pulling.", e);
        }
    }
}
