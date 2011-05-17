package org.jodaengine.eventmanagement;

/**
 * The Event Manager which handles incoming Events.
 * It's core functionality is to provide an interface
 * for inbound and outbound communications, especially
 * those related to events.
 * 
 * This includes, e.g. preparing an email for sending
 * (add information such as an id) and receiving the response.
 * 
 */
public interface EventManager extends CorrelationManager, AdapterRegistrar, EventRegistrar {

}
