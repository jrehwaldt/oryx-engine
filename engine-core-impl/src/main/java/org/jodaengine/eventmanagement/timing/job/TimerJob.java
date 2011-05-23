package org.jodaengine.eventmanagement.timing.job;

import org.jodaengine.eventmanagement.timing.QuartzJobManager;
import org.jodaengine.process.token.BPMNToken;
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

    /**
     * Resumes the token, as the time is up!.
     *
     * @param context the context in whcih this job i sexecuted
     * @throws JobExecutionException the job execution exception
     */
    @Override
    public void execute(JobExecutionContext context)
    throws JobExecutionException {
        
        JobDataMap data = context.getJobDetail().getJobDataMap();
        BPMNToken bPMNToken = (BPMNToken) data.get(QuartzJobManager.TOKEN_KEY);
        bPMNToken.resume();
    }
}
