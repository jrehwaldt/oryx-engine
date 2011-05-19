package org.jodaengine.eventmanagement.timing.job;

import org.jodaengine.eventmanagement.timing.QuartzJobManager;
import org.jodaengine.process.token.Token;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


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
        Token token = (Token) data.get(QuartzJobManager.TOKEN_KEY);
        token.resume();
    }
}
