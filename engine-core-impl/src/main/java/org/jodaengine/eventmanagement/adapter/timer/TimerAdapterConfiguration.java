package org.jodaengine.eventmanagement.adapter.timer;

import org.quartz.Job;

import org.jodaengine.eventmanagement.AdapterRegistrar;
import org.jodaengine.eventmanagement.CorrelationManager;
import org.jodaengine.eventmanagement.adapter.AbstractAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.CorrelationAdapter;
import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.PullAdapterConfiguration;
import org.jodaengine.eventmanagement.timing.PullAdapterJob;
import org.jodaengine.exception.AdapterSchedulingException;


/**
 * The Class TimerConfigurationImpl. The Timer Configuration is used for the intermediate timer event.
 * @author Jannik Streek
 */
public class TimerAdapterConfiguration extends AbstractAdapterConfiguration
implements PullAdapterConfiguration {
    
    private long waitingTime;

    private long timestamp;
    
    /**
     * Instantiates a new timer configuration impl.
     * @param waitingTime the waiting time
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
     * @param correlationManager
     *            - the {@link CorrelationManager}
     * @return the InboundMailAdapter
     */
    private TimerAdapter createAdapter(CorrelationManager correlationManager) {

        TimerAdapter adapter = new TimerAdapter(correlationManager, this);
        return adapter;
    }

    @Override
    public CorrelationAdapter registerAdapter(AdapterRegistrar adapterRegistrar, CorrelationManager correlationService)
    throws AdapterSchedulingException {

        TimerAdapter adapter = createAdapter(correlationService);
        adapterRegistrar.registerPullAdapter(adapter);

        return adapter;
    }
}
