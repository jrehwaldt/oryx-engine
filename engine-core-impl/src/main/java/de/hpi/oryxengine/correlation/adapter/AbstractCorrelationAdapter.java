package de.hpi.oryxengine.correlation.adapter;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.correlation.AdapterEvent;
import de.hpi.oryxengine.correlation.CorrelationManager;

/**
 * Abstract super adapter defining helper functions and a general structure.
 * 
 * @author Jan Rehwaldt
 * @param <Configuration> the adapter's configuration
 */
public abstract class AbstractCorrelationAdapter<Configuration extends AdapterConfiguration> implements CorrelationAdapter {
    
    /**
	 * @uml.property  name="logger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    /**
	 * @uml.property  name="configuration"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    protected final Configuration configuration;
    
    /**
	 * @uml.property  name="correlation"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private final CorrelationManager correlation;
    
    /**
     * Default constructor.
     * 
     * @param correlation the correlation manager
     * @param configuration the adapter's configuration
     */
    public AbstractCorrelationAdapter(@Nonnull CorrelationManager correlation,
                                      @Nonnull Configuration configuration) {
        this.correlation = correlation;
        this.configuration = configuration;
        
        logger.info("Initializing {} with config: {}", getClass().getSimpleName(), this.configuration);
    }
    
    @Override
    public final EventType getAdapterType() {
        return this.configuration.getEventType();
    }
    
    @Override
    public final @Nonnull Configuration getConfiguration() {
        return this.configuration;
    }
    
    /**
     * Correlation method, which calls the underlying {@link CorrelationManager}.
     * 
     * @see CorrelationManager
     * @see EventManager
     * @param event the event to correlate
     */
    protected final void correlate(@Nonnull AdapterEvent event) {
        correlation.correlate(event);
        logger.info("Correlating {} for {}", event, getClass().getSimpleName());
    }
}
