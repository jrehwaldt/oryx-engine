package org.jodaengine.correlation.timing;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.jodaengine.process.token.Token;


/**
 * The Class TimerJob, used for intermediate timer events.
 * @author Jannik Streek
 */
public class TimerJob
implements Job {

    @Override
    public void execute(JobExecutionContext context)
    throws JobExecutionException {
        
        JobDataMap data = context.getJobDetail().getJobDataMap();
        Token token = (Token) data.get(TimingManagerImpl.TOKEN_KEY);
        token.resume();
    }
}
