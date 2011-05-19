package org.jodaengine.eventmanagement.timing;

import javax.annotation.Nonnegative;

import org.quartz.Job;

/**
 * The Interface {@link QuartzTimerConfiguration}. This should be used for adapters or events
 * which have to be called in specific time intervals.
 */
public interface QuartzTimerConfiguration {

    /**
     * Returns the adapter's time interval in ms.
     * 
     * @return the pull interval in ms
     */
    @Nonnegative
    long getTimeInterval();

    /**
     * Gets the scheduled class which is used for the QUARTZ Scheduler.
     * 
     * @return the scheduled class
     */
    Class<? extends Job> getScheduledClass();
}
