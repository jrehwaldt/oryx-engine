package org.jodaengine.eventmanagement.adapter.configuration;

import org.jodaengine.eventmanagement.AdapterManagement;
import org.jodaengine.eventmanagement.EventConfiguration;
import org.jodaengine.eventmanagement.adapter.EventAdapter;

/**
 * Configuration package for our adapter.
 */
public interface AdapterConfiguration extends EventConfiguration {

    /**
     * Registers the adapter for this configuration.
     * 
     * @param adapterRegistrar
     *            - the place where you can register your adapters
     * @return the schedule {@link EventAdapter}
     */
    EventAdapter registerAdapter(AdapterManagement adapterRegistrar);

    /**
     * You need to implement your own method based on the important characteristics. It is needed so there are no new
     * adapters created when a similar adapter already exists.
     * 
     * {@inheritDoc}
     * 
     * @param object
     *            other object (preferably another AdapterConfiguration of the same type to make the comparison viable).
     * @return true, if the Object equals this
     */
    @Override
    boolean equals(Object object);

    /**
     * You need to implement your own method based on the important characteristics. It is needed so there are no new
     * adapters created when a similar adapter already exists.
     * 
     * {@inheritDoc}
     * 
     * @return the hash
     */
    @Override
    int hashCode();

    // TODO @EVENTTEAM: you shall implement toString for them - maybe?.
}
