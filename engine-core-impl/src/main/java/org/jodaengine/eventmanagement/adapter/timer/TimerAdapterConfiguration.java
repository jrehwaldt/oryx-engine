package org.jodaengine.eventmanagement.adapter.timer;

import org.jodaengine.eventmanagement.AdapterManagement;
import org.jodaengine.eventmanagement.adapter.AbstractAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.CorrelationAdapter;
import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.timing.QuartzPullAdapterConfiguration;
import org.jodaengine.eventmanagement.timing.job.PullAdapterJob;
import org.quartz.Job;

/**
 * The Class TimerConfigurationImpl. The Timer Configuration is used for the intermediate timer event.
 * 
 * @author Jannik Streek
 */
public class TimerAdapterConfiguration extends AbstractAdapterConfiguration implements QuartzPullAdapterConfiguration {

    private long waitingTime;

    private long timestamp;

    /**
     * Instantiates a new timer configuration impl.
     * 
     * @param waitingTime
     *            the waiting time
     */
    public TimerAdapterConfiguration(long waitingTime) {

        super(EventTypes.Timer);
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

        return PullAdapterJob.class;
    }

    /**
     * create the Adapter which is defined by this Adapter configuration.
     * 
     * @return the InboundMailAdapter
     */
    private TimerAdapter createAdapter() {

        TimerAdapter adapter = new TimerAdapter(this);
        return adapter;
    }

    @Override
    public CorrelationAdapter registerAdapter(AdapterManagement adapterRegistrar) {

        TimerAdapter adapter = createAdapter();
        adapterRegistrar.registerInboundPullAdapter(adapter);       
        return adapter;
    }

    @Override
    public boolean pullingOnce() {

        return true;
    }
}
