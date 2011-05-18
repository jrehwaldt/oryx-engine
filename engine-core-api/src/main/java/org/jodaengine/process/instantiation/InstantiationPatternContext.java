package org.jodaengine.process.instantiation;

import org.jodaengine.eventmanagement.registration.ProcessStartEvent;
import org.jodaengine.process.definition.ProcessDefinitionInside;
import org.jodaengine.util.ServiceContext;

/**
 * Extends the {@link ServiceContext} with new methods especially for the {@link InstantiationPattern}-Chain.
 */
public interface InstantiationPatternContext extends ServiceContext {

    /**
     * Gets the {@link ProcessDefinitionInside processDefinition}.
     * 
     * @return the {@link ProcessDefinitionInside}
     */
    ProcessDefinitionInside getProcessDefinition();

    /**
     * In case an {@link ProcessStartEvent startEvent} was thrown (starting the {@link InstantiationPattern}) then this method
     * returns the thrown event.
     * 
     * @return the thrown {@link ProcessStartEvent}
     */
    ProcessStartEvent getThrownStartEvent();
}
