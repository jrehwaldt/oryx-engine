package org.jodaengine.eventmanagement.adapter;

import javax.annotation.Nonnull;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.CorrelationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract super adapter defining helper functions and a general structure.
 * 
 * @author Jan Rehwaldt
 * @param <Configuration>
 *            the adapter's configuration
 */
public abstract class AbstractEventAdapter<Configuration extends AdapterConfiguration> implements
CorrelationAdapter {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final Configuration configuration;

    private final CorrelationManager correlation;

    /**
     * Default constructor.
     * 
     * @param correlation
     *            the correlation manager
     * @param configuration
     *            the adapter's configuration
     */
    public AbstractEventAdapter(@Nonnull CorrelationManager correlation, @Nonnull Configuration configuration) {

        this.correlation = correlation;
        this.configuration = configuration;

        logger.info("Initializing {} with config: {}", getClass().getSimpleName(), this.configuration);
    }

    @Override
    public final EventType getAdapterType() {

        return this.configuration.getEventType();
    }

    @Override
    public final @Nonnull
    Configuration getConfiguration() {

        return this.configuration;
    }

    /**
     * Correlation method, which calls the underlying {@link CorrelationManager}.
     * 
     * @see CorrelationManager
     * @see EventManager
     * @param event
     *            the event to correlate
     */
    protected final void correlate(@Nonnull AdapterEvent event) {

        correlation.correlate(event);
        logger.info("Correlating {} for {}", event, getClass().getSimpleName());
    }
}
