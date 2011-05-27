package org.jodaengine.eventmanagement.timing.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SimpleClass for spiking the quartz scheduler.
 */
public class SayHelloJob implements Job {

    public static int timesIsaidHello = 0;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void execute(JobExecutionContext context)
    throws JobExecutionException {

        long currentTimeMillis = System.currentTimeMillis();
        logger.info("Hello World!! - Called at " + currentTimeMillis);
        
        timesIsaidHello++;
    }
    
    /**
     * Reset the {@link SayHelloJob}.
     */
    public static void reset() {

        timesIsaidHello = 0;
    }

}
