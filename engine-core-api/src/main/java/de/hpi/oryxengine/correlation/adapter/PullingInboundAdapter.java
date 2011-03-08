package de.hpi.oryxengine.correlation.adapter;

/**
 * This interface should be implemented by {@link EventManager} adapters,
 * which allow incoming pull communication. This includes,
 * e.g. pop3 email receiving adapter.
 */
public interface PullingInboundAdapter
extends InboundAdapter {
    
}
