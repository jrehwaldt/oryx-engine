package org.jodaengine.eventmanagement;

import javax.annotation.Nonnull;

/**
 * The correlation manager, which correlates Events to the entities (acitivites, etc..) which subscribed for them.
 */
public interface EventCorrelator {

    /**
     * Receives an adapter event from an adapter and tries to correlate it to someone.
     * 
     * @param adapterEvent
     *            the adapter event
     */
    void correlate(@Nonnull AdapterEvent adapterEvent);
}
