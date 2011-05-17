package org.jodaengine.eventmanagement.adapter;

import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.eventmanagement.adapter.TimedConfiguration;
import org.jodaengine.eventmanagement.timing.TimerJob;

import org.quartz.Job;


/**
 * The Class TimerConfigurationImpl. The Timer Configuration is used for the intermediate timer event.
 * @author Jannik Streek
 */
public class TimerConfigurationImpl
implements TimedConfiguration {
    
    private long waitingTime;

    private long timestamp;
    
    /**
     * Instantiates a new timer configuration impl.
     * @param waitingTime the waiting time
     */
    public TimerConfigurationImpl(long waitingTime) {
        this.waitingTime = waitingTime;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public long getTimeInterval() {

        return waitingTime;
    }

    @Override
    public String getUniqueName() {

        return String.valueOf(this.timestamp) + String.valueOf(this.waitingTime);
    }

    @Override
    public Class<? extends Job> getScheduledClass() {
        return TimerJob.class;
    }

    @Override
    public EventType getEventType() {
        return EventTypes.Timer;
    }

}
