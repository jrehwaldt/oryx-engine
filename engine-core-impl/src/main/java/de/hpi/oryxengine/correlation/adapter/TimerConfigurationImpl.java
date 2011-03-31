package de.hpi.oryxengine.correlation.adapter;

import org.quartz.Job;

import de.hpi.oryxengine.correlation.timing.TimerJob;

/**
 * The Class TimerConfigurationImpl. The Timer Configuration is used for the intermediate timer event.
 */
public class TimerConfigurationImpl
extends AbstractAdapterConfiguration
implements TimedAdapterConfiguration {
    
    private long waitingTime;
    private long timestamp;
    
    /**
     * Instantiates a new timer configuration impl.
     *
     * @param type the type
     * @param waitingTime the waiting time
     */
    public TimerConfigurationImpl(AdapterType type, long waitingTime) {

        super(type);
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

}
