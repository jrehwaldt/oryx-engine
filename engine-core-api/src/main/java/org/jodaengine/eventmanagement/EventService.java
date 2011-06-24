package org.jodaengine.eventmanagement;

import org.jodaengine.bootstrap.Service;

/**
 * The interface EventManager which groups all the specific responsibilities of an event manager.
 * 
 * For more specific details see everys single interface it implements.
 */
public interface EventService extends EventSubscriptionManagement, AdapterManagement, Service, SendEvents {

}
