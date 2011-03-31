package de.hpi.oryxengine.correlation.timing;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class TimerJob.
 */
public class TimerJob
implements Job {


    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(JobExecutionContext context)
    throws JobExecutionException {
        
        JobDataMap data = context.getJobDetail().getJobDataMap();
        Token token = (Token) data.get(TimingManagerImpl.TOKEN_KEY);
        try {
            token.resume();
        } catch (DalmatinaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }

}
