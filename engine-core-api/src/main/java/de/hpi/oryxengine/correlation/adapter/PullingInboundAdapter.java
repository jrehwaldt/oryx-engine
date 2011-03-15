package de.hpi.oryxengine.correlation.adapter;

// TODO: Auto-generated Javadoc
/**
 * This interface should be implemented by {@link EventManager} adapters,
 * which allow incoming pull communication. This includes,
 * e.g. pop3 email receiving adapter.
 */
public interface PullingInboundAdapter
extends InboundAdapter {
    
    /**
     * This method is invoked whenever pulling is requested.
     * 
     * @throws Exception may throw an arbitrary exception
     */
    void pull()
    throws Exception;
    
}
