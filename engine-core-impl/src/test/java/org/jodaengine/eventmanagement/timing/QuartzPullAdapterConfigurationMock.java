package org.jodaengine.eventmanagement.timing;

import org.jodaengine.eventmanagement.AdapterManagement;
import org.jodaengine.eventmanagement.adapter.EventAdapter;
import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.quartz.Job;

/**
 * Only a mock for the {@link QuartzPullAdapterConfiguration} that can be used in the test.
 */
public class QuartzPullAdapterConfigurationMock implements QuartzPullAdapterConfiguration {

    private long timeInterval;
    private Class<? extends Job> scheduledClass;
    private boolean pullingOnce;

    /**
     * Default Instantiation.
     * 
     * @param timeInterval
     *            - adapter's time interval in ms
     * @param scheduledClass
     *            - the scheduled class which is used by the QUARTZ Scheduler
     * @param pullingOnce
     *            - specifies whether the adapter needs to be pulled only once or continuously
     */
    public QuartzPullAdapterConfigurationMock(long timeInterval,
                                              Class<? extends Job> scheduledClass,
                                              boolean pullingOnce) {

        this.timeInterval = timeInterval;
        this.scheduledClass = scheduledClass;
        this.pullingOnce = pullingOnce;
    }

    @Override
    public long getTimeInterval() {

        return timeInterval;
    }

    @Override
    public Class<? extends Job> getScheduledClass() {

        return scheduledClass;
    }

    @Override
    public boolean pullingOnce() {

        return pullingOnce;
    }

    @Override
    public String getUniqueName() {

        return this.getClass().getName();
    }

    @Override
    public EventAdapter registerAdapter(AdapterManagement adapterRegistrar) {

        String errorMessage = "Cannot create an adapter."
            + "Should only be used for testing the QuartzJobManager implementation.";
        throw new JodaEngineRuntimeException(errorMessage);
    }

    @Override
    public EventType getEventType() {

        String errorMessage = "Does not have an event type."
            + "Should only be used for testing the QuartzJobManager implementation.";
        throw new JodaEngineRuntimeException(errorMessage);
    }
}
