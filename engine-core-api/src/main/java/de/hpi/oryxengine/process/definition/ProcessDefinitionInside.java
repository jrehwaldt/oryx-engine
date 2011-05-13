package de.hpi.oryxengine.process.definition;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.navigator.NavigatorInside;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.structure.Node;

/**
 * Extends the ProcessDefinitionInterface so that it provides more methods for the internal classes.
 * 
 * This Interface is expected to be used by internal classes.
 * 
 */
public interface ProcessDefinitionInside extends ProcessDefinition {

    // TODO @Gerardo&Co. This can be a class.
    /**
     * Gets the start triggers: events pointing to nodes that are tokens put on, if the event is invoked.
     * 
     * @return the start triggers
     */
    @JsonIgnore
    Map<StartEvent, Node> getStartTriggers();

    /**
     * Adds the start trigger. If event is invoked, a token will spawn on node.
     * 
     * @param event
     *            the event
     * @param node
     *            the node
     * @throws IllegalStarteventException
     *             thrown if the provided node isn't a startnode.
     */
    void addStartTrigger(StartEvent event, Node node)
    throws IllegalStarteventException;

    /**
     * Responsible for instantiating a process.
     * 
     * @param navigator
     *            - the {@link NavigatorInside} creating and modifying tokens
     * @return a {@link AbstractProcessInstance processInstance}
     */
    AbstractProcessInstance createProcessInstance(NavigatorInside navigator);

    /**
     * Is responsible for activating the {@link ProcessDefinition}. Perhaps some {@link StartEvent StartEvents} need to
     * be registered.
     * 
     * @param correlationManager
     *            - the {@link CorrelationManager} in order to register events, perhaps
     * 
     */
    void activate(CorrelationManager correlationManager);

    /**
     * Is responsible for deactivating the {@link ProcessDefinition}. Perhaps some {@link StartEvent StartEvents} need
     * to be unregistered.
     * 
     * @param correlationManager
     *            - the {@link CorrelationManager} in order to unregister events, perhaps
     * 
     */
    void deactivate(CorrelationManager correlationManager);
}
