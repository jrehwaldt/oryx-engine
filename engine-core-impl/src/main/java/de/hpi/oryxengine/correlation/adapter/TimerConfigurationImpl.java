package de.hpi.oryxengine.correlation.adapter;

import org.quartz.Job;

import de.hpi.oryxengine.correlation.timing.TimerJob;

/**
 * The Class TimerConfigurationImpl. The Timer Configuration is used for the intermediate timer event.
 * @author Jannik Streek
 */
public class TimerConfigurationImpl
implements TimedConfiguration {
    
    /**
	 * @uml.property  name="waitingTime"
	 */
    private long waitingTime;
    /**
	 * @uml.property  name="timestamp"
	 */
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
