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

    private static int timesIsaidHello = 0;

    /**
     * Getter for {@link Integer} that represents how many times this job was called.
     * 
     * @return {@link Integer} that represents how many times this job was called.
     */
    public static int getTimesIsaidHello() {

        return timesIsaidHello;
    }

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
