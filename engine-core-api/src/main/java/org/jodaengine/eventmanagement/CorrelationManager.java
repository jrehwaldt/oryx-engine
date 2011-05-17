package org.jodaengine.eventmanagement;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.timing.TimingManager;

/**
 * The correlation manager, which correlates Events to the entities (acitivites, etc..) which subscribed for them.
 */
public interface CorrelationManager {

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
