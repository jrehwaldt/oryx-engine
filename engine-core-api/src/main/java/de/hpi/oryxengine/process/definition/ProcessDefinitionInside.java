package de.hpi.oryxengine.process.definition;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.navigator.NavigatorInside;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.structure.Node;

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

    AbstractProcessInstance createProcessInstance(NavigatorInside navigator);
    
    void activate(CorrelationManager correlationManager);

    void deactivate(CorrelationManager correlationManager);
}
