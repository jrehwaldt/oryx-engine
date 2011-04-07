package de.hpi.oryxengine.correlation;

import javax.annotation.Nonnull;

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
}
