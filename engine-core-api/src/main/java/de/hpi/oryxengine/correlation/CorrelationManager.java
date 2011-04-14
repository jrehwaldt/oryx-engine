package de.hpi.oryxengine.correlation;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.timing.TimingManager;

/**
 * The correlation manager, which correlates Events to the entities (acitivites, etc..) which subscribed for them.
 */
public interface CorrelationManager extends EventRegistrar {

    /**
     * Receives an adapter event from an adapter and tries to correlate it to someone.
     * 
     * @param e
     *            the adapter event
     */
    void correlate(@Nonnull AdapterEvent e);
    
    /**
     * Gets the timer.
     *
     * @return the timer
     */
    TimingManager getTimer();
}
