package org.jodaengine.process.definition;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.jodaengine.eventmanagement.EventCorrelator;
import org.jodaengine.eventmanagement.EventSubscriptionManager;
import org.jodaengine.eventmanagement.subscription.ProcessStartEvent;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.navigator.NavigatorInside;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;


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
    Map<ProcessStartEvent, Node> getStartTriggers();

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
    void addStartTrigger(ProcessStartEvent event, Node node)
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
     * Is responsible for activating the {@link ProcessDefinition}. Perhaps some {@link ProcessStartEvent StartEvents} need to
     * be registered.
     * 
     * @param correlationManager
     *            - the {@link EventCorrelator} in order to register events, perhaps
     * 
     */
    void activate(EventSubscriptionManager correlationManager);

    /**
     * Is responsible for deactivating the {@link ProcessDefinition}. Perhaps some {@link ProcessStartEvent StartEvents} need
     * to be unregistered.
     * 
     * @param correlationManager
     *            - the {@link EventCorrelator} in order to unregister events, perhaps
     * 
     */
    void deactivate(EventSubscriptionManager correlationManager);
}
