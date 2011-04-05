package de.hpi.oryxengine.correlation;

import java.util.List;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.adapter.EventType;

/**
 * Stores the events that did not trigger a registered event 
 * so they may be used if a registration is made after an event
 * occured.
 */
public interface EventStore {

    /**
     * Stores an event in our event store.
     * 
     * @param e the event to store
     */
    void storeAdapterEvent(@Nonnull AdapterEvent e);
    
    /**
     * Deletes an event from our event store.
     * 
     * @param e the event to delete
     */
    void deleteAdapterEvent(@Nonnull AdapterEvent e);
    
    /**
     * Get all stored events of the specified Event Type.
     * 
     * @param e the event type
     * @return a List of all events with the specified type
     */
    @Nonnull List<AdapterEvent> getAdapterEventsFor(@Nonnull EventType e);
}
