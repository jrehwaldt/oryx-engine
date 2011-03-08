package de.hpi.oryxengine.correlation;

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
    void correlateAdapterEvent(AdapterEvent e);

}
