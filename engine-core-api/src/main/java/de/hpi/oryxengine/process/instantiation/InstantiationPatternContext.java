package de.hpi.oryxengine.process.instantiation;

import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.process.definition.ProcessDefinitionInside;
import de.hpi.oryxengine.util.ServiceContext;

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
     * In case an {@link StartEvent startEvent} was thrown (starting the {@link InstantiationPattern}) then this method
     * returns the thrown event.
     * 
     * @return the thrown {@link StartEvent}
     */
    StartEvent getThrownStartEvent();
}
