package de.hpi.oryxengine.correlation;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.registration.IntermediateEvent;
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.process.token.Token;

/**
 * This interface provides methods for registering events to the {@link EventManager}.
 */
public interface EventRegistrar {

    /**
     * Entry point for registering an event with the {@link CorrelationManager}.
     * 
     * @param event the start event
     */
    void registerStartEvent(@Nonnull StartEvent event);

    /**
     * Entry point for registering an event with the {@link CorrelationManager}.
     * 
     * @param event the intermediate event
     */
    void registerIntermediateEvent(@Nonnull IntermediateEvent event, Token token);
}
