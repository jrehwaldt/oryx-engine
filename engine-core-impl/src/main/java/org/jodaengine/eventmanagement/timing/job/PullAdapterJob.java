package org.jodaengine.eventmanagement.timing.job;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.adapter.error.ErrorAdapter;
import org.jodaengine.eventmanagement.adapter.incoming.InboundPullAdapter;
import org.jodaengine.exception.JodaEngineException;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * This is a quartz-scheduler job implementation for
 * pulling adapter.
 * 
 * @author Jan Rehwaldt
 */
public class PullAdapterJob implements Job {

    public static final String ADAPTER_KEY = "adapter";
    public static final String ERROR_HANDLER_KEY = "error-handler";

    /**
     * Whenever it is our turn, the adapter for which this Job is for pulls e.g. gets new mails.
     * 
     * @param context
     *            the context in which this job is executed
     * @throws JobExecutionException
     *             the job execution exception
     */
    @Override
    public void execute(@Nonnull JobExecutionContext context)
    throws JobExecutionException {

        synchronized (PullAdapterJob.class) {

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
}
