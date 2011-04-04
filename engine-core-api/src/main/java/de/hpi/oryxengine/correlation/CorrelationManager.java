package de.hpi.oryxengine.correlation;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.registration.IntermediateEvent;

/**
 * The correlation manager, which correlates Events to the entities (acitivites, etc..) 
 * which subscribed for them.
 */
public interface CorrelationManager {
    
    /**
     * Receives an adapter event from an adapter and
     * tries to correlate it to someone.
     * 
     * @param e the adapter event
     */
    void correlate(@Nonnull AdapterEvent e);
    
    /**
     * Entry point for registering an event with the {@link CorrelationManager}.
     * 
     * @param event the intermediate event
     */
    void registerIntermediateEvent(@Nonnull IntermediateEvent event);

    
}
