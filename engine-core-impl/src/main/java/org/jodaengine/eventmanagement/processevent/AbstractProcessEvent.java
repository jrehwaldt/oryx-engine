package org.jodaengine.eventmanagement.processevent;

import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The most abstract form of a ProcessEvent, just able to get the Configuration and its type.
 */
public abstract class AbstractProcessEvent implements ProcessEvent{

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected AdapterConfiguration config;

    @Override
    public AdapterConfiguration getAdapterConfiguration() {
    
        return config;
    }

    @Override
    public String toString() {
    
        String resultString = "ProcessEvent '" + getAdapterConfiguration().toString() + "' for type "
            + getAdapterConfiguration().getEventType() + ".";
        return resultString;
    }

}
