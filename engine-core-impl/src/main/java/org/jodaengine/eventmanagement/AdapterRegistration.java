package org.jodaengine.eventmanagement;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.jodaengine.bootstrap.Service;
import org.jodaengine.eventmanagement.adapter.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.InboundAdapter;
import org.jodaengine.eventmanagement.adapter.InboundPullAdapter;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapter;
import org.jodaengine.eventmanagement.adapter.error.ErrorAdapterConfiguration;
import org.jodaengine.eventmanagement.registration.ProcessEvent;
import org.jodaengine.eventmanagement.timing.TimingManagerImpl;
import org.jodaengine.exception.AdapterSchedulingException;
import org.jodaengine.exception.EngineInitializationFailedException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Adapter Registrar, here you may register new Event to get your events.
 */
public class AdapterRegistration implements AdapterRegistrar, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    
    private ErrorAdapter errorAdapter;
    // there may be multiple references to an Adapter configuration (i.e. a mail account)
    // and if these all use the same Configuration Object we can avoid duplicated adapters
    // TODO give a process a list of of AdapterConfiguration Objects in order for this to work
    private Map<AdapterConfiguration, InboundAdapter> inboundAdapter;
    private TimingManagerImpl timer;
    
    
    public AdapterRegistration(CorrelationManager correlationManager) {
        this.inboundAdapter = new HashMap<AdapterConfiguration, InboundAdapter>();
        this.errorAdapter = new ErrorAdapter(correlationManager, new ErrorAdapterConfiguration());
            this.timer = new TimingManagerImpl(this.errorAdapter);
       
    }

    /**
     * Creates the adapter for an event according to its event type.
     * 
     * @param event
     *            the event
     * @throws AdapterSchedulingException
     *             the adapter scheduling exception
     */
    
    private void registerAdapaterForEvent(ProcessEvent event)
    throws AdapterSchedulingException { 
        // TODO: Implement Comparable for Adapter configurations
        // check if an adapter with the given configuration already exists
        if (inboundAdapter.containsKey(event.getEventConfiguration())) {
            return;
        }
        // Maybe it is possible to do this better.
        AdapterConfiguration configuration = (AdapterConfiguration) event.getEventConfiguration();
        configuration.registerAdapter(this, null);
    }

    @Override
    public @Nonnull
    InboundPullAdapter registerPullAdapter(@Nonnull InboundPullAdapter adapter)
    throws AdapterSchedulingException {
    
        this.timer.registerPullAdapter(adapter);
        return registerAdapter(adapter);
    }

    @Override
    public @Nonnull
    <Adapter extends InboundAdapter> Adapter registerAdapter(@Nonnull Adapter adapter) {
    
        this.inboundAdapter.put(adapter.getConfiguration(), adapter);
        return adapter;
    }

    /**
     * Returns the error adapter.
     * 
     * @return the error adapter
     */
    public ErrorAdapter getErrorAdapter() {
    
        return this.errorAdapter;
    }

    /**
     * Returns the list of registered inbound adapters.
     * 
     * @return the registered inbound adapter
     */
    public Collection<InboundAdapter> getInboundAdapters() {
    
        return this.inboundAdapter.values();
    }

    @Override
    public void start() {
      registerAdapter(this.errorAdapter);        
    }

    @Override
    public void stop() {

        logger.info("Stopping the AdapterRegistration!");
        
    }

}
